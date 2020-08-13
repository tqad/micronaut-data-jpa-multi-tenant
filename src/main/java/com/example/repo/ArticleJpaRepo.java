/*
 * ArticleJpaRepo.java
 *
 * Created on 13.08.2020
 *
 * Copyright (C) 2018 Volkswagen AG, All rights reserved.
 */
package com.example.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import com.example.entity.Article;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tariq Ahmed
 */
@Singleton
public class ArticleJpaRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleJpaRepo.class);

    private static final String PERSISTENCE_FETCHGRAPH_PARAM = "javax.persistence.fetchgraph";

    private EntityManager entityManager;

    @Inject
    public ArticleJpaRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional
    public Article save(@NotBlank Article article) {
        entityManager.persist(article);
        return article;
    }

    @ReadOnly
    //@io.micronaut.data.jpa.annotation.EntityGraph(attributePaths = "scenarioTypes")   ignored
    public Optional<Article> findById(Long articleId) {
        Map<String, Object> hints = getFullGraphHintMap();
        return Optional.ofNullable(entityManager.find(Article.class, articleId, hints));
    }

    private Map<String, Object> getFullGraphHintMap() {
        LOGGER.trace("Using Graph {} as hint!", Article.GRAPH_WITH_ALL);
        EntityGraph graph = this.entityManager.getEntityGraph(Article.GRAPH_WITH_ALL);
        Map<String, Object> hints = new HashMap<>();
        // We use fetchgraph – Only the specified attributes are retrieved from the database
        hints.put(PERSISTENCE_FETCHGRAPH_PARAM, graph);
        return hints;
    }
}
