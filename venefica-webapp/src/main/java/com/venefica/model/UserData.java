/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Contains user data.
 * 
 * @author gyuszi
 */
@Embeddable
@SuppressWarnings("serial")
public class UserData implements Serializable {
    
    @Temporal(TemporalType.DATE)
    private Date joinedAt;
    
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String firstName;
    private String lastName;
    
    private String phoneNumber;
    
    private String country;
    private String city;
    private String area;
    private String zipCode;
    
    private boolean emailsAllowed;
    private boolean smsAllowed;
    private boolean callsAllowed;
    
    public UserData() {
    }
    
    public UserData(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
    
    public boolean areEmailsAllowed() {
        return emailsAllowed;
    }

    public void setEmailsAllowed(boolean emailsAllowed) {
        this.emailsAllowed = emailsAllowed;
    }

    public boolean areSmsAllowed() {
        return smsAllowed;
    }

    public void setSmsAllowed(boolean smsAllowed) {
        this.smsAllowed = smsAllowed;
    }

    public boolean areCallsAllowed() {
        return callsAllowed;
    }

    public void setCallsAllowed(boolean callsAllowed) {
        this.callsAllowed = callsAllowed;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
}
