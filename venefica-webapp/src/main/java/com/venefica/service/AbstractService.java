/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

/**
 *
 * @author gyuszi
 */
public abstract class AbstractService {
    
    protected static final Log logger = LogFactory.getLog(AbstractService.class);
    
    @Resource
    private WebServiceContext jaxwsContext;
    
    @Inject
    protected UserDao userDao;
    
    @Inject
    protected ThreadSecurityContextHolder securityContextHolder;
    
    @Autowired(required = false)
    protected ConnectionRepository connectionRepository;
    
    // internal helpers
    
    //private User getCurrentUser() {
    //    return securityContextHolder.getContext().getUser();
    //}

    protected User getCurrentUser() {
        Long currentUserId = securityContextHolder.getContext().getUserId();
        return userDao.get(currentUserId);
    }
    
    protected <T> T getSocialNetworkApi(Class<T> socialNetworkInterface) {
        Connection<T> connection = connectionRepository.findPrimaryConnection(socialNetworkInterface);
        return connection != null ? connection.getApi() : null;
    }
    
    /**
     * Extracts the requestor (or caller) IP address from the message.
     * @return the caller IP address
     */
    protected String getIpAddress() { 
        String ipAddress = null;
        HttpServletRequest request;
        String header = "X-Forwarded-For";
        
        if ( jaxwsContext != null ) {
            request = (HttpServletRequest) jaxwsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        } else {
            Message message = PhaseInterceptorChain.getCurrentMessage();
            request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        }

        if ( request != null ) {
            if ( StringUtils.isNotBlank(request.getHeader(header)) ) {
                ipAddress = request.getHeader(header);
            } else {
                ipAddress = request.getRemoteAddr();
            }
        }

        logger.info("IP ADDRESS: " + ipAddress);
        return ipAddress; 
    }
    
}
