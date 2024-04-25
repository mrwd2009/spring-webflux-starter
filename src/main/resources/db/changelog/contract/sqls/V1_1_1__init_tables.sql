CREATE TABLE `authorizedapiclient`
(
    `__pk_authorizedclient`   bigint      NOT NULL AUTO_INCREMENT,
    `sub`                     varchar(200)         DEFAULT '',
    `client_registration_id`  varchar(100)         DEFAULT '',
    `access_token_type`       varchar(100)         DEFAULT '',
    `access_token_value`      varchar(6000)        DEFAULT '',
    `access_token_issued_at`  datetime(6)          DEFAULT NULL,
    `access_token_expires_at` datetime(6)          DEFAULT NULL,
    `access_token_scopes`     varchar(4000)        DEFAULT '',
    `refresh_token_value`     varchar(2000)        DEFAULT '',
    `refresh_token_issued_at` datetime(6)          DEFAULT NULL,
    `creation_date`           datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `last_modified_date`      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`__pk_authorizedclient`),
    KEY `sub_registration` (`sub`, `client_registration_id`),
    KEY `created_at` (`creation_date`),
    KEY `updated_at` (`last_modified_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;