/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import com.venefica.model.ImageModelType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/application.properties")
public class ImageConfig {
    
    @Inject
    private Environment environment;
    
    private Set<Integer> userSizes;
    private Set<Integer> adSizes;
    private boolean deleteImages;
    
    @PostConstruct
    public void init() {
        userSizes = new HashSet<Integer>(0);
        for ( int i = 0; i < 10; i++ ) {
            String sizeKey = "image.size.user." + i;
            if ( !environment.containsProperty(sizeKey) ) {
                continue;
            }
            
            Integer size = environment.getProperty(sizeKey, Integer.class);
            if ( size != null && size > 0 ) {
                userSizes.add(size);
            }
        }
        
        adSizes = new HashSet<Integer>(0);
        for ( int i = 0; i < 10; i++ ) {
            String sizeKey = "image.size.ad." + i;
            if ( !environment.containsProperty(sizeKey) ) {
                continue;
            }
            
            Integer size = environment.getProperty(sizeKey, Integer.class);
            if ( size != null && size > 0 ) {
                adSizes.add(size);
            }
        }
        
        deleteImages = environment.getProperty("image.delete", boolean.class);
    }
    
    public Set<Integer> getSizes(ImageModelType modelType) {
        switch ( modelType ) {
            case AD: return adSizes;
            case USER: return userSizes;
        }
        return Collections.<Integer>emptySet();
    }

    public boolean isDeleteImages() {
        return deleteImages;
    }
}
