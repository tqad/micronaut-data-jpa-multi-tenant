/*
 * MixedArticleRepo.java
 *
 * Created on 13.08.2020
 *
 *
 */
package com.example.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import com.example.entity.Article;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tariq Ahmed
 */
@Repository
public abstract class MixedArticleRepo implements CrudRepository<Article, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixedArticleRepo.class);

    private static final String PERSISTENCE_FETCHGRAPH_PARAM = "javax.persistence.fetchgraph";

    @PersistenceContext
    private final EntityManager entityManager;

    protected MixedArticleRepo(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transactional
    @ReadOnly
    public Optional<Article> findById(final @NotNull Long id) {
        LOGGER.trace("Fetching entity with id {} from Table.", id);
        Map<String, Object> hints = getFullGraphHintMap();
        return Optional.ofNullable(entityManager.find(Article.class, id, hints));
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