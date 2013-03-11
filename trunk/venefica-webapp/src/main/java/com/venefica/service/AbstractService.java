/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;

/**
 *
 * @author gyuszi
 */
public abstract class AbstractService {
    
    protected final static Log logger = LogFactory.getLog(AbstractService.class);
    
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
    
}
