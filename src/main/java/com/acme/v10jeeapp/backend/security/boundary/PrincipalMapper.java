package com.acme.v10jeeapp.backend.security.boundary;

import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PrincipalMapper {

    public PrincipalCollection toPrincipals(User user, String realmName) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(user.getId(), realmName);
        principals.add(user.getUsername(), realmName);
        return principals;
    }

    public Long getUserId(PrincipalCollection principals) {
        return (Long) principals.asList().get(0);
    }

    public String getUsername(PrincipalCollection principals) {
        return (String) principals.asList().get(1);
    }
}
