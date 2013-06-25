/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserPoint;

/**
 *
 * @author gyuszi
 */
public interface UserPointDao {
    
    /**
     * Stores the user point in the database.
     *
     * @param userPoint user point object to store
     * @return user point id
     */
    public Long save(UserPoint userPoint);
    
    /**
     * Updates the user point record.
     *
     * @param userPoint updated user point object
     */
    public void update(UserPoint userPoint);
    
}
