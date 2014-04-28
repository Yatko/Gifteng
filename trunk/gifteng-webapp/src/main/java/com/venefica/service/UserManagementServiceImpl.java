package com.venefica.service;

import com.venefica.common.UserVerificationUtil;
import com.venefica.config.AppConfig;
import com.venefica.dao.AddressWrapperDao;
import com.venefica.dao.BusinessCategoryDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.InvitationDao;
import com.venefica.dao.UserPointDao;
import com.venefica.dao.UserVerificationDao;
import com.venefica.model.BusinessCategory;
import com.venefica.model.BusinessUserData;
import com.venefica.model.Invitation;
import com.venefica.model.MemberUserData;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import com.venefica.model.UserSetting;
import com.venefica.model.UserSocialActivity;
import com.venefica.model.SocialActivityType;
import com.venefica.model.UserSocialPoint;
import com.venefica.model.UserVerification;
import com.venefica.service.dto.BusinessCategoryDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.dto.UserSettingDto;
import com.venefica.service.dto.UserStatisticsDto;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvalidInvitationException;
import com.venefica.service.fault.InvitationNotFoundException;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserField;
import com.venefica.service.fault.UserNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userManagementService")
@WebService(endpointInterface = "com.venefica.service.UserManagementService")
public class UserManagementServiceImpl extends AbstractService implements UserManagementService {
    
    @Inject
    private AdService adService;
    @Inject
    private MessageService messageService;
    
    @Inject
    private AppConfig appConfig;
    @Inject
    private UserVerificationUtil userVerificationUtil;
    @Inject
    private ImageDao imageDao;
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private BusinessCategoryDao businessCategoryDao;
    @Inject
    private AddressWrapperDao addressWrapperDao;
    @Inject
    private UserPointDao userPointDao;
    @Inject
    private UserVerificationDao userVerificationDao;
    
    //*****************************
    //* user verification related *
    //*****************************
    
    @Override
    @Transactional
    public void verifyUser(String code) throws UserNotFoundException, GeneralException {
        UserVerification userVerification = userVerificationDao.findByCode(code);
        if ( userVerification == null ) {
            throw new GeneralException("Verification code was not found.");
        }
        
        if ( userVerification.isVerified() ) {
            logger.info("Code (" + code + ") is already verified");
            return;
        }
        
        userVerification.setVerified(true);
        userVerification.setVerifiedAt(new Date());
        
        User user = userVerification.getUser();
        user.setVerified(true);
        userDao.update(user);
    }
    
    @Override
    @Transactional
    public void resendVerification() throws UserNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        UserVerification userVerification = userVerificationDao.findByUser(currentUser.getId());
        if ( userVerification == null ) {
            throw new GeneralException("User verification was not found.");
        }
        
        if ( userVerification.isVerified() ) {
            logger.warn("User verification is already done (code: " + userVerification.getCode() + ")");
            return;
        }
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("code", userVerification.getCode());
        vars.put("user", userVerification.getUser());

        emailSender.sendNotification(NotificationType.USER_VERIFICATION, userVerification.getUser(), vars);
    }
    
    
    
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
        BusinessCategory category = validateBusinessCategory(userDto.getBusinessCategoryId());
        String email = userDto.getEmail();
        String businessName = userDto.getBusinessName();
        
        if (userDataDao.findByBusinessName(businessName) != null) {
            throw new UserAlreadyExistsException(UserField.NAME, "User with the specified business name already exists!");
        } else if (userDao.findUserByEmail(email) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL, "User with the specified email already exists!");
        }
        
        User user = userDto.toBusinessUser(imageDao, addressWrapperDao);
        user.setPassword(password);
        
        BusinessUserData userData = ((BusinessUserData) user.getUserData());
        userData.setCategory(category);
        userDataDao.save(userData);
        
        UserPoint userPoint = new UserPoint(0);
        userPointDao.save(userPoint);
        
        user.setUserPoint(userPoint);
        Long userId = userDao.save(user);
        return userId;
    }
    
    @Override
    @Transactional
    public Long registerUser(UserDto userDto, String password, String invitationCode, Long referrerId) throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException, GeneralException {
        // Check for existing users
        if (userDao.findUserByName(userDto.getName()) != null) {
            throw new UserAlreadyExistsException(UserField.NAME, "User with the same name already exists!");
        } else if (userDao.findUserByEmail(userDto.getEmail()) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL, "User with the specified email already exists!");
        }
        
        Invitation invitation = null;
        
        if ( invitationCode != null ) {
            invitation = invitationDao.findByCode(invitationCode);
            if (invitation == null) {
                throw new InvitationNotFoundException("Invitation with code '" + invitationCode + "' not found!");
            } else if ( !invitation.isValid() ) {
                throw new InvalidInvitationException("Invitation with code '" + invitationCode + "' is invalid!");
            }
            
            invitation.use();
            invitationDao.update(invitation);
        }
        
        User referrer = null;
        if ( referrerId != null ) {
            referrer = userDao.get(referrerId);
            logger.debug("Referrer user: " + (referrer != null ? referrer.getId() : null));
        }
        
        User user = userDto.toMemberUser(imageDao, addressWrapperDao);
        user.setPassword(password);
        user.setReferrer(referrer);
