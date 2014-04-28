/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.UserDto;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Contains user data of member type.
 * 
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "memberuserdata_fk")
@Table(name = "memberuserdata")
public class MemberUserData extends UserData {
    
    //personal data
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Index(name = "idx_firstName")
    private String firstName;
    @Index(name = "idx_lastName")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    
    @OneToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "usersetting_fk")
    private UserSetting userSetting;
    
    @OneToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "usersocialpoint_fk")
    private UserSocialPoint userSocialPoint;
    
    public MemberUserData() {
        super();
    }
    
    public MemberUserData(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void updateUser(UserDto userDto) {
        dateOfBirth = userDto.getDateOfBirth();
        lastName = userDto.getLastName();
        firstName = userDto.getFirstName();
        gender = userDto.getGender();
    }

    @Override
    public void updateUserDto(UserDto userDto) {
        userDto.setDateOfBirth(dateOfBirth);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setGender(gender);
    }
    
    @Override
    public boolean isBusinessAccount() {
        return false;
    }
    
    @Override
    public boolean isComplete() {
        return firstName != null && lastName != null && phoneNumber != null
                && dateOfBirth != null
                && address != null && address.getCountry() != null && address.getCity() != null;
        // TODO: place other checks here!!
    }
    
    @Override
    public String getFullName() {
        if ( firstName != null && lastName != null ) {
            return firstName + " " + lastName;
        } else if ( firstName != null ) {
            return firstName;
        } else if ( lastName != null ) {
            return lastName;
        } else {
            return "";
        }
    }
    
    // getters/setters
    
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

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    public UserSocialPoint getUserSocialPoint() {
        return userSocialPoint;
    }

    public void setUserSocialPoint(UserSocialPoint userSocialPoint) {
        this.userSocialPoint = userSocialPoint;
    }
}
