package com.venefica.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 * Advertisement rating. After a successful product (ad, gift) pickup/delivery
 * reviews can be left by both parties - requestor and ad owner.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @ForeignKey(name = "rating_ad_fk")
    private Ad ad;
    
    @ManyToOne
    @ForeignKey(name = "rating_from_usr_fk")
    private User from;
    
    @ManyToOne
    @ForeignKey(name = "rating_to_usr_fk")
    private User to;
    
    @Column(length = 1000, nullable = false)
    private String text;
    @Column(name = "valuee")
    private int value;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ratedAt;

    public Rating() {
        value = 0;
    }

    public Rating(Ad ad, User from, User to, String text, int value) {
        this.ad = ad;
        this.from = from;
        this.to = to;
        this.text = text;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }
    
    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getRatedAt() {
        return ratedAt;
    }

    public void setRatedAt(Date ratedAt) {
        this.ratedAt = ratedAt;
    }
}
