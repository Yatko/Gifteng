package com.venefica.dao;

import com.venefica.model.Review;

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
}
