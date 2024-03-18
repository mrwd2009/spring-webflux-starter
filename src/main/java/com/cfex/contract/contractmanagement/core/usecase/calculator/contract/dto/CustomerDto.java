package com.cfex.contract.contractmanagement.core.usecase.calculator.contract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CustomerDto {
    private Integer marketplaceId;
    private Integer subMarketplaceId;
    private String customerTimeZone;
    private CustomerBillingCycleDto customerBillingCycle;
    private CustomerRunConfigDto runConfig;
    private Map<Integer, Map<String, String>> carryover;
    private List<BalanceDto> prevBillBalance;
    private List<BalanceDto> balance;
    private List<CustomerBalanceTransactionDto> balanceTransaction;
    private Map<Integer, Map<Integer, CustomerUsageDataDto>> prevBillChannelData;
    private Map<String, Map<Integer, Map<String, String>>> prevBillAdvancedCalculationResult;
    private Map<String, Map<Integer, BigDecimal>> calendarMonthInput;
    private Map<String, List<ChildrenBillSummaryDto>> childrenBillSummary;
    private List<CustomerAdjustmentBillPairDto> adjustmentBillSummary;
    private List<CustomerAttributeDto> customerAttribute;
    private List<CustomerUsageDto> customerUsage;
    private List<CustomerUsageDto> customerAdditionalUsage;
}
