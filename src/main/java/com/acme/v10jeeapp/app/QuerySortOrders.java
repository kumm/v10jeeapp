package com.acme.v10jeeapp.app;

import com.acme.v10jeeapp.backend.SortOrder;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;

import java.util.List;
import java.util.stream.Collectors;

public class QuerySortOrders {

    public static List<SortOrder> toSortOrderList(List<QuerySortOrder> querySortOrders) {
        return querySortOrders.stream()
                .map(QuerySortOrders::toSortOrder)
                .collect(Collectors.toList());
    }

    public static SortOrder toSortOrder(QuerySortOrder querySortOrder) {
        return new SortOrder(querySortOrder.getSorted(),
                querySortOrder.getDirection().equals(SortDirection.ASCENDING));
    }

}
