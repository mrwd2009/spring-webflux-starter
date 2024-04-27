package com.cfex.contract.contractmanagement.infrastructure.database.model.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("user")
public class UserModel {
    @Id
    @Column("__pk_user")
    private Long id;
    @Column("email")
    private String email;
    @Column("name")
    private String name;
    @Column("sub")
    private String sub;
    @Column("picture")
    private String picture;
    @Column("picture_expires_at")
    private LocalDateTime pictureExpiresAt;
    @Column("source_picture")
    private String sourcePicture;
    @Column("session_id")
    private String sessionId;
    @Column("session_issued_at")
    private LocalDateTime sessionIssuedAt;
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
    private LocalDateTime creationDate;
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;
}
