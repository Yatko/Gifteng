package com.venefica.dao;

import com.venefica.model.UserActivity;

/**
 * Data access interface for {@link UserActivity} entity.
 *
 * @author gyuszi
 */
public interface UserActivityDao {

    /**
     * Stores the user activity in the database.
     *
     * @param activity activity to store
     * @return id of the stored activity
     */
    Long save(UserActivity activity);
}
