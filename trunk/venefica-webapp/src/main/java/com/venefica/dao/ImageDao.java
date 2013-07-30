package com.venefica.dao;

import com.venefica.model.Image;
import java.io.IOException;
import java.util.List;

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
    Long save(Image image) throws IOException;

    /**
     * Get the image by its id.
     *
     * @param imageId the id of the image
     * @return image object
     */
    Image get(Long imageId) throws IOException;

    /**
     * Removes image from the database.
     *
     * @param image the image to remove
     */
    void delete(Image image) throws IOException;
    
    /**
     * Gets all the images that exists in the database,
     * 
     * @return 
     */
    List<Image> getAll();
}
