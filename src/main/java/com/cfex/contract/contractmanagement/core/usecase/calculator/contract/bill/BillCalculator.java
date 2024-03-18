package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.bill;

import com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto.CustomerDto;
import com.cfex.contract.contractmanagement.core.useservice.registry.GlobalRegistryUseService;

public class BillCalculator {
    private final CustomerDto customer;
    private final GlobalRegistryUseService globalRegistry;
    private final String requestId;

    public BillCalculator(CustomerDto customer, GlobalRegistryUseService globalRegistry, String requestId) {
        this.customer = customer;
        this.globalRegistry = globalRegistry;
        this.requestId = requestId;
    }
}
