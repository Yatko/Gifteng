/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.common.EmailSender;
import com.venefica.dao.AdDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserSettingDao;
import com.venefica.model.Ad;
import com.venefica.model.Request;
import com.venefica.model.User;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
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
    protected AdDao adDao;
    @Inject
    protected RequestDao requestDao;
    @Inject
    protected UserDao userDao;
    @Inject
    protected UserSettingDao userSettingDao;
    @Inject
    protected EmailSender emailSender;
    
    @Inject
    protected ThreadSecurityContextHolder securityContextHolder;
    
    @Autowired(required = false)
    protected ConnectionRepository connectionRepository;
    
    // internal helpers
    
    //private User getCurrentUser() {
    //    return securityContextHolder.getContext().getUser();
    //}

    protected Long getCurrentUserId() {
        return securityContextHolder.getContext().getUserId();
    }
    
    protected User getCurrentUser() {
        Long currentUserId = getCurrentUserId();
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
    
    
    
    // common validators
    
    protected User validateUser(Long userId) throws UserNotFoundException {
        if (userId == null) {
            throw new NullPointerException("userId is null!");
        }
        
        User user = userDao.get(userId);
        if ( user == null ) {
            throw new UserNotFoundException("User with id '" + userId + "' not found");
        }
        return user;
    }
    
    protected User validateUser(String name) throws UserNotFoundException {
        if (name == null) {
            throw new NullPointerException("name is null!");
        }
        
        User user = userDao.findUserByName(name);
        if ( user == null ) {
            throw new UserNotFoundException("User with name '" + name + "' not found");
        }
        return user;
    }
    
    protected Ad validateAd(Long adId) throws AdNotFoundException {
        if (adId == null) {
            throw new NullPointerException("adId is null!");
        }
        
        Ad ad = adDao.get(adId);
        if (ad == null) {
            throw new AdNotFoundException(adId);
        } else if ( ad.isDeleted() ) {
            throw new AdNotFoundException(adId);
        }
        return ad;
    }
    
    protected Request validateRequest(Long requestId) throws RequestNotFoundException {
        if (requestId == null) {
            throw new NullPointerException("requestId is null!");
        }
        
        Request request = requestDao.get(requestId);
        if (request == null) {
            throw new RequestNotFoundException(requestId);
        }
        return request;
    }
}
