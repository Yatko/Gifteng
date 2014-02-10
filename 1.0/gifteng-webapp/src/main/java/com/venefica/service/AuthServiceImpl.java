package com.venefica.service;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.common.MailException;
import com.venefica.common.RandomGenerator;
import com.venefica.config.Constants;
import com.venefica.dao.ForgotPasswordDao;
import com.venefica.model.ForgotPassword;
import com.venefica.model.MemberUserData;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserSetting;
import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authService")
@WebService(endpointInterface = "com.venefica.service.AuthService")
public class AuthServiceImpl extends AbstractService implements AuthService {

    private static final boolean AUTO_CREATE_USER_SETTING = false;
    
    @Inject
    private TokenEncryptor tokenEncryptor;
    @Inject
    private ForgotPasswordDao forgotPasswordDao;
    
    @Override
    public String authenticate(String name, String password, String userAgent) throws AuthenticationException {
        User user = userDao.findUserByName(name);
        if (user == null) {
            throw new AuthenticationException("No user for '" + name + "' name!");
        }
        return authenticate(user, password, userAgent);
    }
    
    @Override
    public String authenticateEmail(String email, String password, String userAgent) throws AuthenticationException {
        User user = userDao.findUserByEmail(email);
        if (user == null) {
            throw new AuthenticationException("No user for '" + email + "' email!");
        }
        return authenticate(user, password, userAgent);
    }
    
    @Override
    public String authenticatePhone(String phone, String password, String userAgent) throws AuthenticationException {
        User user = userDao.findUserByPhoneNumber(phone);
        if (user == null) {
            throw new AuthenticationException("No user for '" + phone + "' phone!");
        }
        return authenticate(user, password, userAgent);
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) throws UserNotFoundException, AuthorizationException {
        User user = getCurrentUser();
        changePasswordForUser(oldPassword, newPassword, user);
    }
    
    @Override
    @Transactional
    public void changeForgottenPassword(String newPassword, String code) throws UserNotFoundException, GeneralException {
        ForgotPassword forgotPassword = forgotPasswordDao.findByCode(code);
        if ( forgotPassword == null ) {
            throw new GeneralException("The given request code (" + code + ") does not exists");
        }
        
        if ( forgotPassword.isExpired() ) {
            throw new GeneralException("The given request code (" + code + ") is already expired");
        }
        if ( forgotPassword.isUsed()) {
            throw new GeneralException("The given request code (" + code + ") is already used");
        }
        
        String email = forgotPassword.getEmail();
        User user = userDao.findUserByEmail(email);
        if ( user == null ) {
            throw new UserNotFoundException("User could not be found by email address (" + email + ")");
        }
        
        try {
            String oldPassword = user.getPassword();
            changePasswordForUser(oldPassword, newPassword, user);
        } catch ( AuthorizationException ex ) {
            logger.error("Change password failed for user with email (" + email + ")", ex);
            throw new GeneralException("Cannot change password");
        }
        
        forgotPassword.markAsUsed();
    }
    
    @Override
    @Transactional
    public void forgotPasswordEmail(String email, String ipAddress) throws UserNotFoundException, GeneralException {
        User user = userDao.findUserByEmail(email);
        if ( user == null ) {
            logger.debug("User not found by email address (" + email + ")");
            throw new UserNotFoundException("The email address (" + email + ") does not exists");
        }
        
        ForgotPassword forgotPassword = forgotPasswordDao.findNonExpiredRequestByEmail(email);
        if ( forgotPassword == null ) {
            String code;
            int generationTried = 0;
            while ( true ) {
                code = RandomGenerator.generateAlphanumeric(Constants.FORGOT_PASSWORD_DEFAULT_CODE_LENGTH);
                generationTried++;
                if ( forgotPasswordDao.findByCode(code) == null ) {
                    //the generated code does not exists, found an unused (free) one
                    break;
                } else if ( generationTried >= 10 ) {
                    throw new GeneralException("Cannot generate valid forgot password request code!");
                }
            }

            if ( ipAddress == null || ipAddress.trim().isEmpty() ) {
                ipAddress = getIpAddress();
            }
            
            forgotPassword = new ForgotPassword();
            forgotPassword.setIpAddress(ipAddress);
            forgotPassword.setExpiresAt(DateUtils.addDays(new Date(), Constants.FORGOT_PASSWORD_EXPIRATION_PERIOD_DAYS));
            forgotPassword.setExpired(false);
            forgotPassword.setCode(code);
            forgotPassword.setEmail(email);
            forgotPasswordDao.save(forgotPassword);
        }
        
        try {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("code", forgotPassword.getCode());
            vars.put("user", user);

            boolean success = emailSender.sendNotification(NotificationType.FORGOT_PASSWORD, email, vars);
            if ( !success ) {
                logger.error("Email send exception at forgot password");
                throw new GeneralException(MailException.EMAIL_SEND_ERROR, "Could not send forgot password mail!");
            }
        } catch ( Exception ex ) {
            logger.error("Runtime exception", ex);
            throw new GeneralException(GeneralException.GENERAL_ERROR, ex.getMessage());
        }
    }
    
    // internal
    
    @Transactional
    private String authenticate(User user, String password, String userAgent) throws AuthenticationException {
        if (user == null) {
            throw new AuthenticationException("Wrong user!");
        }

        try {
            if (user.getPassword().equals(password)) {
                Token token = new Token(user.getId());
                String encryptedToken = tokenEncryptor.encrypt(token);
                
                if ( !user.isBusinessAccount() && AUTO_CREATE_USER_SETTING ) {
                    //automatically creating user setting if not present
                    MemberUserData userData = (MemberUserData) user.getUserData();
                    UserSetting userSetting = userData.getUserSetting();
                    if ( userSetting == null ) {
                        createUserSetting(userData);
                    }
                }
                
                user.setLastLoginAt(new Date());
                user.setLastUserAgent(userAgent);
                userDao.update(user);
                
                return encryptedToken;
            }
        } catch (TokenEncryptionException e) {
            logger.error("Encryption error", e);
            throw new AuthenticationException("Internal error!");
        }

        throw new AuthenticationException("Wrong user or password!");
    }
    
    @Transactional
    private void changePasswordForUser(String oldPassword, String newPassword, User user) throws AuthorizationException {
        if (oldPassword == null) {
            throw new NullPointerException("oldPassword is null");
        }
        if (newPassword == null) {
            throw new NullPointerException("newPassword is null");
        }
        
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
        } else {
            throw new AuthorizationException("Old passwod is wrong!");
        }
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("user", user);
        
        emailSender.sendNotification(NotificationType.PASSWORD_CHANGED, user, vars);
    }
}
