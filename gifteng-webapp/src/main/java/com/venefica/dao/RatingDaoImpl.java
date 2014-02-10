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
    public Rating get(Long fromUserId, Long requestId) {
        return (Rating) createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.from.id = :userId and "
                + "r.request.id = :requestId"
                + "")
                .setParameter("userId", fromUserId)
                .setParameter("requestId", requestId)
                .uniqueResult();
    }
    
    @Override
    public List<Rating> getReceivedForUser(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.to.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getSentByUser(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.from.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getReceivedRatingsAsOwner(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.request.ad.creator.id = r.to.id and "
                + "r.to.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getReceivedRatingsAsReceiver(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.request.user.id = r.to.id and "
                + "r.to.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getAllReceivedRatings(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "(r.request.ad.creator.id = r.to.id or r.request.user.id = r.to.id ) and "
                + "r.to.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getSentRatingsAsOwner(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.request.ad.creator.id = r.from.id and "
                + "r.from.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getSentRatingsAsReceiver(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "r.request.user.id = r.from.id and "
                + "r.from.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
    
    @Override
    public List<Rating> getAllSentRatings(Long userId) {
        return createQuery(""
                + "from " + getDomainClassName() + " r where "
                + "(r.request.ad.creator.id = r.from.id or r.request.user.id = r.from.id ) and "
                + "r.from.id = :userId "
                + "order by ratedAt desc"
                + "")
                .setParameter("userId", userId)
                .list();
    }
}
