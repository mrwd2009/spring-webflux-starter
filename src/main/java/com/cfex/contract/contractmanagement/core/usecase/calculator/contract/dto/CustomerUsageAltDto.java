package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerUsageAltDto {
    private String meterId;
    private String usageTime;
    private Integer channel;
    private Integer vee;
    private Integer frequency;
    private String unit;
    private String usage;
    private String timezone;
    private String timeFormat;
    private Boolean startTime;
    private String info;

    private static final String INFO_FIELD_ENCLOSER = "\"";
    private static final String INFO_FIELD_DELIMITER = ",";
    private static final String INFO_COMBO_DELIMITER = ":";
}
