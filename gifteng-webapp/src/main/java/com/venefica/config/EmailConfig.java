/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/" + Constants.APPLICATION_PROPERTIES)
public class EmailConfig {
    
    @Inject
    private Environment environment;
    
    private int smtpPort;
    private int smtpPortSSL;
    private String baseUrl;
    private String charset;
    private String hostName;
    private String username;
    private String password;
    private boolean useSSL;
    private String fromEmailAddress;
    private String fromName;
    private String undeliveredEmailAddress;
    private String issueEmailAddress;
    private String[] imagesBaseUrls;
    private boolean emailEnabled;
    
    private String mailChimpListId;
    private boolean mailChimpDoubleOpt;
    private boolean mailChimpEnabled;
    
    @PostConstruct
    public void init() {
        smtpPort = environment.getProperty("email.smtpPort", int.class);
        smtpPortSSL = environment.getProperty("email.smtpPortSSL", int.class);
        baseUrl = environment.getProperty("email.baseUrl");
        charset = environment.getProperty("email.charset");
        hostName = environment.getProperty("email.hostName");
        username = environment.getProperty("email.username");
        password = environment.getProperty("email.password");
        useSSL = environment.getProperty("email.useSSL", boolean.class);
        fromEmailAddress = environment.getProperty("email.fromEmailAddress");
        fromName = environment.getProperty("email.fromName");
        undeliveredEmailAddress = environment.getProperty("email.undeliveredEmailAddress");
        issueEmailAddress = environment.getProperty("email.issueEmailAddress");
        emailEnabled = environment.getProperty("email.enabled", boolean.class);
        
        if ( environment.containsProperty("email.imagesBaseUrl") ) {
            imagesBaseUrls = new String[] {environment.getProperty("email.imagesBaseUrl")};
        } else {
            Set<String> urls = new HashSet<String>(0);
            for ( int i = 0; i < 10; i++ ) {
                String key = "email.imagesBaseUrl." + i;
                if ( !environment.containsProperty(key) ) {
                    continue;
                }
                String value = environment.getProperty(key);
                if ( value != null && !value.trim().isEmpty() ) {
                    urls.add(value);
                }
            }
            imagesBaseUrls = urls.toArray(new String[0]);
        }
        
        mailChimpListId = environment.getProperty("mj.listId");
        mailChimpDoubleOpt = environment.getProperty("mj.doubleOpt", boolean.class);
        mailChimpEnabled = environment.getProperty("mj.enabled", boolean.class);
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public int getSmtpPortSSL() {
        return smtpPortSSL;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCharset() {
        return charset;
    }

    public String getHostName() {
        return hostName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public String getUndeliveredEmailAddress() {
        return undeliveredEmailAddress;
    }
    
    public String getIssueEmailAddress() {
        return issueEmailAddress;
    }

    public String[] getImagesBaseUrls() {
        return imagesBaseUrls;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public String getMailChimpListId() {
        return mailChimpListId;
    }

    public boolean isMailChimpDoubleOpt() {
        return mailChimpDoubleOpt;
    }

    public boolean isMailChimpEnabled() {
        return mailChimpEnabled;
    }
}
