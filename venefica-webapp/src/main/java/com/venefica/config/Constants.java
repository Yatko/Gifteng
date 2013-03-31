/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gyuszi
 */
public interface Constants {
    
    public static final String MESSAGE_SIGNATURE = "MessageSig";
    public static final String AUTH_TOKEN = "AuthToken";
    
    public static final String CALLBACK_PATH = "callback";
    public static final String OAUTH_TOKEN_ATTRIBUTE = "oauthToken";
    
    public static final String ADDAO = "AdDao";
    
    public static final Set<String> OPERATIONS_FOR_SKIP_TOKEN_AUTHORIZATION = new HashSet<String>(Arrays.asList(
            "RegisterUser",
            "Authenticate",
            "AuthenticateEmail",
            "AuthenticatePhone",
            "RequestInvitation"
    ));
    
}
