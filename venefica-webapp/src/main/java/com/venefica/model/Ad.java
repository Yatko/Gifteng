package com.venefica.model;

import com.venefica.config.Constants;
import com.vividsolutions.jts.geom.Point;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
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
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_category_fk")
    private Category category;
    
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 500)
    private String subtitle;
    @Column(length = 1000)
    private String description;
    
    private Integer quantity; //specifies how many requests can be selected also
    private BigDecimal price; //original price, current value
    
    @ManyToOne
    @ForeignKey(name = "ad_mainimg_fk")
    private Image mainImage; //cover image
    
    @ManyToOne
    @ForeignKey(name = "ad_thumbimg_fk")
    private Image thumbImage;
    
    @OneToMany
    @OrderBy
    @ForeignKey(name = "ad_image_ad_fk", inverseName = "ad_image_image_fk")
    private List<Image> images;
    
    @Column(columnDefinition = "Geometry")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;
    @Embedded
    private Address address;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_creator_fk")
    private User creator;
    
//    private boolean wanted; //not used in the gifteng project
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date availableAt; //beginning of the offer (available since)
    
    private int numExpire; //how many times expired
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
    
    private Boolean freeShipping;
    private Boolean pickUp;
    
    @Enumerated(EnumType.STRING)
    private AdStatus status;
    @Enumerated(EnumType.STRING)
    private AdType type;

    public Ad() {
        images = new LinkedList<Image>();
        ratings = new LinkedList<Rating>();
        spamMarks = new LinkedList<SpamMark>();
        comments = new LinkedList<Comment>();
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
    }
    
    public void select() {
        quantity--;
    }
    
    public void unselect() {
        quantity++;
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
    
    public void markAsSold() {
        sold = true;
        soldAt = new Date();
    }

    public void unmarkAsSold() {
        sold = false;
        soldAt = null;
    }
    
    public void addImage(Image image) {
        if ( images == null ) {
            images = new LinkedList<Image>();
        }
        images.add(image);
    }
    
    public void removeImage(Image image) {
        if ( images == null ) {
            images = new LinkedList<Image>();
        }
        images.remove(image);
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
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    public Image getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(Image thumbImage) {
        this.thumbImage = thumbImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
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

//    public boolean isWanted() {
//        return wanted;
//    }
//
//    public void setWanted(boolean wanted) {
//        this.wanted = wanted;
//    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public Boolean getPickUp() {
        return pickUp;
    }

    public void setPickUp(Boolean pickUp) {
        this.pickUp = pickUp;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
