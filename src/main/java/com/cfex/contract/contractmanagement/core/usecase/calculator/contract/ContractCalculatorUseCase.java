package com.cfex.contract.contractmanagement.core.usecase.calculator.contract;

import com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto.CustomerDto;

public interface ContractCalculatorUseCase {
    void calculate(CustomerDto params);
}
