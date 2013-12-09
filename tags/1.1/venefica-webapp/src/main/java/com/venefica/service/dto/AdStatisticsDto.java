/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Ad;
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
    private int numComments;
    // out
    private int numBookmarks;
    // out
    private int numShares;
    // out
    private int numRequests;
    // out
    private float rating;

    // WARNING: required for JAX-WS
    public AdStatisticsDto() {
    }
    
    // helper methods
    
    public static AdStatisticsDto build(Ad ad) {
        AdStatisticsDto statistics = new AdStatisticsDto();
        statistics.setNumAvailProlongations(ad.getNumAvailProlongations());
        statistics.setNumViews(ad.getNumViews());
        statistics.setRating(ad.getRating());
        statistics.setNumBookmarks(ad.getBookmarks() != null ? ad.getBookmarks().size() : 0);
        statistics.setNumComments(ad.getComments() != null ? ad.getComments().size() : 0);
        statistics.setNumRequests(ad.getRequests() != null ? ad.getRequests().size() : 0);
        statistics.setNumShares(0); //TODO
        return statistics;
    }
    
    // getters/setters
    
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

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public int getNumBookmarks() {
        return numBookmarks;
    }

    public void setNumBookmarks(int numBookmarks) {
        this.numBookmarks = numBookmarks;
    }

    public int getNumShares() {
        return numShares;
    }

    public void setNumShares(int numShares) {
        this.numShares = numShares;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }
    
}
