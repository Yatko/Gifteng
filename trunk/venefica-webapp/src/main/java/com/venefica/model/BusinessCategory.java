/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//import javax.persistence.SequenceGenerator;

/**
 * Describes a business category.
 *
 * @author gyuszi
 */
@Entity
@Table(name = "businesscategory")
//@SequenceGenerator(name = "business_cat_gen", sequenceName = "business_cat_seq", allocationSize = 1)
public class BusinessCategory {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_cat_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private boolean hidden;
    
    public BusinessCategory() {
    }
    
    public BusinessCategory(String name) {
        this.name = name;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
