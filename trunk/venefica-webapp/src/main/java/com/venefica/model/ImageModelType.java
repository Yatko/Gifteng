/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 *
 * @author gyuszi
 */
public enum ImageModelType {
    
    USER("user"),
    AD("ad"),
    ANY(null), //means that image model can be of any type (lookup based on image existence)
    ;
    
    private String folderName;
    
    ImageModelType(String folderName) {
        this.folderName = folderName;
    }
    
    public String getFolderName() {
        return folderName;
    }
}
