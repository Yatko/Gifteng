/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity stores all the user points (pending, insider, exclusive).
 * 
 * TODO: not yet finished.
 * 
 * @author gyuszi
 */
@Entity
@Table(name = "user_point")
public class UserPoint {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userdata_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }
}
