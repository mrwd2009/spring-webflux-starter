package com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator;

import com.cfex.contract.contractmanagement.infrastructure.database.model.gateway.UserModel;
import com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway.UserDbRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@AllArgsConstructor
@RequestMapping("${contract.baseUri:/api}/calculator/contract")
@Slf4j
public class ContractCalculatorController {
    private final SecurityWebFilterChain chain;
    private final UserDbRepository userDbRepository;
    @Qualifier("gatewayTransactionManager")
    private final R2dbcTransactionManager transactionManager;
    @Qualifier("contractTransactionManager")
    private final R2dbcTransactionManager contractTransactionManager;

    @GetMapping("/calculation")
    public Mono<Long> calculateContract(ServerWebExchange exchange, UriComponentsBuilder builder, Authentication authentication) {
        System.out.println(builder.fromHttpUrl("https://cfex.com/hotel list/{city}").build("new york"));
        System.out.println("Authentication: " + authentication);
        log.info(exchange.getLogPrefix());
        return this.userDbRepository.count();
//        return exchange.getSession()
//                .flatMap((session) -> {
//                    System.out.println("Session: " + session.getId());
//                    var attrs = session.getAttributes();
//                    attrs.put("current", System.currentTimeMillis());
//                    return exchange.getSession();
//                })
//                .flatMap((session) -> {
//                    System.out.println("Session: " + session.getId());
//                    var attrs = session.getAttributes();
//                    attrs.put("current2", System.currentTimeMillis());
//                    return this.userDbRepository.count();
//                });


//        return Mono.just("Contract Calculation").log();
    }

    @GetMapping("test-lock")
//    @Transactional("gatewayTransactionManager")
    public Mono<UserModel> testLock(@RequestParam("id") Long id, @RequestParam("name") String name) {
        var operator = TransactionalOperator.create(transactionManager);
        var contractOpt = TransactionalOperator.create(contractTransactionManager);
        return this.userDbRepository.findById(id)
                .map((user) -> {
                    System.out.println("1: " +user.toString());
                    return user;
                })
                .flatMap((user) -> {
                    return this.userDbRepository.getLockedUser(id);
                })
//                .delayElement(Duration.ofSeconds(30))
                .flatMap((user) -> {
                    System.out.println("2: " + user.toString());
                    user.setName(name);
                    return userDbRepository.save(user);
                })
                .as(operator::transactional)
                .as(contractOpt::transactional)
                ;
    }
}
