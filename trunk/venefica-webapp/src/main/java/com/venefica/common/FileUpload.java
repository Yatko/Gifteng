/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.FileConfig;
import com.venefica.model.ImageModelType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author gyuszi
 */
@Component
public class FileUpload {
    
    private static final Log logger = LogFactory.getLog(FileUpload.class);
    
    private static final Long FILE_NOT_FOUND_IMGID = -1L;
    private static final Long ERROR_IMGID = -9L;
    
    @Inject
    private FileConfig fileConfig;
    
    @PostConstruct
    public void init() {
        for ( ImageModelType type : ImageModelType.values() ) {
            if ( type == ImageModelType.ANY ) {
                continue;
            }
            String folderName = buildFolderName(type);
            new File(folderName).mkdirs();
        }
    }
    
    public File save(Long imgId, ImageModelType modelType, byte[] data) throws IOException {
        return save(imgId, modelType, null, data);
    }
    
    public File save(Long imgId, ImageModelType modelType, String suffix, byte[] data) throws IOException {
        File file = buildFile(imgId, modelType, suffix, true);
        FileUtils.writeByteArrayToFile(file, data);
        return file;
    }
    
    public void delete(Long imgId, ImageModelType modelType) {
        delete(imgId, modelType, null);
    }
    
    public void delete(Long imgId, ImageModelType modelType, String suffix) {
        File file = buildFile(imgId, modelType, suffix, false);
        boolean result = FileUtils.deleteQuietly(file);
        if ( !result ) {
            logger.warn("File (imgId: " + imgId + ", suffix: " + suffix + ") delete failed");
        }
    }
    
    public byte[] getData(Long imgId, ImageModelType modelType) throws IOException {
        return getData(imgId, modelType, null);
    }
    
    public byte[] getData(Long imgId, ImageModelType modelType, String suffix) throws IOException {
        File file = buildFile(imgId, modelType, suffix, false);
        try {
            return FileUtils.readFileToByteArray(file);
        } catch ( FileNotFoundException ex ) {
            logger.error("File not found trying to read image (imgId: " + imgId + ", suffix: " + suffix + ")", ex);
            if ( !imgId.equals(FILE_NOT_FOUND_IMGID) ) {
                return getData(FILE_NOT_FOUND_IMGID, modelType, null);
            } else {
                throw ex;
            }
        } catch ( IOException ex ) {
            logger.error("Exception caught when trying to read image (imgId: " + imgId + ", suffix: " + suffix + ")", ex);
            if ( !imgId.equals(ERROR_IMGID) ) {
                return getData(ERROR_IMGID, modelType, null);
            } else {
                throw ex;
            }
        }
    }
    
    // helper methods
    
//    private File buildFile(Long imgId, ImageModelType modelType, boolean isNew) {
//        return buildFile(imgId, modelType, null, isNew);
//    }
    
    private File buildFile(Long imgId, ImageModelType modelType, String suffix, boolean isNew) {
        String target = buildFileName(imgId, modelType, suffix);
        return buildFile(target, isNew);
    }
    
    private File buildFile(String target, boolean isNew) {
        File file = new File(target);
        if ( isNew && fileConfig.isDeleteOnExit() ) {
            file.deleteOnExit();
        }
        return file;
    }
    
//    private String buildFileName(Long imgId, ImageModelType modelType) {
//        return buildFileName(imgId, modelType, null);
//    }
    
    private String buildFileName(Long imgId, ImageModelType modelType, String suffix) {
        if ( modelType == ImageModelType.ANY ) {
            for ( ImageModelType type : ImageModelType.values() ) {
                if ( type == ImageModelType.ANY ) {
                    continue;
                }
                
                String fileName = buildFileName(imgId, type, suffix);
                if ( new File(fileName).exists() ) {
                    return fileName;
                }
            }
            logger.warn("Cannot lookup image for any model type (imgId: " + imgId + ", suffix: " + suffix + ")");
        }
        
        String fileName =
                buildFolderName(modelType) +
                imgId +
                (suffix != null && !suffix.trim().isEmpty() ? "_" + suffix : "");
        return fileName;
    }
    
    private String buildFolderName(ImageModelType modelType) {
        String folderName = modelType.getFolderName();
        String folder =
                fileConfig.getPath() +
                (fileConfig.getPath().endsWith("/") ? "" : "/") +
                (folderName != null ? folderName + "/" : "");
        return folder;
    }
}
