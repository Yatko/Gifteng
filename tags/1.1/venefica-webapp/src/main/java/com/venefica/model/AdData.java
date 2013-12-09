/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AdDto;
import com.vividsolutions.jts.geom.Point;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "addata")
@Inheritance(strategy = InheritanceType.JOINED)
@org.hibernate.annotations.Table(appliesTo = "addata", indexes = {
})
public abstract class AdData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "ad_category_fk")
    private Category category;
    
    @ManyToOne
    @ForeignKey(name = "ad_mainimg_fk")
    private Image mainImage; //cover image
    
    @ManyToOne
    @ForeignKey(name = "ad_thumbimg_fk")
    private Image thumbImage;
    
    @OneToMany
    @OrderBy
    @JoinTable(name = "ad_image")
    @ForeignKey(name = "ad_image_ad_fk", inverseName = "ad_image_image_fk")
    private Set<Image> images;
    
    @Column(length = 500, nullable = false)
    @Index(name = "idx_title")
    private String title;
    @Column(length = 500)
    @Index(name = "idx_subtitle")
    private String subtitle;
    @Column(length = 1000)
    @Index(name = "idx_description")
    private String description;
    
    @Index(name = "idx_quantity")
    private Integer quantity; //specifies how many requests can be selected also
    @Index(name = "idx_price")
    private BigDecimal price; //original price, current value
    
    @Column(columnDefinition = "Geometry")
    @Type(type = "org.hibernate.spatial.GeometryType")
    private Point location;
    @Embedded
    private Address address;
    
    private Boolean freeShipping;
    private Boolean pickUp;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Index(name = "idx_type")
    private AdType type;
    @Enumerated(EnumType.STRING)
    private AdPlace place;
    
    protected AdData() {
    }
    
    public AdData(AdType adType) {
        images = new LinkedHashSet<Image>();
        type = adType;
    }
    
    public abstract void updateAd(AdDto adDto);
    
    public abstract void updateAdDto(AdDto adDto);
    
    public void select() {
        quantity--;
    }
    
    public void unselect() {
        quantity++;
    }
    
    public void addImage(Image image) {
        if ( images == null ) {
            images = new LinkedHashSet<Image>();
        }
        images.add(image);
    }
    
    public void removeImage(Image image) {
        if ( images == null ) {
            images = new LinkedHashSet<Image>();
        }
        images.remove(image);
    }
    
    // getters/setters
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
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

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
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
    
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    
    public AdPlace getPlace() {
        return place;
    }

    public void setPlace(AdPlace place) {
        this.place = place;
    }
}
