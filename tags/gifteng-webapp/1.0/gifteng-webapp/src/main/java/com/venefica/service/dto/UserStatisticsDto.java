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
    private int numUnreadMessages;
    private int requestLimit;
    private int numReceivedRatings;
    private int numSentRatings;
    private int numPositiveReceivedRatings;
    private int numNegativeReceivedRatings;
    private int numPositiveSentRatings;
    private int numNegativeSentRatings;

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

    public int getNumReceivedRatings() {
        return numReceivedRatings;
    }

    public void setNumReceivedRatings(int numReceivedRatings) {
        this.numReceivedRatings = numReceivedRatings;
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

    public int getNumSentRatings() {
        return numSentRatings;
    }

    public void setNumSentRatings(int numSentRatings) {
        this.numSentRatings = numSentRatings;
    }

    public int getNumPositiveReceivedRatings() {
        return numPositiveReceivedRatings;
    }

    public void setNumPositiveReceivedRatings(int numPositiveReceivedRatings) {
        this.numPositiveReceivedRatings = numPositiveReceivedRatings;
    }

    public int getNumNegativeReceivedRatings() {
        return numNegativeReceivedRatings;
    }

    public void setNumNegativeReceivedRatings(int numNegativeReceivedRatings) {
        this.numNegativeReceivedRatings = numNegativeReceivedRatings;
    }

    public int getNumPositiveSentRatings() {
        return numPositiveSentRatings;
    }

    public void setNumPositiveSentRatings(int numPositiveSentRatings) {
        this.numPositiveSentRatings = numPositiveSentRatings;
    }

    public int getNumNegativeSentRatings() {
        return numNegativeSentRatings;
    }

    public void setNumNegativeSentRatings(int numNegativeSentRatings) {
        this.numNegativeSentRatings = numNegativeSentRatings;
    }
    
}
