# Micronaut - Multi Tenancy by Database

This sample project shows how to use micronaut and hibernate with multi-tenancy on database level.

Included:
- [Micronaut](https://micronaut.io/)
- [Micronaut Hibernate JPA documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate)
- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)
- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)
- [https://www.liquibase.org/](https://www.liquibase.org/)
- [https://www.testcontainers.org/](https://www.testcontainers.org/)

##Update for Micronaut 3.6.1

### The solution
I'm just using the default steps to enable multi-tenancy with hibernate except that I was forced to use a programmatic approach to configure the default jpa configuration.

Configuration in application.yml:
* ```Datasource``` Configuration for every tenant
* Default ```JpaConfiguration``` to define which entity packages should be scanned (multi tenancy configuration is done by ```JpaConfigurationCreatedEventListener```)

### MultiTenantResolver
Custom implementation of Hibernate ```CurrentTenantIdentifierResolver```.
Determine the current tenant by using Micronaut-TenantResolver.

### MultiTenantConnectionProvider
Custom implementation of Hibernate ```AbstractDataSourceBasedMultiTenantConnectionProviderImpl```.
Provide the tenant related ```DataSource``` by utilizing the Micronaut ```BeanProvider<DataSource>``` and ```DataSourceResolver```.

### JpaConfigurationCreatedEventListener
Programmatic approach to configure the default ```JpaConfiguration``` to set the **multi tenancy** parameter for Hibernate.

### ~~MultiTenantDataEntityManagerFactoryBean~~
_removed - no longer needed_