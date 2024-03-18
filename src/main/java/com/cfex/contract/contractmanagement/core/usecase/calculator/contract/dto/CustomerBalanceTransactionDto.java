package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CustomerBalanceTransactionDto {
    String balanceParty;
    String balanceType;
    String balanceUnit;
    String transactionType;
    BigDecimal transactionAmount;
}
