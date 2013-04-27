/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 * Ad viewer.
 * 
 * @author gyuszi
 */
@Entity
@Table(name = "viewer")
public class Viewer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @ForeignKey(name = "viewer_ad_fk")
    private Ad ad;
    
    @ManyToOne
    @ForeignKey(name = "viewer_usr_fk")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewedAt;
    
    public Viewer() {
    }

    public Viewer(Ad ad, User user) {
        this.ad = ad;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Date viewedAt) {
        this.viewedAt = viewedAt;
    }
}
