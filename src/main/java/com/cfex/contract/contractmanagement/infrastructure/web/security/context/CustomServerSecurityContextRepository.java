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
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

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
            var issuedAt = authorizedClient.getAccessToken().getIssuedAt();
            var expiredAt = authorizedClient.getAccessToken().getExpiresAt();

            if (issuedAt == null || expiredAt == null) {
                throw new IllegalArgumentException("Invalid issued at or expired at value.");
            }

            var claims = JwtClaimsSet.builder()
                    .subject(sub)
                    .expiresAt(expiredAt)
                    .notBefore(issuedAt)
                    .issuedAt(issuedAt)
                    .claim(JwtHelper.OC_CLAIM_EMAIL, email)
                    .claim(JwtHelper.OC_CLAIM_NONCE, nonce)
                    .claim(JwtHelper.OC_CLAIM_ROLES, organizationRoles)
                    .claim(JwtHelper.OC_CLAIM_SUBSCRIPTIONS, subscriptions == null ? Collections.emptyList() : subscriptions)
                    .claim(JwtHelper.OC_CLAIM_ORG_CODE, organizationCode == null ? "" : organizationCode)
                    .build();

            return jwtHelper.encode(claims)
                    .doOnNext(jwtToken -> {
                        var tokenValue = jwtToken.getTokenValue();
                        var response = exchange.getResponse();
                        var cookie = ResponseCookie.from(config.getSessionCookieKey(), tokenValue)
                                .secure(true)
                                .httpOnly(true)
                                .path("/")
                                .maxAge(Duration.ofMinutes(config.getSessionEffectiveMinutes()))
                                .sameSite(config.isDev() ? "None" : "Lax")
                                .build();
                        response.addCookie(cookie);
                    })
                    .then();

        }
        return Mono.error(new IllegalArgumentException("Unsupported authentication class: " + authentication.getClass()));
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst(config.getSessionCookieKey()))
                .map(HttpCookie::getValue)
                .flatMap((tokenValue) -> jwtHelper.decode(tokenValue))
                .map((jwt) -> {
                    var sub = jwt.getSubject();
                    var email = jwt.getClaimAsString(JwtHelper.OC_CLAIM_EMAIL);
                    var nonce = jwt.getClaimAsString(JwtHelper.OC_CLAIM_NONCE);
                    var organizationRoles = jwt.getClaimAsStringList(JwtHelper.OC_CLAIM_ROLES);
                    var subscriptions = jwt.getClaimAsStringList(JwtHelper.OC_CLAIM_SUBSCRIPTIONS);
                    var organizationCode = jwt.getClaimAsString(JwtHelper.OC_CLAIM_ORG_CODE);

                    var principal = CustomPrincipal.builder()
                            .sub(sub)
                            .email(email)
                            .nonce(nonce)
                            .organizationRoles(organizationRoles)
                            .subscriptions(subscriptions)
                            .organizationCode(organizationCode)
                            .build();

                    var authorities = new HashSet<>(
                            organizationRoles.stream()
                                    .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .toList()
                    );

                    var authentication = new CustomAuthenticationToken(principal, authorities);
                    var securityContext = new SecurityContextImpl();
                    securityContext.setAuthentication(authentication);
                    return securityContext;
                });
    }
}
