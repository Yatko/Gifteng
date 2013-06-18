/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AdStatisticsDto {
    
    // out
    private int numAvailProlongations;
    // out
    private long numViews;
    // out
    private long numComments;
    // out
    private long numBookmarks;
    // out
    private long numShares;
    // out
    private float rating;

    // WARNING: required for JAX-WS
    public AdStatisticsDto() {
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

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public long getNumBookmarks() {
        return numBookmarks;
    }

    public void setNumBookmarks(long numBookmarks) {
        this.numBookmarks = numBookmarks;
    }

    public long getNumShares() {
        return numShares;
    }

    public void setNumShares(long numShares) {
        this.numShares = numShares;
    }
    
}
