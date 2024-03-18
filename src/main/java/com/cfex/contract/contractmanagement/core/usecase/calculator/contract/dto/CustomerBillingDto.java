package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerBillingDto {
    private Integer billNumDays;
    private String billRateCode;
    private BigDecimal billUsage;
    // why is this a BigDecimal?
    private BigDecimal billUsageUnit;
    private BigDecimal billAmount;
    private BigDecimal billCurrencyCode;
    private List<CustomerBillingDetailDto> billDetail;
}
