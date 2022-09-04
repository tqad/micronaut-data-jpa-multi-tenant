package com.example.multitenant;

import javax.sql.DataSource;

import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Any;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.inject.qualifiers.Qualifiers;
import io.micronaut.jdbc.DataSourceResolver;
import jakarta.inject.Singleton;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

/**
 * MultiTenantConnectionProvider.java
 *
 * Created on 13.08.2020
 *
 * @author Tariq Ahmed
 */
@Singleton
public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    public static final String DEFAULT_TENANT_ID = "default";

    @Any
    private final BeanProvider<DataSource> dataSourceBeanProvider;
    private final DataSourceResolver dataSourceResolver;

    MultiTenantConnectionProvider(BeanProvider<DataSource> dataSourceBeanProvider, @Nullable DataSourceResolver dataSourceResolver) {
        this.dataSourceBeanProvider = dataSourceBeanProvider;
        this.dataSourceResolver = dataSourceResolver;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return selectDataSource(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        DataSource dataSource = dataSourceBeanProvider.find(Qualifiers.byName(tenantIdentifier)).orElse(null);
        if (dataSourceResolver != null && dataSource != null) {
            dataSource = dataSourceResolver.resolve(dataSource);
        }
        return dataSource;
    }
}