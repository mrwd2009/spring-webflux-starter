package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class BillAggregatorOutputDto {
    private String billCategory;
    private String billPresentationCategory;
    private String billSuperCategory;
    private String billAggregatorStartDate;
    private String billAggregatorEndDate;
    private String billAggregatorAmount;
    private Map<String, String> rateInfo;
    private Map<String, Map<String, String>> dynamicVariableRateInfo;
    private Map<String, String> channelTableInfo;
    private Map<String, String> advancedCalculationResultTableInfo;
    private Map<String, String> additionalInfo;
    private Map<String, List<TieredCostOutputDto>> tierInfo;
    private Map<String, String> advancedCalculationInfo;
    private BillPresentationDto billPresentationInfo;
    private Map<String, String> eventGeneratorInfo;
}
