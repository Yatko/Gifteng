/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.auth;

import com.venefica.config.Constants;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import com.venefica.service.fault.AuthenticationException;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gyuszi
 */
public class TokenUtil {
    
    private final Log log = LogFactory.getLog(TokenUtil.class);
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    @Inject
    private UserDao userDao;
    @Inject
    private TokenEncryptor tokenEncryptor;
    
    public void decryptAndStoreToken(String encryptedToken) throws TokenDecryptionException, AuthenticationException {
        if ( encryptedToken == null ) {
            throw new NullPointerException("The given token is null");
        }
        
        Token token = tokenEncryptor.decrypt(encryptedToken);
        if ( token.isExpired() ) {
            log.error("Expired token: " + encryptedToken);
            throw new AuthenticationException("Auth token is expired!");
        }
        
        Long userId = token.getUserId();
        User user = userDao.get(userId);
        
        if ( user == null ) {
            throw new AuthenticationException("Invalid auth token (user with userId=" + userId + " not found)!");
        }
        
        SecurityContext securityContext = new SecurityContext(user);
        securityContextHolder.setContext(securityContext);
    }
    
    public String getEncryptedToken(HttpServletRequest request) {
        String encryptedToken = request.getHeader(Constants.AUTH_TOKEN);

        if ( encryptedToken == null || encryptedToken.isEmpty() ) {
            //Try to get from the params
            encryptedToken = request.getParameter(Constants.AUTH_TOKEN);
            encryptedToken = encryptedToken != null ? encryptedToken.replace(' ', '+') : null;
        }
        
        if ( encryptedToken == null || encryptedToken.isEmpty() ) {
            // Try to get token from the cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(Constants.AUTH_TOKEN)) {
                        encryptedToken = cookie.getValue();
                        break;
                    }
                }
            }
        }
        
        if (encryptedToken != null) {
            log.debug("Encrypted token: " + encryptedToken);
            log.debug("Encrypted token length: " + encryptedToken.length());
        }
        
        return encryptedToken;
    }
}
