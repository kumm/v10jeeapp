package com.acme.v10jeeapp.backend.security.boundary;

import com.acme.v10jeeapp.backend.SortOrder;
import com.acme.v10jeeapp.backend.security.control.HashConfig;
import com.acme.v10jeeapp.backend.security.control.Roles;
import com.acme.v10jeeapp.backend.security.control.Users;
import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.deltaspike.data.api.QueryResult;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.util.ByteSource;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.acme.v10jeeapp.backend.SortOrders.applySortOrderList;

@Stateless
public class UserService {
    @Inject
    private Users users;
    @Inject
    private Roles roles;
    @Inject
    private HashConfig hashConfig;


    private RandomNumberGenerator rng;

    public Long createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        generateSaltedHash(password, user);
        users.persist(user);
        return user.getId();
    }

    public void grant(Long userId, String... role) {
        User user = users.findBy(userId);
        Set<Role> grantedRoles = user.getRoles();
        Stream.of(role).map(this.roles::findBy).forEach(grantedRoles::add);
    }

    public List<User> list(int offset, int limit, List<SortOrder> sortBy) {
        QueryResult<User> queryResult = users.findAllFetchRoles(offset, limit);
        applySortOrderList(sortBy, queryResult,  s -> "u." + s);
        return queryResult.getResultList();
    }

    public int count() {
        return users.count().intValue();
    }

    private void generateSaltedHash(String password, User user) {
        ByteSource saltByteSrc = getRng().nextBytes();
        user.setSalt(saltByteSrc.toHex());
        Hash hash = hashConfig.createHash(password, saltByteSrc);
        user.setHash(hash.toHex());
    }

    private RandomNumberGenerator getRng() {
        if (rng == null) {
            rng = new SecureRandomNumberGenerator();
        }
        return rng;
    }
}
