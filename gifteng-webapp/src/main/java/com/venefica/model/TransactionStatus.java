/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 * User transaction finalization reasons.
 * 
 * @author gyuszi
 */
public enum TransactionStatus {
    
    NONE, //transaction having no status
    DELETED, //ad removed - no pending point were added (for the owner and for all the requestors)
    CANCELED, //request canceled - pending points remained on requestor side
    RECEIVED, //transaction pending points added to requestor score
    SENT, //transaction pending points added to owner score
    EXPIRED, //ad expired - no pending point will be added (for the owner and for all the requestors)
    ;
    
}
