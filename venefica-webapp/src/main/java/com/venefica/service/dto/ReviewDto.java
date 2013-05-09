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
    private Long reviewedUserId;
    // out
    private Long reviewerUserId;

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
        reviewedUserId = review.getRequest().getAd().getCreator().getId();
        reviewerUserId = review.getRequest().getUser().getId();
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

    public Long getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public Long getReviewedUserId() {
        return reviewedUserId;
    }

    public void setReviewedUserId(Long reviewedUserId) {
        this.reviewedUserId = reviewedUserId;
    }
}
