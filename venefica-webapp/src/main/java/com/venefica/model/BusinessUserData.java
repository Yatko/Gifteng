/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import javax.persistence.Entity;
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
    
    private String businessName;
    private String contactName;
    private Boolean verified;

    public BusinessUserData() {
        super();
    }
    
    public BusinessUserData(String businessName) {
        this();
        this.businessName = businessName;
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
    
}
