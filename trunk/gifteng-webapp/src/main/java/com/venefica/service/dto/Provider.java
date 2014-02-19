/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

/**
 *
 * @author gyuszi
 */
public enum Provider {
    
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    PINTEREST("pinterest"),
    VKONTAKTE("vk"), //probably is not needed
    //... more to come
    ;
    
    private final String name;

    private Provider(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static Provider findByName(String name) {
        if ( name != null ) {
            for ( Provider provider : values() ) {
                if ( provider.getName() != null && provider.getName().equals(name) ) {
                    return provider;
                }
            }
        }
        return null;
    }
}
