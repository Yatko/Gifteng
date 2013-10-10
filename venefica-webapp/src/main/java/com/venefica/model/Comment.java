package com.venefica.model;

import java.util.Date;
import javax.persistence.Basic;
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
 * Comment for an ad. Comments are publicly available for every user.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "comment_ad_fk")
    private Ad ad;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "comment_usr_fk")
    private User publisher;
    
    @Column(length = 255)
    private String text;
    
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @ManyToOne()
    @ForeignKey(name = "comment_reply_fk")
    private Comment reply;

    public Comment() {
    }

    public Comment(Ad ad, User publisher, String text) {
        this.ad = ad;
        this.publisher = publisher;
        this.text = text;
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

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Comment getReply() {
        return reply;
    }

    public void setReply(Comment reply) {
        this.reply = reply;
    }
}
