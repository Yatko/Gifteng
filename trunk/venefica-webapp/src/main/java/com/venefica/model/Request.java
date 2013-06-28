/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "request")
public class Request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "request_ad_fk")
    private Ad ad;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "request_usr_fk")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    private boolean selected;
    @Temporal(TemporalType.TIMESTAMP)
    private Date selectedAt;
    
    private boolean hidden; //marking hidden by the user in the GUI
    @Temporal(TemporalType.TIMESTAMP)
    private Date hideAt;
    
//    @OneToOne(mappedBy = "request")
//    private Review review;
    
    public Request() {
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request)) {
            return false;
        }

        Request other = (Request) obj;

        return id != null && id.equals(other.id);
    }
    
    public void markAsDeleted() {
        deleted = true;
        deletedAt = new Date();
    }

    public void unmarkAsDeleted() {
        deleted = false;
        deletedAt = null;
    }
    
    public void markAsSelected() {
        selected = true;
        selectedAt = new Date();
    }

    public void unmarkAsSelected() {
        selected = false;
        selectedAt = null;
    }
    
    // getter/setter

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

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
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

//    public Review getReview() {
//        return review;
//    }
//
//    public void setReview(Review review) {
//        this.review = review;
//    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Date getSelectedAt() {
        return selectedAt;
    }

    public void setSelectedAt(Date selectedAt) {
        this.selectedAt = selectedAt;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Date getHideAt() {
        return hideAt;
    }

    public void setHideAt(Date hideAt) {
        this.hideAt = hideAt;
    }
}
