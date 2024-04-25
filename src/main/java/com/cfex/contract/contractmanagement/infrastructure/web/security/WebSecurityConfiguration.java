package com.cfex.contract.contractmanagement.infrastructure.web.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;

@Configuration
public class WebSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    ReactiveClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
        var registrations = new ArrayList<>(
                new OAuth2ClientPropertiesMapper(properties).asClientRegistrations().values()
        );

        return new InMemoryReactiveClientRegistrationRepository(registrations);
    }

    @Bean
    ReactiveOAuth2AuthorizedClientService auth2AuthorizedClientService(ReactiveClientRegistrationRepository registrationRepository) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(registrationRepository);
    }

    @Bean
    ServerOAuth2AuthorizedClientRepository authorizedClientRepository(ReactiveOAuth2AuthorizedClientService clientService) {
        return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(clientService);
    }
}
