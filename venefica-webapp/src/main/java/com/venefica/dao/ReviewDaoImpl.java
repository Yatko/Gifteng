package com.venefica.dao;

import com.venefica.model.Review;
import java.util.Date;
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
}
