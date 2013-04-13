/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.email;

import com.venefica.common.EmailSender;
import com.venefica.config.Constants;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author gyuszi
 */
public abstract class AbstractEmailConfig {
    
    private static final int DEFAULT_SMTP_PORT = 25;
    private static final int DEFAULT_SMTP_PORT_SSL = 465;
    private static final String DEFAULT_BASE_URL = "http://veneficalabs.com/";
    
    @Inject
    private int smtpPort = DEFAULT_SMTP_PORT;
    @Inject
    private int smtpPortSSL = DEFAULT_SMTP_PORT_SSL;
    @Inject
    private String charset = Constants.DEFAULT_CHARSET;
    @Inject
    private String hostName;
    @Inject
    private String username;
    @Inject
    private String password;
    @Inject
    private boolean useSSL;
    @Inject
    private String fromEmailAddress;
    @Inject
    private String fromName;
    @Inject
    private String undeliveredEmailAddress;
    @Inject
    private String imagesBaseUrl = DEFAULT_BASE_URL;
    
    @Bean
    public EmailSender emailSender() {
        EmailSender emailService = new EmailSender();
        emailService.setSmtpPort(smtpPort);
        emailService.setSmtpPortSSL(smtpPortSSL);
        emailService.setCharset(charset);
        emailService.setHostName(hostName);
        emailService.setUsername(username);
        emailService.setPassword(password);
        emailService.setUseSSL(useSSL);
        emailService.setFromEmailAddress(fromEmailAddress);
        emailService.setFromName(fromName);
        emailService.setUndeliveredEmailAddress(undeliveredEmailAddress);
        emailService.setImagesBaseUrl(imagesBaseUrl);
        return emailService;
    }
}
