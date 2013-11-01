/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.AdData;

/**
 * Data access interface for {@link AdData} entity.
 * 
 * @author gyuszi
 */
public interface AdDataDao {
    
    /**
     * Stores the ad data in the database.
     *
     * @param adData ad data object to store
     * @return ad data id
     */
    public Long save(AdData adData);
    
    /**
     * Updates the ad data.
     *
     * @param adData updated ad data object
     */
    public void update(AdData adData);
    
    /**
     * Returns the corresponding ad data for the given ad.
     * 
     * @param adId
     * @return 
     */
    public AdData getByAd(Long adId);
}
