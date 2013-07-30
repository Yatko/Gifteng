package com.venefica.dao;

import com.venefica.common.FileUpload;
import com.venefica.model.Image;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ImageDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ImageDaoImpl extends DaoBase<Image> implements ImageDao {

    @Inject
    private FileUpload fileUpload;
    
    @Override
    public Long save(Image image) throws IOException {
        Long imageId = saveEntity(image);
        fileUpload.save(imageId, image.getData());
        return imageId;
    }

    @Override
    public Image get(Long imageId) throws IOException {
        Image image = getEntity(imageId);
        if ( image != null && image.getData() == null ) {
            image.setData(fileUpload.getData(image.getId()));
        }
        return image;
    }

    @Override
    public void delete(Image image) throws IOException {
        fileUpload.delete(image.getId());
        deleteEntity(image);
    }
    
    @Override
    public List<Image> getAll() {
        List<Image> images = createQuery(""
                + "from " + getDomainClassName() + " i "
                + "")
                .list();
        
        for ( Image image : images ) {
            if ( image.getData() == null ) {
                Long imageId = image.getId();
                try {
                    image.setData(fileUpload.getData(imageId));
                } catch ( IOException ex ) {
                    logger.warn("Could not get image (imageId: " + imageId + ") data", ex);
                }
            }
        }
        return images;
    }
}
