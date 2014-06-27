/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.UserDto;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Contains user data of business type.
 *
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "businessuserdata_fk")
@Table(name = BusinessUserData.TABLE_NAME)
public class BusinessUserData  extends UserData {
    
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "businessuserdata";
    
    @Column(unique = true, nullable = false)
    @Index(name = "idx_businessName")
    private String businessName;
    @Index(name = "idx_contactName")
    private String contactName;
    private Boolean verified;
    private String contactNumber;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "business_category_fk")
    private BusinessCategory category;
    
    @OneToMany
    @ForeignKey(name = "businessuserdata_userdata_fk", inverseName = "businessuserdata_address_fk")
    private Set<AddressWrapper> addresses;

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
        contactNumber = userDto.getContactNumber();
        addresses = AddressWrapper.toAddressWrappers(userDto.getAddresses());
    }

    @Override
    public void updateUserDto(UserDto userDto) {
        userDto.setBusinessName(businessName);
        userDto.setContactName(contactName);
        userDto.setContactNumber(contactNumber);
        userDto.setAddresses(AddressDto.toAddressDtos(addresses));
        
        if ( category != null ) {
            userDto.setBusinessCategoryId(category.getId());
            userDto.setBusinessCategory(category.getName());
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
            addresses = new HashSet<AddressWrapper>(0);
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

    public Set<AddressWrapper> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressWrapper> addresses) {
        this.addresses = addresses;
    }

    public BusinessCategory getCategory() {
        return category;
    }

    public void setCategory(BusinessCategory category) {
        this.category = category;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
}
