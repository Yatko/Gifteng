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
//import javax.persistence.SequenceGenerator;

/**
 * Advertisement rating.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
//@SequenceGenerator(name = "rating_gen", sequenceName = "rating_seq", allocationSize = 1)
@Table(name = "rating")
public class Rating {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @ForeignKey(name = "rating_ad_fk")
    private Ad ad;
    
    @ManyToOne
    @ForeignKey(name = "rating_usr_fk")
    private User user;
    
    @Column(name = "valuee")
    private int value;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ratedAt;

    public Rating() {
        value = 0;
    }

    public Rating(Ad ad, User user, int value) {
        this.ad = ad;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
