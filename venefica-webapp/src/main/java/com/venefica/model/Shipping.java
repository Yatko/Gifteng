/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "shipping")
public class Shipping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "idx_createdAt")
    private Date createdAt;
    
    @OneToOne
    private Request request;
    @ManyToOne
    @ForeignKey(name = "shipping_barcodeimg_fk")
    private Image barcodeImage;
    
    private BigDecimal receivedAmount;
    private String trackingNumber;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;
    
    private boolean emailCreatorSent; //ad creator user received the mail
    @Temporal(TemporalType.TIMESTAMP)
    private Date emailCreatorSentAt;
    
    private boolean emailReceiverSent; //ad receiver (requestor) user received the mail
    @Temporal(TemporalType.TIMESTAMP)
    private Date emailReceiverSentAt;
    
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    // getter/setter
    
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Image getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(Image barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public boolean isEmailCreatorSent() {
        return emailCreatorSent;
    }

    public void setEmailCreatorSent(boolean emailCreatorSent) {
        this.emailCreatorSent = emailCreatorSent;
    }

    public Date getEmailCreatorSentAt() {
        return emailCreatorSentAt;
    }

    public void setEmailCreatorSentAt(Date emailCreatorSentAt) {
        this.emailCreatorSentAt = emailCreatorSentAt;
    }

    public boolean isEmailReceiverSent() {
        return emailReceiverSent;
    }

    public void setEmailReceiverSent(boolean emailReceiverSent) {
        this.emailReceiverSent = emailReceiverSent;
    }

    public Date getEmailReceiverSentAt() {
        return emailReceiverSentAt;
    }

    public void setEmailReceiverSentAt(Date emailReceiverSentAt) {
        this.emailReceiverSentAt = emailReceiverSentAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
