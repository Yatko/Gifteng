/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Share;

/**
 *
 * @author gyuszi
 */
public interface ShareDao {
    
    Long save(Share share);
    
    Share get(Long shareId);
    
}
