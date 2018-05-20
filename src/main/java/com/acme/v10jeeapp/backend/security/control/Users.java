package com.acme.v10jeeapp.backend.security.control;

import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.deltaspike.data.api.EntityManagerDelegate;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface Users extends EntityRepository<User, Long>, EntityManagerDelegate<User> {
    User findByUsername(String username);
}
