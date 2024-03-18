package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CustomerBillingDetailDto {
    private String billAggregatorStartDate;
    private String billAggregatorEndDate;
    private String billKey;
    private BigDecimal billAggregatorAmount;
}
