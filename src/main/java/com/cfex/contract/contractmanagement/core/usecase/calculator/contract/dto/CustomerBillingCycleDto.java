package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class CustomerBillingCycleDto {
    private Integer billYear;
    private Integer billMonth;
    private OffsetDateTime billingCycleStartDate;
    private OffsetDateTime billingCycleEndDate;
    private String billId;
    private CustomerBillingDto customerBill;
}
