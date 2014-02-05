package com.venefica.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenDecryptionInterceptorMvc extends HandlerInterceptorAdapter {

    protected final Log log = LogFactory.getLog(TokenDecryptionInterceptorMvc.class);
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    @Inject
    private TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String encryptedToken = tokenUtil.getEncryptedToken(request);
            tokenUtil.decryptAndStoreToken(encryptedToken);
        } catch ( Exception ex ) {
            //log.error("", ex);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        securityContextHolder.clearContext();
    }
}
