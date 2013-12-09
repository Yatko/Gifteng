/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserVerification;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface UserVerificationDao {
    
    /**
     * 
     * @param userVerification
     * @return 
     */
    public Long save(UserVerification userVerification);
    
    /**
     * 
     * @param code
     * @return 
     */
    public UserVerification findByCode(String code);
    
    /**
     * 
     * @param userId
     * @return 
     */
    public UserVerification findByUser(Long userId);
    
    /**
     * 
     * @return 
     */
    public List<UserVerification> getAllUnverified();
}
