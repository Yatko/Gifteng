package com.venefica.service.dto;

import com.venefica.model.AdType;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Describes a filter for ads.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterDto {

//    public static final boolean DEFAULT_INCLUDE_CAN_REQUEST = true;
//    public static final boolean DEFAULT_INCLUDE_REQUESTED = true;
//    public static final boolean DEFAULT_INCLUDE_SHIPPED = false;
//    public static final boolean DEFAULT_INCLUDE_INACTIVE = false;
//    public static final boolean DEFAULT_INCLUDE_OWNED = true;
    public static final boolean DEFAULT_ORDER_ASC = false;
    public static final boolean DEFAULT_ORDER_CLOSEST = false;
    
    private String searchString;
    // all fields are input ones
    @XmlElementWrapper(name = "categories")
    @XmlElement(name = "item")
    private List<Long> categories;
    private Long distance;
    private Double latitude;
    private Double longitude;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean hasPhoto;
    private AdType type;
    //private Boolean includeOwned;
    private Boolean orderAsc; //if true the older ads will be in the first position
    private Boolean orderClosest; //if true the closest ads will be in the first position
    private FilterType filterType;
    
    //private Boolean includeInactive;
    //private Boolean includeShipped; //has request with SENT or RECEIVED status
    //private Boolean includeRequested; //include already requested gifts or not
    //private Boolean includeCanRequest; //quantity > 0; ad active (status != [OFFLINE, FINALIZED, EXPIRED]); sum of active (not deleted and not hidden and status = [PENDING, ACCEPTED, UNACCEPTED, SENT, RECEIVED]) requests < REQUEST_MAX_ALLOWED (3); is requested = false
    
    // WARNING: required for JAX-WS
    public FilterDto() {
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Boolean getHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(Boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

//    public Boolean getIncludeOwned() {
//        return includeOwned;
//    }
//
//    public void setIncludeOwned(Boolean includeOwned) {
//        this.includeOwned = includeOwned;
//    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public Boolean getOrderAsc() {
        return orderAsc;
    }

    public void setOrderAsc(Boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

    public Boolean getOrderClosest() {
        return orderClosest;
    }

    public void setOrderClosest(Boolean orderClosest) {
        this.orderClosest = orderClosest;
    }

//    public Boolean getIncludeShipped() {
//        return includeShipped;
//    }
//
//    public void setIncludeShipped(Boolean includeShipped) {
//        this.includeShipped = includeShipped;
//    }
//
//    public Boolean getIncludeCanRequest() {
//        return includeCanRequest;
//    }
//
//    public void setIncludeCanRequest(Boolean includeCanRequest) {
//        this.includeCanRequest = includeCanRequest;
//    }
//
//    public Boolean getIncludeInactive() {
//        return includeInactive;
//    }
//
//    public void setIncludeInactive(Boolean includeInactive) {
//        this.includeInactive = includeInactive;
//    }
//
//    public Boolean getIncludeRequested() {
//        return includeRequested;
//    }
//
//    public void setIncludeRequested(Boolean includeRequested) {
//        this.includeRequested = includeRequested;
//    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }
}
