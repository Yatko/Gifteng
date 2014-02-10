package com.venefica.dao;

import com.venefica.model.Image;
import com.venefica.model.ImageModelType;
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
     * @param modelType the image model type
     * @return id of the saved image
     */
    Long save(Image image, ImageModelType modelType) throws IOException;

    /**
     * Get the image by its id.
     *
     * @param imageId the id of the image
     * @param modelType 
     * @return image object
     */
    Image get(Long imageId, ImageModelType modelType) throws IOException;
    
    /**
     * Get the resized image by its id. If the suffix is null the original image
     * will be returned.
     *
     * @param imageId the id of the image
     * @param modelType 
     * @param suffix the resized image suffix (marking the size)
     * @return image object
     */
    Image get(Long imageId, ImageModelType modelType, String suffix) throws IOException;

    /**
     * Removes image from the database.
     *
     * @param image the image to remove
     * @param modelType 
     */
    void delete(Image image, ImageModelType modelType);
    
    /**
     * Gets all the images that exists in the database,
     * 
     * @return 
     */
    List<Image> getAll();
}
