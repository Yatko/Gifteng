package com.venefica.dao;

import com.venefica.model.Review;
import java.util.List;

/**
 * Data access interface for {@link Review} entity.
 * 
 * @author gyuszi
 */
public interface ReviewDao {
    
    /**
     * Saves the review for a user in the database.
     *
     * @param review review to save
     * @return id of the stored review
     */
    Long save(Review review);
    
    /**
     * Searches after an existing review for the given request. Only one
     * review can exist per selected request.
     * 
     * @param requestId
     * @return 
     */
    Review getByRequest(Long requestId);
    
    /**
     * Gets all received reviews for the given user.
     * 
     * @param userId
     * @return list of reviews
     */
    List<Review> getReceivedForUser(Long userId);
    
    /**
     * Gets all created reviews for the given user.
     * 
     * @param userId
     * @return list of reviews
     */
    List<Review> getSentForUser(Long userId);
}
