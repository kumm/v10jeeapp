package com.acme.v10jeeapp.backend.security.boundary;

import com.acme.v10jeeapp.backend.security.control.Roles;
import com.acme.v10jeeapp.backend.security.entity.Role;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Set;

@Stateless
public class RoleService {
    @Inject
    private Roles roles;

    public void createRole(String id, Set<String> permissions) {
        Role role = new Role();
        role.setId(id);
        role.getPermissions().addAll(permissions);
        roles.persist(role);
    }
}
