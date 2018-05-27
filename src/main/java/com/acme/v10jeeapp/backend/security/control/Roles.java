package com.acme.v10jeeapp.backend.security.control;

import com.acme.v10jeeapp.backend.security.entity.Role;
import org.apache.deltaspike.data.api.EntityManagerDelegate;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@Repository
public interface Roles extends EntityRepository<Role, String>, EntityManagerDelegate<Role> {
}
