package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ChildrenBillDetailDto {
    private List<ChildrenBillCategoryOutputDto> billCategoryOutput;
    private Map<Integer, String> simpleAttributeOutput;
}
