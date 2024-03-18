package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class CustomerUsageDataDto {
    private Integer ch;
    private OffsetDateTime start;
    private BigDecimal usage;
    private String unit;
    private String freq;
    private Integer vee;
    private Map<String, String> info;
}
