package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerUsageDto {
    private String gId;
    private String vId;
    private String id;
    private List<CustomerUsageDataDto> usageData;
}
