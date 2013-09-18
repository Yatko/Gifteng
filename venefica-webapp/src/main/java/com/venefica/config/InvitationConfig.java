/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 *
 * @author gyuszi
 */
@Configuration
@PropertySource("/application.properties")
public class InvitationConfig {
    
    @Inject
    private Environment environment;
    
    private Set<String> zipCodes;

    @PostConstruct
    public void init() {
        String invitationRequestZipcodes = environment.getProperty("invitation.request.zipcodes");
        
        zipCodes = StringUtils.commaDelimitedListToSet(invitationRequestZipcodes);
    }
    
    public Set<String> getZipCodes() {
        return zipCodes;
    }
}
