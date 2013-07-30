/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.math.BigDecimal;
import java.util.Set;
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
    
//    private static final BigDecimal HUNDRED = new BigDecimal(100);
    
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userpoint_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    @ForeignKey(name = "userpoint_user_fk")
    private User user;
    
    @OneToMany(mappedBy = "userPoint")
    private Set<UserTransaction> transactions;
    
    private BigDecimal givingNumber;
    private BigDecimal receivingNumber;
    
//    private BigDecimal number; //generosity number
//    private BigDecimal score; //generosity score
    
    public UserPoint() {
    }
    
    public UserPoint(int givingNumber, int receivingNumber) {
        this.givingNumber = new BigDecimal(givingNumber);
        this.receivingNumber = new BigDecimal(receivingNumber);
    }
    
//    public UserPoint(int number, int score) {
//        this.number = new BigDecimal(number);
//        this.score = new BigDecimal(score);
//    }
    
    // helper methods

    public void addGivingNumber(BigDecimal n) {
        n = checkNumber(n);
        givingNumber = checkNumber(givingNumber);
        givingNumber = givingNumber.add(n);
    }
    
//    public void removeGivingNumber(BigDecimal n) {
//        n = checkNumber(n);
//        givingNumber = checkNumber(givingNumber);
//        givingNumber = givingNumber.subtract(n);
//    }
    
    public void addReceivingNumber(BigDecimal n) {
        n = checkNumber(n);
        receivingNumber = checkNumber(receivingNumber);
        receivingNumber = receivingNumber.add(n);
    }
    
//    public void removeReceivingNumber(BigDecimal n) {
//        n = checkNumber(n);
//        receivingNumber = checkNumber(receivingNumber);
//        receivingNumber = receivingNumber.subtract(n);
//    }
    
    private BigDecimal checkNumber(BigDecimal n) {
        if ( n == null ) {
            n = BigDecimal.ZERO;
        }
        return n;
    }
    
//    public void addNumber(BigDecimal n) {
//        if ( number == null ) {
//            number = BigDecimal.ZERO;
//        }
//        number = number.add(n);
//    }
//    
//    public void removeNumber(BigDecimal n) {
//        if ( number == null ) {
//            number = BigDecimal.ZERO;
//        }
//        number = number.subtract(n);
//    }
//    
//    public void addScore(BigDecimal s) {
//        if ( score == null ) {
//            score = BigDecimal.ZERO;
//        }
//        score = score.add(s);
//    }
//    
//    public void removeScore(BigDecimal s) {
//        if ( score == null ) {
//            score = BigDecimal.ZERO;
//        }
//        score = score.subtract(s);
//    }
    
    public BigDecimal getPendingGivingNumber() {
        if ( transactions == null || transactions.isEmpty() ) {
            return BigDecimal.ZERO;
        }
        BigDecimal pendingGivingNumber = BigDecimal.ZERO;
        for ( UserTransaction transaction : transactions ) {
            pendingGivingNumber = pendingGivingNumber.add(transaction.getPendingGivingNumber() != null ? transaction.getPendingGivingNumber() : BigDecimal.ZERO);
        }
        return pendingGivingNumber;
    }
    
    public BigDecimal getPendingReceivingNumber() {
        if ( transactions == null || transactions.isEmpty() ) {
            return BigDecimal.ZERO;
        }
        BigDecimal pendingReceivingNumber = BigDecimal.ZERO;
        for ( UserTransaction transaction : transactions ) {
            pendingReceivingNumber = pendingReceivingNumber.add(transaction.getPendingReceivingNumber() != null ? transaction.getPendingReceivingNumber() : BigDecimal.ZERO);
        }
        return pendingReceivingNumber;
    }
    
//    public BigDecimal getPendingScore() {
//        if ( transactions == null || transactions.isEmpty() ) {
//            return BigDecimal.ZERO;
//        }
//        BigDecimal pendingScore = BigDecimal.ZERO;
//        for ( UserTransaction transaction : transactions ) {
//            pendingScore = pendingScore.add(transaction.getPendingScore());
//        }
//        return pendingScore;
//    }
    
//    private BigDecimal getGivingNumber(boolean includePending) {
//        return getGivingNumber(user, includePending);
//    }
    
//    private BigDecimal getReceivingNumber(boolean includePending) {
//        return getReceivingNumber(user, includePending);
//    }
    
//    private int getActivityBalance(boolean includePending) {
//        return getActivityBalance(user, includePending);
//    }
    
//    private int getGivingActivity(boolean includePending) {
//        return getGivingActivity(user, includePending);
//    }
    
//    private int getReceivingActivity(boolean includePending) {
//        return getReceivingActivity(user, includePending);
//    }
    
