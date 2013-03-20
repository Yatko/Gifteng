package com.venefica.model;

import com.vividsolutions.jts.geom.Point;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
//@Inheritance(strategy = InheritanceType.JOINED)
public class Ad {

    private static final int DEFAULT_AVAIL_PROLONGATIONS = 1;
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_creator_fk")
    private User creator;
    @ManyToOne
    @ForeignKey(name = "ad_buyer_fk")
    private User buyer;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_category_fk")
    private Category category;
    
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 1000)
    private String description;
    
    private BigDecimal price;
    
    @ManyToOne
    @ForeignKey(name = "ad_mainimg_fk")
    private Image mainImage;
    
    @ManyToOne
    @ForeignKey(name = "ad_thumbimg_fk")
    private Image thumbImage;
    
    @OneToMany
    @OrderBy
    @ForeignKey(name = "ad_image_ad_fk", inverseName = "ad_image_image_fk")
    private List<Image> images;
    
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    private boolean wanted;
    
    @Column(nullable = false)
    private boolean expired;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;
    
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    
    private boolean sold;
    @Temporal(TemporalType.TIMESTAMP)
    private Date soldAt;
    
    private long numViews;
    
    private float rating;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Rating> ratings;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<SpamMark> spamMarks;
    
    private boolean spam;
    
    private boolean reviewed;
    
    private int numAvailProlongations;
    
    @OneToMany(mappedBy = "ad")
    @OrderBy
    private List<Comment> comments;

    // private List<Viewers> viewers;
    
    public Ad() {
        images = new LinkedList<Image>();
        ratings = new LinkedList<Rating>();
        spamMarks = new LinkedList<SpamMark>();
        comments = new LinkedList<Comment>();
        createdAt = new Date();
        rating = 0.0f;
        numViews = 0;
        numAvailProlongations = DEFAULT_AVAIL_PROLONGATIONS;
    }

    public void visit() {
        numViews++;
    }

    public boolean prolong(int days) {
        if (numAvailProlongations <= 0) {
            return false;
        }

        numAvailProlongations--;
        expired = false;
        expiresAt = DateUtils.addDays(new Date(), days);
        return true;
    }

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

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
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

    public void markAsDeleted() {
        deleted = true;
        deletedAt = new Date();
    }

    public void unmarkAsDeleted() {
        deleted = false;
        deletedAt = null;
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

    public void markAsSold() {
        sold = true;
        soldAt = new Date();
    }

    public void unmarkAsSold() {
        sold = false;
        soldAt = null;
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

    public boolean isWanted() {
        return wanted;
    }

    public void setWanted(boolean wanted) {
        this.wanted = wanted;
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
}
