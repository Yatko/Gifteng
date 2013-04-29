/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Review;
import java.util.Date;

/**
 *
 * @author gyuszi
 */
public class ReviewDto {
    
    // in, out
    private String text;
    // out
    private UserDto from;
    // in
    private UserDto to;
    // out
    private Date reviewedAt;

    public ReviewDto() {
    }
    
    /**
     * Constructs the DTO object form the domain object.
     * 
     * @param review domain object
     */
    public ReviewDto(Review review) {
        text = review.getText();
        from = new UserDto(review.getFrom());
        to = new UserDto(review.getUser());
        reviewedAt = review.getReviewedAt();
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDto getFrom() {
        return from;
    }

    public void setFrom(UserDto from) {
        this.from = from;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public UserDto getTo() {
        return to;
    }

    public void setTo(UserDto to) {
        this.to = to;
    }
}
