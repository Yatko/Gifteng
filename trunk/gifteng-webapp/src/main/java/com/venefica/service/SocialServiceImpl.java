/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.config.AppConfig;
import com.venefica.dao.UserConnectionDao;
import com.venefica.model.MemberUserData;
import com.venefica.model.User;
import com.venefica.service.dto.Provider;
import com.venefica.model.UserConnection;
import com.venefica.model.UserSocialActivity;
import com.venefica.model.SocialActivityType;
import com.venefica.model.UserSocialPoint;
import com.venefica.service.dto.UserConnectionDto;
import com.venefica.service.fault.UserNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.twitter.api.Twitter;
//import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author gyuszi
 */
@Service("socialService")
@WebService(endpointInterface = "com.venefica.service.SocialService")
public class SocialServiceImpl extends AbstractService implements SocialService {
    
    //@Autowired(required = false)
    @Inject
    private ConnectionRepository connectionRepository;
    @Inject
    private AppConfig appConfig;
    @Inject
    private UserConnectionDao userConnectionDao;
    
    //******************
    //* social network *
    //******************
    
    @Override
    public Set<String> getConnectedSocialNetworks() {
        MultiValueMap<String, Connection<?>> allConnections = connectionRepository.findAllConnections();
        Set<String> result = new HashSet<String>();

        for (String network : allConnections.keySet()) {
            List<Connection<?>> connections = allConnections.get(network);
            if (!connections.isEmpty()) {
                result.add(network);
            }
        }
        return result;
    }

    @Override
    public void disconnectFromNetwork(String networkName) {
        connectionRepository.removeConnections(networkName);
    }
    
    @Override
    public UserConnectionDto getUserConnection(String networkName) {
        Long userId = getCurrentUserId();
        Provider provider = Provider.valueOf(networkName);
        
        UserConnection userConnection = userConnectionDao.getByUserId(provider, userId);
        if ( userConnection == null ) {
            return null;
        }
        
        UserConnectionDto userConnectionDto = new UserConnectionDto(userConnection);
        
        switch ( provider ) {
            case FACEBOOK:
                Facebook facebook = getSocialNetworkApi(Facebook.class);
                FacebookProfile userProfile = facebook.userOperations().getUserProfile();
                userConnectionDto.update(userProfile);
                break;
            case TWITTER:
                // TODO: needs to be implemented
                break;
        }
        
        return userConnectionDto;
    }
    
    @Override
    @Transactional
    public int calculateSocialPoints() throws UserNotFoundException {
        Long userId = getCurrentUserId();
        User currentUser = getCurrentUser();
        if ( currentUser.isBusinessAccount() ) {
            logger.debug("Current user (userId: " + userId + ") is not a member type.");
            return 0;
        }
        
        Set<String> networks = getConnectedSocialNetworks();
        if ( networks == null || networks.isEmpty() ) {
            logger.debug("Current user (userId: " + userId + ") has no any social connection.");
            return 0;
        }
        
        MemberUserData userData = (MemberUserData) currentUser.getUserData();
        UserSocialPoint socialPoint = userData.getUserSocialPoint();
        if ( socialPoint == null ) {
            socialPoint = createUserSocialPoint(userData);
        }
        
        for ( String network : networks ) {
            Provider provider = Provider.valueOf(network);
            UserConnection userConnection = userConnectionDao.getByUserId(provider, userId);
            if ( userConnection == null ) {
                logger.warn("The <" + network + "> network appears as connected, but there is no valid user connection (userId: " + userId + ")");
                continue;
            }
            
            if ( provider == Provider.FACEBOOK ) {
                UserSocialActivity activity = userSocialActivityDao.getBySocialPointAndActivityType(socialPoint.getId(), SocialActivityType.GIFTENG_PAGE_LIKED_ON_FACEBOOK);
                if ( activity == null && isGiftengPageLikedOnFacebook() ) {
                    createSocialActivity(currentUser, SocialActivityType.GIFTENG_PAGE_LIKED_ON_FACEBOOK, null, appConfig.getSocialPointGiftengPageLikedOnFacebook());
                }
            } else if ( provider == Provider.TWITTER ) {
                // TODO: needs to be implemented
            }
        }
        
        return socialPoint.getSocialPoint();
    }
    
    
    //*********
    //* share *
    //*********

    @Override
    public void shareOnSocialNetworks(String message) {
        // Facebook
        Facebook facebook = getSocialNetworkApi(Facebook.class);
        if (facebook != null) {
            facebook.feedOperations().updateStatus(message);
        }

        // Twitter
        Twitter twitter = getSocialNetworkApi(Twitter.class);
        if (twitter != null) {
            twitter.timelineOperations().updateStatus(message);
        }

//        // VKontakte
//        VKontakte vkontakte = getSocialNetworkApi(VKontakte.class);
//        if (vkontakte != null) {
//            vkontakte.wallOperations().post(message);
//        }
    }
    
    
    
    //***********
    //* friends *
    //***********
    
    @Override
    public List<UserConnectionDto> getFriendList(String networkName, int offset, int limit) {
        Long userId = getCurrentUserId();
        Provider provider = Provider.valueOf(networkName);
        UserConnection userConnection = userConnectionDao.getByUserId(provider, userId);
        
        if ( userConnection == null ) {
            return null;
        }
        
        List<UserConnectionDto> result = new ArrayList<UserConnectionDto>(0);
        switch ( provider ) {
            case FACEBOOK:
                Facebook facebook = getSocialNetworkApi(Facebook.class);
                for ( FacebookProfile friendProfile : facebook.friendOperations().getFriendProfiles(offset, limit) ) {
                    UserConnectionDto userConnectionDto = new UserConnectionDto(Provider.FACEBOOK);
                    userConnectionDto.update(friendProfile);
                }
                break;
            case TWITTER:
                // TODO: needs to be implemented
                break;
        }
        return result;
    }
    
    // internal helpers
    
    private <T> T getSocialNetworkApi(Class<T> socialNetworkInterface) {
        Connection<T> connection = connectionRepository.findPrimaryConnection(socialNetworkInterface);
        return connection != null ? connection.getApi() : null;
    }
    
    private boolean isGiftengPageLikedOnFacebook() {
        Facebook facebook = getSocialNetworkApi(Facebook.class);
        for ( Page page : facebook.likeOperations().getPagesLiked() ) {
            if ( page.getName().equals(Provider.GIFTENG_FACEBOOK_PAGE_NAME) ) {
                return true;
            }
        }
        return false;
    }
}
