package com.venefica.service;

import com.venefica.dao.ImageDao;
import com.venefica.dao.InvitationDao;
import com.venefica.dao.UserDataDao;
import com.venefica.model.Invitation;
import com.venefica.model.User;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.InvalidInvitationException;
import com.venefica.service.fault.InvitationNotFoundException;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserField;
import com.venefica.service.fault.UserNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@Service("userManagementService")
@WebService(endpointInterface = "com.venefica.service.UserManagementService")
public class UserManagementServiceImpl extends AbstractService implements UserManagementService {

    @Inject
    private ImageDao imageDao;
    @Inject
    private InvitationDao invitationDao;
    @Inject
    protected UserDataDao userDataDao;
    
    @Override
    @Transactional
    public Long registerUser(UserDto userDto, String password, String invitationCode) throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        User user = userDto.toUser(imageDao);
        user.setPassword(password);

        // Check for existing users
        if (userDao.findUserByName(user.getName()) != null) {
            throw new UserAlreadyExistsException(UserField.NAME,
                    "User with the same name already exists!");
        } else if (userDao.findUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL,
                    "User with the specified email already exists!");
        } else if ( invitationCode == null ) {
            throw new InvalidInvitationException("Invitation code cannot be empty!");
        }
        
        Invitation invitation = invitationDao.findByCode(invitationCode);
        if (invitation == null) {
            throw new InvitationNotFoundException("Invitation with code '" + invitationCode + "' not found!");
        } else if ( !invitation.isValid() ) {
            throw new InvalidInvitationException("Invitation with code '" + invitationCode + "' is invalid!");
        }
        
        invitation.use();
        invitationDao.update(invitation);
        
        user.getUserData().setInvitation(invitation);
        userDataDao.save(user.getUserData());
        
        return userDao.save(user);
    }

    @Override
    @Transactional
    public UserDto getUser() throws UserNotFoundException {
        try {
            User user = getCurrentUser();
            return new UserDto(user);
        } catch (NumberFormatException e) {
            logger.error("Getting user failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public UserDto getUserByName(String name) throws UserNotFoundException {
        User user = userDao.findUserByName(name);

        if (user == null) {
            throw new UserNotFoundException("User with name '" + name + "' not found!");
        }

        return new UserDto(user);
    }
    
    @Override
    @Transactional
    public UserDto getUserByEmail(String email) throws UserNotFoundException {
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User with email '" + email + "' not found!");
        }

        return new UserDto(user);
    }
    
    @Override
    @Transactional
    public UserDto getUserByPhone(String phone) throws UserNotFoundException {
        User user = userDao.findUserByPhoneNumber(phone);

        if (user == null) {
            throw new UserNotFoundException("User with phone number '" + phone + "' not found!");
        }

        return new UserDto(user);
    }

    @Override
    public boolean isUserComplete() {
        User user = getCurrentUser();
        return user.isComplete();
    }

    @Override
    @Transactional
    public boolean updateUser(UserDto userDto) throws UserAlreadyExistsException {
        try {
            User user = getCurrentUser();

            User userWithTheSameName = userDao.findUserByName(userDto.getName());

            if (userWithTheSameName != null && !userWithTheSameName.getId().equals(user.getId())) {
                throw new UserAlreadyExistsException(UserField.NAME,
                        "User with the same name already exists!");
            }

            User userWithTheSameEmail = userDao.findUserByEmail(userDto.getEmail());

            if (userWithTheSameEmail != null && !userWithTheSameEmail.getId().equals(user.getId())) {
                throw new UserAlreadyExistsException(UserField.EMAIL,
                        "User with the same email already exists!");
            }
            
            userDto.update(user, imageDao);
            userDataDao.update(user.getUserData());
            
            return user.isComplete();
        } catch (NumberFormatException e) {
            logger.error("Update user failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void follow(Long userId) throws UserNotFoundException {
        User user = getCurrentUser();
        User following = userDao.get(userId);
        
        if ( following == null ) {
            throw new UserNotFoundException("Cannot find the given following (userId: " + userId + ") user.");
        }
        
        user.addFollowing(following);
    }
    
    @Override
    @Transactional
    public void unfollow(Long userId) throws UserNotFoundException {
        User user = getCurrentUser();
        User following = userDao.get(userId);
        
        if ( following == null ) {
            throw new UserNotFoundException("Cannot find the given following (userId: " + userId + ") user.");
        }
        
        user.removeFollowing(following);
    }
    
    @Override
    @Transactional
    public List<UserDto> getFollowers(Long userId) throws UserNotFoundException {
        List<UserDto> result = new LinkedList<UserDto>();
        User user = userDao.get(userId);
        if ( user == null ) {
            throw new UserNotFoundException("Cannot find user (userId: " + userId + ") user.");
        }
        
        if ( user.getFollowers() != null ) {
            for ( User follower : user.getFollowers() ) {
                result.add(new UserDto(follower));
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<UserDto> getFollowings(Long userId) throws UserNotFoundException {
        List<UserDto> result = new LinkedList<UserDto>();
        User user = userDao.get(userId);
        if ( user == null ) {
            throw new UserNotFoundException("Cannot find user (userId: " + userId + ") user.");
        }
        
        if ( user.getFollowings() != null ) {
            for ( User following : user.getFollowings()) {
                result.add(new UserDto(following));
            }
        }
        
        return result;
    }
    
    @Override
    public Set<String> getConnectedSocialNetworks() {
        MultiValueMap<String, Connection<?>> allConnections = connectionRepository
                .findAllConnections();

        HashSet<String> result = new HashSet<String>();

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
}
