package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class BillSummaryDto {
    private BigDecimal billAmount;
    private String billCurrencyCode;
    private BigDecimal billUsage;
    private String billUsageUnit;
    private OffsetDateTime billStartDate;
    private OffsetDateTime billEndDate;
    private String billRateCode;
    private Integer billNumDay;
    private Map<String, CarbonDto> carbonInfo;
    private Map<String, Map<String, String>> eac;
    private Map<String, Map<String, String>> scope2Carbon;
    private Map<String, Map<String, String>> scope3Carbon;
    private Map<String, Map<String, String>> taxCredit;
}
