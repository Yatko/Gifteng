package com.venefica.auth;

import com.venefica.dao.UserDao;
import com.venefica.model.User;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenDecryptionInterceptorMvc extends HandlerInterceptorAdapter {

    public final static String AUTH_TOKEN = "AuthToken";
    
    protected final Log log = LogFactory.getLog(TokenAuthorizationInterceptor.class);
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private TokenEncryptor tokenEncryptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String encryptedToken = getEncryptedToken(request);

        if (encryptedToken != null) {
            log.debug("Encrypted token: " + encryptedToken);
            log.debug("Encrypted token length: " + encryptedToken.length());
        }

        if (encryptedToken != null) {
            Token token = tokenEncryptor.decrypt(encryptedToken);

            if (!token.isExpired()) {
                Long userId = token.getUserId();
                User user = userDao.get(userId);

                if (user != null) {
                    SecurityContext securityContext = new SecurityContext(user);
                    securityContextHolder.setContext(securityContext);
                }
            }
        }

        return true;
    }

    private String getEncryptedToken(HttpServletRequest request) {
        String encryptedToken = request.getHeader(AUTH_TOKEN);

        if (encryptedToken == null) {
            // Try to get token from the cookies
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(AUTH_TOKEN)) {
                        encryptedToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        return encryptedToken;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        securityContextHolder.clearContext();
    }
}
