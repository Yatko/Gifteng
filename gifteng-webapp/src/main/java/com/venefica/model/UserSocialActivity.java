/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "user_social_activity")
public class UserSocialActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "socialactivity_usersocialpoint_fk")
    private UserSocialPoint userSocialPoint;

    @Enumerated(EnumType.STRING)
    private SocialActivityType activityType;
    private String externalRef; //holds various id's, like referrer user id, fb user name etc
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    public UserSocialActivity() {
    }
    
    // getters/setters
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public UserSocialPoint getUserSocialPoint() {
        return userSocialPoint;
    }

    public void setUserSocialPoint(UserSocialPoint userSocialPoint) {
        this.userSocialPoint = userSocialPoint;
    }

    public SocialActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(SocialActivityType activityType) {
        this.activityType = activityType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }
}
