package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerAdjustmentBillPairDto {
    private CustomerAdjustmentBillDto preAdjustmentBill;
    private CustomerAdjustmentBillDto postAdjustmentBill;
}