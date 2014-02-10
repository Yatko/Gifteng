/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Shipping;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface ShippingDao {
    
    Long save(Shipping shipping);
    
    void update(Shipping shipping);
    
    Shipping get(Long shippingId);
    
    Shipping getByRequest(Long requestId);
    
    void delete(Long shippingId);
    
    List<Shipping> getShippings();
}
