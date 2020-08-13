/*
 * MultiTenantDataEntityManagerFactoryBean.java
 *
 * Created on 11.08.2020
 *
 * Copyright (C) 2018 Volkswagen AG, All rights reserved.
 */
package com.example.multitenant;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import io.micronaut.configuration.hibernate.jpa.JpaConfiguration;
import io.micronaut.context.BeanLocator;
import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.data.hibernate.runtime.DataEntityManagerFactoryBean;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.transaction.hibernate5.MicronautSessionContext;
import io.micronaut.transaction.jdbc.DelegatingDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tariq Ahmed
 */
@Factory
//@Requires(classes = {MultiTenantConnectionProvider.class, MultiTenantResolver.class})
public class MultiTenantDataEntityManagerFactoryBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantDataEntityManagerFactoryBean.class);

    private final JpaConfiguration jpaConfiguration;

    private final BeanLocator beanLocator;

    private final MultiTenantConnectionProvider multiTenantConnectionProviderImpl;

    private final MultiTenantResolver multiTenantResolver;

    /**
     * @param jpaConfiguration The JPA configuration
     * @param beanLocator      The bean locator
     * @param multiTenantResolver
     */
    public MultiTenantDataEntityManagerFactoryBean(
            JpaConfiguration jpaConfiguration,
            BeanLocator beanLocator, MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
            MultiTenantResolver multiTenantResolver) {

        this.jpaConfiguration = jpaConfiguration;
        this.beanLocator = beanLocator;
        this.multiTenantConnectionProviderImpl = multiTenantConnectionProviderImpl;
        this.multiTenantResolver = multiTenantResolver;
    }

    /**
     * Builds the {@link StandardServiceRegistry} bean for the given {@link DataSource}.
     *
     * @param dataSourceName The data source name
     * @param dataSource     The data source
     * @return The {@link StandardServiceRegistry}
     */
    @EachBean(DataSource.class)
    @Replaces(
            factory = DataEntityManagerFactoryBean.class,
            bean = StandardServiceRegistry.class)
    @Requires(missingClasses = "org.springframework.orm.hibernate5.HibernateTransactionManager")
    protected StandardServiceRegistry hibernateStandardServiceRegistry(
            @Parameter String dataSourceName,
            DataSource dataSource) {
        if (dataSource instanceof DelegatingDataSource) {
            dataSource = ((DelegatingDataSource) dataSource).getTargetDataSource();
        }
        multiTenantConnectionProviderImpl.register(dataSourceName, dataSource);

        Map<String, Object> additionalSettings = new LinkedHashMap<>();
        additionalSettings.put(AvailableSettings.DATASOURCE, dataSource);
        additionalSettings
                .put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, MicronautSessionContext.class.getName());
        additionalSettings.put(AvailableSettings.SESSION_FACTORY_NAME, dataSourceName);
        additionalSettings.put(AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, false);
        additionalSettings.putIfAbsent(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        additionalSettings.putIfAbsent(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER,
                multiTenantConnectionProviderImpl);
        additionalSettings.putIfAbsent(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, multiTenantResolver);

        JpaConfiguration jpaConfiguration =
                beanLocator.findBean(JpaConfiguration.class, Qualifiers.byName(dataSourceName))
                        .orElse(this.jpaConfiguration);

        return jpaConfiguration.buildStandardServiceRegistry(
                additionalSettings
        );
    }

}
