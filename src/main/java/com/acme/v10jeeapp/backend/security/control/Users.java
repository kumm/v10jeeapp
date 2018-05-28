package com.acme.v10jeeapp.backend.security.control;

import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.deltaspike.data.api.*;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@Repository
public interface Users extends
        EntityRepository<User, Long>, EntityManagerDelegate<User> {

    User findByUsername(String username);

    @Query(named = User.FILTERED_FETCH_ROLES)
    QueryResult<User> findFilteredFetchRoles(
            @FirstResult int offset,
            @MaxResults int limit,
            @QueryParam("filter") String filter);
}
