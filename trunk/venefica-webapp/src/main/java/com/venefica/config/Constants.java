/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import com.venefica.common.EmailSender;
import com.venefica.dao.AdDao;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.ForgotPasswordDao;
import com.venefica.dao.InvitationDao;
import com.venefica.dao.IssueDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserPointDao;
import com.venefica.dao.UserTransactionDao;
import com.venefica.dao.UserVerificationDao;
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
    public static final int FORGOT_PASSWORD_EXPIRATION_PERIOD_DAYS = 1; //default expiration of a forgot password request
    
    public static final int USER_VERIFICATION_DEFAULT_CODE_LENGTH = 24; //default user varification code size
    
    public static final int SPAMMARK_MAX_ALLOWED = 3; //number of allowed marks, after this the ad will be considered as spam
    public static final int REQUEST_MAX_ALLOWED = 3; //number of allowed requests for an ad
    
    public static final int INVITATION_MAX_ALLOWED_USE = 5; //max allowed user creation with the same invitation
    public static final int INVITATION_DEFAULT_CODE_LENGTH = 10; //default invitation code size
    public static final int INVITATION_EXPIRATION_PERIOD_DAYS = 30; //the default expiration in days of an invitation
    public static final int INVITATION_EXPIRATION_REMINDER_DAYS = 1; //the days when invitation will expire
    
    public static final int REQUEST_PROMO_CODE_DEFAULT_CODE_LENGTH = 15;
    
    public static final String MODEL_PACKAGE = "com.venefica.model";
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    public static final String APP_CONFIG = AppConfig.class.getSimpleName();
    public static final String EMAIL_CONFIG = EmailConfig.class.getSimpleName();
    public static final String INVITATION_CONFIG = InvitationConfig.class.getSimpleName();
    
    public static final String AD_DAO = AdDao.class.getSimpleName();
    public static final String AD_DATA_DAO = AdDataDao.class.getSimpleName();
    public static final String REQUEST_DAO = RequestDao.class.getSimpleName();
    public static final String USER_TRANSACTION_DAO = UserTransactionDao.class.getSimpleName();
    public static final String USER_DAO = UserDao.class.getSimpleName();
    public static final String USER_POINT_DAO = UserPointDao.class.getSimpleName();
    public static final String INVITATION_DAO = InvitationDao.class.getSimpleName();
    public static final String FORGOT_PASSWORD_DAO = ForgotPasswordDao.class.getSimpleName();
    public static final String USER_VERIFICATION_DAO = UserVerificationDao.class.getSimpleName();
    public static final String ISSUE_DAO = IssueDao.class.getSimpleName();
    public static final String EMAIL_SENDER = EmailSender.class.getSimpleName();
    
    public static final int TOKEN_EXPIRES_IN_DAYS = 365;
    public static final String MESSAGE_SIGNATURE = "MessageSig";
    public static final String AUTH_TOKEN = "AuthToken";
    public static final Set<String> OPERATIONS_FOR_SKIP_TOKEN_AUTHORIZATION = new HashSet<String>(Arrays.asList(
            //UserManagementService: registration related
            "RegisterUser",
            "RegisterBusinessUser",
            "GetAllBusinessCategories",
            "VerifyUser",
            
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
