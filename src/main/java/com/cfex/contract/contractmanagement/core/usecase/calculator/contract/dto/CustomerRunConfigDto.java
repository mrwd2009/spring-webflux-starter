package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerRunConfigDto {
    private String contractVersion;
    private String defTableVersion;
    private String billingRunType;
    private String runType;
    private String subDomain;
    private String runTimeStamp;
    private Integer validateCustomer;
    private Integer calculationStage;
    private Integer calibration;
    private Integer performanceMetrics;
    private Integer rollUpLevelOutput;
    private Integer usageDataSummary;
    private Integer attributeOutput;
    private Integer billCategoryChargeOutput;
    private Integer templateVariableOutput;
    private Integer adjustmentOutputMode;
    private Integer rateCodeTableMode;
    private Integer eventGeneratorOutputMode;
    private Integer advancedCalculationAuditReportCompactMode;
    private Integer advancedCalculationAuditReportOutputMode;
    private Integer usageDataFilterMode;
    private Integer dateTimeOutputFormat;
    private Integer calibrationOutputFormat;
    private Integer lineItemOutputOrder;
    private Integer upToDateRunBlockSize;
    private Integer asyncWaitTime;
    private String asyncWaitTimeUnit;
    private Integer billAuditSummary;
}
