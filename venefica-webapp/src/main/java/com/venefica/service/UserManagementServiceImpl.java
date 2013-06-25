package com.venefica.service;

import com.venefica.dao.AddressWrapperDao;
import com.venefica.dao.BusinessCategoryDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.InvitationDao;
import com.venefica.dao.UserDataDao;
import com.venefica.dao.UserPointDao;
import com.venefica.model.BusinessCategory;
import com.venefica.model.BusinessUserData;
import com.venefica.model.Invitation;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import com.venefica.service.dto.BusinessCategoryDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvalidInvitationException;
import com.venefica.service.fault.InvitationNotFoundException;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserField;
import com.venefica.service.fault.UserNotFoundException;
import java.util.ArrayList;
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
    private UserDataDao userDataDao;
    @Inject
    private BusinessCategoryDao businessCategoryDao;
    @Inject
    private AddressWrapperDao addressWrapperDao;
    @Inject
    private UserPointDao userPointDao;
    
    //**********************
    //* categories related *
    //**********************
    
    @Override
    public List<BusinessCategoryDto> getAllBusinessCategories() {
        List<BusinessCategoryDto> result = new LinkedList<BusinessCategoryDto>();
        List<BusinessCategory> categories = businessCategoryDao.getCategories();
        
        for (BusinessCategory category : categories) {
            BusinessCategoryDto categoryDto = new BusinessCategoryDto(category);
            result.add(categoryDto);
        }
        
        return result;
    }
    
    
    
    //************************************
    //* user crud (create/update/delete) *
    //************************************
    
    @Override
    @Transactional
    public Long registerBusinessUser(UserDto userDto, String password) throws UserAlreadyExistsException, GeneralException {
        String email = userDto.getEmail();
        String businessName = userDto.getBusinessName();
        Long categoryId = userDto.getBusinessCategoryId();
        
        if (userDataDao.findByBusinessName(businessName) != null) {
            throw new UserAlreadyExistsException(UserField.NAME, "User with the specified business name already exists!");
        } else if (userDao.findUserByEmail(email) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL, "User with the specified email already exists!");
        }
        
        BusinessCategory category = validateBusinessCategory(categoryId);
        
        User user = userDto.toBusinessUser(imageDao, addressWrapperDao);
        user.setPassword(password);
        ((BusinessUserData) user.getUserData()).setCategory(category);
        
        userDataDao.save(user.getUserData());
        return userDao.save(user);
    }
    
    @Override
    @Transactional
    public Long registerUser(UserDto userDto, String password, String invitationCode) throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        // Check for existing users
        if (userDao.findUserByName(userDto.getName()) != null) {
            throw new UserAlreadyExistsException(UserField.NAME, "User with the same name already exists!");
        } else if (userDao.findUserByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL, "User with the specified email already exists!");
        } else if ( invitationCode == null ) {
            throw new InvalidInvitationException("Invitation code cannot be empty!");
        }
        
        Invitation invitation = invitationDao.findByCode(invitationCode);
        if (invitation == null) {
            throw new InvitationNotFoundException("Invitation with code '" + invitationCode + "' not found!");
        } else if ( !invitation.isValid() ) {
            throw new InvalidInvitationException("Invitation with code '" + invitationCode + "' is invalid!");
        }
        
        User user = userDto.toMemberUser(imageDao, addressWrapperDao);
        user.setPassword(password);
        
        invitation.use();
        invitationDao.update(invitation);
        
        user.getUserData().setInvitation(invitation);
        userDataDao.save(user.getUserData());
        
        UserPoint userPoint = new UserPoint(0, 0);
        userPoint.setUser(user);
        userPointDao.save(userPoint);
        
        user.setUserPoint(userPoint);
        
        return userDao.save(user);
    }
    
    @Override
    public boolean isUserComplete() {
        User user = getCurrentUser();
        return user.isComplete();
    }

    @Override
    @Transactional
    public boolean updateUser(UserDto userDto) throws UserAlreadyExistsException {
        User user = getCurrentUser();
        User userWithTheSameName = userDao.findUserByName(userDto.getName());

        if (userWithTheSameName != null && !userWithTheSameName.getId().equals(user.getId())) {
            throw new UserAlreadyExistsException(UserField.NAME, "User with the same name already exists!");
        }

        User userWithTheSameEmail = userDao.findUserByEmail(userDto.getEmail());

        if (userWithTheSameEmail != null && !userWithTheSameEmail.getId().equals(user.getId())) {
            throw new UserAlreadyExistsException(UserField.EMAIL, "User with the same email already exists!");
        }

        userDto.update(user, imageDao, addressWrapperDao);
        userDataDao.update(user.getUserData());

        return user.isComplete();
    }
    
    
    
    //***************
    //* user search *
    //***************

    @Override
    @Transactional
    public List<UserDto> getUsers() {
        List<UserDto> result = new ArrayList<UserDto>(0);
        List<User> users = userDao.getAll();
        for ( User user : users ) {
            UserDto userDto = new UserDto(user);
            result.add(userDto);
        }
        return result;
    }
    
    @Override
    @Transactional
    public UserDto getUser() throws UserNotFoundException {
        User user = getCurrentUser();
        
        if ( user == null ) {
            Long userId = getCurrentUserId();
            logger.error("Getting user (userId: " + userId + ") failed");
            throw new UserNotFoundException("User with ID '" + userId + "' not found!");
        }
        
        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto getUserByName(String name) throws UserNotFoundException {
        User user = validateUser(name);
        User currentUser = getCurrentUser();
        
        UserDto userDto = new UserDto(user);
        populateRelations(userDto, currentUser, user);
        return userDto;
    }
    
    @Override
    @Transactional
    public UserDto getUserByEmail(String email) throws UserNotFoundException {
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User with email '" + email + "' not found!");
        }

        User currentUser = getCurrentUser();
        
        UserDto userDto = new UserDto(user);
        populateRelations(userDto, currentUser, user);
        return userDto;
    }
    
    @Override
    @Transactional
    public UserDto getUserByPhone(String phone) throws UserNotFoundException {
        User user = userDao.findUserByPhoneNumber(phone);

        if (user == null) {
            throw new UserNotFoundException("User with phone number '" + phone + "' not found!");
        }

        User currentUser = getCurrentUser();
        
        UserDto userDto = new UserDto(user);
        populateRelations(userDto, currentUser, user);
        return userDto;
    }

    
    
    //***************
    //* user follow *
    //***************

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
        User user = validateUser(userId);
        User currentUser = getCurrentUser();
        
        if ( user.getFollowers() != null ) {
            for ( User follower : user.getFollowers() ) {
                UserDto userDto = new UserDto(follower);
                populateRelations(userDto, currentUser, follower);
                
                result.add(userDto);
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<UserDto> getFollowings(Long userId) throws UserNotFoundException {
        List<UserDto> result = new LinkedList<UserDto>();
        User user = validateUser(userId);
        User currentUser = getCurrentUser();
        
        if ( user.getFollowings() != null ) {
            for ( User following : user.getFollowings()) {
                UserDto userDto = new UserDto(following);
                populateRelations(userDto, currentUser, following);
                
                result.add(userDto);
            }
        }
        
        return result;
    }
    
    
    
    //******************
    //* social network *
    //******************
    
    @Override
    public Set<String> getConnectedSocialNetworks() {
        MultiValueMap<String, Connection<?>> allConnections = connectionRepository.findAllConnections();
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
    
    // internal helpers
    
    private BusinessCategory validateBusinessCategory(Long categoryId) throws GeneralException {
        if (categoryId == null) {
            throw new GeneralException("Category id not specified!");
        }

        BusinessCategory category = businessCategoryDao.get(categoryId);
        if (category == null) {
            throw new GeneralException("Category with id = " + categoryId + " not found!");
        }
        return category;
    }
    
    private void populateRelations(UserDto userDto, User currentUser, User user) {
        //populates only when the user and currentUser is not the same person
        if ( !currentUser.equals(user) ) {
            userDto.setInFollowers(currentUser.inFollowers(user));
            userDto.setInFollowings(currentUser.inFollowings(user));
        }
    }
}
