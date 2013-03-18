package com.venefica.connect;

import com.venefica.dao.ImageDao;
import com.venefica.dao.UserDao;
import com.venefica.model.Image;
import com.venefica.model.ImageType;
import com.venefica.model.User;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

/**
 * Creates partially completed user account and stores him in the database.
 *
 * @author Sviatoslav Grebenchukov
 */
public class UserSignUpAdapter implements ConnectionSignUp {

    private final static Log log = LogFactory.getLog(UserSignUpAdapter.class);
    
    private static final int BUFFER_SIZE = 4 * 1024;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private ImageDao imageDao;

    @Override
    public String execute(Connection<?> connection) {
        UserProfile userProfile = connection.fetchUserProfile();
        User user = null;
        Long userId;
        String email = userProfile.getEmail();

        if (email != null) {
            user = userDao.findUserByEmail(email);
        }

        if (user == null) {
            user = new User(userProfile.getUsername(), userProfile.getFirstName(),
                    userProfile.getLastName(), email);

            String avatarUrlStr = connection.getImageUrl();

            try {
                if (avatarUrlStr != null & !avatarUrlStr.isEmpty()) {
                    URL avatarUrl = new URL(avatarUrlStr);

                    // Read image
                    InputStream avatarStream = avatarUrl.openStream();
                    byte[] data = readToEndOfStream(avatarStream);
                    avatarStream.close();

                    // FIXME: image type should be extracted form the URL
                    Image avatar = new Image(ImageType.JPEG, data);
                    imageDao.save(avatar);
                    user.setAvatar(avatar);
                }
            } catch (MalformedURLException e) {
                log.error("", e);
            } catch (IOException e) {
                log.error("", e);
            }

            if (user.getName() == null) {
                Long maxUserId = userDao.getMaxUserId();
                user.setName("user" + maxUserId + 1);
            }

            userId = userDao.save(user);
        } else {
            userId = user.getId();
        }

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
