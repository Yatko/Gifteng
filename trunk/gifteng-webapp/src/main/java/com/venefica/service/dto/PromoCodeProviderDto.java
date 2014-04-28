/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.PromoCodeProvider;
import com.venefica.model.PromoCodeProviderType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PromoCodeProviderDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private PromoCodeProviderType providerType;
    // out
    private String name;
    // out
    private String description;

    public PromoCodeProviderDto() {
    }

    public PromoCodeProviderDto(PromoCodeProvider promoCodeProvider) {
        this.id = promoCodeProvider.getId();
        this.providerType = promoCodeProvider.getProviderType();
        this.name = promoCodeProvider.getName();
        this.description = promoCodeProvider.getDescription();
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

    public PromoCodeProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(PromoCodeProviderType providerType) {
        this.providerType = providerType;
    }
}
