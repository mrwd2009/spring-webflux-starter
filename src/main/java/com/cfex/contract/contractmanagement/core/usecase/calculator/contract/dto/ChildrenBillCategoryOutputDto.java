package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChildrenBillCategoryOutputDto {
    private String billCategory;
    private List<String> advancedCalculationLabel;
}
