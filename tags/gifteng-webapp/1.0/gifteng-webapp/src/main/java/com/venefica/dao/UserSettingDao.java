/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSetting;

/**
 *
 * @author gyuszi
 */
public interface UserSettingDao {
    
    /**
     * Stores the user settings in the database.
     *
     * @param userSetting user setting object to store
     * @return user setting id
     */
    public Long save(UserSetting userSetting);
    
    /**
     * Updates the user settings record.
     *
     * @param userSetting updated user setting object
     */
    public void update(UserSetting userSetting);
    
    /**
     * Returns the user setting by its id.
     *
     * @param id the id
     * @return userSetting object
     */
    public UserSetting get(Long id);
}
