package com.example.entity;

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
