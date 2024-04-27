CREATE TABLE `contractroot`
(
    `__pk_contractroot`    mediumint                                NOT NULL AUTO_INCREMENT,
    `_fk_marketplace`      smallint                                 NOT NULL COMMENT 'marketplace id to tag the marketplace, not officially supported as a foreign key in conjunction with partitioning',
    `name`                 varchar(100)                             NOT NULL,
    `active_version`       smallint                                 NOT NULL DEFAULT '1',
    `release_version`      smallint                                          DEFAULT NULL COMMENT 'only need to be populated for global table',
    `start_date`           date                                     NOT NULL,
    `end_date`             date                                              DEFAULT NULL,
    `status`               enum ('active','inactive')               NOT NULL DEFAULT 'active',
    `root_category`        enum ('instance','prc','umc','pcc')      NOT NULL DEFAULT 'instance',
    `root_type`            enum ('contract','subcontract','charge') NOT NULL DEFAULT 'contract',
    `access_level`         enum ('global','local')                           DEFAULT 'local',
    `domain`               set ('retail','wholesale')                        DEFAULT 'retail' COMMENT 'retail vs wholesale vs usage vs collection etc.',
    `sub_domain`           varchar(20)                                       DEFAULT NULL COMMENT 'e.g. electricity vs gas under retail domain',
    `scope`                varchar(20)                                       DEFAULT NULL COMMENT 'res vs com vs agr',
    `class`                varchar(20)                                       DEFAULT NULL COMMENT 'large vs medium vs small',
    `reusable_category`    varchar(100)                                      DEFAULT '',
    `description`          varchar(1000)                                     DEFAULT NULL,
    `search_tag`           varchar(1000)                            NOT NULL DEFAULT '' COMMENT 'used to tag the assoicated sub-marketplace(s) and other searchable categories',
    `additional_field`     json                                              DEFAULT NULL,
    `creation_date`        timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_modified_date`   timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `migration_process_id` int                                               DEFAULT NULL COMMENT 'the source migration process which migrates the records here',
    `migration_notes`      varchar(100)                                      DEFAULT NULL,
    `sid`                  int                                               DEFAULT NULL COMMENT 'int field reserved for potentila use, e.g. sorting id besides primary key',
    `uid`                  varchar(100)                                      DEFAULT NULL COMMENT 'varchar field reserved for potentila use, e.g. unique identifier',
    PRIMARY KEY (`__pk_contractroot`, `_fk_marketplace`),
    UNIQUE KEY `idx_contractroot_unique` (`_fk_marketplace`, `name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci