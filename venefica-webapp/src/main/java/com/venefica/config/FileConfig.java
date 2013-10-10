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
@PropertySource("/application.properties")
public class FileConfig {
    
    @Inject
    private Environment environment;
    
    private String path;
    private boolean deleteOnExit;
    
    private String amazonBucket;
    private String amazonUser;
    private String amazonAccessKeyID;
    private String amazonSecretAccessKey;
    private String amazonPassword;
    private boolean amazonEnabled;
    
    @PostConstruct
    public void init() {
        path = environment.getProperty("upload.path").trim();
        deleteOnExit = environment.getProperty("upload.deleteOnExit", boolean.class, false);
        
        amazonBucket = environment.getProperty("amazon.bucket");
        amazonUser = environment.getProperty("amazon.user");
        amazonAccessKeyID = environment.getProperty("amazon.accessKeyID");
        amazonSecretAccessKey = environment.getProperty("amazon.secretAccessKey");
        amazonPassword = environment.getProperty("amazon.password");
        amazonEnabled = environment.getProperty("amazon.enabled", boolean.class);
    }

    public String getPath() {
        return path;
    }

    public boolean isDeleteOnExit() {
        return deleteOnExit;
    }

    public String getAmazonBucket() {
        return amazonBucket;
    }

    public String getAmazonUser() {
        return amazonUser;
    }

    public String getAmazonAccessKeyID() {
        return amazonAccessKeyID;
    }

    public String getAmazonSecretAccessKey() {
        return amazonSecretAccessKey;
    }

    public String getAmazonPassword() {
        return amazonPassword;
    }

    public boolean isAmazonEnabled() {
        return amazonEnabled;
    }
    
}
