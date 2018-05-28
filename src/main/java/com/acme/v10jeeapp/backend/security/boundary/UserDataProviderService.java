package com.acme.v10jeeapp.backend.security.boundary;

import com.acme.v10jeeapp.backend.DataProviderService;
import com.acme.v10jeeapp.backend.SortOrder;
import com.acme.v10jeeapp.backend.security.control.Users;
import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.deltaspike.data.api.QueryResult;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserDataProviderService implements DataProviderService<User, String> {
    @Inject
    private Users users;

    @Override
    public List<User> fetch(int offset, int limit, List<SortOrder> sortBy, String filter) {
        QueryResult<User> queryResult = users.findFilteredFetchRoles(
                offset, limit, likePattern(filter));
        sortBy.forEach(order -> order.apply(queryResult));
        return queryResult.getResultList();
    }

    @Override
    public int count(String filter) {
        QueryResult<User> queryResult = users.findFilteredFetchRoles(
                0, 0, likePattern(filter));
        return (int) queryResult.count();
    }

    private String likePattern(String filter) {
        return filter + "%";
    }

}
