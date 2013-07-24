/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Location;

/**
 *
 * @author gyuszi
 */
public interface LocationDao {
    
    /**
     * Finds the location by its zipcode. Null is returned if the zipcode is
     * not present in the database.
     *
     * @param zipcode zipcode of the location
     * @return location object
     */
    public Location findByZipcode(String zipcode);
    
}
