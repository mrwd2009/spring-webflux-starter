package com.cfex.contract.contractmanagement.lib.rule.parser;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class UnmappedSection {
    private Map<String, Map<String, String>> cfexBillCategory;
    private Map<String, Map<String, String>> utilityLineItem;
}
