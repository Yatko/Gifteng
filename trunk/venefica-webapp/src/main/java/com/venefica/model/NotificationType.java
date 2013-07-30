/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.model;

/**
 *
 * @author gyuszi
 */
public enum NotificationType {
    
    //invitation service
    //INVITATION_REQUESTED("invitation-request"), //sending out the Invitation Code
    //INVITATION_WILL_EXPIRE("invitation-reminder"), //when there is a small amount of time till the invitation will expire (invitation code reminder)
    
    //user management service
    //USER_REGISTERED("user-new"),
    //PASSWORD_RESET_REQUESTED("forgot-password"), //this will contain the link to reset password
    FOLLOWER_ADDED("follower-new"), //someone followed me
    
    //ad and message service
    AD_COMMENTED("ad-comment-new"), //someone commented an ad of yours
    AD_REQUESTED("ad-request"), //someone requested an ad of yours
    REQUEST_MESSAGED("request-message-new"), //someone sent a private message, without changing any giving or receiving status
    REQUEST_ACCEPTED("request-accept"), //the giver accepted your gift request
    REQUEST_SENT("request-sent"), //the giver gifted the gift
    REQUEST_RECEIVED("request-received"), //the requestor received the gift
    REQUEST_CANCELED("request-cancel"), //the requestor cancelled the request
    REQUEST_DECLINED("request-decline"), //the giver cancelled your request
    FOLLOWER_AD_CREATED("follower-ad-new"), //a member I follow posted a new gift
    
    //administration service
    //APPROVAL_AD_REJECTED("ad-approval-reject"), //if we don’t approve a gift
    //APPROVAL_AD_ACCEPTED("ad-approval-accept"), //if we approve a gift
    ;
    
    private String templateName;
    
    NotificationType(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public String getSubjectVelocityTemplate() {
        return getTemplateName() + "/subject.vm";
    }
    
    public String getHtmlMessageVelocityTemplate() {
        return getTemplateName() + "/message.html.vm";
    }
    
    public String getPlainMessageVelocityTemplate() {
        return getTemplateName() + "/message.txt.vm";
    }
}
