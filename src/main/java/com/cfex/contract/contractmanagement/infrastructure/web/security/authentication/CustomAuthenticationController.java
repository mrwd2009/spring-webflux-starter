package com.cfex.contract.contractmanagement.infrastructure.web.security.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
public class CustomAuthenticationController {
    private final ObjectMapper objectMapper;

    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.defer(() -> {
            var exchange = webFilterExchange.getExchange();
            var response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FOUND);
//            var context = exchange.getRequest().getPath().contextPath().value();
            try {
                var authBytes = objectMapper.writeValueAsBytes(new ArrayList<String>());
                response.setStatusCode(HttpStatus.OK);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return response
                        .writeWith(Mono.just(response.bufferFactory().wrap(authBytes)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
//            response.getHeaders().setLocation(URI.create(context + "/"));
        });
    }

    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.fromRunnable(() -> {
            var exchange = webFilterExchange.getExchange();
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create("/login?error"));
        });
    }

    public Mono<Void> onAuthenticationEntryPoint(ServerWebExchange exchange, AuthenticationException exception) {
        return Mono.defer(() -> {
            var response = exchange.getResponse();

            var data = new HashMap<String, Object>();
            data.put("meta", Map.of("code", "401", "publicMessage", "Please login."));
            data.put("data", null);

            byte[] dataBytes;
            try {
                dataBytes = objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(dataBytes)));
        });
    }

    public Mono<Void> onAccessDenied(ServerWebExchange exchange, AccessDeniedException exception) {
        return Mono.defer(() -> {
            var response = exchange.getResponse();

            var data = new HashMap<String, Object>();
            data.put("meta", Map.of("code", "403", "publicMessage", "No permission."));
            data.put("data", null);

            byte[] dataBytes;
            try {
                dataBytes = objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(dataBytes)));
        });
    }
}
