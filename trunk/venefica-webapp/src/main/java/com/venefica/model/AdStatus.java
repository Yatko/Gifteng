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
    ACTIVE, //there is no active requests - logically is INACTIVE or something similar (TODO: needs refactoring)
    IN_PROGRESS, //there is an (one or more) active request for this ad
    FINALIZED, //sold out
    EXPIRED,
    ;
    
    /**
     * An ad is considered inactive if its status is other than ACTIVE and IN_PROGRESS.
     * 
     * @return 
     */
    public boolean isInactive() {
        if ( this == OFFLINE || this == FINALIZED || this == EXPIRED ) {
            return true;
        }
        //ACTIVE or IN_PROGRESS
        return false;
    }
}
