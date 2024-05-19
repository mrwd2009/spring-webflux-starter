package com.cfex.contract.contractmanagement.infrastructure.web.security;

import com.cfex.contract.contractmanagement.infrastructure.web.security.authentication.CustomAuthenticationController;
import com.cfex.contract.contractmanagement.infrastructure.web.security.context.CustomServerSecurityContextRepository;
import com.cfex.contract.contractmanagement.lib.crypto.JwtHelper;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.stream.Stream;

@Configuration
public class WebSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity httpSecurity,
            ReactiveClientRegistrationRepository registrationRepository,
            ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository,
            CustomAuthenticationController authenticationController,
            CustomServerSecurityContextRepository securityContextRepository
    ) {
        return httpSecurity
                .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                .securityContextRepository(securityContextRepository)
                .oauth2Login(builder -> {
                    builder
                            .authorizationRequestResolver(getRequestResolver(registrationRepository))
                            .authorizationRequestRepository(authorizationRequestRepository)
                            .authenticationSuccessHandler(authenticationController::onAuthenticationSuccess)
                            .authenticationFailureHandler(authenticationController::onAuthenticationFailure)
                    ;
                })
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                .build();
    }

    ServerOAuth2AuthorizationRequestResolver getRequestResolver(ReactiveClientRegistrationRepository registrationRepository) {
        var resolver = new DefaultServerOAuth2AuthorizationRequestResolver(registrationRepository);

        resolver.setAuthorizationRequestCustomizer((builder) -> {
            OAuth2AuthorizationRequestCustomizers.withPkce().accept(builder);
        });
        return resolver;
    }

//    ServerRequestCache getRequestCache() {
//        return new WebSessionServerRequestCache();
//    }

//    ServerSecurityContextRepository getSecurityContextRepository() {
//        return new WebSessionServerSecurityContextRepository();
//    }

//    @Bean
//    GrantedAuthoritiesMapper getAuthoritiesMapper() {
//        return (authorities) -> {
//            // need to extract roles from token
//            System.out.println(authorities);
//            return authorities;
//        };
//    }

    @Bean
    ReactiveClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
        var registrations = new ArrayList<>(
                new OAuth2ClientPropertiesMapper(properties).asClientRegistrations().values()
        );

        return new InMemoryReactiveClientRegistrationRepository(registrations);
    }

    @Bean
    ReactiveOAuth2UserService<OidcUserRequest, OidcUser> getOidcUserService() {
        final var userService = new OidcReactiveOAuth2UserService();
        return (userRequest) -> userService.loadUser(userRequest)
                    .map(user -> {
                        var roles = user.getClaimAsStringList(JwtHelper.AUTH0_CLAIM_ROLES);
                        var authorities = user.getAuthorities();
                        var roleAuthorities = roles.stream().map((role) -> new SimpleGrantedAuthority("ROLE_" + role));
                        return new DefaultOidcUser(Stream.concat(authorities.stream(), roleAuthorities).distinct().toList(), user.getIdToken(), user.getUserInfo());
                    });
    }
}
