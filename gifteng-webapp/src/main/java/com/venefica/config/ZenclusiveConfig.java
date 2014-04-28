/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

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
public class ZenclusiveConfig {
    
    @Inject
    private Environment environment;
    
    private String accountId;
    private String password;
    private String emailPrefix;
    private String emailDomain;
    
    @PostConstruct
    public void init() {
        accountId = environment.getProperty("zenclusive.accountId");
        password = environment.getProperty("zenclusive.password");
        emailPrefix = environment.getProperty("zenclusive.emailPrefix");
        emailDomain = environment.getProperty("zenclusive.emailDomain");
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailPrefix() {
        return emailPrefix;
    }

    public String getEmailDomain() {
        return emailDomain;
    }
}
