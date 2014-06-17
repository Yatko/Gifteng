package com.venefica.model;

import com.venefica.config.Constants;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * Advertisement class containing generic fields.
 *
 * @author Sviatoslav Grebenchukov
 *
 */
@Entity
@Table(name = Ad.TABLE_NAME)
public class Ad {

    public static final String TABLE_NAME = "ad";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "ad_cloned_from_fk")
    private Ad clonedFrom;
    
    @OneToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "addata_fk")
    private AdData adData;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "idx_createdAt")
    private Date createdAt;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @ForeignKey(name = "ad_creator_fk")
    private User creator;
    
    private Integer revision; //the modification number of the ad (at creation 1, after every update increment by 1)
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableAt; //beginning of the offer (available since or start date)
    
    private int numExpire; //how many times expired
    @Index(name = "idx_expires")
    private boolean expires; //never expire?
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "idx_expiresAt")
    private Date expiresAt; //end date
    
    @Column(nullable = false)
    @Index(name = "idx_expired")
    private boolean expired; //ad is in expired state (flag changed by cron job)
    
    @Index(name = "idx_deleted")
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    @Index(name = "idx_sold")
    private boolean sold; //product/gift ended
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldAt;
    
    @Index(name = "idx_approved")
    private boolean approved; //can go live or not
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "idx_approvedAt")
    private Date approvedAt;
    
    @Index(name = "idx_online")
    private boolean online; //is visible on site
    @Temporal(TemporalType.TIMESTAMP)
    private Date onlinedAt;
    
    private long numViews;
    @OneToMany(mappedBy = "ad")
    private Set<Viewer> viewers;
    
    private float rating;
    
    private boolean spam;
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private Set<SpamMark> spamMarks;
    
    private boolean reviewed; //reviewed by a power-user (admin)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt; //last review date
    
    private int numAvailProlongations; //decrementing at relist ad
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private Set<Comment> comments;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private Set<Request> requests;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private Set<Bookmark> bookmarks;
    
    @Enumerated(EnumType.STRING)
    @Index(name = "idx_status")
    private AdStatus status;
    
    private boolean hiddenForSearch; //cannot be searched but it's an existing ad and can be referenced
    
    protected Ad() {
    }
    
    public Ad(AdType type, int maxAllowedProlongations) {
        if ( type.isMember() ) {
            adData = new MemberAdData();
        } else if ( type.isBusiness() ) {
            adData = new BusinessAdData();
        }
        
        spamMarks = new LinkedHashSet<SpamMark>();
        comments = new LinkedHashSet<Comment>();
        bookmarks = new LinkedHashSet<Bookmark>();
        viewers = new HashSet<Viewer>(0);
        createdAt = new Date();
        rating = 0.0f;
        numViews = 0;
        numExpire = 0;
        numAvailProlongations = maxAllowedProlongations;
    }
    
    public boolean isValid() {
        if ( creator.isDeleted() ) {
            return false;
        }
        return true;
    }

    /**
     * An ad is considered inactive if:
     * - its status is OFFLINE, FINALIZED or EXPIRED
     * - is expired
     * 
     * Inactive ads cannot be requested.
     * 
     * @return 
     */
    public boolean isInactive() {
        if ( status.isInactive() || expired ) {
            return true;
        }
        return false;
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
    
    public void prolong(int minutes) {
        expired = false;
        expiresAt = DateUtils.addMinutes(new Date(), minutes);
        numAvailProlongations--;
        
        updateStatus();
    }
    
    public boolean canRequest() {
        if ( isInactive() ) {
            //inactive ads cannot be requested
            return false;
        }
        if ( adData.getQuantity() <= 0 ) {
            //no more available
            return false;
        }
        if ( isMemberAd() && getActiveRequests().size() >= Constants.REQUEST_MAX_ALLOWED ) {
            //active requests limit reched the allowed size (only for member ads)
            return false;
        }
        return true;
    }
    
    /**
     * Selecting the given request. Quantity is decreasing after this operation.
     * If the quantity reach 0 all the other PENDING requests will become UNACCEPTED.
     * 
     * @param request 
     */
    public void select(Request request) {
        adData.use(true); //decrement quantity
        
        request.markAsAccepted();
        updateStatus();
        
        if ( adData.getQuantity() <= 0 ) {
            //if there are no more pieces mark all the other PENDING requests as UNACCEPTED
            for ( Request r : getVisibleRequests() ) {
                if ( r.isPending() ) {
                    //all the PENDING requests will be marked as UNACCEPTED
                    r.markAsUnaccepted();
                }
            }
        }
    }
    
    /**
     * Canceling a request made previously by the requestor. This should be
     * possible only if the request is not selected by the ad owner.
     * 
     * Cancel is made by the requestor.
     * 
     * @param request 
     */
    public void cancel(Request request) {
        if ( request.isAccepted() ) {
            adData.use(false);
        }
        
        request.cancel();
        updateStatus();
    }
    
    /**
     * Decline the given request. Decline can be made on selected or pending
     * requests as well. When declining a selected request the quantity is increased.
     * All the UNACCEPTED requests will be put into PENDING state, so can be selected
     * again.
     * 
     * Decline is made by the ad creator (owner).
     * 
     * @param request 
     */
    public void decline(Request request) {
        if ( request.isAccepted() ) {
            adData.use(false);
        }
        
        request.decline();
        
        for ( Request r : getVisibleRequests() ) {
            if ( r.isUnaccepted()) {
                //all the UNACCEPTED requests will be marked as PENDING again
                r.markAsPending();
            }
        }
        
        updateStatus();
    }
    
    //public void select() {
    //    adData.select();
    //    status = AdStatus.IN_PROGRESS;
    //}
    
    //public void unselect() {
    //    adData.unselect();
    //    
    //    if ( getType() == AdType.MEMBER ) {
    //        status = AdStatus.ACTIVE;
    //    } else if ( getType() == AdType.BUSINESS ) {
    //        
    //    } else {
    //        //
    //    }
    //}
    
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

    //public void unmarkAsDeleted() {
    //    deleted = false;
    //    deletedAt = null;
    //}
    
    public void markAsSold() {
        sold = true;
        soldAt = new Date();
        status = AdStatus.FINALIZED;
    }
    
    public void markAsApproved() {
        approved = true;
        approvedAt = new Date();
    }
    
    public void unmarkAsApproved() {
        approved = false;
        approvedAt = null;
    }
    
    public void markAsOnline() {
        online = true;
        onlinedAt = new Date();
    }
    
    public void unmarkAsOnline() {
        online = false;
        onlinedAt = null;
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
    
    public BigDecimal getValue() {
        return adData.getValue();
    }
    
    public final boolean isBusinessAd() {
        return getType().isBusiness();
    }
    
    public final boolean isMemberAd() {
        return getType().isMember();
    }
    
    /**
     * Returns all valid requests. Valid request means that the implied user
     * is not deleted.
     * @return 
     */
    public Set<Request> getValidRequests() {
        Set<Request> result = new LinkedHashSet<Request>();
        if ( requests != null && !requests.isEmpty() ) {
            for ( Request request : requests ) {
                if ( request.isValid()) {
                    result.add(request);
                }
            }
        }
        return result;
    }
    
    /**
     * Returns all the visible and non deleted requests.
     * 
     * @return 
     */
    public Set<Request> getVisibleRequests() {
        Set<Request> result = new LinkedHashSet<Request>();
        Set<Request> validRequests = getValidRequests();
        if ( validRequests != null && !validRequests.isEmpty() ) {
            for ( Request request : validRequests ) {
                if ( request.isVisible() ) {
                    result.add(request);
                }
            }
        }
        return result;
    }
    
    /**
     * Returns all the visible and active requests.
     * 
     * Visible means that is not deleted.
     * Active means that is not DECLINED or CANCELED.
     * 
     * @return 
     */
    public Set<Request> getActiveRequests() {
        Set<Request> result = new LinkedHashSet<Request>();
        for ( Request request : getVisibleRequests() ) {
            if ( request.isActive() ) {
                result.add(request);
            }
        }
        return result;
    }
    
    /**
     * Returns all the accepted and visible (not deleted) requests.
     * In case of member ads only one request should exist.
     * 
     * @return 
     */
    public Set<Request> getAcceptedRequests() {
        Set<Request> result = new LinkedHashSet<Request>();
        for ( Request request : getVisibleRequests() ) {
            boolean isAccepted = request.isAccepted() || request.getStatus() == RequestStatus.ACCEPTED;
            
            if ( isAccepted) {
                result.add(request);
            }
        }
        return result;
    }
    
    /**
     * Returns all the visible requests those status is SENT or RECEIVED.
     * 
     * @return 
     */
    public Set<Request> getShippedRequests(boolean includeSent, boolean includeReceived) {
        Set<Request> result = new LinkedHashSet<Request>();
        for ( Request request : getVisibleRequests() ) {
            boolean isSent = request.isSent() || request.getStatus() == RequestStatus.SENT;
            boolean isReceived = request.isReceived() || request.getStatus() == RequestStatus.RECEIVED;
            
            if ( (includeSent && isSent) || (includeReceived && isReceived) ) {
                result.add(request);
            }
        }
        return result;
    }
    
    /**
     * Verifies if a visible request is available for the given user.
     * 
     * Visible means that is not deleted.
     * 
     * @param user
     * @return 
     */
    public boolean isRequested(User user) {
        Set<Request> userRequests = getUserRequests(user);
        if ( userRequests == null || userRequests.isEmpty() ) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns all the visible requests made by the given user.
     * 
     * @param user
     * @return 
     */
    public Set<Request> getUserRequests(User user) {
        Set<Request> result = new LinkedHashSet<Request>();
        for ( Request request: getVisibleRequests() ) {
            if ( request.getUser().equals(user) ) {
                result.add(request);
            }
        }
        return result;
    }
    
    /**
     * Verifies if a visible and active request is available for the given user.
     * 
     * Visible means that is not deleted.
     * Active means that is not DECLINED or CANCELED.
     * 
     * @param user
     * @return 
     */
    public boolean hasActiveRequest(User user) {
        for ( Request request : getUserRequests(user) ) {
            if ( request.isActive() ) {
                //there is a visible and active request
                return true;
            }
        }
        return false;
    }
    
    public void initRevision() {
        this.revision = 1;
    }
    
    public void incrementRevision() {
        if ( revision == null ) {
            initRevision();
        }
        this.revision++;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Ad)) {
            return false;
        }

        Ad other = (Ad) obj;
        return id != null && id.equals(other.getId()); //the getter usage is a must as proxies needs to be activated
    }
    
    private boolean containsActiveRequest() {
        for ( Request r : getVisibleRequests() ) {
            if ( r.isActive() ) {
                return true;
            }
        }
        return false;
    }
    
    private void updateStatus() {
        if ( containsActiveRequest() ) {
            status = AdStatus.IN_PROGRESS;
        } else {
            //ad become ACTIVE as there are no other active requests
            status = AdStatus.ACTIVE;
        }
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

    public Set<SpamMark> getSpamMarks() {
        return spamMarks;
    }

    public void setSpamMarks(Set<SpamMark> spamMarks) {
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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Viewer> getViewers() {
        return viewers;
    }

    public void setViewers(Set<Viewer> viewers) {
        this.viewers = viewers;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
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

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Date getOnlinedAt() {
        return onlinedAt;
    }

    public void setOnlinedAt(Date onlinedAt) {
        this.onlinedAt = onlinedAt;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Ad getClonedFrom() {
        return clonedFrom;
    }

    public void setClonedFrom(Ad clonedFrom) {
        this.clonedFrom = clonedFrom;
    }
    
    public boolean isHiddenForSearch() {
        return hiddenForSearch;
    }

    public void setHiddenForSearch(boolean hiddenForSearch) {
        this.hiddenForSearch = hiddenForSearch;
    }
}
