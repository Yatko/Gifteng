/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "user_social_point")
public class UserSocialPoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @OneToMany(mappedBy = "userSocialPoint")
    private Set<UserSocialActivity> socialActivities;
    
    private int socialPoint;

    public UserSocialPoint() {
    }
    
    // helper methods
    
    public void incrementSocialPoint() {
        incrementSocialPoint(1);
    }
    
    public void decrementSocialPoint() {
        incrementSocialPoint(-1);
    }
    
    public void incrementSocialPoint(int value) {
        socialPoint = socialPoint + value;
    }
    
    public void decrementSocialPoint(int value) {
        incrementSocialPoint(-value);
    }
    
    // getters/setters
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Set<UserSocialActivity> getSocialActivities() {
        return socialActivities;
    }

    public void setSocialActivities(Set<UserSocialActivity> socialActivities) {
        this.socialActivities = socialActivities;
    }

    public int getSocialPoint() {
        return socialPoint;
    }

    private void setSocialPoint(int socialPoint) {
        this.socialPoint = socialPoint;
    }
}
