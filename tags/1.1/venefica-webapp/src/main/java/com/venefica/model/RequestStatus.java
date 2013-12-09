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
    
    PENDING, //REQUESTED - giver didn't make a decision (the starter status of a request)
    ACCEPTED, //SELECTED - the receiver/owner acceped the request
    UNACCEPTED, //someone else selected by the owner and the available quantity is 0 (auto status)
    CANCELED, //the receiver/requestor cancelled the request (it's own request)
    DECLINED, //REJECTED - the giver/owner declined the request - no more status change on this request (it will remain as DECLINED forever)
    SENT, //the giver/owner clicked on 'Mark as shipped'
    RECEIVED, //the receiver/requestor selected 'Received'
    
    //WAITING_FOR_PAYMENT, //used at shipping - not yet implemented
    //PAYMENT_RECEIVED, //used at shipping - not yet implemented
    ;
    
    /**
     * If the request is having CANCELED or DECLINED status is considered
     * to be inactive.
     * 
     * @return 
     */
    public boolean isActive() {
        switch ( this ) {
            case PENDING:
            case ACCEPTED:
            case UNACCEPTED:
            case SENT:
            case RECEIVED:
                return true;
        }
        return false;
    }
}
