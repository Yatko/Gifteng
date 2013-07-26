/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author gyuszi
 */
@Entity
@Table(name = "user_setting")
public class UserSetting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ElementCollection(fetch=FetchType.EAGER, targetClass=NotificationType.class)
    @CollectionTable(name = "usersetting_notifiable_types", joinColumns = @JoinColumn(name="usersetting_id"))
    @ForeignKey(name = "usersetting_id")
    @Column(name="notifiable_type")
    @Enumerated(EnumType.STRING)
    private Set<NotificationType> notifiableTypes;
    
    private boolean emailsAllowed; //TODO: maybe it's not needed
    private boolean smsAllowed; //TODO: maybe it's not needed
    private boolean callsAllowed; //TODO: maybe it's not needed
    
    public UserSetting() {
    }
    
    /**
     * Checks if the given notification is set for the current user.
     * 
     * @param notificationType
     * @return 
     */
    public boolean notificationExists(NotificationType notificationType) {
        if ( notifiableTypes == null || notifiableTypes.isEmpty() ) {
            return false;
        }
        return notifiableTypes.contains(notificationType);
    }
    
    // getters/setters
    
    public boolean isEmailsAllowed() {
        return emailsAllowed;
    }

    public void setEmailsAllowed(boolean emailsAllowed) {
        this.emailsAllowed = emailsAllowed;
    }

    public boolean isSmsAllowed() {
        return smsAllowed;
    }

    public void setSmsAllowed(boolean smsAllowed) {
        this.smsAllowed = smsAllowed;
    }

    public boolean isCallsAllowed() {
        return callsAllowed;
    }

    public void setCallsAllowed(boolean callsAllowed) {
        this.callsAllowed = callsAllowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<NotificationType> getNotifiableTypes() {
        return notifiableTypes;
    }

    public void setNotifiableTypes(Set<NotificationType> notifiableTypes) {
        this.notifiableTypes = notifiableTypes;
    }
}
