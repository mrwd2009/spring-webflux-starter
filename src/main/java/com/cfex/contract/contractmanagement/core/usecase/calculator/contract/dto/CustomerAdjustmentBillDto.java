package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerAdjustmentBillDto {
    private BillSummaryDto billSummary;
    private TemplateVariableDto templateVariable;
    private List<BillPresentationDto> billPresentationInfo;
}
