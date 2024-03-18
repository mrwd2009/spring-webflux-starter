package com.cfex.contract.contractmanagement.core.usecase.calculator.contract;

import com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto.CustomerDto;
import com.cfex.contract.contractmanagement.core.useservice.registry.GlobalRegistryUseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContractCalculatorUseCaseImpl implements ContractCalculatorUseCase {
    private GlobalRegistryUseService globalRegistryUseService;
    @Override
    public void calculate(CustomerDto params) {

    }
}
