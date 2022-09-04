package com.example.entity;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Article.java
 *
 * Created on 13.08.2020
 *
 * @author Tariq Ahmed
 */
@Entity
@Table(name = "ARTICLE")
@NamedEntityGraph(name = Article.GRAPH_WITH_ALL,
        attributeNodes = {
                @NamedAttributeNode("scenarioTypes")
        }
)
@Data
public class Article {

    public static final String BUSINESS_PREFIX = "ART_";
    public static final String GRAPH_WITH_ALL = "graph.art.with.all";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_ARTICLE")
    @SequenceGenerator(name = "SEQUENCE_ARTICLE", sequenceName = "SEQUENCE_ARTICLE", allocationSize = 1)
    private Long id;

    @Column(name = "business_id")
    private String businessId;

    @Column(updatable = false)
    @NotNull
    private Integer version = 1;

    @NotBlank
    @Column(name = "name", unique = true)
    private String name;

    @NotEmpty
    @ElementCollection(targetClass = ScenarioType.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "ARTICLE_SCENARIO_TYPE", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "scenario_type")
    @Enumerated(EnumType.STRING)
    private Set<ScenarioType> scenarioTypes;
}
