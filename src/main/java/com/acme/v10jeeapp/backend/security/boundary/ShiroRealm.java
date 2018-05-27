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

import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public class ShiroRealm extends AuthorizingRealm {
    private final RealmService service;

    @Dependent
    public static class Producer {

        @Produces
        private ShiroRealm createRealm(
                RealmService service,
                HashConfig hashConfig) {
            ShiroRealm realm = new ShiroRealm(service);
            realm.setRolePermissionResolver(service::resolvePermissionsInRole);
            realm.setCredentialsMatcher(hashConfig.createMatcher());
            realm.setCacheManager(new MemoryConstrainedCacheManager());
            return realm;
        }
    }

    private ShiroRealm(RealmService service) {
        super();
        this.service = service;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return service.doGetAuthorizationInfo(principals);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        if (!(token instanceof UsernamePasswordToken)) {
            throw new UnsupportedTokenException();
        }
        String username = ((UsernamePasswordToken) token).getUsername();
        return service.doGetAuthenticationInfo(username, getName());
    }

    @Stateless
    public static class RealmService {
        @Inject
        private Users users;
        @Inject
        private Roles roles;
        @Inject
        private PrincipalMapper principalMapper;


        public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
            Long userId = principalMapper.getUserId(principals);
            Set<String> roleIds = users.findBy(userId).getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            return new SimpleAuthorizationInfo(roleIds);
        }

        public AuthenticationInfo doGetAuthenticationInfo(String username, String realmName)
                throws UnknownUserException {
            User user;
            try {
                user = users.findByUsername(username);
            } catch (NoResultException e) {
                throw new UnknownUserException();
            }
            return new SimpleAuthenticationInfo(
                    principalMapper.toPrincipals(user, realmName),
                    user.getHash(),
                    new SimpleByteSource(Hex.decode(user.getSalt()))
            );
        }

        public Collection<Permission> resolvePermissionsInRole(String roleString) {
            return roles.findBy(roleString).getPermissions().stream()
                    .map(WildcardPermission::new)
                    .collect(Collectors.toList());
        }
    }

    @ApplicationException
    public static class UnknownUserException extends UnknownAccountException {
    }
}
