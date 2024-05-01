package com.cfex.contract.contractmanagement.infrastructure.database.model.gateway;

import com.cfex.contract.contractmanagement.core.repository.gateway.auth.AuthorizedClientValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorizedclient")
public class AuthorizedClientModel {
    @Id
    @Column("__pk_authorizedclient")
    private Long id;
    @Column("sub")
    private String sub;
    @Column("client_registration_id")
    private String clientRegistrationId;
    @Column("access_token_type")
    private String accessTokenType;
    @Column("access_token_value")
    private String accessTokenValue;
    @Column("access_token_issued_at")
    private LocalDateTime accessTokenIssuedAt;
    @Column("access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;
    @Column("access_token_scopes")
    private String accessTokenScopes;
    @Column("refresh_token_value")
    private String refreshTokenValue;
    @Column("refresh_token_issued_at")
    private LocalDateTime refreshTokenIssuedAt;
    @Column("creation_date")
    @InsertOnlyProperty
    private LocalDateTime creationDate;
    @Column("last_modified_date")
    @InsertOnlyProperty
    private LocalDateTime lastModifiedDate;

    public AuthorizedClientValue toValue() {
        return AuthorizedClientValue.builder()
                .id(id)
                .sub(sub)
                .clientRegistrationId(clientRegistrationId)
                .accessTokenType(accessTokenType)
                .accessTokenValue(accessTokenValue)
                .accessTokenIssuedAt(accessTokenIssuedAt)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .accessTokenScopes(accessTokenScopes)
                .refreshTokenValue(refreshTokenValue)
                .refreshTokenIssuedAt(refreshTokenIssuedAt)
                .creationDate(creationDate)
                .lastModifiedDate(lastModifiedDate)
                .build();
    }

    public static AuthorizedClientModel fromValue(AuthorizedClientValue value) {
        return AuthorizedClientModel.builder()
                .id(value.getId())
                .sub(value.getSub())
                .clientRegistrationId(value.getClientRegistrationId())
                .accessTokenType(value.getAccessTokenType())
                .accessTokenValue(value.getAccessTokenValue())
                .accessTokenIssuedAt(value.getAccessTokenIssuedAt())
                .accessTokenExpiresAt(value.getAccessTokenExpiresAt())
                .accessTokenScopes(value.getAccessTokenScopes())
                .refreshTokenValue(value.getRefreshTokenValue())
                .refreshTokenIssuedAt(value.getRefreshTokenIssuedAt())
                .creationDate(value.getCreationDate())
                .lastModifiedDate(value.getLastModifiedDate())
                .build();
    }
}
