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
public class AppConfig {
    
    @Inject
    private Environment environment;
    
    private int adProlongationPeriodMinutes; //with how many minutes will be incremented the expiration date at relist
    private int adExpirationPeriodMinutes; //the default expiration in minutes at ad creation
    private int adMaxAllowedProlongations; //number of allowed relisting
    private int requestStartupLimit; //request startup limit
    private int requestIncrementLimit; //request limit increment number
    
    @PostConstruct
    public void init() {
        //ad settings
        adProlongationPeriodMinutes = environment.getProperty("config.ad.prolongationPeriodMinutes", int.class);
        adExpirationPeriodMinutes = environment.getProperty("config.ad.expirationPeriodMinutes", int.class);
        adMaxAllowedProlongations = environment.getProperty("config.ad.maxAllowedProlongations", int.class);
        //request configs
        requestStartupLimit = environment.getProperty("config.request.startupLimit", int.class);
        requestIncrementLimit = environment.getProperty("config.request.incrementLimit", int.class);
    }
    
    public int getAdProlongationPeriodMinutes() {
        return adProlongationPeriodMinutes;
    }

    public int getAdExpirationPeriodMinutes() {
        return adExpirationPeriodMinutes;
    }

    public int getAdMaxAllowedProlongations() {
        return adMaxAllowedProlongations;
    }

    public int getRequestStartupLimit() {
        return requestStartupLimit;
    }

    public int getRequestIncrementLimit() {
        return requestIncrementLimit;
    }
}
