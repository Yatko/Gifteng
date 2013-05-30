/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import com.venefica.service.dto.AdDto;
import java.util.Date;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "businessaddata")
public class BusinessAdData extends AdData {
    
    private String promoCode;
    private String website;
    private Boolean needsReservation;
    
    @Temporal(TemporalType.TIME)
    private Date availableFromTime;
    @Temporal(TemporalType.TIME)
    private Date availableToTime;
    private Boolean availableAllDay;
    
    @ElementCollection(fetch=FetchType.EAGER, targetClass=WeekDay.class)
    @CollectionTable(name = "businessaddata_available_days", joinColumns = @JoinColumn(name="businessaddata_id"))
    @ForeignKey(name = "businessaddata_id")
    @Column(name="available_day")
    @Enumerated(EnumType.STRING)
    private Set<WeekDay> availableDays;
    
    public BusinessAdData() {
        super(AdType.BUSINESS);
    }

    @Override
    public void updateAd(AdDto adDto) {
        promoCode = adDto.getPromoCode();
        website = adDto.getWebsite();
        needsReservation = adDto.getNeedsReservation();
        availableFromTime = adDto.getAvailableFromTime();
        availableToTime = adDto.getAvailableToTime();
        availableAllDay = adDto.getAvailableAllDay();
        availableDays = adDto.getAvailableDays();
    }
    
    @Override
    public void updateAdDto(AdDto adDto) {
        adDto.setPromoCode(promoCode);
        adDto.setWebsite(website);
        adDto.setNeedsReservation(needsReservation);
        adDto.setAvailableFromTime(availableFromTime);
        adDto.setAvailableToTime(availableToTime);
        adDto.setAvailableAllDay(availableAllDay);
        adDto.setAvailableDays(availableDays);
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
}
