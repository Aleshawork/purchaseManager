package com.manager.purchaseManager.jpa;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Function;

@Repository
public class JpaRepository {
    @PersistenceContext
    private EntityManager em;

    public <R>  R executeRequest(Function<Session,R> f)throws NoResultException {
        Session session = em.unwrap(Session.class);
        return f.apply(session);
    }

    public EntityManager getEntityManager(){
        return em;
    }
}
