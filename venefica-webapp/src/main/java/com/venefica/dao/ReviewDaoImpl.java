package com.venefica.dao;

import com.venefica.model.Review;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ReviewDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ReviewDaoImpl extends DaoBase<Review> implements ReviewDao {
    
    @Override
    public Long save(Review review) {
        review.setReviewedAt(new Date());
        return saveEntity(review);
    }

    @Override
    public Review getByRequest(Long requestId) {
        // @formatter:off
        return (Review) createQuery("from " + getDomainClassName() + " r where r.request.id = :requestId")
                .setParameter("requestId", requestId)
                .uniqueResult();
        // @formatter:off
    }
    
    @Override
    public List<Review> getReceivedForUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.request.ad.creator.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
    
    @Override
    public List<Review> getSentByUser(Long userId) {
        // @formatter:off
        return createQuery("from " + getDomainClassName() + " r where r.request.user.id = :userId")
                .setParameter("userId", userId)
                .list();
        // @formatter:off
    }
}
