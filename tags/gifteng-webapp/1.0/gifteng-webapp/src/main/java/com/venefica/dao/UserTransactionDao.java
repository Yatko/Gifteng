/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserTransaction;

/**
 *
 * @author gyuszi
 */
public interface UserTransactionDao {
    
    /**
     * Stores the user transaction in the database.
     *
     * @param userPoint user transaction object to store
     * @return user transaction id
     */
    public Long save(UserTransaction userPoint);
    
    /**
     * Updates the user transaction record.
     *
     * @param userPoint updated user transaction object
     */
    public void update(UserTransaction userPoint);
    
    /**
     * Finds the user transaction if any for the given ad.
     * If there is no matching record null will be returned.
     * 
     * @param adId
     * @return 
     */
    public UserTransaction getByAd(Long adId);
    
    /**
     * Finds the user transaction if any for the given request.
     * If there is no matching record null will be returned.
     * 
     * @param requestId
     * @return 
     */
    public UserTransaction getByRequest(Long requestId);
}
