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
    
    @PostConstruct
    public void init() {
        adProlongationPeriodMinutes = environment.getProperty("config.ad.prolongationPeriodMinutes", int.class);
        adExpirationPeriodMinutes = environment.getProperty("config.ad.expirationPeriodMinutes", int.class);
    }
    
    public int getAdProlongationPeriodMinutes() {
        return adProlongationPeriodMinutes;
    }

    public int getAdExpirationPeriodMinutes() {
        return adExpirationPeriodMinutes;
    }
}
