package com.cfex.contract.contractmanagement.infrastructure.web.security.request;

import com.cfex.contract.contractmanagement.config.ApplicationProperties;
import com.cfex.contract.contractmanagement.lib.crypto.CryptoHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CustomServerAuthorizationRequestRepository implements ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private final ApplicationProperties config;
    private final CryptoHelper cryptoHelper;

    @Override
    public Mono<OAuth2AuthorizationRequest> loadAuthorizationRequest(ServerWebExchange exchange) {
        var cookies = exchange.getRequest().getCookies();
        return Mono.justOrEmpty(cookies.getFirst(config.getAuthorizationRequestCookieKey()))
                .map(HttpCookie::getValue)
                .map(cryptoHelper::decryptFromBase64String);
    }

    @Override
    public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
        return Mono.just(exchange.getResponse())
                .doOnNext(response -> {
                    ResponseCookie cookie = createAuthRequestCookie(cryptoHelper.encryptToBase64String(authorizationRequest), config.getAuthorizationRequestCookieAge());
                    response.addCookie(cookie);
                })
                .then();
    }

    ResponseCookie createAuthRequestCookie(String value, Long duration) {
        return ResponseCookie.from(config.getAuthorizationRequestCookieKey(), value)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(duration)
                .sameSite(config.isDev() ? "None" : "Lax")
                .build();
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> removeAuthorizationRequest(ServerWebExchange exchange) {
        var cookies = exchange.getRequest().getCookies();
        return Mono.justOrEmpty(cookies.getFirst(config.getAuthorizationRequestCookieKey()))
                .map(HttpCookie::getValue)
                .map(value -> {
                    exchange.getResponse().addCookie(createAuthRequestCookie("", 0L));
                    return cryptoHelper.decryptFromBase64String(value);
                });
    }
}
