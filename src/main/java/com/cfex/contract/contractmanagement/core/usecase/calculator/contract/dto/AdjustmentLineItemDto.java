package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdjustmentLineItemDto {
    private String lineItemDescription;
    private String lineItemSection;
    private String lineItemOrder;
    private BillPresentationDto preAdjustmentDetail;
    private BillPresentationDto postAdjustmentDetail;
}
