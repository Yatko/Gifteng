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
     * Returns the rating by the user and ad. Only one rating for an ad
     * by the same user.
     *
     * @param fromUserId id of the user who rated
     * @param requestId id of the request
     * @return rating object or null if not found.
     */
    Rating get(Long fromUserId, Long requestId);
    
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
    
    /**
     * Returns all the ratings that the given user received as ad creator.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getReceivedRatingsAsOwner(Long userId);
    
    /**
     * Returns all the ratings that the given user received as requestor.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getReceivedRatingsAsReceiver(Long userId);
    
    /**
     * Returns all the rating that the given user received as owner or requestor.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getAllReceivedRatings(Long userId);
    
    /**
     * Returns all the ratings that the given user sent as ad creator.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getSentRatingsAsOwner(Long userId);
    
    /**
     * Returns all the ratings that the given user sent as requestor.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getSentRatingsAsReceiver(Long userId);
    
    /**
     * Returns all the rating that the given user sent as owner or requestor.
     * 
     * @param userId
     * @return 
     */
    List<Rating> getAllSentRatings(Long userId);
}
