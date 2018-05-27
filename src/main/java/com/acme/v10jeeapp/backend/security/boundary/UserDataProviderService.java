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
        QueryResult<User> queryResult = users.findAllFetchRoles(offset, limit);
        sortBy.forEach(order -> order.apply(queryResult));
        return queryResult.getResultList();
    }

    @Override
    public int count(String filter) {
        return users.count().intValue();
    }

}
