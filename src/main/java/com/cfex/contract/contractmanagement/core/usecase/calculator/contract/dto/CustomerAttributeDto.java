package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class CustomerAttributeDto {
    private String partyId;
    private Integer id;
    private String value;
    private OffsetDateTime start;
    private OffsetDateTime end;
}
