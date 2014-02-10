/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 *
 * @author gyuszi
 */
public enum ActivityType {

    LOGIN_USER,
    LOGOUT_USER,
    REGISTER_USER,
    UPDATE_USER,
    
    CREATE_AD,
    UPDATE_AD,
    END_AD,
    PUBLISH_AD,
    DELETE_AD,
    RELIST_AD,
    BOOKMARK_AD, //create bookmark
    REMOVE_BOOKMARK_AD, //remove bookmark
    MARK_AD, //spam
    UNMARK_AD, //spam
    RATE_AD, //do we need this?
    SHARE_AD,
    REQUEST_AD,
    
    CREATE_COMMENT,
    UPDATE_COMMENT,
    SHARE_COMMENT,
    
    CREATE_MESSAGE,
    UPDATE_MESSAGE,
    DELETE_MESSAGE,
    
    //there will come more types
    ;
}
