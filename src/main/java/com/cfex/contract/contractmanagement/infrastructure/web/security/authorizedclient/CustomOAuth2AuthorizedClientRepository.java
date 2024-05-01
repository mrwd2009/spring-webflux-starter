package com.cfex.contract.contractmanagement.infrastructure.web.security.authorizedclient;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class CustomOAuth2AuthorizedClientRepository implements ServerOAuth2AuthorizedClientRepository {
    private final ReactiveOAuth2AuthorizedClientService clientService;

    @Override
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId, Authentication principal, ServerWebExchange exchange) {
        return clientService.loadAuthorizedClient(clientRegistrationId, principal.getName());
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, ServerWebExchange exchange) {
        return clientService.saveAuthorizedClient(authorizedClient, principal);
    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, Authentication principal, ServerWebExchange exchange) {
        return clientService.removeAuthorizedClient(clientRegistrationId, principal.getName());
    }
}
