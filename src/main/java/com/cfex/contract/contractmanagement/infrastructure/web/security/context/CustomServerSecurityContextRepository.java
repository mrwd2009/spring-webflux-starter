package com.cfex.contract.contractmanagement.infrastructure.web.security.context;

import com.cfex.contract.contractmanagement.config.ApplicationProperties;
import com.cfex.contract.contractmanagement.infrastructure.web.security.authentication.CustomAuthenticationToken;
import com.cfex.contract.contractmanagement.infrastructure.web.security.authentication.CustomPrincipal;
import com.cfex.contract.contractmanagement.infrastructure.web.security.authorizedclient.CustomOAuth2AuthorizedClientRepository;
import com.cfex.contract.contractmanagement.lib.crypto.JwtHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Component
@AllArgsConstructor
public class CustomServerSecurityContextRepository implements ServerSecurityContextRepository {
    private JwtHelper jwtHelper;
    private ApplicationProperties config;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        var authentication = context.getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            var authorizedClient = (OAuth2AuthorizedClient) exchange.getAttributes().get(CustomOAuth2AuthorizedClientRepository.AUTHORIZED_CLIENT_ATTR_NAME);
            var user = (DefaultOidcUser)oAuth2AuthenticationToken.getPrincipal();
            var sub = user.getSubject();
            var nonce = user.getNonce();
            var email = user.getEmail();
            var organizationCode = user.getClaimAsString(JwtHelper.AUTH0_CLAIM_MARKETPLACE_CODE);
            var organizationRoles = user.getClaimAsStringList(JwtHelper.AUTH0_CLAIM_ROLES);
            var subscriptions = user.getClaimAsStringList(JwtHelper.AUTH0_CLAIM_SUBSCRIPTIONS);
            var issuedAt = Instant.now();
            var expiredAt = issuedAt.plus(config.getSessionEffectiveMinutes(), ChronoUnit.MINUTES);

            var claims = JwtClaimsSet.builder()
                    .subject(sub)
                    .expiresAt(expiredAt)
                    .notBefore(issuedAt)
                    .issuedAt(issuedAt)
                    .claim(JwtHelper.OC_CLAIM_SESSION_ISSUED_AT, issuedAt)
                    .claim(JwtHelper.OC_CLAIM_EMAIL, email)
                    .claim(JwtHelper.OC_CLAIM_NONCE, nonce)
                    .claim(JwtHelper.OC_CLAIM_ROLES, organizationRoles)
                    .claim(JwtHelper.OC_CLAIM_SUBSCRIPTIONS, subscriptions == null ? Collections.emptyList() : subscriptions)
                    .claim(JwtHelper.OC_CLAIM_ORG_CODE, organizationCode == null ? "" : organizationCode)
                    .claim(JwtHelper.OC_CLAIM_CLIENT_REGISTRATION_ID, authorizedClient.getClientRegistration().getRegistrationId())
                    .build();

