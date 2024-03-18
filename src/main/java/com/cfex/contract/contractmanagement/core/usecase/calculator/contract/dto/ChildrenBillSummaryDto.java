package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildrenBillSummaryDto {
    private String billCategory;
    private String billCategoryAmount;
    private String includeLineItem;
}
