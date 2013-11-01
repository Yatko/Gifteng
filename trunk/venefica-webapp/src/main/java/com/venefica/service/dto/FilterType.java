/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

/**
 *
 * @author gyuszi
 */
public enum FilterType {
    
    //- gifts that are available to request AND gifts that I requested but owner didn’t selected a recipient yet
    //- my gifts that have less than three request
    //- gifts that I can still request (other words: gift has less the 3 requests)
    //- show those gifts that have 0 or less than 3 requests AND has no accepted recipient
    ACTIVE,
    //gifts that are in SENT or RECEIVED status (other words: finalized)
    GIFTED,
    ;
    
}
