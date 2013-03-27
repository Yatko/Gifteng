/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserData;

/**
 * Data access interface for {@link UserData} entity.
 * 
 * @author gyuszi
 */
public interface UserDataDao {
    
    public void saveOrUpdate(UserData userData);
    
}
