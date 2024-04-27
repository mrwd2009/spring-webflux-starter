package com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator;

import com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway.UserDbRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("${contract.baseUri:/api}/calculator/contract")
@Slf4j
public class ContractCalculatorController {
    private final SecurityWebFilterChain chain;
    private final UserDbRepository userDbRepository;

    @GetMapping("/calculation")
    public Mono<Long> calculateContract(ServerWebExchange exchange, UriComponentsBuilder builder) {
        System.out.println(builder.fromHttpUrl("https://cfex.com/hotel list/{city}").build("new york"));
        log.info(exchange.getLogPrefix());
        return this.userDbRepository.count();

//        return Mono.just("Contract Calculation").log();
    }
}
