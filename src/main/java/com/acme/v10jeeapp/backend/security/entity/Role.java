package com.acme.v10jeeapp.backend.security.entity;

import com.acme.v10jeeapp.backend.AbstractEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.HashSet;
import java.util.Set;

import static com.acme.v10jeeapp.backend.security.entity.Role.IDS_BY_USER_ID;

@Entity
@NamedQuery(name = IDS_BY_USER_ID,
        query = "select r.id from User u, Role r where r member u.roles and u.id=:userId")
public class Role extends AbstractEntity {
    public static final String IDS_BY_USER_ID = "idsByUserId";
    @Id
    private String id;
    private String name;
    @ElementCollection
    private Set<String> permissions = new HashSet<>();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
