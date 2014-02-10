/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 *
 * @author gyuszi
 */
public enum UserType {

    GIVER("Giving"),
    RECEIVER("Receiving"),
    ;
    
    private String description;
    
    UserType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
