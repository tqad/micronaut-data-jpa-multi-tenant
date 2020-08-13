package com.example.repo;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.example.entity.Article;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.annotation.EntityGraph;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    @Override
    @EntityGraph(attributePaths = "scenarioTypes")
    public abstract Optional<Article> findById(@NotNull @NonNull Long aLong);
}