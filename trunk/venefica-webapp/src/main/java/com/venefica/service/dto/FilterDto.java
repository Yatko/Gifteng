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
    private Boolean includeOwned;
    private Boolean orderAsc; //if true the older ads will be in the first position
    private Boolean includeCannotRequest; //ads that cannot be requested by the caller user to be included or not
    private Boolean includeOnlyCannotRequest; //load only requestable ads or not

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

    public Boolean getIncludeOwned() {
        return includeOwned;
    }

    public void setIncludeOwned(Boolean includeOwned) {
        this.includeOwned = includeOwned;
    }

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

    public Boolean getIncludeCannotRequest() {
        return includeCannotRequest;
    }

    public void setIncludeCannotRequest(Boolean includeCannotRequest) {
        this.includeCannotRequest = includeCannotRequest;
    }

    public Boolean getIncludeOnlyCannotRequest() {
        return includeOnlyCannotRequest;
    }

    public void setIncludeOnlyCannotRequest(Boolean includeOnlyCannotRequest) {
        this.includeOnlyCannotRequest = includeOnlyCannotRequest;
    }
}