//        user.setVerified(userDto.getEmail().trim().equals(invitation.getEmail().trim()));
        
        UserSetting userSetting = createUserSetting(null);
        UserSocialPoint socialPoint = createUserSocialPoint(null);
        
        MemberUserData userData = ((MemberUserData) user.getUserData());
        userData.setInvitation(invitation);
        userData.setUserSetting(userSetting);
        userData.setUserSocialPoint(socialPoint);
        userDataDao.save(userData);
        
        UserPoint userPoint = new UserPoint(appConfig.getRequestLimitUserRegister(), 0, 0);
        userPointDao.save(userPoint);
        
        user.setUserPoint(userPoint);
        Long userId = userDao.save(user);
        
        userVerificationUtil.createAndSendUserWelcome(user);
        
        createSocialActivity(referrer, SocialActivityType.REFERRER_SIGNUP, String.valueOf(userId), appConfig.getSocialPointReferrerSignup());
        
        return userId;
    }
    
    @Override
    public boolean isUserComplete() throws UserNotFoundException{
        User user = getCurrentUser();
        return user.isComplete();
    }

    @Override
    @Transactional
    public boolean updateUser(UserDto userDto) throws UserNotFoundException, UserAlreadyExistsException {
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
    
    @Override
    @Transactional
    public void deactivateUser() throws UserNotFoundException {
        User user = getCurrentUser();
        user.markAsDeleted();
        userDao.update(user);
        
        logger.warn("User (" + user.getEmail() + ") deactivated !");
    }
    
    
    //*******************
    //* user statistics *
    //*******************
    
    @Override
    @Transactional
    public UserStatisticsDto getStatistics(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        return buildStatistics(user);
    }
    
    
    
    //***************
    //* user search *
    //***************
    
    @Override
    @Transactional
    public List<UserDto> getTopUsers(int numberUsers) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        List<UserDto> result = new LinkedList<UserDto>();
        List<User> users = userDao.getTopUsers(numberUsers);
        
        for (User user : users) {
            UserDto userDto = new UserDto(user);
            populateRelations(userDto, currentUser, user);
            result.add(userDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public UserDto getUser() throws UserNotFoundException {
        User user = getCurrentUser();
        user.setLastLoginAt(new Date());
        userDao.update(user);
        
        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto getUserByName(String name) throws UserNotFoundException {
        User user = validateUser(name);
        User currentUser = getCurrentUser();
        UserStatisticsDto statistics = buildStatistics(user);
        
        UserDto userDto = new UserDto(user);
        userDto.setStatistics(statistics);
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
        UserStatisticsDto statistics = buildStatistics(user);
        
        UserDto userDto = new UserDto(user);
        userDto.setStatistics(statistics);
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
        UserStatisticsDto statistics = buildStatistics(user);
        
        UserDto userDto = new UserDto(user);
        userDto.setStatistics(statistics);
        populateRelations(userDto, currentUser, user);
        return userDto;
    }
    
    @Override
    @Transactional
    public UserDto getUserById(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        User currentUser = getCurrentUser();
        UserStatisticsDto statistics = buildStatistics(user);
        
        UserDto userDto = new UserDto(user);
        userDto.setStatistics(statistics);
        populateRelations(userDto, currentUser, user);
        return userDto;
    }

    
    
    //***************
    //* user follow *
    //***************

    @Override
    @Transactional
    public UserStatisticsDto follow(Long userId) throws UserNotFoundException {
        User user = getCurrentUser();
        User following = userDao.get(userId);
        
        if ( following == null ) {
            throw new UserNotFoundException("Cannot find the given following (userId: " + userId + ") user.");
        }
        
        user.addFollowing(following);
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("user", following);
        vars.put("follower", user);

        emailSender.sendNotification(NotificationType.FOLLOWER_ADDED, following, vars);
        
        return buildStatistics(user);
    }
    
    @Override
    @Transactional
    public UserStatisticsDto unfollow(Long userId) throws UserNotFoundException {
        User user = getCurrentUser();
        User following = userDao.get(userId);
        
        if ( following == null ) {
            throw new UserNotFoundException("Cannot find the given following (userId: " + userId + ") user.");
        }
        
        user.removeFollowing(following);
        
        return buildStatistics(user);
    }
    
    @Override
    @Transactional
    public List<UserDto> getFollowers(Long userId) throws UserNotFoundException {
        List<UserDto> result = new LinkedList<UserDto>();
        User user = validateUser(userId);
        User currentUser = getCurrentUser();
        
        List<User> validFollowers = user.getValidFollowers();
        if ( validFollowers != null ) {
            for ( User follower : validFollowers ) {
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
        
        List<User> validFollowings = user.getValidFollowings();
        if ( validFollowings != null ) {
            for ( User following : validFollowings ) {
                UserDto userDto = new UserDto(following);
                populateRelations(userDto, currentUser, following);
                
                result.add(userDto);
            }
        }
        
        return result;
    }
    
    
    
    //*****************
    //* user settings *
    //*****************
    
    @Override
    @Transactional
    public UserSettingDto getUserSetting() throws UserNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        
        if ( currentUser.isBusinessAccount() ) {
            throw new GeneralException("User is a business type, there is no setting for it.");
        }
        
        MemberUserData userData = (MemberUserData) currentUser.getUserData();
        UserSetting userSetting = userData.getUserSetting();
        
        if ( userSetting == null ) {
            //automatically creating user setting if not present
            userSetting = createUserSetting(userData);
        }
        
        return new UserSettingDto(currentUser, userSetting);
    }
    
    @Override
    @Transactional
    public void saveUserSetting(UserSettingDto userSettingDto) throws UserNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        
        if ( currentUser.isBusinessAccount() ) {
            throw new GeneralException("User is a business type, there is no setting for it.");
        }
        
        MemberUserData userData = (MemberUserData) currentUser.getUserData();
        UserSetting userSetting = userData.getUserSetting();
        
        if ( userSetting == null ) {
            //automatically creating user setting if not present
            userSetting = createUserSetting(userData);
        }
        
        userSetting.setNotifiableTypes(userSettingDto.getNotifiableTypes());
        userSettingDao.update(userSetting);
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

    private int getFollowersSize(User user) throws UserNotFoundException {
        return user.getValidFollowers().size();
    }
    
    private int getFollowingsSize(User user) throws UserNotFoundException {
        return user.getValidFollowings().size();
    }
    
    private UserStatisticsDto buildStatistics(User user) throws UserNotFoundException {
        Long userId = user.getId();
        int numReceivings = adService.getUserRequestedAdsSize(userId);
        int numGivings = adService.getUserAdsSize(userId);
        int numReceivedRatings = adService.getAllReceivedRatingsSize(userId);
        int numSentRatings = adService.getAllSentRatingsSize(userId);
        int numBookmarks = adService.getBookmarkedAdsSize(userId);
        int numFollowers = this.getFollowersSize(user);
        int numFollowings = this.getFollowingsSize(user);
        int numUnreadMessages = messageService.getUnreadMessagesSize(userId);
        int requestLimit = user.getUserPoint() != null ? user.getUserPoint().getRequestLimit() : 0;
        
        int numPositiveReceivedRatings = 0;
        int numNegativeReceivedRatings = 0;
        for ( RatingDto rating : adService.getReceivedRatings(userId) ) {
            numPositiveReceivedRatings += (rating.getValue() > 0 ? 1 : 0);
            numNegativeReceivedRatings += (rating.getValue() < 0 ? 1 : 0);
        }
        
        int numPositiveSentRatings = 0;
        int numNegativeSentRatings = 0;
        for ( RatingDto rating : adService.getSentRatings(userId) ) {
            numPositiveSentRatings += (rating.getValue() > 0 ? 1 : 0);
            numNegativeSentRatings += (rating.getValue() < 0 ? 1 : 0);
        }
        
        UserStatisticsDto statistics = new UserStatisticsDto();
        statistics.setNumReceivings(numReceivings);
        statistics.setNumGivings(numGivings);
        statistics.setNumBookmarks(numBookmarks);
        statistics.setNumFollowers(numFollowers);
        statistics.setNumFollowings(numFollowings);
        statistics.setNumReceivedRatings(numReceivedRatings);
        statistics.setNumSentRatings(numSentRatings);
        statistics.setNumPositiveReceivedRatings(numPositiveReceivedRatings);
        statistics.setNumNegativeReceivedRatings(numNegativeReceivedRatings);
        statistics.setNumPositiveSentRatings(numPositiveSentRatings);
        statistics.setNumNegativeSentRatings(numNegativeSentRatings);
        statistics.setNumUnreadMessages(numUnreadMessages);
        statistics.setRequestLimit(requestLimit);
        return statistics;
    }
}
