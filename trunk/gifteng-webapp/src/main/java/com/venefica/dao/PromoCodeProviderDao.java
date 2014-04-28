/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.PromoCodeProvider;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface PromoCodeProviderDao {
    
    PromoCodeProvider get(Long providerId);
    
    List<PromoCodeProvider> getProviders();
    
}
