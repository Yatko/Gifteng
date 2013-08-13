/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Approval;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface ApprovalDao {
    
    /**
     * Saves the approval in the database
     *
     * @param approval approval to save
     * @return id of the saved object
     */
    Long save(Approval approval);
    
    /**
     * Returns a list of approved (positive or negative) for the given ad.
     * @param adId
     * @return 
     */
    List<Approval> getByAd(Long adId);
    
    /**
     * Return (if exists) the approval for the given ad at the given moment
     * defined by the revision number.
     * 
     * @param adId
     * @param revision
     * @return 
     */
    Approval getByAdAndRevision(Long adId, Integer revision);
}
