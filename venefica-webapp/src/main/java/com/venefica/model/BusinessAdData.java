/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.ImageDto;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@ForeignKey(name = "businessaddata_fk")
@Table(name = BusinessAdData.TABLE_NAME)
public class BusinessAdData extends AdData {
    
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String TABLE_NAME = "businessaddata";
    
    private String promoCode; //static code available for every request
    private boolean generatePromoCodeForRequests; //new unique code for all the requests
    private String website; //place in (ONLINE, BOTH)
    private BigDecimal fixedValue;
    
    private boolean allAddresses;
    @OneToMany
    @ForeignKey(name = "businessaddata_addata_fk", inverseName = "businessaddata_address_fk")
    private Set<AddressWrapper> addresses; //place in (LOCATION, BOTH)
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date redemptionEndDate;
    
    private Boolean needsReservation; //TODO: probably is not needed
    @Temporal(TemporalType.TIME)
    private Date availableFromTime; //TODO: probably is not needed
    @Temporal(TemporalType.TIME)
    private Date availableToTime; //TODO: probably is not needed
    private Boolean availableAllDay; //TODO: probably is not needed
    
    @ElementCollection(fetch=FetchType.EAGER, targetClass=WeekDay.class)
    @CollectionTable(name = "businessaddata_available_days", joinColumns = @JoinColumn(name="businessaddata_id"))
    @ForeignKey(name = "businessaddata_id")
    @Column(name="available_day")
    @Enumerated(EnumType.STRING)
    private Set<WeekDay> availableDays; //TODO: probably is not needed
    
    @ManyToOne
    @ForeignKey(name = "businessaddata_barcodeimg_fk")
    private Image barcodeImage; //TODO: probably is not needed
    
    public BusinessAdData() {
        super(AdType.BUSINESS);
    }

    @Override
    public void updateAd(AdDto adDto) {
        promoCode = adDto.getPromoCode();
        generatePromoCodeForRequests = adDto.isGeneratePromoCodeForRequests();
        website = adDto.getWebsite();
        fixedValue = adDto.isFree() ? BigDecimal.ZERO : null;
        redemptionEndDate = adDto.getRedemptionEndDate();
        allAddresses = adDto.isAllAddresses();
        addresses = AddressWrapper.toAddressWrappers(adDto.getAddresses());
        
        needsReservation = adDto.getNeedsReservation();
        availableFromTime = adDto.getAvailableFromTime();
        availableToTime = adDto.getAvailableToTime();
        availableAllDay = adDto.getAvailableAllDay();
        availableDays = adDto.getAvailableDays();
    }
    
    @Override
    public void updateAdDto(AdDto adDto) {
        adDto.setPromoCode(promoCode);
        adDto.setGeneratePromoCodeForRequests(generatePromoCodeForRequests);
        adDto.setWebsite(website);
        adDto.setFree(isFree());
        adDto.setRedemptionEndDate(redemptionEndDate);
        adDto.setAllAddresses(allAddresses);
        adDto.setAddresses(AddressDto.toAddressDtos(addresses));
        
        adDto.setNeedsReservation(needsReservation);
        adDto.setAvailableFromTime(availableFromTime);
        adDto.setAvailableToTime(availableToTime);
        adDto.setAvailableAllDay(availableAllDay);
        adDto.setAvailableDays(availableDays);
        
        if ( barcodeImage != null ) {
            adDto.setImageBarcode(new ImageDto(barcodeImage));
        }
    }
    
    /**
     * Returns the ad value by using a simple calculation.
     * The actual math algorithm is 62.5 % of the ad price if the fixed number is not set.
     * 
     * @return 
     */
    @Override
    public BigDecimal getValue() {
        if ( isFree() ) {
            return fixedValue;
        }
        
        BigDecimal price = getPrice();
        if ( price == null ) {
            price = BigDecimal.ZERO;
        }
        
        BigDecimal percent = new BigDecimal("62.5");
        BigDecimal HUNDRED = new BigDecimal(100);
        BigDecimal value = price.multiply(percent).divide(HUNDRED);
        return value;
    }
    
    public void addAddress(Address address) {
        initAddresses();
        addresses.add(new AddressWrapper(address));
    }
    
    private void initAddresses() {
        if ( addresses == null ) {
            addresses = new HashSet<AddressWrapper>(0);
        }
    }
    
    private boolean isFree() {
        return fixedValue == null;
    }
    
    // getters/setters
    
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

    public Boolean getAvailableAllDay() {
        return availableAllDay;
    }

    public void setAvailableAllDay(Boolean availableAllDay) {
        this.availableAllDay = availableAllDay;
    }

    public Image getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(Image barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public boolean isGeneratePromoCodeForRequests() {
        return generatePromoCodeForRequests;
    }

    public void setGeneratePromoCodeForRequests(boolean generatePromoCodeForRequests) {
        this.generatePromoCodeForRequests = generatePromoCodeForRequests;
    }

    public Date getRedemptionEndDate() {
        return redemptionEndDate;
    }

    public void setRedemptionEndDate(Date redemptionEndDate) {
        this.redemptionEndDate = redemptionEndDate;
    }
    
    public Set<AddressWrapper> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressWrapper> addresses) {
        this.addresses = addresses;
    }

    public BigDecimal getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(BigDecimal fixedValue) {
        this.fixedValue = fixedValue;
    }

    public boolean isAllAddresses() {
        return allAddresses;
    }

    public void setAllAddresses(boolean allAddresses) {
        this.allAddresses = allAddresses;
    }
}
