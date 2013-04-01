package com.venefica.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Creates hibernate template by providing session factory from the spring
 * context.
 *
 * @param <D> the domain class
 * @author Sviatoslav Grebenchukov
 */
public class DaoBase<D> {

    protected static final Log logger = LogFactory.getLog(DaoBase.class);
    
    @Inject
    private SessionFactory sessionFactory;
    
    private Class<D> domainClass;
    
    public DaoBase() {
        this.domainClass = getDomainClass();
    }
    
    /**
     * Method useful when creating HQL queries to help when refactoring
     * domain classes.
     * 
     * @return the current domain class simple name.
     */
    protected String getDomainClassName() {
        return getDomainClass().getSimpleName();
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

    protected D getEntity(Serializable id) {
        return (D) getCurrentSession().get(domainClass, id);
    }

    protected Long saveEntity(D entity) {
        return (Long) getCurrentSession().save(entity);
    }
    
    protected void updateEntity(D entity) {
        getCurrentSession().update(entity);
    }
    
    //protected void saveOrUpdateEntity(D entity) {
    //    getCurrentSession().saveOrUpdate(entity);
    //}

    protected void deleteEntity(D entity) {
        getCurrentSession().delete(entity);
    }
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    /**
     *
     * @return the domain class
     */
    private Class<D> getDomainClass() {
        if (this.domainClass == null) {
            this.domainClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return domainClass;
    }
}
