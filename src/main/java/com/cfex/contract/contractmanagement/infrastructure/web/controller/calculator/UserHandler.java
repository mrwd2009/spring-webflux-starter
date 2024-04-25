package com.cfex.contract.contractmanagement.infrastructure.web.controller.calculator;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class UserHandler {
    @ResponseBody
    public String getUser(@PathVariable Long userId) {
        return "User " + userId;
    }
}
