/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.ShippingBox;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingBoxDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private String name;
    // out
    private String description;
    // out
    private BigDecimal price;
    // out
    private String paypalUrl;

    // Required for JAX-WS
    public ShippingBoxDto() {
    }
    
    public ShippingBoxDto(ShippingBox shippingBox) {
        this.id = shippingBox.getId();
        this.name = shippingBox.getName();
        this.description = shippingBox.getDescription();
        this.price = shippingBox.getPrice();
        this.paypalUrl = shippingBox.getPaypalUrl();
    }
    
    // getter/setter
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPaypalUrl() {
        return paypalUrl;
    }

    public void setPaypalUrl(String paypalUrl) {
        this.paypalUrl = paypalUrl;
    }
    
}
