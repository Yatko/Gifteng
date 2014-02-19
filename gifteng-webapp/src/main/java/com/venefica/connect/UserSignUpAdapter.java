package com.venefica.connect;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.common.EmailSender;
import com.venefica.common.UserVerificationUtil;
import com.venefica.config.AppConfig;
import com.venefica.dao.ImageDao;
import com.venefica.dao.UserDao;
import com.venefica.dao.UserDataDao;
import com.venefica.dao.UserPointDao;
import com.venefica.dao.UserSettingDao;
import com.venefica.model.Image;
import com.venefica.model.ImageModelType;
import com.venefica.model.ImageType;
import com.venefica.model.MemberUserData;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import com.venefica.model.UserSetting;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

/**
 * Creates partially completed user account and stores him in the database.
 * 
 * In the SocialConfig at UsersConnectionRepository creation can be specified to auto
 * sign up provider user as local one. The default is null, which means that explicit
 * sign up is needed. This part was commented out as there is no such an auto
 * sign up need currently.
 *
 * @author Sviatoslav Grebenchukov
 */
public class UserSignUpAdapter implements ConnectionSignUp {

    private final static Log log = LogFactory.getLog(UserSignUpAdapter.class);
    
    private static final int BUFFER_SIZE = 4 * 1024;
    
    @Inject
    private AppConfig appConfig;
    
    @Inject
    private ThreadSecurityContextHolder securityContextHolder;
    @Inject
    private UserVerificationUtil userVerificationUtil;
    
    @Inject
    private UserDao userDao;
    @Inject
    private UserDataDao userDataDao;
    @Inject
    private ImageDao imageDao;
    @Inject
    private UserSettingDao userSettingDao;
    @Inject
    private UserPointDao userPointDao;

    private boolean generateUserName = false;
    
    @Override
    public String execute(Connection<?> connection) {
        UserProfile userProfile = connection.fetchUserProfile();
        String email = userProfile.getEmail();
        
        Long userId = securityContextHolder.getContext() != null ? securityContextHolder.getContext().getUserId() : null;
        if ( userId == null ) {
            //if there is no logged user
            if ( email != null ) {
                User user = userDao.findUserByEmail(email);
                userId = user != null ? user.getId() : null;
            }
        }

        if ( userId != null ) {
            //user already signed up, returning its userId
            return userId.toString();
        }
        
        String userName = userProfile.getUsername();
        if ( generateUserName && userName == null ) {
            Long maxUserId = userDao.getMaxUserId();
            userName = "user" + (maxUserId + 1);
        }
        
        UserSetting userSetting = new UserSetting();
        userSetting.markDefaultNotifiableTypes();
        userSettingDao.save(userSetting);
        
        MemberUserData userData = new MemberUserData(userProfile.getFirstName(), userProfile.getLastName());
        userData.setUserSetting(userSetting);
        userDataDao.save(userData);
        
        UserPoint userPoint = new UserPoint(appConfig.getRequestLimitUserRegister(), 0, 0);
        userPointDao.save(userPoint);
        
        User user = new User(userName, email);
        user.setUserData(userData);
        user.setUserPoint(userPoint);
        //user.setPassword("xxx");
        user.setPreviousLoginAt(user.getLastLoginAt());
        user.setLastLoginAt(new Date());

        try {
            String avatarUrlStr = connection.getImageUrl();
            if (avatarUrlStr != null & !avatarUrlStr.isEmpty()) {
                URL avatarUrl = new URL(avatarUrlStr);

                // Read image
                InputStream avatarStream = avatarUrl.openStream();
                byte[] data = readToEndOfStream(avatarStream);
                avatarStream.close();

                // FIXME: image type should be extracted form the URL
                Image avatar = new Image(ImageType.JPEG, data);
                imageDao.save(avatar, ImageModelType.USER);
                
                user.setAvatar(avatar);
            }
        } catch (MalformedURLException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }

        userId = userDao.save(user);
        
        userVerificationUtil.createAndSendUserWelcome(user);
        
        return userId.toString();
    }

    // internal helpers
    
    private static byte[] readToEndOfStream(InputStream in) throws IOException {
        byte[] data = new byte[0];
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) > 0) {
            byte[] newData = new byte[data.length + bytesRead];
            System.arraycopy(data, 0, newData, 0, data.length);
            System.arraycopy(buffer, 0, newData, data.length, bytesRead);
            data = newData;
        }
        return data;
    }
}
