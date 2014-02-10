/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import com.venefica.config.ImageConfig;
import com.venefica.model.ImageModelType;
import com.venefica.model.ImageType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gyuszi
 */
@Named
public class ImageUtils {
    
    protected static final Log logger = LogFactory.getLog(ImageUtils.class);
    
    @Inject
    private ImageConfig imageConfig;
    @Inject
    private FileUpload fileUpload;
    
    public List<File> save(Long imageId, byte[] originalData, ImageModelType modelType, ImageType type) throws IOException {
        File originalFile = fileUpload.save(imageId, modelType, originalData); //original sized image
        
        List<File> files = new ArrayList<File>(0);
        files.add(originalFile);
        
        if ( type.isImage() && !imageConfig.getSizes(modelType).isEmpty() ) {
            BufferedImage originalBufferedImage = ImageIO.read(new ByteArrayInputStream(originalData));
            if ( originalBufferedImage != null && originalBufferedImage.getWidth() > 0 && originalBufferedImage.getHeight() > 0 ) {
                for ( Integer size : imageConfig.getSizes(modelType) ) {
                    String suffix = size.toString();
                    byte[] resized;

                    try {
                        resized = resizeImage(originalBufferedImage, size, type);
                    } catch ( IOException ex ) {
                        logger.error("Exception thrown when trying to generate resized image (imgId: " + imageId + ", suffix: " + suffix + ")", ex);
                        continue;
                    }

                    if ( resized.length == 0 ) {
                        logger.warn("The generated resized image size is 0 (imgId: " + imageId + ", suffix: " + suffix + ")");
                        continue;
                    }

                    try {
                        File file = fileUpload.save(imageId, modelType, suffix, resized);
                        files.add(file);
                    } catch ( IOException ex ) {
                        logger.error("Could not save resized image (imgId: " + imageId + ", suffix: " + suffix + ")", ex);
                    }
                }
            } else {
                logger.error("Could not determine image size (imgId: " + imageId + ")");
            }
        }
        
        return files;
    }
    
    public void delete(Long imageId, ImageModelType modelType) {
        fileUpload.delete(imageId, modelType);
        
        for ( Integer size : imageConfig.getSizes(modelType) ) {
            String suffix = size.toString();
            fileUpload.delete(imageId, modelType, suffix);
        }
    }
    
    // helper methods
    
    private byte[] resizeImage(BufferedImage originalBufferedImage, int newMaxSize, ImageType type) throws IOException {
        double newWidthDouble;
        double newHeightDouble;
        
        double originalWidth = (double) originalBufferedImage.getWidth();
        double originalHeight = (double) originalBufferedImage.getHeight();
        
        newWidthDouble = newMaxSize;
        newHeightDouble = newMaxSize * (originalHeight / originalWidth);
        
        /**
        if ( originalWidth == originalHeight ) {
            //square
            newWidthDouble = newMaxSize;
            newHeightDouble = newMaxSize;
        } else if ( originalWidth < originalHeight ) {
            //portrait
            newWidthDouble = newMaxSize * (originalWidth / originalHeight);
            newHeightDouble = newMaxSize;
        } else {
            //landscape
            newWidthDouble = newMaxSize;
            newHeightDouble = newMaxSize * (originalHeight / originalWidth);
        }
        /**/
        
        int newWidth = (int) Math.ceil(newWidthDouble);
        int newHeight = (int) Math.ceil(newHeightDouble);
        
        //Reference:
        //https://code.google.com/p/java-image-scaling/wiki/Getting_started
        
        ResampleOp resampleOp = new ResampleOp(newWidth, newHeight);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
        BufferedImage resizedImage = resampleOp.filter(originalBufferedImage, null);
        
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, type.getFormatName(), baos);
            baos.flush();
            
            byte[] newData = baos.toByteArray();
            return newData;
        } finally {
            if ( baos != null ) {
                baos.close();
            }
        }
    }
}
