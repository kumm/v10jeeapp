package com.acme.v10jeeapp.backend;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    @PersistenceUnit
    private EntityManagerFactory emf;

    @PostConstruct
    private void init() {
        // Workaround for persistence providers trying to
        // load metamodel lazily but failing. ( like OpenJpa in TomEE )
        emf.getMetamodel();
    }

}