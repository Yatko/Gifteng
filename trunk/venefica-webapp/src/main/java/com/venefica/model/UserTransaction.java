/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.math.BigDecimal;
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
 *
 * @author gyuszi
 */
@Entity
@Table(name = "user_transaction")
public class UserTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "usertransaction_user_fk")
    private User user;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "usertransaction_userpoint_fk")
    private UserPoint userPoint;
    
    @ManyToOne
    @ForeignKey(name = "usertransaction_ad_fk")
    private Ad ad;

    @ManyToOne
    @ForeignKey(name = "usertransaction_request_fk")
    private Request request;
    
    private BigDecimal pendingGivingNumber;
    private BigDecimal pendingReceivingNumber;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date finalizedAt;
    private boolean finalized;

    public UserTransaction() {
    }

    public UserTransaction(Ad ad) {
        this.ad = ad;
        this.pendingGivingNumber = UserPoint.getGivingNumber(ad);
        this.pendingReceivingNumber = BigDecimal.ZERO;
    }

    public UserTransaction(Request request) {
        this.request = request;
        this.pendingGivingNumber = BigDecimal.ZERO;
        this.pendingReceivingNumber = UserPoint.getReceivingNumber(request);
    }
    
    public void markAsFinalized() {
        finalized = true;
        finalizedAt = new Date();
    }
    
    // getters/setters
    
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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getFinalizedAt() {
        return finalizedAt;
    }

    public void setFinalizedAt(Date finalizedAt) {
        this.finalizedAt = finalizedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserPoint getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(UserPoint userPoint) {
        this.userPoint = userPoint;
    }

    public BigDecimal getPendingGivingNumber() {
        return pendingGivingNumber;
    }

    public void setPendingGivingNumber(BigDecimal pendingGivingNumber) {
        this.pendingGivingNumber = pendingGivingNumber;
    }

    public BigDecimal getPendingReceivingNumber() {
        return pendingReceivingNumber;
    }

    public void setPendingReceivingNumber(BigDecimal pendingReceivingNumber) {
        this.pendingReceivingNumber = pendingReceivingNumber;
    }
}
