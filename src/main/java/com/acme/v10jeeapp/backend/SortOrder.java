package com.acme.v10jeeapp.backend;

import org.apache.deltaspike.data.api.QueryResult;

public class SortOrder {
    private final String property;
    private final boolean ascending;

    public SortOrder(String property, boolean ascending) {
        this.property = property;
        this.ascending = ascending;
    }

    public void apply(QueryResult<?> queryResult) {
        if (ascending) {
            queryResult.orderAsc(property);
        } else {
            queryResult.orderDesc(property);
        }
    }
}
