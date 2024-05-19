package com.cfex.contract.contractmanagement.infrastructure.web.security.authentication;

import lombok.Builder;
import lombok.Data;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CustomPrincipal implements Principal {
    private final String sub;
    private final String email;
    private final String nonce;
    private List<String> organizationRoles;
    private List<String> subscriptions;
    private String organizationCode;

    @Override
    public String getName() {
        return sub;
    }
}
