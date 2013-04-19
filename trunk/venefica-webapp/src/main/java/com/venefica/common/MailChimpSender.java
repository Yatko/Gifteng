/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import java.util.Map;
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
            logger.error("MailChimp error (code: " + ex.getStatusCode() + ")", ex);
            if ( ex.getStatusCode() == 214 ) {
                throw new MailException(MailException.ALREADY_SUBSCRIBED, ex);
            }
            throw new MailException(MailException.GENERAL_ERROR, ex);
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
