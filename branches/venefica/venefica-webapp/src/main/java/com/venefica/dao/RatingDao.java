package com.venefica.dao;

import com.venefica.model.Rating;

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
}
