package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AdjustmentBillOutputDto {
    private OffsetDateTime billStartDate;
    private OffsetDateTime billEndDate;
    private BigDecimal adjustmentAmount;
    private AdjustmentTemplateVariableDto adjustmentTemplateVariable;
    private List<AdjustmentLineItemDto> adjustmentLineItem;
}
