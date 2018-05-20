package com.acme.v10jeeapp.app.dbinit;

import com.acme.v10jeeapp.backend.security.boundary.RoleService;
import com.acme.v10jeeapp.backend.security.boundary.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Collections;

@Singleton
@Startup
public class SecurityInit {
    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;

    @PostConstruct
    private void init() {
        Long adminId = userService.createUser("root", "root");
        roleService.createRole("admin", Collections.singleton("*"));
        userService.grant(adminId, "admin");
    }
}
