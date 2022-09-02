package com.example.repo;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.example.entity.Article;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    @Override
    @EntityGraph(attributePaths = "scenarioTypes")
    public abstract Optional<Article> findById(@NotNull @NonNull Long aLong);
}