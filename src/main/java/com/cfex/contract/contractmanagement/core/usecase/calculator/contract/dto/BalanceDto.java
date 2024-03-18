package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BalanceDto {
    private String balanceParty;
    private String balanceType;
    private String balanceUnit;
    private BigDecimal balanceAmount;
}
