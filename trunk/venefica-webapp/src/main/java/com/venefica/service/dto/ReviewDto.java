/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Review;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewDto extends DtoBase {
    
    // in, out
    @NotNull
    private Long adId;
    // in, out
    private Boolean positive;
    // in, out
    private String text;
    // out
    private Date reviewedAt;
    // out
    private UserDto reviewedUser;
    // out
    private UserDto reviewerUser;

    public ReviewDto() {
    }
    
    /**
     * Constructs the DTO object form the domain object.
     * 
     * @param review domain object
     */
    public ReviewDto(Review review) {
        positive = review.getPositive();
        text = review.getText();
        reviewedAt = review.getReviewedAt();
        adId = review.getRequest().getAd().getId();
        reviewedUser = new UserDto(review.getRequest().getAd().getCreator());
        reviewerUser = new UserDto(review.getRequest().getUser());
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public UserDto getReviewerUser() {
        return reviewerUser;
    }

    public void setReviewerUser(UserDto reviewerUser) {
        this.reviewerUser = reviewerUser;
    }

    public UserDto getReviewedUser() {
        return reviewedUser;
    }

    public void setReviewedUserId(UserDto reviewedUser) {
        this.reviewedUser = reviewedUser;
    }
}
