/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.UserDto;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 * Contains user data of business type.
 *
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "businessuserdata_fk")
@Table(name = "businessuserdata")
public class BusinessUserData  extends UserData {
    
    @Column(unique = true, nullable = false)
    private String businessName;
    private String contactName;
    private Boolean verified;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "business_category_fk")
    private BusinessCategory category;
    
    @OneToMany
    @ForeignKey(name = "businessuserdata_userdata_fk", inverseName = "businessuserdata_address_fk")
    private List<AddressWrapper> addresses;

    public BusinessUserData() {
        super();
    }
    
    public BusinessUserData(String businessName) {
        this();
        this.businessName = businessName;
    }

    @Override
    public void updateUser(UserDto userDto) {
        businessName = userDto.getBusinessName();
        contactName = userDto.getContactName();
        
        if ( userDto.getAddresses() != null && !userDto.getAddresses().isEmpty() ) {
            addresses = new LinkedList<AddressWrapper>();
            for ( AddressDto addressDto : userDto.getAddresses() ) {
                AddressWrapper addressWrapper = addressDto.getAddressWrapper();
                addresses.add(addressWrapper);
            }
        }
    }

    @Override
    public void updateUserDto(UserDto userDto) {
        userDto.setBusinessName(businessName);
        userDto.setContactName(contactName);
        userDto.setBusinessCategoryId(category.getId());
        userDto.setBusinessCategory(category.getName());
        
        if ( addresses != null && !addresses.isEmpty() ) {
            List<AddressDto> addressesDto = new LinkedList<AddressDto>();
            for ( AddressWrapper addressWrapper : addresses ) {
                addressesDto.add(new AddressDto(addressWrapper));
            }
            userDto.setAddresses(addressesDto);
        }
    }
    
    @Override
    public boolean isBusinessAccount() {
        return true;
    }
    
    @Override
    public boolean isComplete() {
        return businessName != null;
    }

    @Override
    public String getFullName() {
        return contactName;
    }
    
    public void addAddress(Address address) {
        initAddresses();
        addresses.add(new AddressWrapper(address));
    }
    
    private void initAddresses() {
        if ( addresses == null ) {
            addresses = new ArrayList<AddressWrapper>(0);
        }
    }
    
    // getters/setters
    
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public List<AddressWrapper> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressWrapper> addresses) {
        this.addresses = addresses;
    }

    public BusinessCategory getCategory() {
        return category;
    }

    public void setCategory(BusinessCategory category) {
        this.category = category;
    }
    
}
