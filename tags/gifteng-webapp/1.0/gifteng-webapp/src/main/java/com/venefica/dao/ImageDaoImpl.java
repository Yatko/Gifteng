package com.venefica.dao;

import com.venefica.common.AmazonUpload;
import com.venefica.common.FileUpload;
import com.venefica.common.ImageUtils;
import com.venefica.config.ImageConfig;
import com.venefica.model.Image;
import com.venefica.model.ImageModelType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
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
    private ImageUtils imageUtils;
    @Inject
    private FileUpload fileUpload;
    @Inject
    private ImageConfig imageConfig;
    @Inject
    private AmazonUpload amazonUpload;
    
    @PostConstruct
    public void init() {
        ImageIO.setUseCache(false);
    }
    
    @Override
    public Long save(Image image, ImageModelType modelType) throws IOException {
        Long imageId = saveEntity(image);
        List<File> files = imageUtils.save(imageId, image.getData(), modelType, image.getImgType());
        
        amazonUpload.upload(files, modelType, image.getImgType());
        
        if ( !amazonUpload.isAsyncUpload() && imageConfig.isDeleteImages() ) {
            imageUtils.delete(imageId, modelType);
        }
        return imageId;
    }

    @Override
    public Image get(Long imageId, ImageModelType modelType) throws IOException {
        return get(imageId, modelType, null);
    }
    
    @Override
    public Image get(Long imageId, ImageModelType modelType, String suffix) throws IOException {
        Image image = getEntity(imageId);
        if ( image != null && image.getData() == null ) {
            File file = fileUpload.buildFile(imageId, modelType, suffix, true);
            amazonUpload.download(file, modelType);
            
            byte[] data = fileUpload.getData(image.getId(), modelType, suffix);
            
            image.setData(data);
            image.setFile(file);
        }
        return image;
    }

    @Override
    public void delete(Image image, ImageModelType modelType) {
        imageUtils.delete(image.getId(), modelType);
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
                    image.setData(fileUpload.getData(imageId, ImageModelType.ANY));
                } catch ( IOException ex ) {
                    logger.warn("Could not get image (imageId: " + imageId + ") data", ex);
                }
            }
        }
        return images;
    }
}
