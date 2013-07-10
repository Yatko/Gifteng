package com.venefica.service.dto;

import com.venefica.model.Ad;
import com.venefica.model.AdPlace;
import com.venefica.model.AdStatus;
import com.venefica.model.AdType;
import com.venefica.model.WeekDay;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Min;
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
    private String subtitle;
    // in,out
    private String description;
    // in,out
    private BigDecimal price;
    // in,out
    @Min(1)
    @NotNull
    private Integer quantity;
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
    private boolean inBookmarks;
    // out
    private boolean expired;
    // out
    private boolean sent;
    // out
    private boolean received;
    // out
    private boolean requested; //there is a valid request for this ad by the current user
    // in, out
    private Boolean expires; //never expire?
    // in, out
    private Date availableAt;
    // in, out
    private Date expiresAt;
    // out
    private Boolean canRate;
    // out
    private UserDto creator;
    // out
    private Boolean canMarkAsSpam;
    // in, out
    private Boolean freeShipping;
    // in, out
    private Boolean pickUp;
    // in, out
    private AdPlace place;
    // out
    private AdType type;
    // out
    @XmlElementWrapper(name = "comments")
    @XmlElement(name = "item")
    private List<CommentDto> comments;
    // in, out
    private AddressDto address;
    // out
    private AdStatus status;
    // out
    @XmlElementWrapper(name = "requests")
    @XmlElement(name = "item")
    private List<RequestDto> requests;
    // out
    private Boolean canRequest;
    // out
    private AdStatisticsDto statistics;
    
    // business ad data
    
    // in, out
    private String promoCode;
    // in, out
    private String website;
    // in, out
    private Boolean needsReservation;
    // in, out
    private Date availableFromTime;
    // in, out
    private Date availableToTime;
    // in, out
    private Boolean availableAllDay;
    // in, out
    private Set<WeekDay> availableDays;
    // in, out
    private ImageDto imageBarcode;
    
    // Required for JAX-WS
    public AdDto() {
    }

    public void update(Ad ad) {
        ad.getAdData().setTitle(title);
        ad.getAdData().setSubtitle(subtitle);
        ad.getAdData().setDescription(description);
        ad.getAdData().setPrice(price);
        ad.getAdData().setQuantity(quantity != null ? quantity : 1);
        ad.getAdData().setFreeShipping(freeShipping);
        ad.getAdData().setPickUp(pickUp);
        ad.getAdData().setPlace(place);
        ad.getAdData().setAddress(address != null ? address.getAddress() : null);
        ad.getAdData().setLocation(address != null ? address.getLocation() : null);
        
        ad.getAdData().updateAd(this);
    }
    
    // getter/setter

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

    public boolean isInBookmarks() {
        return inBookmarks;
    }

    public void setInBookmarks(boolean inBookmarks) {
        this.inBookmarks = inBookmarks;
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Date getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(Date availableAt) {
        this.availableAt = availableAt;
    }

    public Boolean isExpires() {
        return expires;
    }

    public void setExpires(Boolean expires) {
        this.expires = expires;
    }

    public AdPlace getPlace() {
        return place;
    }

    public void setPlace(AdPlace place) {
        this.place = place;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getNeedsReservation() {
        return needsReservation;
    }

    public void setNeedsReservation(Boolean needsReservation) {
        this.needsReservation = needsReservation;
    }

    public Date getAvailableFromTime() {
        return availableFromTime;
    }

    public void setAvailableFromTime(Date availableFromTime) {
        this.availableFromTime = availableFromTime;
    }

    public Date getAvailableToTime() {
        return availableToTime;
    }

    public void setAvailableToTime(Date availableToTime) {
        this.availableToTime = availableToTime;
    }

    public Set<WeekDay> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Set<WeekDay> availableDays) {
        this.availableDays = availableDays;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public Boolean getAvailableAllDay() {
        return availableAllDay;
    }

    public void setAvailableAllDay(Boolean availableAllDay) {
        this.availableAllDay = availableAllDay;
    }

    public ImageDto getImageBarcode() {
        return imageBarcode;
    }

    public void setImageBarcode(ImageDto imageBarcode) {
        this.imageBarcode = imageBarcode;
    }

    public AdStatus getStatus() {
        return status;
    }

    public void setStatus(AdStatus status) {
        this.status = status;
    }

    public List<RequestDto> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestDto> requests) {
        this.requests = requests;
    }

    public AdStatisticsDto getStatistics() {
        return statistics;
    }

    public void setStatistics(AdStatisticsDto statistics) {
        this.statistics = statistics;
    }

    public Boolean getCanRequest() {
        return canRequest;
    }

    public void setCanRequest(Boolean canRequest) {
        this.canRequest = canRequest;
    }
}
