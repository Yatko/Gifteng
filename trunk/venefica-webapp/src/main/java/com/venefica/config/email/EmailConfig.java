/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config.email;

import com.venefica.common.EmailSender;
import com.venefica.common.MailChimpSender;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/application.properties")
public class EmailConfig {
    
    @Inject
    private Environment environment;
    
    @Bean(initMethod = "init")
    public EmailSender emailSender() {
        int smtpPort = environment.getProperty("email.smtpPort", int.class);
        int smtpPortSSL = environment.getProperty("email.smtpPortSSL", int.class);
        String charset = environment.getProperty("email.charset");
        String imagesBaseUrl = environment.getProperty("email.imagesBaseUrl");
        String hostName = environment.getProperty("email.hostName");
        String username = environment.getProperty("email.username");
        String password = environment.getProperty("email.password");
        boolean useSSL = environment.getProperty("email.useSSL", boolean.class);
        String fromEmailAddress = environment.getProperty("email.fromEmailAddress");
        String fromName = environment.getProperty("email.fromName");
        String undeliveredEmailAddress = environment.getProperty("email.undeliveredEmailAddress");
        boolean enabled = environment.getProperty("email.enabled", boolean.class);
        
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
        emailService.setEnabled(enabled);
        return emailService;
    }
    
    @Bean
    public MailChimpSender mailChimpSender() {
        String listId = environment.getProperty("mj.listId");
        boolean doubleOpt = environment.getProperty("mj.doubleOpt", boolean.class);
        boolean enabled = environment.getProperty("mj.enabled", boolean.class);
        
        MailChimpSender mailChimpSender = new MailChimpSender();
        mailChimpSender.setListId(listId);
        mailChimpSender.setDoubleOpt(doubleOpt);
        mailChimpSender.setEnabled(enabled);
        return mailChimpSender;
    }
}
