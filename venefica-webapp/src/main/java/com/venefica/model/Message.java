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
//import javax.persistence.SequenceGenerator;

/**
 * Describes a message sent to a user. Messages are meant to be private.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
//@SequenceGenerator(name = "message_gen", sequenceName = "message_seq", allocationSize = 1)
@Table(name = "message")
public class Message {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_gen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @ForeignKey(name = "message_request_fk")
    private Request request;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "message_to_fk")
    private User to;
    
    @ManyToOne(optional = false)
    @ForeignKey(name = "message_from_fk")
    private User from;
    
    private boolean hiddenByRecipient;
    private boolean hiddenBySender;
    
    @Column(length = 2000)
    private String text;
    
    @Column(name = "readd")
    private boolean read;
    
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    private boolean deleted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    public Message() {
        read = false;
    }

    public Message(String text) {
        this();
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public boolean isHiddenByRecipient() {
        return hiddenByRecipient;
    }

    public void setHiddenByRecipient(boolean hiddenByRecipient) {
        this.hiddenByRecipient = hiddenByRecipient;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public boolean isHiddenBySender() {
        return hiddenBySender;
    }

    public void setHiddenBySender(boolean hiddenBySender) {
        this.hiddenBySender = hiddenBySender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
