package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import com.cfex.contract.contractmanagement.lib.contractmodel.RollUpLevelEnum;
import com.cfex.contract.contractmanagement.lib.contractmodel.UsageDataModeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsageDataSummary {
    private UsageDataModeEnum usageDataMode;
    private RollUpLevelEnum rollUpLevel;
}
