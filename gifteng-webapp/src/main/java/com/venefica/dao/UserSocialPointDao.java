/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSocialPoint;

/**
 *
 * @author gyuszi
 */
public interface UserSocialPointDao {
    
    public Long save(UserSocialPoint socialPoint);
    
    public void update(UserSocialPoint socialPoint);
}
