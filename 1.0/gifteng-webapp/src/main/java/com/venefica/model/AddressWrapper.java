/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AddressDto;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    private String name;
    private boolean deleted;
    
    @Embedded
    private Address address;

    public AddressWrapper() {
    }
    
    public AddressWrapper(Address address) {
        this.address = address;
    }
    
    // helper methods
    
    public static Set<AddressDto> toAddressDtos(Set<AddressWrapper> addresses) {
        return AddressDto.toAddressDtos(addresses);
    }
    
    public static Set<AddressWrapper> toAddressWrappers(Set<AddressDto> addressesDto) {
        if ( addressesDto == null || addressesDto.isEmpty() ) {
            return null;
        }
        
        Set<AddressWrapper> addresses = new LinkedHashSet<AddressWrapper>();
        for ( AddressDto addressDto : addressesDto ) {
            if ( addressDto == null ) {
                continue;
            }
            AddressWrapper addressWrapper = addressDto.toAddressWrapper();
            addresses.add(addressWrapper);
        }
        return addresses;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
