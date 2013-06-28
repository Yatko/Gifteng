/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Request;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RequestDaoImpl extends DaoBase<Request> implements RequestDao {

    @Override
    public Long save(Request request) {
        request.setRequestedAt(new Date());
        return saveEntity(request);
    }

    @Override
    public Request get(Long requestId) {
        return getEntity(requestId);
    }
    
    @Override
    public Request get(Long userId, Long adId) {
        // @formatter:off
        return (Request) createQuery("from " + getDomainClassName() + " r where r.deleted = false and r.user.id = :userId and r.ad.id = :adId")
                .setParameter("userId", userId)
                .setParameter("adId", adId)
                .uniqueResult();
        // @formatter:off
    }
    
    @Override
    public List<Request> getByAd(Long adId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.deleted = false and r.ad.id = :adId")
                .setParameter("adId", adId)
                .list();
        // @formatter:off
    }
    
    @Override
    public List<Request> getByUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.deleted = false and r.user.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
    
    @Override
    public List<Request> getForUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.deleted = false and r.ad.creator.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
    
    @Override
    public void hide(Long requestId) {
        Request request = getEntity(requestId);
        request.setHidden(true);
        request.setHideAt(new Date());
        updateEntity(request);
    }
}
