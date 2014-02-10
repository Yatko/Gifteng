/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Stores all the approve attempt made by the admin user. Un-approved ads will
 * be notified via email by specifying the reject reason.
 * 
 * @author gyuszi
 */
@Entity
@Table(name = "approval")
public class Approval {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "approval_ad_fk")
    private Ad ad;
    
    @Index(name = "idx_revision")
    private Integer revision; //the revision of the ad when the approval was created
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "approval_decider_fk")
    private User decider;
    
    @Index(name = "idx_approved")
    private boolean approved;
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "idx_approvedAt")
    private Date approvedAt;
    
    private String text;

    protected Approval() {
    }
    
    public Approval(boolean approved) {
        this.approved = approved;
    }
    
    // getter/setter
    
    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getDecider() {
        return decider;
    }

    public void setDecider(User decider) {
        this.decider = decider;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    
}
