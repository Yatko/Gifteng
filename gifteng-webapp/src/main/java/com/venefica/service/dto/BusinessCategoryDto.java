/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.BusinessCategory;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Business category data transfer object.
 * 
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessCategoryDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private String name;

    public BusinessCategoryDto() {
    }
    
    public BusinessCategoryDto(BusinessCategory category) {
        this.id = category.getId();
        this.name = category.getName();
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
}
