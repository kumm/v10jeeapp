package com.acme.v10jeeapp.backend;

import org.apache.deltaspike.data.api.QueryResult;

import java.util.List;
import java.util.function.Function;

public class SortOrders {

    public static void applySortOrder(SortOrder sortOrder,
                                QueryResult<?> queryResult,
                                Function<String, String> propertyToPath) {
        String path = propertyToPath.apply(sortOrder.getProperty());
        if (sortOrder.isAscending()) {
            queryResult.orderAsc(path);
        } else {
            queryResult.orderDesc(path);
        }
    }

    public static void applySortOrderList(List<SortOrder> sortOrderList,
                                QueryResult<?> queryResult,
                                Function<String, String> propertyToPath) {
        sortOrderList.forEach(sortOrder
                -> applySortOrder(sortOrder, queryResult, propertyToPath));
    }

}
