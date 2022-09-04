package com.example.entity;

/**
 * ScenarioType.java
 *
 * Created on 13.08.2020
 *
 * @author Tariq Ahmed
 */
public enum ScenarioType {
    AFTER_SALES("aftersales"),
    NEW_CAR("newCar");

    private final String desc;

    ScenarioType(final String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