//    private BigDecimal getGenerosityNumber(boolean includePending) {
//        return getGenerosityNumber(user, includePending);
//    }
    
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
    
    // static helpers
    
    public static BigDecimal getGivingNumber(Ad ad) {
        Integer quantity = ad.getAdData().getQuantity();
        BigDecimal number = ad.getNumber();
        
        if ( quantity == null || quantity <= 0 ) {
            quantity = 1;
        }
        
        number = number.multiply(new BigDecimal(quantity));
        return number;
    }
    
    public static BigDecimal getReceivingNumber(Request request) {
        Ad ad = request.getAd();
        BigDecimal number = ad.getNumber();
        return number;
    }
    
//    public static BigDecimal getGenerosityNumber(User user, boolean includePending) {
//        BigDecimal numberBalance = getGivingNumber(user, includePending).subtract(getReceivingNumber(user, includePending));
//        BigDecimal activityBalance = new BigDecimal(getActivityBalance(user, includePending)).divide(HUNDRED).add(BigDecimal.ONE);
//        return numberBalance.multiply(activityBalance).add(new BigDecimal(getGivingActivity(user, includePending)));
//    }
    
//    private static int getActivityBalance(User user, boolean includePending) {
//        return getGivingActivity(user, includePending) - getReceivingActivity(user, includePending);
//    }
    
//    private static int getGivingActivity(User user, boolean includePending) {
//        int activity = 0;
//        for ( Ad ad : user.getAds() ) {
//            if ( ad.getRequests() == null ) {
//                continue;
//            }
//            
//            for ( Request request : ad.getRequests()) {
//                if ( !includePending && !request.isSent()) {
//                    //only sent requests will be considered
//                    continue;
//                }
//                
//                Integer quantity = ad.getAdData().getQuantity();
//                if ( quantity == null ) {
//                    quantity = 0;
//                }
//                activity += quantity;
//            }
//        }
//        return activity;
//    }
    
//    private static int getReceivingActivity(User user, boolean includePending) {
//        int activity = 0;
//        for ( Request request : user.getRequests()) {
//            Ad ad = request.getAd();
//            
//            if ( !includePending && !request.isAccepted() && !request.isReceived() ) {
//                //only selected and received request is considered
//                continue;
//            }
//            
//            activity += 1;
//        }
//        return activity;
//    }
    
//    private static BigDecimal getGivingNumber(User user, boolean includePending) {
//        UserPoint userPoint = user.getUserPoint();
//        BigDecimal number = BigDecimal.ZERO;
//        for ( Ad ad : user.getAds() ) {
//            if ( userPoint.containsTransaction(ad) ) {
//                //ad is already present in a transaction (there was a culculation with it)
//                continue;
//            } else if ( ad.getRequests() == null ) {
//                continue;
//            }
//            
//            for ( Request request : ad.getRequests() ) {
//                if ( !includePending && !request.isSent()) {
//                    //only sent requests will be considered
//                    continue;
//                }
//
//                number = number.add(getNumber(ad));
//            }
//        }
//        return number;
//    }
    
//    private static BigDecimal getReceivingNumber(User user, boolean includePending) {
//        UserPoint userPoint = user.getUserPoint();
//        BigDecimal number = BigDecimal.ZERO;
//        for ( Request request : user.getRequests()) {
//            Ad ad = request.getAd();
//            
//            if ( !includePending && !request.isAccepted() && !request.isReceived() ) {
//                //only selected and received request is considered
//                continue;
//            } else if ( userPoint.containsTransaction(request) ) {
//                //request is already present in a transaction (there was a culculation with it)
//                continue;
//            }
//            
//            number = number.add(getNumber(ad));
//        }
//        return number;
//    }
    
//    private static BigDecimal getNumber(Ad ad) {
//        BigDecimal price = ad.getAdData().getPrice();
//        if ( price == null ) {
//            price = BigDecimal.ZERO;
//        }
//        BigDecimal number = price.multiply(BigDecimal.TEN).divide(HUNDRED);
//        return number;
//    }
    
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

//    public BigDecimal getScore() {
//        return score;
//    }
//
//    public void setScore(BigDecimal score) {
//        this.score = score;
//    }
//
//    public BigDecimal getNumber() {
//        return number;
//    }
//
//    public void setNumber(BigDecimal number) {
//        this.number = number;
//    }

    public Set<UserTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<UserTransaction> transactions) {
        this.transactions = transactions;
    }

    public BigDecimal getGivingNumber() {
        return givingNumber;
    }

    public void setGivingNumber(BigDecimal givingNumber) {
        this.givingNumber = givingNumber;
    }

    public BigDecimal getReceivingNumber() {
        return receivingNumber;
    }

    public void setReceivingNumber(BigDecimal receivingNumber) {
        this.receivingNumber = receivingNumber;
    }
}
