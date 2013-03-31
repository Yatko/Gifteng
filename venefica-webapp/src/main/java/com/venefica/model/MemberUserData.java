/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 * Contains user data of member type.
 * 
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "memberuserdata_fk")
@Table(name = "memberuserdata")
public class MemberUserData extends UserData {
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    
    private boolean emailsAllowed;
    private boolean smsAllowed;
    private boolean callsAllowed;
    
    public MemberUserData() {
    }
    
    public MemberUserData(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean isBusinessAccount() {
        return false;
    }
    
    @Override
    public boolean isComplete() {
        return firstName != null && lastName != null
                && getPhoneNumber() != null && dateOfBirth != null
                && getCountry() != null && getCity() != null;
        // TODO: place other checks here!!
    }
    
    @Override
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
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
