/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.config.Constants;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "invitation")
public class Invitation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String code;
    
    private String country;
    private String zipCode;
    private String source;
    private String otherSource;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String ipAddress;
    
    @Column(nullable = false)
    private boolean expired;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;
    
    private int numAvailUse;

    public Invitation() {
        createdAt = new Date();
        numAvailUse = Constants.INVITATION_MAX_ALLOWED_USE;
    }
    
    public Invitation(String code) {
        this();
        this.code = code;
    }
    
    public boolean use() {
        if (numAvailUse <= 0) {
            return false;
        }
        
        numAvailUse--;
        return true;
    }
    
    public boolean isValid() {
        if ( expired ) {
            return false;
        } else if ( numAvailUse <= 0 ) {
            return false;
        }
        return true;
    }
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getNumAvailUse() {
        return numAvailUse;
    }

    public void setNumAvailUse(int numAvailUse) {
        this.numAvailUse = numAvailUse;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOtherSource() {
        return otherSource;
    }

    public void setOtherSource(String otherSource) {
        this.otherSource = otherSource;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
