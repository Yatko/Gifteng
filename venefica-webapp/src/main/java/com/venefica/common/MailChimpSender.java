/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.venefica.service.fault.GeneralException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mailjimp.dom.enums.EmailType;
import mailjimp.service.IMailJimpService;
import mailjimp.service.MailJimpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gyuszi
 */
public class MailChimpSender {
    
    private static final Log logger = LogFactory.getLog(MailChimpSender.class);
    
    @Autowired
    private IMailJimpService mailJimpService;
    
    private String listId;
    private boolean doubleOpt;
    private boolean enabled;
    
    /**
     * Returns the enabled status of this service.
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Subscribes the given email address to a predefined MailChimp list.
     * 
     * @param emailAddress
     * @param vars variables that are used on generated email
     * @throws MailException 
     */
    public void listSubscribe(String emailAddress, Map<String, Object> vars) throws MailException {
        if ( !enabled ) {
            logger.info("MailChimp usage is not enabled!");
            return;
        }
        
        try {
            boolean result = mailJimpService.listSubscribe(listId, emailAddress, vars, EmailType.HTML, doubleOpt, false, false, true);
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

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setDoubleOpt(boolean doubleOpt) {
        this.doubleOpt = doubleOpt;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
