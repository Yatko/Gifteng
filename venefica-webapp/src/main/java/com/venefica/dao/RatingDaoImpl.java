package com.venefica.dao;

import com.venefica.model.Rating;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link RatingDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 *
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RatingDaoImpl extends DaoBase<Rating> implements RatingDao {

    @Override
    public Long save(Rating rating) {
        rating.setRatedAt(new Date());
        return saveEntity(rating);
    }

    @Override
    public void delete(Rating rating) {
        deleteEntity(rating);
    }
    
    @Override
    public Rating get(Long fromUserId, Long adId) {
        // @formatter:off
        return (Rating) createQuery("from " + getDomainClassName() + " r where r.from.id = :fromUserId and r.ad.id = :adId")
                .setParameter("fromUserId", fromUserId)
                .setParameter("adId", adId)
                .uniqueResult();
        // @formatter:off
    }
    
    @Override
    public List<Rating> getReceivedForUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.to.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
    
    @Override
    public List<Rating> getSentByUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.from.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
}
