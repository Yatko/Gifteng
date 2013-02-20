package com.venefica.dao;

import com.venefica.model.SpamMark;

/**
 * Data access interface for {@link SpamMark} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface SpamMarkDao {

    /**
     * Saves the spam mark in the database.
     *
     * @param mark mark to save
     * @return id of the stored mark
     */
    Long save(SpamMark mark);

    /**
     * Removes the spam mark from the database.
     *
     * @param mark mark to remvoe
     */
    void delete(SpamMark mark);
}
