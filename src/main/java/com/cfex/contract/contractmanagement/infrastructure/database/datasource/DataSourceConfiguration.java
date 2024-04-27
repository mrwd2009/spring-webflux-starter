package com.cfex.contract.contractmanagement.infrastructure.database.datasource;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.util.ClassUtils;

import java.util.*;

@Configuration
public class DataSourceConfiguration {
    @Bean("contractProperties")
    @ConfigurationProperties("cfex-oc.r2dbc.contract")
    R2dbcProperties getContractR2dbcProperties() {
        return new R2dbcProperties();
    }

    @Bean("gatewayProperties")
    @ConfigurationProperties("cfex-oc.r2dbc.gateway")
    R2dbcProperties getGatewayR2dbcProperties() {
        return new R2dbcProperties();
    }

    ConnectionFactory createConnectionFactory(R2dbcProperties properties) {
        var urlOptions = ConnectionFactoryOptions.parse(properties.getUrl());
        var optionsBuilder = urlOptions.mutate();
        optionsBuilder.option(ConnectionFactoryOptions.USER, properties.getUsername());
        optionsBuilder.option(ConnectionFactoryOptions.PASSWORD, properties.getPassword());

        return ConnectionFactoryBuilder.withOptions(optionsBuilder).build();
    }

    @Bean("contractConnectionFactory")
    ConnectionFactory getContractConnectionFactory(@Qualifier("contractProperties") R2dbcProperties properties) {
        return createConnectionFactory(properties);
    }

    @Bean("gatewayConnectionFactory")
    ConnectionFactory getGatewayConnectionFactory(@Qualifier("gatewayProperties") R2dbcProperties properties) {
        return createConnectionFactory(properties);
    }

    DatabaseClient createDatabaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder().connectionFactory(connectionFactory).build();
    }

    @Bean("contractDatabaseClient")
    DatabaseClient getContractDatabaseClient(@Qualifier("contractConnectionFactory") ConnectionFactory connectionFactory) {
        return createDatabaseClient(connectionFactory);
    }

    @Bean("gatewayDatabaseClient")
    DatabaseClient getGatewayDatabaseClient(@Qualifier("gatewayConnectionFactory") ConnectionFactory connectionFactory) {
        return createDatabaseClient(connectionFactory);
    }

    R2dbcEntityTemplate createR2dbcEntityTemplate(DatabaseClient databaseClient, String tablePackage, ApplicationContext applicationContext) throws ClassNotFoundException {
        R2dbcDialect dialect = DialectResolver.getDialect(databaseClient.getConnectionFactory());

        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.setEnvironment(applicationContext.getEnvironment());
        scanner.setResourceLoader(applicationContext);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

        Set<Class<?>> entitySet = new HashSet<>();

        for (var candidate: scanner.findCandidateComponents(tablePackage)) {
            entitySet.add(ClassUtils.forName(Objects.requireNonNull(candidate.getBeanClassName()), applicationContext.getClassLoader()));
        }

        var managedTypes = RelationalManagedTypes.fromIterable(entitySet);

        List<Object> converters = new ArrayList<>(dialect.getConverters());
        converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS);
        var customConversions = new R2dbcCustomConversions(
                CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder(), converters),
                Collections.emptyList()
        );

        var mappingContext = new R2dbcMappingContext(DefaultNamingStrategy.INSTANCE);
        mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        mappingContext.setManagedTypes(managedTypes);

        var mappingR2dbcConverter = new MappingR2dbcConverter(mappingContext, customConversions);

        return new R2dbcEntityTemplate(databaseClient, dialect, mappingR2dbcConverter);
    }

    @Bean("contractEntityTemplate")
    R2dbcEntityTemplate getContractR2dbcEntityTemplate(@Qualifier("contractDatabaseClient") DatabaseClient databaseClient, ApplicationContext applicationContext) throws ClassNotFoundException {
        return createR2dbcEntityTemplate(databaseClient, "com.cfex.contract.contractmanagement.infrastructure.database.model.contract", applicationContext);
    }

    @Bean("gatewayEntityTemplate")
    R2dbcEntityTemplate getGatewayR2dbcEntityTemplate(@Qualifier("gatewayDatabaseClient") DatabaseClient databaseClient, ApplicationContext applicationContext) throws ClassNotFoundException {
        return createR2dbcEntityTemplate(databaseClient, "com.cfex.contract.contractmanagement.infrastructure.database.model.gateway", applicationContext);
    }

    @Configuration
    @EnableR2dbcRepositories(basePackages = "com.cfex.contract.contractmanagement.infrastructure.database.repository.contract", entityOperationsRef = "contractEntityTemplate")
    static class ContractRepositoryConfiguration {

    }

    @Configuration
    @EnableR2dbcRepositories(basePackages = "com.cfex.contract.contractmanagement.infrastructure.database.repository.gateway", entityOperationsRef = "gatewayEntityTemplate")
    static class GatewayRepositoryConfiguration {

    }
}
