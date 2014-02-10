/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Invitation;
import java.util.List;

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
     * Updates the invitation.
     *
     * @param invitation updated invitation object
     */
    public void update(Invitation invitation);
    
    /**
     * Finds the invitation by its code.
     *
     * @param code code of the invitation
     * @return invitation object
     */
    public Invitation findByCode(String code);
    
    /**
     * 
     * @param email
     * @return 
     */
    public Invitation findByEmail(String email);
    
    /**
     * Set 'expired' flag to true for all expired invitations in the database.
     */
    void markExpiredInvitations();
    
    /**
     * Returns a list of un-expired invitations that will expire within
     * the specified number of day. Useful if you want to get all the invitations
     * that will expire soon or having a close expiration date.
     * 
     * @param day number of day to consider
     * @return 
     */
    List<Invitation> getByRemainingDay(int day);
}
