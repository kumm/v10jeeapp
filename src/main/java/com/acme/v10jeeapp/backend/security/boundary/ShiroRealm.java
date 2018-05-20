package com.acme.v10jeeapp.backend.security.boundary;

import com.acme.v10jeeapp.backend.security.control.HashConfig;
import com.acme.v10jeeapp.backend.security.control.Roles;
import com.acme.v10jeeapp.backend.security.control.Users;
import com.acme.v10jeeapp.backend.security.entity.Role;
import com.acme.v10jeeapp.backend.security.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public class ShiroRealm extends AuthorizingRealm {
    private final Users users;
    private final Roles roles;
    private final PrincipalMapper principalMapper;

    @Dependent
    public static class Producer {

        @Produces
        private ShiroRealm createRealm(
                Users users,
                Roles roles,
                HashConfig hashConfig,
                PrincipalMapper principalMapper) {
            ShiroRealm realm = new ShiroRealm(users, roles, principalMapper);
            realm.setRolePermissionResolver(realm::resolvePermissionsInRole);
            realm.setCredentialsMatcher(hashConfig.createMatcher());
            realm.setCacheManager(new MemoryConstrainedCacheManager());
            return realm;
        }
    }

    private ShiroRealm(Users users, Roles roles, PrincipalMapper principalMapper) {
        super();
        this.users = users;
        this.roles = roles;
        this.principalMapper = principalMapper;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = users.findBy(principalMapper.getUserId(principals));
        Set<String> roles = user.getRoles().stream()
                .map(Role::getId).collect(Collectors.toSet());
        return new SimpleAuthorizationInfo(roles);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        if (!(token instanceof UsernamePasswordToken)) {
            throw new UnsupportedTokenException();
        }
        String username = ((UsernamePasswordToken) token).getUsername();
        User user;
        try {
            user = users.findByUsername(username);
        } catch (NoResultException e) {
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(
                principalMapper.toPrincipals(user, getName()),
                user.getHash(),
                new SimpleByteSource(Hex.decode(user.getSalt()))
        );
    }

    private Collection<Permission> resolvePermissionsInRole(String roleString) {
        return roles.findBy(roleString).getPermissions().stream()
                .map(WildcardPermission::new)
                .collect(Collectors.toList());
    }

}
