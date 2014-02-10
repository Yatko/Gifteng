/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Provider;
import com.venefica.model.UserConnection;

/**
 *
 * @author gyuszi
 */
public interface UserConnectionDao {
    
    UserConnection getByUserId(Provider provider, Long userId);
    
}
