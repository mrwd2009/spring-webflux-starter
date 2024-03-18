package com.cfex.contract.contractmanagement.lib.rule.parser;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CalibrationOutput {
    private Map<String, Map<String, Map<String, Map<String, String>>>> mappedSection;

}
