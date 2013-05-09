package com.venefica.service.dto;

import com.venefica.model.Ad;
import com.venefica.common.GeoUtils;
import com.vividsolutions.jts.geom.Point;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Describes an advertisement data transfer object.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AdDto extends DtoBase {
    
    // out
    private Long id;
    // in, out
    private Long categoryId;
    // out
    private String category;
    // in,out
    @NotNull
    private String title;
    // in,out
    private String description;
    // in,out
    private BigDecimal price;
    // in,out
    private Integer quantity;
    // in,out
    private Double latitude;
    // in,out
    private Double longitude;
    // in, out
    private ImageDto image;
    // in, out
    private ImageDto imageThumbnail;
    // out
    @XmlElementWrapper(name = "images")
    @XmlElement(name = "item")
    private List<ImageDto> images;
    // out
    private Date createdAt;
    // out
    private boolean owner;
    // out
    private boolean inBookmars;
    // in, out
    private boolean wanted;
    // out
    private boolean expired;
    // out
    private boolean sent;
    // out
    private boolean received;
    // out
    private boolean requested; //there is a valid request for this ad by the current user
//    // out
//    private boolean favorited; //is in the user favorite list
    // in, out
    private Date expiresAt;
    // out
    private int numAvailProlongations;
    // out
    private long numViews;
    // out
    private float rating;
    // out
    private Boolean canRate;
    // out
    private UserDto creator;
    // out
    private Boolean canMarkAsSpam;
    // out
    @XmlElementWrapper(name = "comments")
    @XmlElement(name = "item")
    private List<CommentDto> comments;

    // Required for JAX-WS
    public AdDto() {
    }

    public void update(Ad ad) {
        ad.setTitle(title);
        ad.setDescription(description);
        ad.setPrice(price);
        ad.setQuantity(quantity);
        ad.setWanted(wanted);

        if (latitude != null && longitude != null) {
            Point newLocation = GeoUtils.createPoint(latitude, longitude);
            ad.setLocation(newLocation);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public ImageDto getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(ImageDto imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isInBookmars() {
        return inBookmars;
    }

    public void setInBookmars(boolean inBookmars) {
        this.inBookmars = inBookmars;
    }

    public boolean isWanted() {
        return wanted;
    }

    public void setWanted(boolean wanted) {
        this.wanted = wanted;
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

    public int getNumAvailProlongations() {
        return numAvailProlongations;
    }

    public void setNumAvailProlongations(int numAvailProlongations) {
        this.numAvailProlongations = numAvailProlongations;
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

    public Boolean getCanRate() {
        return canRate;
    }

    public void setCanRate(Boolean canRate) {
        this.canRate = canRate;
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public Boolean getCanMarkAsSpam() {
        return canMarkAsSpam;
    }

    public void setCanMarkAsSpam(Boolean canMarkAsSpam) {
        this.canMarkAsSpam = canMarkAsSpam;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

//    public boolean isFavorited() {
//        return favorited;
//    }
//
//    public void setFavorited(boolean favorited) {
//        this.favorited = favorited;
//    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
