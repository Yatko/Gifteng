/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.Constants;
import com.venefica.dao.UserVerificationDao;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserVerification;
import com.venefica.service.fault.GeneralException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gyuszi
 */
@Named
public class UserVerificationUtil {
    
    private static final Log logger = LogFactory.getLog(UserVerificationUtil.class);
    
    @Inject
    protected EmailSender emailSender;
    @Inject
    private UserVerificationDao userVerificationDao;
    
    public void createAndSendUserWelcome(User user) {
        try {
            UserVerification userVerification = createUserVerification(user);

            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("code", userVerification.getCode());
            vars.put("user", user);

            emailSender.sendNotification(NotificationType.USER_WELCOME, user, vars);
        } catch ( GeneralException ex ) {
            logger.error("Cannot create user verification", ex);
        }
    }
    
    // internal helpers
    
    private UserVerification createUserVerification(User user) throws GeneralException {
        UserVerification userVerification = new UserVerification();
        userVerification.setCode(getUserVerificationCode());
        userVerification.setUser(user);
        userVerificationDao.save(userVerification);
        
        return userVerification;
    }
    
    private String getUserVerificationCode() throws GeneralException {
        String code;
        int generationTried = 0;
        while ( true ) {
            code = RandomGenerator.generateAlphanumeric(Constants.USER_VERIFICATION_DEFAULT_CODE_LENGTH);
            generationTried++;
            if ( userVerificationDao.findByCode(code) == null ) {
                //the generated code does not exists, found an unused (free) one
                break;
            } else if ( generationTried >= 10 ) {
                throw new GeneralException("Cannot generate valid user verification code!");
            }
        }
        return code;
    }
}
