/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 * The 'lifecycle' of an ad.
 * 
 * The following possible next statuses are allowed:
 * - ACTIVE -> EXPIRED, SELECTED
 * - EXPIRED -> ACTIVE, SELECTED
 * - SELECTED -> SENT, ACTIVE
 * - SENT -> RECEIVED, ACTIVE
 * - RECEIVED
 * 
 * @author gyuszi
 */
public enum AdStatus {
    
    ACTIVE,
    EXPIRED,
    SELECTED,
    SENT,
    RECEIVED,
    
}
