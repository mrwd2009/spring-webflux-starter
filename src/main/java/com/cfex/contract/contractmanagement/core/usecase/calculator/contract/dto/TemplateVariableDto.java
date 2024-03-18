package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class TemplateVariableDto {
    private OffsetDateTime billStartDate;
    private OffsetDateTime billEndDate;
    private LocalDate invoiceDate;
    private LocalDate invoiceDueDate;
    private BigDecimal billAmount;
    private BigDecimal billAmountBeforeAdjustment;
}
