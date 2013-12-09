/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.connect;

import com.venefica.auth.ThreadSecurityContextHolder;
import javax.inject.Inject;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 *
 * @author gyuszi
 */
public class DummySignUpAdapter implements ConnectionSignUp {

    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    
    @Override
    public String execute(Connection<?> connection) {
        if ( securityContextHolder.getContext() == null ) {
            return null;
        }
        return securityContextHolder.getContext().getUserId().toString();
    }
    
}
