/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.ForgotPassword;

/**
 * Data access interface for {@link ForgotPassword} entity.
 * 
 * @author gyuszi
 */
public interface ForgotPasswordDao {
    
    /**
     * Saves the request into the database.
     * 
     * @param forgotPassword request to save
     * @return id of the saved object
     */
    public Long save(ForgotPassword forgotPassword);
    
    /**
     * Finds the forgot password request by its code.
     *
     * @param code code of the request
     * @return request object
     */
    public ForgotPassword findByCode(String code);
    
    /**
     * Set 'expired' flag to true for all expired requests in the database.
     */
    void markExpiredRequests();
}
