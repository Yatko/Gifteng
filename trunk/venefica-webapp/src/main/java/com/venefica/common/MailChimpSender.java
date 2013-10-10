/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.config.EmailConfig;
import com.venefica.service.fault.GeneralException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import mailjimp.dom.enums.EmailType;
import mailjimp.service.IMailJimpService;
import mailjimp.service.MailJimpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author gyuszi
 */
@Component
public class MailChimpSender {
    
    private static final Log logger = LogFactory.getLog(MailChimpSender.class);
    
    @Autowired
    private IMailJimpService mailJimpService;
    @Inject
    private EmailConfig emailConfig;
    
    /**
     * Subscribes the given email address to a predefined MailChimp list.
     * 
     * @param emailAddress
     * @param vars variables that are used on generated email
     * @throws MailException 
     */
    public void listSubscribe(String emailAddress, Map<String, Object> vars) throws MailException {
        if ( !emailConfig.isMailChimpEnabled() ) {
            logger.info("MailChimp usage is not enabled!");
            return;
        }
        
        try {
            boolean result = mailJimpService.listSubscribe(
                    emailConfig.getMailChimpListId(),
                    emailAddress,
                    vars,
                    EmailType.HTML,
                    emailConfig.isMailChimpDoubleOpt(),
                    false,
                    false,
                    true
                    );
            if ( result ) {
                logger.info("MailChimp listSubscribe() succeeded");
            } else {
                logger.info("MailChimp listSubscribe() failed");
            }
        } catch (MailJimpException ex) {
            logger.error("MailChimp error (status code: " + ex.getStatusCode() + ")", ex);
            
            int errorCode = GeneralException.GENERAL_ERROR;
            String message = ex.getMessage();
            Pattern pattern = Pattern.compile("Code: (.*?)\\.");
            Matcher matcher = pattern.matcher(message);
            if ( matcher.find() ) {
                String code = matcher.group(1);
                if ( code.equals("214") ) {
                    errorCode = MailException.ALREADY_SUBSCRIBED;
                }
            }
            
            throw new MailException(errorCode, ex);
        }
    }
}
