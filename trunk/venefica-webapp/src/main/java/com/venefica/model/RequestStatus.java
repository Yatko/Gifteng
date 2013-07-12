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
    DECLINED, //REJECTED - the giver/owner declined the request
    SENT, //the giver/owner clicked on 'Mark as shipped'
    RECEIVED, //the receiver/requestor selected 'Received'
    ;
}
