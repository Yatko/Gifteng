/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "address")
public class AddressWrapper {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Embedded
    private Address address;

    public AddressWrapper() {
    }
    
    public AddressWrapper(Address address) {
        this.address = address;
    }
    
    // getter/setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
}
