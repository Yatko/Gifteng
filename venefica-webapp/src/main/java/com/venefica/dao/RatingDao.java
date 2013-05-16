package com.venefica.dao;

import com.venefica.model.Rating;
import java.util.List;

/**
 * Data access interface for {@link Rating} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface RatingDao {

    /**
     * Stores the ad rating in the database.
     *
     * @param rating rating to store
     * @return id of the stored rating
     */
    Long save(Rating rating);

    /**
     * Removes the ad rating from the database.
     *
     * @param rating rating to remove.
     */
    void delete(Rating rating);
    
    /**
     * Returns the rating by the user and ad. Only one rating for an ad
     * by the same user.
     *
     * @param fromUserId id of the user who rated
     * @param adId id of the ad
     * @return rating object or null if not found.
     */
    Rating get(Long fromUserId, Long adId);
    
    /**
     * Returns a list of ratings that were sent to the given user.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getReceivedForUser(Long userId);
    
    /**
     * Returns a list of ratings that were sent by the given user.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getSentByUser(Long userId);
}
