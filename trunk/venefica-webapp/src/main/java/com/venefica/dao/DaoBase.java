package com.venefica.dao;

import java.io.Serializable;
import javax.inject.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Creates hibernate template by providing session factory from the spring
 * context.
 *
 * @author Sviatoslav Grebenchukov
 */
public class DaoBase {

    @Inject
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected Session newSession() {
        return sessionFactory.openSession();
    }

    protected Query createQuery(String queryString) {
        return getCurrentSession().createQuery(queryString);
    }

    protected Query createFilter(Object collection, String queryString) {
        return getCurrentSession().createFilter(collection, queryString);
    }

    protected Object getEntity(Class<?> clazz, Serializable id) {
        return getCurrentSession().get(clazz, id);
    }

    protected Long saveEntity(Object entity) {
        return (Long) getCurrentSession().save(entity);
    }

    protected void deleteEntity(Object entity) {
        getCurrentSession().delete(entity);
    }
}
