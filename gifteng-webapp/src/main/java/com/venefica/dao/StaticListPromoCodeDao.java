/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.StaticListPromoCode;

/**
 *
 * @author gyuszi
 */
public interface StaticListPromoCodeDao {
    
    void update(StaticListPromoCode staticListPromoCode);
    
    StaticListPromoCode getOneByAd(Long adId);
    
}
