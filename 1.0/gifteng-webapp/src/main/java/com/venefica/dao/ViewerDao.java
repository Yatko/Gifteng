package com.venefica.dao;

import com.venefica.model.Viewer;

/**
 * Data access interface for {@link Viewer} entity.
 *
 * @author gyuszi
 */
public interface ViewerDao {

    /**
     * Saves the viewer of an ad in the database.
     *
     * @param viewer viewer to save
     * @return id of the stored viewer
     */
    Long save(Viewer viewer);
}
