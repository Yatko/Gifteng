/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import java.util.HashMap;
import java.util.Map;
import mailjimp.dom.enums.EmailType;
import mailjimp.service.IMailJimpService;
import mailjimp.service.MailJimpException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
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
    
    public void listSubscribe(String emailAddress, Map<String, Object> vars) throws EmailException {
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
            logger.error("MailChimp error", ex);
            throw new EmailException("MailChimp error", ex);
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
