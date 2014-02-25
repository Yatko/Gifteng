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
    private int requestLimitUserRegister;
    private int requestLimitAdNew; //owner
    private int requestLimitAdDeleted; //owner
    private int requestLimitRequestNew; //owner
    private int requestLimitRequestReceived; //owner
    private int requestLimitRequestCanceled; //receiver
    private int requestLimitRequestDeclined; //receiver
    private int requestLimitRequestAccepted; //receiver
    
    @PostConstruct
    public void init() {
        //ad settings
        adProlongationPeriodMinutes = environment.getProperty("config.ad.prolongationPeriodMinutes", int.class);
        adExpirationPeriodMinutes = environment.getProperty("config.ad.expirationPeriodMinutes", int.class);
        adMaxAllowedProlongations = environment.getProperty("config.ad.maxAllowedProlongations", int.class);
        //request limit configs
        requestLimitUserRegister = environment.getProperty("config.request.limit.userRegister", int.class);
        requestLimitAdNew = environment.getProperty("config.request.limit.adNew", int.class);
        requestLimitAdDeleted = environment.getProperty("config.request.limit.adDeleted", int.class);
        requestLimitRequestNew = environment.getProperty("config.request.limit.requestNew", int.class);
        requestLimitRequestReceived = environment.getProperty("config.request.limit.requestReceived", int.class);
        requestLimitRequestCanceled = environment.getProperty("config.request.limit.requestCanceled", int.class);
        requestLimitRequestDeclined = environment.getProperty("config.request.limit.requestDeclined", int.class);
        requestLimitRequestAccepted = environment.getProperty("config.request.limit.requestAccepted", int.class);
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

    public int getRequestLimitAdNew() {
        return requestLimitAdNew;
    }
    
    public int getRequestLimitAdDeleted() {
        return requestLimitAdDeleted;
    }
    
    public int getRequestLimitRequestNew() {
        return requestLimitRequestNew;
    }

    public int getRequestLimitRequestReceived() {
        return requestLimitRequestReceived;
    }

    public int getRequestLimitRequestCanceled() {
        return requestLimitRequestCanceled;
    }

    public int getRequestLimitRequestDeclined() {
        return requestLimitRequestDeclined;
    }

    public int getRequestLimitRequestAccepted() {
        return requestLimitRequestAccepted;
    }

    public int getRequestLimitUserRegister() {
        return requestLimitUserRegister;
    }
}
