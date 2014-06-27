/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This entity stores all the user points (pending, insider, exclusive).
 * 
 * @author gyuszi
 */
@Entity
@Table(name = "user_point")
public class UserPoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @OneToMany(mappedBy = "userPoint")
    private Set<UserTransaction> transactions;
    
    private int requestLimit;
    private BigDecimal givingNumber;
    private BigDecimal memberReceivingNumber; //receiveing number of member ads
    private BigDecimal businessReceivingNumber; //receiveing number of business ads
    
    public UserPoint() {
    }
    
    public UserPoint(int requestLimit, int givingNumber, int memberReceivingNumber) {
        this.requestLimit = requestLimit;
        this.givingNumber = new BigDecimal(givingNumber);
        this.memberReceivingNumber = new BigDecimal(memberReceivingNumber);
        this.businessReceivingNumber = BigDecimal.ZERO;
    }
    
    public UserPoint(int businessReceivingNumber) {
        this.requestLimit = 0;
        this.givingNumber = BigDecimal.ZERO;
        this.memberReceivingNumber = BigDecimal.ZERO;
        this.businessReceivingNumber = new BigDecimal(businessReceivingNumber);
    }
    
    // helper methods
    
    public static boolean canUpdateRequestLimit(Ad ad) {
        // do not change request limit for the business ads
        return !ad.isBusinessAd();
    }
    
    public boolean canRequest(Ad ad, UserSocialPoint socialPoint) {
        if ( ad.isMemberAd() ) {
            // there is no limitation on member ads
            return true;
        }
        
        BigDecimal value = ad.getValue();
        businessReceivingNumber = safeNumber(businessReceivingNumber);
        givingNumber = safeNumber(givingNumber);
        
        if ( value.compareTo(BigDecimal.ZERO) == 0 ) {
            //0 valued ads always can be requested
            return true;
        } else if (
                value
                .add(businessReceivingNumber)
                .subtract(new BigDecimal(socialPoint.getSocialPoint()))
                .compareTo(givingNumber) <= 0
        ) {
            //there were enough gives to procced with the business gift request
            return true;
        }
        return false;
    }
    
//    public void addGivingNumber(int number) {
//        givingNumber = safeNumber(givingNumber);
//        givingNumber = givingNumber.add(new BigDecimal(number));
//    }
    
    public void addPendingGivingNumber(UserTransaction tx) {
        if ( tx.isFinalized() ) {
            //transaction is already finalized
            return;
        }
        
        BigDecimal n = safeNumber(tx.getPendingGivingNumber());
        givingNumber = safeNumber(givingNumber);
        givingNumber = givingNumber.add(n);
    }
    
    public void addPendingMemberReceivingNumber(UserTransaction tx) {
        if ( tx.isFinalized() ) {
            //transaction is already finalized
            return;
        }
        
        BigDecimal n = safeNumber(tx.getPendingReceivingNumber());
        memberReceivingNumber = safeNumber(memberReceivingNumber);
        memberReceivingNumber = memberReceivingNumber.add(n);
    }
    
    public void addPendingBusinessReceivingNumber(UserTransaction tx) {
        if ( tx.isFinalized() ) {
            //transaction is already finalized
            return;
        }
        
        BigDecimal n = safeNumber(tx.getPendingReceivingNumber());
        businessReceivingNumber = safeNumber(businessReceivingNumber);
        businessReceivingNumber = businessReceivingNumber.add(n);
    }
    
    public BigDecimal getCalculatedPendingGivingNumber() {
        return calculatePendingNumber(true);
    }
    
    public BigDecimal getCalculatedPendingReceivingNumber() {
        return calculatePendingNumber(false);
    }
    
    public void incrementRequestLimit() {
        incrementRequestLimit(1);
    }
    
    public void decrementRequestLimit() {
        incrementRequestLimit(-1);
    }
    
    public void incrementRequestLimit(int value) {
        requestLimit = requestLimit + value;
    }
    
    private BigDecimal calculatePendingNumber(boolean isGiving) {
        if ( transactions == null || transactions.isEmpty() ) {
            return BigDecimal.ZERO;
        }
        BigDecimal pendingNumber = BigDecimal.ZERO;
        for ( UserTransaction tx : transactions ) {
            if ( tx.isFinalized() ) {
                //transaction is already finalized, the pending number already added to user final score
                continue;
            } else if ( getAd(tx) == null ) {
                continue;
            }
            
            BigDecimal value = safeNumber(isGiving ? tx.getPendingGivingNumber() : tx.getPendingReceivingNumber());
            pendingNumber = pendingNumber.add(value);
        }
        return pendingNumber;
    }
    
    private static Ad getAd(UserTransaction transaction) {
        Ad ad = null;
        if ( transaction.getAd() != null ) {
            ad = transaction.getAd();
        } else if ( transaction.getRequest() != null && transaction.getRequest().getAd() != null ) {
            ad = transaction.getRequest().getAd();
        }
        if ( ad != null ) {
            if ( ad.isExpired() ) {
                return null;
            } else if ( !ad.isApproved() ) {
                return null;
            }
        }
        return ad;
    }
    
    private static BigDecimal safeNumber(BigDecimal n) {
        if ( n == null ) {
            n = BigDecimal.ZERO;
        }
        return n;
    }
    
    // getters/setters
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Set<UserTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<UserTransaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getGivingNumber() {
        return givingNumber;
    }

    @SuppressWarnings("unused")
    private void setGivingNumber(BigDecimal givingNumber) {
        this.givingNumber = givingNumber;
    }

    public BigDecimal getMemberReceivingNumber() {
        return memberReceivingNumber;
    }

    @SuppressWarnings("unused")
    private void setMemberReceivingNumber(BigDecimal memberReceivingNumber) {
        this.memberReceivingNumber = memberReceivingNumber;
    }

    public int getRequestLimit() {
        return requestLimit;
    }

    private void setRequestLimit(int requestLimit) {
        this.requestLimit = requestLimit;
    }

    public BigDecimal getBusinessReceivingNumber() {
        return businessReceivingNumber;
    }

    private void setBusinessReceivingNumber(BigDecimal businessReceivingNumber) {
        this.businessReceivingNumber = businessReceivingNumber;
    }
}
