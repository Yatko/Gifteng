/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.UserDto;
import com.vividsolutions.jts.geom.Point;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

/**
 * Contains user data. Based on user type (business or simple member) the
 * extender contains additional informations (columns). This class contains
 * fields that are in common use for every type.
 *
 * @author gyuszi
 */
@Entity
@Table(name = "userdata")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Index(name = "idx_phoneNumber")
    protected String phoneNumber;
    private String website;
    private String about;
    
    @Column(columnDefinition = "Geometry")
    @Type(type = "org.hibernate.spatial.GeometryType")
    protected Point location;
    @Embedded
    protected Address address;
    
    @ManyToOne
    @ForeignKey(name = "invitation_fk")
    private Invitation invitation;
    
    public UserData() {
        this.address = new Address();
    }
    
    public abstract void updateUser(UserDto userDto);
    
    public abstract void updateUserDto(UserDto userDto);
    
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

    public Invitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
