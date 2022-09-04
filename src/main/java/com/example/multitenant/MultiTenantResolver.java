package com.example.multitenant;


import java.io.Serializable;

import io.micronaut.multitenancy.exceptions.TenantNotFoundException;
import io.micronaut.multitenancy.tenantresolver.TenantResolver;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * MultiTenantResolver.java
 *
 * Created on 13.08.2020
 *
 * @author Tariq Ahmed
 */
@Singleton
@Named("currentTenantResolver")
public class MultiTenantResolver implements CurrentTenantIdentifierResolver {
    @Inject
    TenantResolver resolver;

    @Override
    public String resolveCurrentTenantIdentifier() {
        try {
            Serializable tenantIdRaw = resolver.resolveTenantIdentifier();
            String tenantId = (String) tenantIdRaw;
            if(tenantId.equalsIgnoreCase("example")) {
                tenantId = MultiTenantConnectionProvider.DEFAULT_TENANT_ID;
            }
            return tenantId;
        } catch (TenantNotFoundException notFoundException) {
            return MultiTenantConnectionProvider.DEFAULT_TENANT_ID;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
