/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.BusinessUserData;
import com.venefica.model.UserData;

/**
 * Data access interface for {@link UserData} entity.
 * 
 * @author gyuszi
 */
public interface UserDataDao {
    
    /**
     * Stores the user data in the database.
     *
     * @param userData user data object to store
     * @return user data id
     */
    public Long save(UserData userData);
    
    /**
     * Updates the user data.
     *
     * @param userData updated user data object
     */
    public void update(UserData userData);
    
    /**
     * Finds business user data by name.
     * 
     * @param name name of the business
     * @return business user data object
     */
    public BusinessUserData findByBusinessName(String name);
}
