/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 * Contains user data. Based on user type (business or simple member) the
 * extender contains additional informations (columns). This class contains
 * fields that are in common use for every type.
 *
 * @author gyuszi
 */
@Entity
//@SequenceGenerator(name = "userdata_gen", sequenceName = "userdata_seq", allocationSize = 1)
@Table(name = "userdata")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserData implements Serializable {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userdata_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String phoneNumber;
    private String website;
    
    private String address1;
    private String address2;
    private String city;
    private String county;
    private String country;
    @Column(name = "statee")
    private String state;
    private String area;
    private String zipCode;
    
    @ManyToOne
    @ForeignKey(name = "invitation_fk")
    private Invitation invitation;
    
    public abstract boolean isBusinessAccount();
    
    public abstract boolean isComplete();
    
    public abstract String getFullName();
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }
}
