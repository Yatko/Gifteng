/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gyuszi
 */
public class FileUpload {
    
    private static final Log logger = LogFactory.getLog(FileUpload.class);
    
    private String path;
    private boolean deleteOnExit = false;
    
    public void save(Long imgId, byte[] data) throws IOException {
        File file = buildFile(imgId);
        FileUtils.writeByteArrayToFile(file, data);
    }
    
    public void delete(Long imgId) {
        File file = buildFile(imgId);
        boolean result = FileUtils.deleteQuietly(file);
        if ( !result ) {
            logger.warn("File (imgId: " + imgId + ") delete failed");
        }
    }
    
    public byte[] getData(Long imgId) throws IOException {
        File file = buildFile(imgId);
        return FileUtils.readFileToByteArray(file);
    }
    
    public void setPath(String path) {
        if ( path == null || path.trim().isEmpty() ) {
            logger.error("Path must have valid value.");
            return;
        }
        this.path = path.trim();
    }
    
    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }
    
    private File buildFile(Long imgId) {
        String target = buildFileName(imgId);
        return buildFile(target);
    }
    
    private File buildFile(String target) {
        File file = new File(target);
        if ( deleteOnExit ) {
            file.deleteOnExit();
        }
        return file;
    }
    
    private String buildFileName(Long imgId) {
        String fileName = path + (path.endsWith("/") ? "" : "/") + imgId;
        return fileName;
    }
}
