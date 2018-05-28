package com.acme.v10jeeapp.backend.security.entity;

import com.acme.v10jeeapp.backend.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(
        name = User.FILTERED_FETCH_ROLES,
        query = "SELECT e FROM User e LEFT JOIN FETCH e.roles " +
                "WHERE e.username like :filter")
public class User extends AbstractEntity {
    public static final String FILTERED_FETCH_ROLES = "filteredFetchRoles";

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String salt;
    private String hash;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();
    @Version
    private int version;

    @Override
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> role) {
        this.roles = role;
    }
}