            return jwtHelper.encode(claims)
                    .doOnNext(jwtToken -> {
                        var tokenValue = jwtToken.getTokenValue();
                        var response = exchange.getResponse();
                        response.addCookie(createContextCookie(tokenValue, Duration.ofMinutes(config.getSessionEffectiveMinutes())));
                    })
                    .then();

        }
        return Mono.error(new IllegalArgumentException("Unsupported authentication class: " + authentication.getClass()));
    }

    private ResponseCookie createContextCookie(String tokenValue, Duration maxAge) {
        return ResponseCookie.from(config.getSessionCookieKey(), tokenValue)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite(config.isDev() ? "None" : "Lax")
                .build();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // empty context will cause it to be subscribed twice https://github.com/spring-projects/spring-security/issues/8422
        Runnable clearCookie = () -> {
            var response = exchange.getResponse();
//            var existed = response.getCookies().getFirst(config.getSessionCookieKey());
//            if (existed != null) {
//                return;
//            }
            response.addCookie(createContextCookie("", Duration.ZERO));
        };
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst(config.getSessionCookieKey()))
                .map(HttpCookie::getValue)
                .flatMap((tokenValue) -> jwtHelper.decode(tokenValue))
                .flatMap((jwt) -> {
                    var sessionIssuedAt = jwt.getClaimAsInstant(JwtHelper.OC_CLAIM_SESSION_ISSUED_AT);
                    if (sessionIssuedAt == null || Instant.now().isAfter(sessionIssuedAt)) {
                        clearCookie.run();
                        return Mono.empty();
                    }
                    var sub = jwt.getSubject();
                    var email = jwt.getClaimAsString(JwtHelper.OC_CLAIM_EMAIL);
                    var nonce = jwt.getClaimAsString(JwtHelper.OC_CLAIM_NONCE);
                    var organizationRoles = jwt.getClaimAsStringList(JwtHelper.OC_CLAIM_ROLES);
                    var subscriptions = jwt.getClaimAsStringList(JwtHelper.OC_CLAIM_SUBSCRIPTIONS);
                    var organizationCode = jwt.getClaimAsString(JwtHelper.OC_CLAIM_ORG_CODE);
                    var clientRegistrationId = jwt.getClaimAsString(JwtHelper.OC_CLAIM_CLIENT_REGISTRATION_ID);

                    var principal = CustomPrincipal.builder()
                            .sub(sub)
                            .email(email)
                            .nonce(nonce)
                            .organizationRoles(organizationRoles)
                            .subscriptions(subscriptions)
                            .organizationCode(organizationCode)
                            .clientRegistrationId(clientRegistrationId)
                            .build();

                    var authorities = new HashSet<>(
                            organizationRoles.stream()
                                    .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .toList()
                    );

                    var authentication = new CustomAuthenticationToken(principal, authorities);
                    SecurityContext securityContext = new SecurityContextImpl();
                    securityContext.setAuthentication(authentication);

                    var expiredTime = Instant.now().plus(config.getSessionExtendingMinutes(), ChronoUnit.MINUTES);

                    if (expiredTime.isBefore(Objects.requireNonNull(jwt.getExpiresAt()))) {
                        return Mono.just(securityContext);
                    }

                    var issuedAt = Instant.now();
                    var expiredAt = issuedAt.plus(config.getSessionEffectiveMinutes(), ChronoUnit.MINUTES);
                    var claims = JwtClaimsSet.builder()
                            .subject(sub)
                            .expiresAt(expiredAt)
                            .notBefore(issuedAt)
                            .issuedAt(issuedAt)
                            .claim(JwtHelper.OC_CLAIM_SESSION_ISSUED_AT, sessionIssuedAt.getEpochSecond())
                            .claim(JwtHelper.OC_CLAIM_EMAIL, email)
                            .claim(JwtHelper.OC_CLAIM_NONCE, nonce)
                            .claim(JwtHelper.OC_CLAIM_ROLES, organizationRoles)
                            .claim(JwtHelper.OC_CLAIM_SUBSCRIPTIONS, subscriptions)
                            .claim(JwtHelper.OC_CLAIM_ORG_CODE, organizationCode)
                            .claim(JwtHelper.OC_CLAIM_CLIENT_REGISTRATION_ID, jwt.getClaimAsString(JwtHelper.OC_CLAIM_CLIENT_REGISTRATION_ID))
                            .build();

                    return jwtHelper.encode(claims)
                            .doOnNext(jwtToken -> {
                                var tokenValue = jwtToken.getTokenValue();
                                var response = exchange.getResponse();
                                response.addCookie(createContextCookie(tokenValue, Duration.ofMinutes(config.getSessionEffectiveMinutes())));
                            })
                            .then(Mono.just(securityContext));
                })
                .onErrorResume(BadJwtException.class, (exception) -> {
                    clearCookie.run();
                    return Mono.empty();
                })
                .cache();
    }
}
