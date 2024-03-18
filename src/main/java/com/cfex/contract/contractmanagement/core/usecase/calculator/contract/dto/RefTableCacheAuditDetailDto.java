package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RefTableCacheAuditDetailDto {
    private Integer cacheId;
    private Boolean staticRefTableCache;
    private Boolean useStaticRefTableCache;
    private String cutOffTimeStamp;
    private Integer startYear;
    private Integer endYear;
    private Map<Integer, Integer> dynamicFetchTimeInfo;
    private Map<Integer, Integer> dynamicFetchCountINfo;
}
