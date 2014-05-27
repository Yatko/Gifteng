/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public enum ImageModelType {
    
    USER("user"),
    AD("ad"),
    SHIPPING("shipping"),
    ANY(null), //means that image model can be of any type (lookup based on image existence)
    ;
    
    private String folderName;
    
    ImageModelType(String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderName() {
        return folderName;
    }
    
    public boolean isValidType() {
        return !isInvalidType();
    }
    
    public boolean isInvalidType() {
        return (this == ANY);
    }
    
    public static List<ImageModelType> getValidTypes() {
        List<ImageModelType> result = new ArrayList<ImageModelType>(0);
        for ( ImageModelType type : ImageModelType.values() ) {
            if ( type.isValidType() ) {
                result.add(type);
            }
        }
        return result;
    }
}
