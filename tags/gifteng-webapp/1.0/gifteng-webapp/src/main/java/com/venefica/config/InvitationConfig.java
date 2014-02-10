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
    
    public boolean contains(String zipcode) {
        boolean containsZipcode = false;
        if ( zipCodes != null && zipcode != null ) {
            if ( zipCodes.contains(zipcode) ) {
                containsZipcode = true;
            } else if ( zipCodes.contains(org.apache.commons.lang.StringUtils.stripStart(zipcode, "0")) ) {
                containsZipcode = true;
            }
        }
        return containsZipcode;
    }
    
    public Set<String> getZipCodes() {
        return zipCodes;
    }
}
