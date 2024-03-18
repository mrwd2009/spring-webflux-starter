package com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("${contract.baseUri:/api}/calculator/contract")
public class ContractCalculatorController {
    @GetMapping("/calculation")
    public Mono<String> calculateContract() {
        return Mono.just("Contract Calculation");
    }
}
