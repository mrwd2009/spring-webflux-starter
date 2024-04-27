package com.cfex.contract.contractmanagement.core.repository.gateway.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuthorizedClientValue {
    private Long id;
    private String sub;
    private String clientRegistrationId;
    private String accessTokenType;
    private String accessTokenValue;
    private LocalDateTime accessTokenIssuedAt;
    private LocalDateTime accessTokenExpiresAt;
    private String accessTokenScopes;
    private String refreshTokenValue;
    private LocalDateTime refreshTokenIssuedAt;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
}
