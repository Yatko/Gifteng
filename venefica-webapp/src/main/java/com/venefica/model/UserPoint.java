/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 * This entity stores all the user points (pending, insider, exclusive).
 * 
 * TODO: not yet finished.
 * 
 * @author gyuszi
 */
@Entity
//@SequenceGenerator(name = "userpoint_gen", sequenceName = "userpoint_seq", allocationSize = 1)
@Table(name = "user_point")
public class UserPoint {
    
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userpoint_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    @ForeignKey(name = "userpoint_user_fk")
    private User user;
    
    @OneToMany(mappedBy = "userPoint")
    private List<UserTransaction> transactions;
    
    private BigDecimal number; //generosity number
    private BigDecimal score; //generosity score
    
    public UserPoint() {
    }
    
    public UserPoint(int number, int score) {
        this.number = new BigDecimal(number);
        this.score = new BigDecimal(score);
    }
    
    // helper methods

    public void addNumber(BigDecimal n) {
        if ( number == null ) {
            number = BigDecimal.ZERO;
        }
        number = number.add(n);
    }
    
    public void addScore(BigDecimal s) {
        if ( score == null ) {
            score = BigDecimal.ZERO;
        }
        score = score.add(s);
    }
    
    public BigDecimal getPendingNumber() {
        if ( transactions == null || transactions.isEmpty() ) {
            return BigDecimal.ZERO;
        }
        BigDecimal pendingNumber = BigDecimal.ZERO;
        for ( UserTransaction transaction : transactions ) {
            pendingNumber = pendingNumber.add(transaction.getPendingNumber());
        }
        return pendingNumber;
    }
    
    public BigDecimal getPendingScore() {
        if ( transactions == null || transactions.isEmpty() ) {
            return BigDecimal.ZERO;
        }
        BigDecimal pendingScore = BigDecimal.ZERO;
        for ( UserTransaction transaction : transactions ) {
            pendingScore = pendingScore.add(transaction.getPendingScore());
        }
        return pendingScore;
    }
    
    public BigDecimal getGivingNumber(boolean includePending) {
        return getGivingNumber(user, includePending);
    }
    
    public BigDecimal getReceivingNumber(boolean includePending) {
        return getReceivingNumber(user, includePending);
    }
    
    public int getActivityBalance(boolean includePending) {
        return getActivityBalance(user, includePending);
    }
    
    public int getGivingActivity(boolean includePending) {
        return getGivingActivity(user, includePending);
    }
    
    public int getReceivingActivity(boolean includePending) {
        return getReceivingActivity(user, includePending);
    }
    
    public BigDecimal getGenerosityNumber(boolean includePending) {
        return getGenerosityNumber(user, includePending);
    }
    
    // static helpers
    
    public static int getActivityBalance(User user, boolean includePending) {
        return getGivingActivity(user, includePending) - getReceivingActivity(user, includePending);
    }
    
    public static int getGivingActivity(User user, boolean includePending) {
        int activity = 0;
        for ( Ad ad : user.getAds() ) {
            if ( !includePending && !ad.isSent()) {
                //only sent ads will be considered
                continue;
            }
            
            Integer quantity = ad.getAdData().getQuantity();
            if ( quantity == null ) {
                quantity = 0;
            }
            activity += quantity;
        }
        return activity;
    }
    
    public static int getReceivingActivity(User user, boolean includePending) {
        int activity = 0;
        for ( Request request : user.getRequests()) {
            Ad ad = request.getAd();
            
            if ( !includePending && !request.isSelected() && !ad.isReceived() ) {
                //only selected request and received ad is considered
                continue;
            }
            
            activity += 1;
        }
        return activity;
    }
    
    public static BigDecimal getGenerosityNumber(User user, boolean includePending) {
        BigDecimal numberBalance = getGivingNumber(user, includePending).subtract(getReceivingNumber(user, includePending));
        BigDecimal activityBalance = new BigDecimal(getActivityBalance(user, includePending)).divide(HUNDRED).add(BigDecimal.ONE);
        return numberBalance.multiply(activityBalance).add(new BigDecimal(getGivingActivity(user, includePending)));
    }
    
    public static BigDecimal getGivingNumber(User user, boolean includePending) {
        UserPoint userPoint = user.getUserPoint();
        BigDecimal number = BigDecimal.ZERO;
        for ( Ad ad : user.getAds() ) {
            if ( !includePending && !ad.isSent()) {
                //only sent ads will be considered
                continue;
            } else if ( userPoint.containsTransaction(ad) ) {
                //ad is already present in a transaction (there was a culculatio with it)
                continue;
            }
            
            number = number.add(getNumber(ad));
        }
        return number;
    }
    
    public static BigDecimal getReceivingNumber(User user, boolean includePending) {
        UserPoint userPoint = user.getUserPoint();
        BigDecimal number = BigDecimal.ZERO;
        for ( Request request : user.getRequests()) {
            Ad ad = request.getAd();
            
            if ( !includePending && !request.isSelected() && !ad.isReceived() ) {
                //only selected request and received ad is considered
                continue;
            } else if ( userPoint.containsTransaction(request) ) {
                //request is already present in a transaction (there was a culculatio with it)
                continue;
            }
            
            number = number.add(getNumber(ad));
        }
        return number;
    }
    
    private static BigDecimal getNumber(Ad ad) {
        BigDecimal price = ad.getAdData().getPrice();
        if ( price == null ) {
            price = BigDecimal.ZERO;
        }
        BigDecimal number = price.multiply(BigDecimal.TEN).divide(HUNDRED);
        return number;
    }
    
    private boolean containsTransaction(Ad ad) {
        if ( transactions == null || transactions.isEmpty() ) {
            return false;
        }
        for ( UserTransaction transaction : transactions ) {
            if ( transaction.getAd() != null && transaction.getAd().equals(ad) ) {
                return true;
            }
        }
        return false;
    }
    
    private boolean containsTransaction(Request request) {
        if ( transactions == null || transactions.isEmpty() ) {
            return false;
        }
        for ( UserTransaction transaction : transactions ) {
            if ( transaction.getRequest()!= null && transaction.getRequest().equals(request) ) {
                return true;
            }
        }
        return false;
    }
    
    // getters/setters
    
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public List<UserTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<UserTransaction> transactions) {
        this.transactions = transactions;
    }
}
