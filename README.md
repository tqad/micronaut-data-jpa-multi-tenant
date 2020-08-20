# Micronaut - Multi Tenancy by Database

This sample project shows how to use micronaut and hibernate with multi-tenancy on database level.

Included: 
-  [Micronaut 2.0.1](https://micronaut.io/)
- [Micronaut Hibernate JPA documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate)
- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)
- [Micronaut Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
- [Micronaut Liquibase Database Migration documentation](https://micronaut-projects.github.io/micronaut-liquibase/latest/guide/index.html)
- [https://www.liquibase.org/](https://www.liquibase.org/)
- [https://www.testcontainers.org/](https://www.testcontainers.org/)

## The solution
I'm just using the default steps to enable multi-tenancy with hibernate except that I was forced to use a programmatic approach to select the right datasource.  

### MultiTenantResolver
To determine the current tenant I'm using micronauts TenantResolver in the Hibernate implementation of CurrentTenantIdentifierResolver.

### MultiTenantConnectionProvider
This class contains the DataSources which were registered in DataEntityManagerFactoryBean.

### MultiTenantDataEntityManagerFactoryBean
This class override the default implementation from micronaut by registering the datasources and add multi tenancy 
properties to jpa before the session build. 