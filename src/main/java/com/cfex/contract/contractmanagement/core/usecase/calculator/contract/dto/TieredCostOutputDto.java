package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TieredCostOutputDto {
    private Integer tier;
    private String tierUsage;
    private String tierRate;
    private String tierCost;
    private String tierStartPercent;
    private String billAggregatorBaselineAllowance;
    private String billingCycleBaselineAllowance;
}
