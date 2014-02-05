/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.ShippingBox;
import java.util.List;

/**
 *
 * @author gyuszi
 */
public interface ShippingBoxDao {
    
    ShippingBox get(Long boxId);
    
    List<ShippingBox> getAllShippingBoxes();
}
