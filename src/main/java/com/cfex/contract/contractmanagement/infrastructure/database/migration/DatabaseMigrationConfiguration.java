package com.cfex.contract.contractmanagement.infrastructure.database.migration;

import liquibase.UpdateSummaryEnum;
import liquibase.UpdateSummaryOutputEnum;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DatabaseMigrationConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "cfex-oc.liquibase.contract")
    @Qualifier("contract")
    public LiquibaseProperties getContractLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cfex-oc.liquibase.gateway")
    @Qualifier("gateway")
    public LiquibaseProperties getGatewayLiquibasePropertiesList() {
        return new LiquibaseProperties();
    }

    private SpringLiquibase createMigration(LiquibaseProperties properties) {
        var liquibase = new DataSourceClosingSpringLiquibase();

        var driverClassName = DatabaseDriver.fromJdbcUrl(properties.getUrl()).getDriverClassName();
        var datasource = DataSourceBuilder
                .create()
                .type(SimpleDriverDataSource.class)
                .url(properties.getUrl())
                .username(properties.getUser())
                .password(properties.getPassword())
                .driverClassName(driverClassName)
                .build();

        liquibase.setDataSource(datasource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setClearCheckSums(properties.isClearChecksums());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabelFilter(properties.getLabelFilter());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(properties.isTestRollbackOnUpdate());
        liquibase.setTag(properties.getTag());
        if (properties.getShowSummary() != null) {
            liquibase.setShowSummary(UpdateSummaryEnum.valueOf(properties.getShowSummary().name()));
        }
        if (properties.getShowSummaryOutput() != null) {
            liquibase
                    .setShowSummaryOutput(UpdateSummaryOutputEnum.valueOf(properties.getShowSummaryOutput().name()));
        }
        return liquibase;
    }

    @Bean
    public SpringLiquibase getContractMigration(@Qualifier("contract") LiquibaseProperties properties) {
        return createMigration(properties);
    }

    @Bean
    public SpringLiquibase getGatewayMigration(@Qualifier("gateway") LiquibaseProperties properties) {
        return createMigration(properties);
    }
}
