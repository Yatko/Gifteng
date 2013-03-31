/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Invitation;

/**
 * Data access interface for {@link Invitation} entity.
 * 
 * @author gyuszi
 */
public interface InvitationDao {
    
    /**
     * Returns the invitation by its id.
     *
     * @param invitationId the id of the invitation
     * @return ad object
     */
    Invitation get(Long invitationId);
    
    /**
     * Saves the invitation into the database.
     * 
     * @param invitation invitation to save
     * @return id of the saved object
     */
    public Long save(Invitation invitation);
    
    /**
     * Finds the invitation by its code.
     *
     * @param code code of the invitation
     * @return invitation object
     */
    public Invitation findByCode(String code);
    
}
