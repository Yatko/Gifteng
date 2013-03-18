package com.venefica.dao;

import com.venefica.model.Image;

/**
 * Data access interface for {@link Image} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface ImageDao {

    /**
     * Saves the image in the database.
     *
     * @param image image to save
     * @return id of the saved image
     */
    Long save(Image image);

    /**
     * Get the image by its id.
     *
     * @param imageId the id of the image
     * @return image object
     */
    Image get(Long imageId);

    /**
     * Removes image from the database.
     *
     * @param image the image to remove
     */
    void delete(Image image);
}
