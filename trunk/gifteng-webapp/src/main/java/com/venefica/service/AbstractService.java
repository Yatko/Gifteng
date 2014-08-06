/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.common.EmailSender;
import com.venefica.dao.AdDao;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.PromoCodeProviderDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.ShippingDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserDataDao;
import com.venefica.dao.UserPointDao;
import com.venefica.dao.UserSettingDao;
import com.venefica.dao.UserSocialActivityDao;
import com.venefica.dao.UserSocialPointDao;
import com.venefica.model.Ad;
import com.venefica.model.AdData;
import com.venefica.model.MemberUserData;
import com.venefica.model.Request;
import com.venefica.model.User;
import com.venefica.model.UserSetting;
import com.venefica.model.UserSocialActivity;
import com.venefica.model.SocialActivityType;
import com.venefica.model.UserData;
import com.venefica.model.UserSocialPoint;
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
    protected AdDataDao adDataDao;
    @Inject
    protected RequestDao requestDao;
    @Inject
    protected UserDao userDao;
    @Inject
    protected UserDataDao userDataDao;
    @Inject
    protected UserSettingDao userSettingDao;
    @Inject
    protected UserSocialPointDao userSocialPointDao;
    @Inject
    protected UserPointDao userPointDao;
    @Inject
    protected UserSocialActivityDao userSocialActivityDao;
    @Inject
    protected ShippingDao shippingDao;
    @Inject
    protected PromoCodeProviderDao promoCodeProviderDao;
    @Inject
    protected EmailSender emailSender;
    
    @Inject
    protected ThreadSecurityContextHolder securityContextHolder;
    
    // internal helpers
    
    protected Long getCurrentUserId() {
        return securityContextHolder.getContext() != null ? securityContextHolder.getContext().getUserId() : null;
    }
    
    protected User getCurrentUser() throws UserNotFoundException {
        //User currentUser = securityContextHolder.getContext().getUser(); //using this throws lazy fetch exceptions
        Long currentUserId = getCurrentUserId();
        User currentUser = userDao.get(currentUserId);
        if ( currentUser == null || currentUser.isDeleted() ) {
            logger.error("Getting user (userId: " + currentUserId + ") failed");
            throw new UserNotFoundException("User with ID '" + currentUserId + "' not found!");
        }
        return currentUser;
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
        } else if ( ad.isMemberAd() && ad.isDeleted() ) {
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
    
    protected AdData getAdData(Ad ad) {
        AdData adData;
        if ( ad.isBusinessAd() ) {
            adData = adDataDao.getBusinessAdDataByAd(ad.getId());
        } else {
            adData = adDataDao.getMemberAdDataByAd(ad.getId());
        }
        ad.setAdData(adData);
        return adData;
    }
    
    protected UserData getUserData(User user) {
        UserData userData;
        if ( user.isBusinessAccount()) {
            userData = userDataDao.getBusinessUserDataByUser(user.getId());
        } else {
            userData = userDataDao.getMemberUserDataByUser(user.getId());
        }
        user.setUserData(userData);
        return userData;
    }
    
    protected UserSetting createUserSetting(MemberUserData userData) {
        UserSetting userSetting = new UserSetting();
        userSetting.markDefaultNotifiableTypes();
        userSettingDao.save(userSetting);
        
        if ( userData != null ) {
            userData.setUserSetting(userSetting);
            userDataDao.update(userData);
        }
        
        return userSetting;
    }
    
    protected UserSocialPoint createUserSocialPoint(MemberUserData userData) {
        UserSocialPoint socialPoint = new UserSocialPoint();
        userSocialPointDao.save(socialPoint);
        
        if ( userData != null ) {
            userData.setUserSocialPoint(socialPoint);
            userDataDao.update(userData);
        }
        
        return socialPoint;
    }
    
    protected void createSocialActivity(User user, SocialActivityType activityType) {
        createSocialActivity(user, activityType, null);
    }
    
    protected void createSocialActivity(User user, SocialActivityType activityType, String externalRef) {
        createSocialActivity(user, activityType, externalRef, 0);
    }
    
    protected void createSocialActivity(User user, SocialActivityType activityType, String externalRef, int incrementSocialPoint) {
        if ( user != null && !user.isBusinessAccount() ) {
            MemberUserData userData = (MemberUserData) user.getUserData();
            UserSocialPoint socialPoint = userData.getUserSocialPoint();
            if ( socialPoint == null ) {
                socialPoint = createUserSocialPoint(userData);
            }
            
            UserSocialActivity socialActivity = new UserSocialActivity();
            socialActivity.setActivityType(activityType);
            socialActivity.setExternalRef(externalRef);
            socialActivity.setUserSocialPoint(socialPoint);
            userSocialActivityDao.save(socialActivity);
            
            if ( incrementSocialPoint != 0 ) {
                socialPoint.incrementSocialPoint(incrementSocialPoint);
                userSocialPointDao.update(socialPoint);
                
//                UserPoint userPoint = user.getUserPoint();
//                userPoint.addGivingNumber(incrementSocialPoint);
//                userPointDao.update(userPoint);
            }
        }
    }
}
