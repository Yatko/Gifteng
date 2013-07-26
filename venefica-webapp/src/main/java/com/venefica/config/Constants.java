/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gyuszi
 */
public interface Constants {
    
    public static final int USER_DEFAULT_PASSWORD_LENGTH = 5; //default password size
    public static final int FORGOT_PASSWORD_DEFAULT_CODE_LENGTH = 24; //default forgot password request code size
    public static final int FORGOT_PASSWORD_EXPIRATION_INTERVAL_CHECK_SECS = 1 * 60 * 60; // default: 1 hour
    public static final int FORGOT_PASSWORD_EXPIRATION_PERIOD_DAYS = 1; //default expiration of a forgot password request
    
    public static final int AD_MAX_ALLOWED_PROLONGATION = 1; //number of allowed relisting
    public static final int AD_EXPIRATION_INTERVAL_CHECK_SECS = 1 * 60; // default: 1 minut
    public static final int AD_PROLONGATION_PERIOD_DAYS = 30; //with how many days will be incremented the expiration date at relist
    public static final int AD_EXPIRATION_PERIOD_DAYS = 30; //the default expiration in days at ad creation
    public static final int SPAMMARK_MAX_ALLOWED = 3; //number of allowed marks, after this the ad will be considered as spam
    public static final int REQUEST_MAX_ALLOWED = 3; //number of allowed requests for an ad
    
    public static final int INVITATION_MAX_ALLOWED_USE = 5; //max allowed user creation with the same invitation
    public static final int INVITATION_DEFAULT_CODE_LENGTH = 4; //default invitation code size
    public static final int INVITATION_EXPIRATION_PERIOD_DAYS = 3; //the default expiration in days of an invitation
    public static final int INVITATION_EXPIRATION_REMINDER_DAYS = 1; //the days when invitation will expire
    public static final int INVITATION_EXPIRATION_INTERVAL_CHECK_SECS = 1 * 60 * 60; // default: 1 hour
    public static final int INVITATION_EXPIRATION_REMINDER_CHECK_SECS = 4 * 60 * 60; // default: 4 hour
    
    public static final String MODEL_PACKAGE = "com.venefica.model";
    
    public static final String CALLBACK_PATH = "callback";
    public static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";
    
    public static final String AD_DAO = "AdDao";
    public static final String INVITATION_DAO = "InvitationDao";
    public static final String FORGOT_PASSWORD_DAO = "ForgotPasswordDao";
    public static final String EMAIL_SENDER = "EmailSender";
    
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    public static final int TOKEN_EXPIRES_IN_DAYS = 14;
    public static final String MESSAGE_SIGNATURE = "MessageSig";
    public static final String AUTH_TOKEN = "AuthToken";
    public static final Set<String> OPERATIONS_FOR_SKIP_TOKEN_AUTHORIZATION = new HashSet<String>(Arrays.asList(
            //UserManagementService: registration related
            "RegisterUser",
            "RegisterBusinessUser",
            "GetAllBusinessCategories",
            
            //AuthService: login related
            "Authenticate",
            "AuthenticateEmail",
            "AuthenticatePhone",
            //AuthService: forgot password
            "ForgotPasswordEmail",
            "ChangeForgottenPassword",
            
            //InvitationService: invitation related
            "RequestInvitation",
            "IsInvitationValid",
            
            //UtilityService
            "GetAddressByZipcode"
    ));
    
}
