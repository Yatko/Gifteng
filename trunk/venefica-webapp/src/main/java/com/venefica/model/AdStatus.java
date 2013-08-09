/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 * The 'lifecycle' of an ad.
 * 
 * The following possible next statuses are allowed:
 * - OFFLINE -> ACTIVE
 * - ACTIVE -> EXPIRED, IN_PROGRESS
 * - IN_PROGRESS -> ACTIVE, EXPIRED, FINALIZED
 * - EXPIRED -> ACTIVE
 * - FINALIZED
 * 
 * @author gyuszi
 */
public enum AdStatus {
    
    OFFLINE, //unapproved ad status (status will be changed by admin user approval)
    ACTIVE, //there is no active request
    IN_PROGRESS, //there is an (one or more) active request for this ad
    FINALIZED,
    EXPIRED,
    
}
