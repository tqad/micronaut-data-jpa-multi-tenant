package com.example.repo;

import java.util.Optional;

import com.example.entity.Article;
import com.example.entity.ScenarioType;
import com.example.multitenant.MultiTenantResolver;
import com.example.multitenant.Tenant;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * ArticleRepositoryTest.java
 *
 * Created on 04.09.2022
 *
 * @author Tariq Ahmed
 */
@MicronautTest(transactional = false)
class ArticleRepositoryTest {

    public static final String ALPHA_TENANT = "default";
    public static final String BETA_TENANT = "beta";

    @Inject
    ArticleRepository articleRepository;

    @Inject
    MultiTenantResolver resolveCurrentTenant;

    @ParameterizedTest
    @EnumSource(Tenant.class)
    public void testFindByIdNoResult(Tenant tenant) {
        when(resolveCurrentTenant.resolveCurrentTenantIdentifier()).thenReturn(tenant.getValue());
        final Optional<Article> result = articleRepository.findById(999999L);
        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @EnumSource(Tenant.class)
    public void testFindById(Tenant tenant) {
        when(resolveCurrentTenant.resolveCurrentTenantIdentifier()).thenReturn(tenant.getValue());

        final Optional<Article> result = articleRepository.findById(1L);
        assertTrue(result.isPresent());
        assertThat(result.get().getName(), is("Generic Article"));
        assertThat(result.get().getScenarioTypes(), containsInAnyOrder(ScenarioType.NEW_CAR, ScenarioType.AFTER_SALES));
    }

    @Test
    public void testFindByIdAlpha() {
        when(resolveCurrentTenant.resolveCurrentTenantIdentifier()).thenReturn(Tenant.ALPHA.getValue());

        final Optional<Article> result = articleRepository.findById(10L);
        assertTrue(result.isPresent());
        assertThat(result.get().getName(), containsStringIgnoringCase("Alpha Article"));
    }

    @Test
    public void testFindByIdBeta() {
        when(resolveCurrentTenant.resolveCurrentTenantIdentifier()).thenReturn(Tenant.BETA.getValue());

        final Optional<Article> result = articleRepository.findById(100L);
        assertTrue(result.isPresent());
        assertThat(result.get().getName(), containsStringIgnoringCase("Beta Article"));
    }

    @Test
    public void updateByIdAlpha() {
        when(resolveCurrentTenant.resolveCurrentTenantIdentifier()).thenReturn(Tenant.ALPHA.getValue());

        final Optional<Article> articleToUpdateOpt = articleRepository.findById(11L);
        assertTrue(articleToUpdateOpt.isPresent());

        final Article articleToUpdate = articleToUpdateOpt.get();
        final String updatedName = "Alpha Article updated";

        articleToUpdate.setName(updatedName);
        final Article result = articleRepository.update(articleToUpdate);
        assertThat(result.getName(), is(updatedName));
    }


    @MockBean(named = "currentTenantResolver")
    MultiTenantResolver mockCurrentTenantIdentifierResolver() {
        MultiTenantResolver resolveCurrentTenant = mock(MultiTenantResolver.class);
        return resolveCurrentTenant;
    }
}