/*
 * JpaConfigurationCreatedEventListener.java
 *
 * Created on 01.09.2022
 */
package com.example.multitenant;

import io.micronaut.configuration.hibernate.jpa.JpaConfiguration;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;

@Singleton
public class JpaConfigurationCreatedEventListener implements BeanCreatedEventListener<JpaConfiguration> {

    @Inject
    MultiTenantConnectionProvider connectionProvider;

    @Inject
    MultiTenantResolver tenantResolver;

    @Override
    public JpaConfiguration onCreated(final BeanCreatedEvent<JpaConfiguration> event) {
        final JpaConfiguration jpaConfiguration = event.getBean();
        jpaConfiguration.getProperties().put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        jpaConfiguration.getProperties().put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        jpaConfiguration.getProperties().put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        return jpaConfiguration;
    }
}
