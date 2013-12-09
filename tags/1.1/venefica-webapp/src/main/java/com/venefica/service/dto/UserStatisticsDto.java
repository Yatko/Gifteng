/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Holds data regarding some user statistics.
 * 
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserStatisticsDto {
    
    private int numReceivings;
    private int numGivings;
    private int numBookmarks;
    private int numFollowers;
    private int numFollowings;
    private int numRatings;
    private int numUnreadMessages;
    private int requestLimit;

    // WARNING: required for JAX-WS
    public UserStatisticsDto() {
    }
    
    public int getNumReceivings() {
        return numReceivings;
    }

    public void setNumReceivings(int numReceivings) {
        this.numReceivings = numReceivings;
    }

    public int getNumGivings() {
        return numGivings;
    }

    public void setNumGivings(int numGivings) {
        this.numGivings = numGivings;
    }

    public int getNumBookmarks() {
        return numBookmarks;
    }

    public void setNumBookmarks(int numBookmarks) {
        this.numBookmarks = numBookmarks;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumFollowings() {
        return numFollowings;
    }

    public void setNumFollowings(int numFollowings) {
        this.numFollowings = numFollowings;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public int getNumUnreadMessages() {
        return numUnreadMessages;
    }

    public void setNumUnreadMessages(int numUnreadMessages) {
        this.numUnreadMessages = numUnreadMessages;
    }

    public int getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(int requestLimit) {
        this.requestLimit = requestLimit;
    }
    
}
