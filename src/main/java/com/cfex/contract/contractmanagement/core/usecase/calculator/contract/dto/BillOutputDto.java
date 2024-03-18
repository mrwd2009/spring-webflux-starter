package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import com.cfex.contract.contractmanagement.lib.contractmodel.CategorizationTypeEnum;
import com.cfex.contract.contractmanagement.lib.contractmodel.RollUpLevelEnum;
import com.cfex.contract.contractmanagement.lib.rule.parser.CalibrationOutput;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class BillOutputDto {
    private String resultRetrievalId;
    private BillSummaryDto billSummary;
    private Map<String, String> templateAccessPath;
    private TemplateVariableDto templateVariableOutput;
    private Map<String, String> templateAttributeOutput;
    private Map<Integer, Map<String, String>> carryover;
    private List<BalanceDto> balance;
    private List<BalanceActivityDto> balanceActivity;
    private Map<CategorizationTypeEnum, Set<String>> categorizationOutput;
    private Map<String, String> eventGeneratorOutput;
    private Map<String, EventInfo> eventGeneratorAdvancedOutput;
    private Map<String, String> presentationTotalBillGroupSummary;
    private Map<String, Integer> performanceMetrics;
    private Map<String, RollUpLevelEnum> rollUpLevelOutput;
    private Map<String, UsageDataSummary> usageDataSummary;
    private Map<Integer, String> simpleAttributeOutput;
    private Map<String, Map<String, String>> attributeCategorizationOutput;
    private Map<String, Map<String, List<String>>> advancedCalculationLabelCategorizationOutput;
    private Map<String, String> billCategoryChargeOutput;
    private List<AdjustmentBillOutputDto> adjustmentBillOutput;
    private Map<Integer, Map<Integer, CustomerUsageDataDto>> channelData;
    private Map<String, Map<Integer, Map<String, String>>> advancedCalculationResult;
    private Map<String, List<BillAggregatorOutputDto>> billLineItemOutput;
    private CalibrationOutput calibrationOutput;
    private List<ChildrenBillSummaryDto> childrenBillSummary;
    private ChildrenBillDetailDto childrenBillDetail;
    private List<CustomerUsageDto> advancedCalculationUsageOutput;
    private List<CustomerUsageAltDto> advancedCalculationUsageOutputAlt;
    private BillAuditSummaryDto billAuditSummary;
}
