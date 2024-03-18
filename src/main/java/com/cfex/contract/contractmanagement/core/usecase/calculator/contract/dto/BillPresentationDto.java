package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BillPresentationDto {
    private String lineItemDescription;
    private String lineItemAmount;
    private String lineItemPrice;
    private String lineItemQuantity;
    private String lineItemQuantityUnit;
    private String lineItemSection;
    private String lineItemOrder;
    private String lineItemTotalBillFlag;
    private List<String> lineItemTotalBillGroup;
}
