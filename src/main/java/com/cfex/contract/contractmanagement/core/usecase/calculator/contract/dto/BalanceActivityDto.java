package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BalanceActivityDto {
    private String balanceFromParty;
    private String balanceToParty;
    private String balanceType;
    private String balanceUnit;
    private BigDecimal transactionAmount;
    private String billCategory;
}
