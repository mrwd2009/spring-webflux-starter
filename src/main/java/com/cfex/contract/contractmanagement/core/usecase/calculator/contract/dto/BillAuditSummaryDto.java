package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import com.cfex.contract.contractmanagement.lib.contractmodel.TableSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class BillAuditSummaryDto {
    private Integer contractVersion;
    private String defTableVersion;
    private Integer cacheId;
    private Integer timeSeriesDefaultYearActiveVersion;
    private Map<Integer, Integer> timeSeriesDefaultYearInfo;
    private Map<TableSourceEnum, RefTableCacheAuditDetailDto> refTableCacheInfo;
    private String commitId;
}
