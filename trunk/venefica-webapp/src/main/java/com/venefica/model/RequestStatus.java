/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 * The 'lifecycle' of an ad request.
 * 
 * @author gyuszi
 */
public enum RequestStatus {
    
    PENDING, //REQUESTED - giver didn't make a decision
    EXPIRED, //REJECTED - if someone else selected
    ACCEPTED, //selected
    ;
}
