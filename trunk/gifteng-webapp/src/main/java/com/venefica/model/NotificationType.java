/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author gyuszi
 */
public enum NotificationType {
    
    //user management service
    FOLLOWER_ADDED("follower-new", false, false), //someone followed me
    //ad and message service
    AD_COMMENTED("ad-comment-new", false, false), //someone commented an ad of yours
    AD_REQUESTED("ad-request", false, false), //someone requested an ad of yours
    REQUEST_MESSAGED("request-message-new", false, false), //someone sent a private message, without changing any giving or receiving status
    REQUEST_ACCEPTED("request-accept", false, false), //the giver accepted your gift request
    REQUEST_SENT("request-sent", false, false), //the giver gifted the gift
    REQUEST_RECEIVED("request-received", false, false), //the requestor received the gift
    REQUEST_CANCELED("request-cancel", false, false), //the requestor cancelled the request
    REQUEST_DECLINED("request-decline", false, false), //the giver cancelled your request
    FOLLOWER_AD_CREATED("follower-ad-new", false, false), //a member I follow posted a new gift
    
    //system messages
    
    INVITATION_REMINDER("invitation-reminder", true, false),
    USER_VERIFICATION("user-verification", true, false),
    USER_WELCOME("user-welcome", true, false),
    AD_APPROVED("ad-approval-accept", true, false),
    AD_UNAPPROVED("ad-approval-reject", true, false),
    AD_ONLINE("ad-online", true, false),
    FORGOT_PASSWORD("forgot-password", true, false),
    UNKNOWN_USER_INVITATION("unknown-user-invitation", true, false),
    PASSWORD_CHANGED("password-changed", true, false),
    INVITATION_REQUEST("invitation-request", true, false),
    AD_NEW("ad-new", true, true),
    ISSUE_NEW("issue-new", true, true),
    SHIPPING("shipping", true, false),
    EMAIL_NEW("email-new", true, true), //TODO: needs to be renamed
    INVITATION_EMAIL_NEW("invitation-email-new", true, false), //TODO: needs to be renamed
    PROMOCODE_UPDATED("promocode-updated", true, false),
    SHARE("share", true, false),
    ;
    
    public static final String SUBTYPE_REQUEST_ACCEPTED_PICKUP = "pickup"; //the giver accepted your gift request - only pickup is possible
    public static final String SUBTYPE_REQUEST_ACCEPTED_SHIPPING = "shipping"; //the giver accepted your gift request - only shipping is possible
    public static final String SUBTYPE_REQUEST_ACCEPTED_BOTH = "both"; //the giver accepted your gift request - both shipping and local pickup is allowed
    
    public static final String SUBTYPE_INVITATION_REQUEST_RANGE = "range"; //spcial invitation request message for users with defined zipcodes
    public static final String SUBTYPE_INVITATION_REQUEST_OTHERS = "others"; //other type of invitation request message for users being outside of the defined zipcodes
    
    public static final String SUBTYPE_SHIPPING_CREATOR = "creator";
    public static final String SUBTYPE_SHIPPING_RECEIVER = "receiver";
    
    public static final Set<NotificationType> defaultMarkedNotifications = EnumSet.of(
        NotificationType.FOLLOWER_ADDED,
        NotificationType.AD_COMMENTED,
        NotificationType.AD_REQUESTED,
        NotificationType.REQUEST_MESSAGED,
        NotificationType.REQUEST_ACCEPTED,
        NotificationType.REQUEST_CANCELED,
        NotificationType.REQUEST_DECLINED
    );
    
    private String templateName;
    private boolean isSystemNotification;
    private boolean isForAdmins;
    
    NotificationType(String templateName, boolean isSystemNotification, boolean isForAdmins) {
        this.templateName = templateName;
        this.isSystemNotification = isSystemNotification;
        this.isForAdmins = isForAdmins;
    }
    
    public String getVelocityTemplatePath(String subtype) {
        return templateName + safeSubtype(subtype) + "/";
    }
    
    public boolean isSystemNotification() {
        return this.isSystemNotification;
    }
    
    private String safeSubtype(String subtype) {
        if ( subtype == null || subtype.trim().isEmpty() ) {
            subtype = "";
        } else {
            subtype = "/" + subtype.trim();
        }
        return subtype;
    }
}
