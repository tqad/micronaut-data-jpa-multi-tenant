package com.example.multitenant;

/**
 * Tenant.java
 * <p>
 * Created on 04.09.22
 */
public enum Tenant {
    ALPHA("default"),
    BETA("beta");

    private final String value;

    Tenant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
