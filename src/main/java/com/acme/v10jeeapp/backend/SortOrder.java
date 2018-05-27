package com.acme.v10jeeapp.backend;

public class SortOrder {
    private final String property;
    private final boolean ascending;

    public SortOrder(String property, boolean ascending) {
        this.property = property;
        this.ascending = ascending;
    }

    public String getProperty() {
        return property;
    }

    public boolean isAscending() {
        return ascending;
    }

}
