/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "request")
public class Request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "request_ad_fk")
    private Ad ad;
    
    private String promoCode; //for business gifts
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "request_usr_fk")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;

    @Enumerated(EnumType.STRING)
    @Index(name = "idx_status")
    private RequestStatus status;
    
    @Index(name = "idx_deleted")
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    private boolean sent; //product/gift marked as sent (by the seller/owner/giver)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;
    
    private boolean received; //product/gift marked as received (by the buyer/receiver/requestor)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;
    
    @Column(name = "selected")
    private boolean accepted;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "selectedAt")
    private Date acceptedAt;
    
    @Index(name = "idx_hidden")
    private boolean hidden; //marking hidden by the user in the GUI
    @Temporal(TemporalType.TIMESTAMP)
    private Date hideAt;
    
    private boolean messagesHiddenByRequestor; //default is false
    private boolean messagesHiddenByCreator; //default is false
    
    @OneToMany(mappedBy = "request")
    private Set<Rating> ratings;
    
    public Request() {
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request)) {
            return false;
        }

        Request other = (Request) obj;
        return id != null && id.equals(other.getId()); //the getter usage is a must as proxies needs to be activated
    }
    
    public boolean isValid() {
        if ( user.isDeleted() ) {
            return false;
        }
        return true;
    }
    
    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }
    
    public boolean isUnaccepted() {
        return status == RequestStatus.UNACCEPTED;
    }
    
    public boolean isDeclined() {
        return status == RequestStatus.DECLINED;
    }
    
    public boolean isCanceled() {
        return status == RequestStatus.CANCELED;
    }
    
    public boolean isVisible() {
        return isValid() && !isHidden() && !isDeleted();
    }
    
    /**
     * If the request is having CANCELED or DECLINED status returns false.
     * Deleted and hidden request is also not active - returns false.
     * 
     * @return 
     */
    public boolean isActive() {
        if ( !isVisible() ) {
            return false;
        }
        return status.isActive();
    }
    
    public void markAsDeleted() {
        deleted = true;
        deletedAt = new Date();
    }

    public void markAsSent() {
        sent = true;
        sentAt = new Date();
        status = RequestStatus.SENT;
    }
    
    public void markAsReceived() {
        received = true;
        receivedAt = new Date();
        status = RequestStatus.RECEIVED;
    }
    
    public void markAsAccepted() {
        accepted = true;
        acceptedAt = new Date();
        status = RequestStatus.ACCEPTED;
    }
    
    public void markAsPending() {
        unmarkAsAccepted();
        status = RequestStatus.PENDING;
    }
    
    public void markAsUnaccepted() {
        unmarkAsAccepted();
        status = RequestStatus.UNACCEPTED;
    }
    
    public void cancel() {
        unmarkAsAccepted(); //this should have no effect as cancelation is possible only if the request was not selected
        status = RequestStatus.CANCELED;
    }
    
    public void decline() {
        unmarkAsAccepted();
        status = RequestStatus.DECLINED;
    }
    
    public boolean isAlreadyRated(User from) {
        if ( ratings == null || ratings.isEmpty() ) {
            return false;
        }
        for (Rating r : ratings) {
            if (r.getFrom().equals(from)) {
                return true;
            }
        }
        return false;
    }
    
    private void unmarkAsAccepted() {
        accepted = false;
        acceptedAt = null;
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
    
    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
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

    public boolean isMessagesHiddenByRequestor() {
        return messagesHiddenByRequestor;
    }

    public void setMessagesHiddenByRequestor(boolean messagesHiddenByRequestor) {
        this.messagesHiddenByRequestor = messagesHiddenByRequestor;
    }

    public boolean isMessagesHiddenByCreator() {
        return messagesHiddenByCreator;
    }

    public void setMessagesHiddenByCreator(boolean messagesHiddenByCreator) {
        this.messagesHiddenByCreator = messagesHiddenByCreator;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
    
    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }
}
