/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

/**
 *
 * @author gyuszi
 */
public class EmailSender {
    
    private static final Log logger = LogFactory.getLog(EmailSender.class);
    
    private int smtpPort;
    private int smtpPortSSL;
    private String charset;
    private String hostName;
    private String username;
    private String password;
    private boolean useSSL;
    private String fromEmailAddress;
    private String fromName;
    private String undeliveredEmailAddress;
    private String imagesBaseUrl;
    private boolean enabled;
    
    /**
     * Returns the enabled status of this service.
     * @return 
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Send a html message to the given address.
     * 
     * @param subject email subject
     * @param htmlMessage html message content
     * @param textMessage plain text message (in case that the recipient does
     * not support html)
     * @param toEmailAddress recipient email address
     * @throws EmailException can be thrown in multiple cases: wrong email address,
     * invalid message
     */
    public void sendHtmlEmail(String subject, String htmlMessage, String textMessage, String toEmailAddress) throws EmailException {
        if ( !enabled ) {
            logger.info("Email sending is not enabled!");
            return;
        }
        
        ImageHtmlEmail email = new ImageHtmlEmail();
        try {
            email.setDataSourceResolver(new DataSourceUrlResolver(new URL(imagesBaseUrl)));
        } catch ( MalformedURLException ex ) {
            logger.error("The given image base URL (" + imagesBaseUrl + ") is invalid. Email sending is not skipped.", ex);
        }
        email.setHostName(hostName);
        email.setSmtpPort(smtpPort);
        email.setSslSmtpPort(Integer.toString(smtpPortSSL));
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(useSSL);
        email.setFrom(fromEmailAddress, fromName, charset);
        email.setBounceAddress(undeliveredEmailAddress);
        email.setCharset(charset);
        email.addTo(toEmailAddress);
        email.setSubject(subject);
        email.setHtmlMsg(htmlMessage);
        email.setTextMsg(textMessage);
        email.send();
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpPortSSL(int smtpPortSSL) {
        this.smtpPortSSL = smtpPortSSL;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setUndeliveredEmailAddress(String undeliveredEmailAddress) {
        this.undeliveredEmailAddress = undeliveredEmailAddress;
    }

    public void setImagesBaseUrl(String imagesBaseUrl) {
        this.imagesBaseUrl = imagesBaseUrl;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
