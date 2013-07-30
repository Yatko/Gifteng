/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.file;

import com.venefica.common.FileUpload;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/application.properties")
public class FileConfig {
    
    @Inject
    private Environment environment;
    
    @Bean
    public FileUpload fileUpload() {
        String path = environment.getProperty("upload.path");
        boolean deleteOnExit = environment.getProperty("upload.deleteOnExit", boolean.class, false);
        
        FileUpload fileUpload = new FileUpload();
        fileUpload.setPath(path);
        fileUpload.setDeleteOnExit(deleteOnExit);
        return fileUpload;
    }
    
}
