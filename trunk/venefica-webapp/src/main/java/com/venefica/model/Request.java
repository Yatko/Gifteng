/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "request")
public class Request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "request_ad_fk")
    private Ad ad;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "request_usr_fk")
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;
    
//    @Enumerated(EnumType.STRING)
//    private RequestStatus status;
    
    public Request() {
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Request)) {
            return false;
        }

        Request other = (Request) obj;

        return id != null && id.equals(other.id);
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

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

//    public RequestStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(RequestStatus status) {
//        this.status = status;
//    }
}
