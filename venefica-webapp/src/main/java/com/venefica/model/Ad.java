package com.venefica.model;

import com.venefica.config.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.Inheritance;
//import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.annotations.ForeignKey;
//import javax.persistence.SequenceGenerator;

/**
 * Advertisement class containing generic fields.
 *
 * @author Sviatoslav Grebenchukov
 *
 */
@Entity
//@SequenceGenerator(name = "ad_gen", sequenceName = "ad_seq", allocationSize = 1)
@Table(name = "ad")
public class Ad {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    @ForeignKey(name = "addata_fk")
    private AdData adData;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_creator_fk")
    private User creator;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableAt; //beginning of the offer (available since)
    
    private int numExpire; //how many times expired
    private boolean expires; //never expire?
    @Column(nullable = false)
    private boolean expired;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;
    
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    //TODO: not sure if these are needed
    private boolean sold;
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldAt;
    
    private boolean sent; //product/gift marked as sent (by the seller/owner/giver)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;
    
    private boolean received; //product/gift marked as received (by the buyer/receiver)
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;
    
    private long numViews;
    @OneToMany(mappedBy = "ad")
    private List<Viewer> viewers;
    
    private float rating;
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Rating> ratings;
    
    private boolean spam;
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<SpamMark> spamMarks;
    
    private boolean reviewed; //reviewed by a power-user (admin)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt; //last review date
    
    private int numAvailProlongations; //decrementing at relist ad
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Request> requests;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Bookmark> bookmarks;
    
    @Enumerated(EnumType.STRING)
    private AdStatus status;
    
    protected Ad() {
    }
    
    public Ad(AdType type) {
        if ( type == AdType.MEMBER ) {
            adData = new MemberAdData();
        } else if ( type == AdType.BUSINESS ) {
            adData = new BusinessAdData();
        }
        
        ratings = new LinkedList<Rating>();
        spamMarks = new LinkedList<SpamMark>();
        comments = new LinkedList<Comment>();
        bookmarks = new LinkedList<Bookmark>();
        viewers = new ArrayList<Viewer>(0);
        createdAt = new Date();
        rating = 0.0f;
        numViews = 0;
        numExpire = 0;
        numAvailProlongations = Constants.AD_MAX_ALLOWED_PROLONGATION;
    }

    public void visit() {
        numViews++;
    }

    public boolean canProlong() {
        if (numAvailProlongations <= 0) {
            return false;
        }
        return true;
    }
    
    public void prolong(int days) {
        expired = false;
        expiresAt = DateUtils.addDays(new Date(), days);
        numAvailProlongations--;
        status = AdStatus.ACTIVE;
    }
    
    public void select() {
        adData.select();
        status = AdStatus.SELECTED;
    }
    
    public void unselect() {
        adData.unselect();
    }
    
    public boolean isAlreadyViewedBy(User user) {
        if ( viewers == null || viewers.isEmpty() ) {
            return false;
        }
        for ( Viewer viewer : viewers ) {
            if ( viewer.getUser().equals(user) ) {
                return true;
            }
        }
        return false;
    }
    
    public void markAsDeleted() {
        deleted = true;
        deletedAt = new Date();
    }

    public void unmarkAsDeleted() {
        deleted = false;
        deletedAt = null;
    }
    
    public void markAsSent() {
        sent = true;
        sentAt = new Date();
        status = AdStatus.SENT;
    }
    
    public void markAsReceived() {
        received = true;
        receivedAt = new Date();
        status = AdStatus.RECEIVED;
    }
    
    public void markAsSold() {
        sold = true;
        soldAt = new Date();
    }

    public void unmarkAsSold() {
        sold = false;
        soldAt = null;
    }
    
    public void addImage(Image image) {
        adData.addImage(image);
    }
    
    public void removeImage(Image image) {
        adData.removeImage(image);
    }
    
    public AdType getType() {
        return adData.getType();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Ad)) {
            return false;
        }

        Ad other = (Ad) obj;

        return id != null && id.equals(other.id);
    }
    
    // getter/setter
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        this.creator.addAd(this);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public Date getSoldAt() {
        return soldAt;
    }

    public void setSoldAt(Date soldAt) {
        this.soldAt = soldAt;
    }

    public long getNumViews() {
        return numViews;
    }

    public void setNumViews(long numViews) {
        this.numViews = numViews;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<SpamMark> getSpamMarks() {
        return spamMarks;
    }

    public void setSpamMarks(List<SpamMark> spamMarks) {
        this.spamMarks = spamMarks;
    }

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }

    public void markAsSpam() {
        this.spam = true;
    }

    public void unmarkAsSpam() {
        this.spam = false;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public int getNumAvailProlongations() {
        return numAvailProlongations;
    }

    public void setNumAvailProlongations(int numAvailProlongations) {
        this.numAvailProlongations = numAvailProlongations;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Viewer> getViewers() {
        return viewers;
    }

    public void setViewers(List<Viewer> viewers) {
        this.viewers = viewers;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
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

    public AdStatus getStatus() {
        return status;
    }

    public void setStatus(AdStatus status) {
        this.status = status;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public int getNumExpire() {
        return numExpire;
    }

    public void setNumExpire(int numExpire) {
        this.numExpire = numExpire;
    }

    public Date getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(Date availableAt) {
        this.availableAt = availableAt;
    }

    public boolean isExpires() {
        return expires;
    }

    public void setExpires(boolean expires) {
        this.expires = expires;
    }

    public AdData getAdData() {
        return adData;
    }

    public void setAdData(AdData adData) {
        this.adData = adData;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
