package com.venefica.service;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import com.venefica.service.dto.ReviewDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.InvalidInvitationException;
import com.venefica.service.fault.InvitationNotFoundException;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/UserManagementServiceTest-context.xml")
public class UserManagementServiceTest extends ServiceTestBase<UserManagementService> {

    private static final String TEST_USER_NAME = "userManagementServiceTestUser2";
    private static final String TEST_INVITATION_CODE = "12345";
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private TokenEncryptor tokenEncryptor;
    
    private User testUser;
    private String testUserAuthToken;

    public UserManagementServiceTest() {
        super(UserManagementService.class);
    }

    @Before
    public void loadTestUser() throws TokenEncryptionException {
        testUser = userDao.findUserByName(TEST_USER_NAME);
        testUserAuthToken = testUser != null ? tokenEncryptor.encrypt(new Token(testUser.getId()))
                : null;
    }

    @Test
    public void getUserTest() throws UserNotFoundException, UserAlreadyExistsException {
        authenticateClientAsFirstUser();
        UserDto userDto = client.getUser();
        assertNotNull("User object must be returned!", userDto);
    }

    // register
    
    @Test
    public void registerUserTest() throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        UserDto testUserDto = createUserDto(TEST_USER_NAME);
        client.registerUser(testUserDto, "pa$$word", TEST_INVITATION_CODE);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerUserWithTheSameNameTest() throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        UserDto userWithRegisteredName = createUserDto("otherUser");
        userWithRegisteredName.setName(getFirstUser().getName());
        client.registerUser(userWithRegisteredName, "pa$$word", TEST_INVITATION_CODE);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerUserWithTheSameEmailTest() throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        UserDto userWithRegisteredEmail = createUserDto("otherUser");
        userWithRegisteredEmail.setEmail(getFirstUser().getEmail());
        client.registerUser(userWithRegisteredEmail, "pass$$word", TEST_INVITATION_CODE);
    }
    
    @Test(expected = InvalidInvitationException.class)
    public void registerMultipleUsersWithTheSameInvitationTest() throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException {
        String invitationCode = "11111";
        
        UserDto testUserDto_1 = createUserDto("test_1");
        Long testUserId_1 = client.registerUser(testUserDto_1, "pa$$word", invitationCode);
        assertNotNull("Test user might been created", testUserId_1);
        
        UserDto testUserDto_2 = createUserDto("test_2");
        Long testUserId_2 = client.registerUser(testUserDto_2, "pa$$word", invitationCode);
        assertNotNull("Test user might been created", testUserId_2);
        
        UserDto testUserDto_3 = createUserDto("test_3");
        client.registerUser(testUserDto_3, "pa$$word", invitationCode);
    }
    
    // update
    
    @Test(expected = UserAlreadyExistsException.class)
    public void updateUserWithTheSameNameTest() throws UserAlreadyExistsException {
        UserDto userWithRegisteredName = new UserDto(testUser);
        userWithRegisteredName.setName(getFirstUser().getName());
        authenticateClientWithToken(testUserAuthToken);
        client.updateUser(userWithRegisteredName);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void updateUserWithTheSameEmailTest() throws UserAlreadyExistsException {
        UserDto userWithRegisteredEmail = new UserDto(testUser);
        userWithRegisteredEmail.setEmail(getFirstUser().getEmail());
        authenticateClientWithToken(testUserAuthToken);
        client.updateUser(userWithRegisteredEmail);
    }

    @Test
    public void updateUserTest() throws UserAlreadyExistsException {
        UserDto userDto = new UserDto(testUser);
        userDto.setPhoneNumber("47093");
        authenticateClientWithToken(testUserAuthToken);
        client.updateUser(userDto);
    }

    // follow
    
    @Test
    public void followTest() throws UserNotFoundException {
        //1 follows 2
        authenticateClientAsFirstUser();
        client.follow(SECOND_USER_ID);
        
        List<UserDto> followings_1 = client.getFollowings(FIRST_USER_ID);
        List<UserDto> followers_1 = client.getFollowers(FIRST_USER_ID);
        
        assertNotNull(followings_1);
        assertEquals(1, followings_1.size());
        assertNull(followers_1);
        
        //3 follows 2
        authenticateClientAsThirdUser();
        client.follow(SECOND_USER_ID);
        
        List<UserDto> followings_3 = client.getFollowings(THIRD_USER_ID);
        List<UserDto> followers_3 = client.getFollowers(THIRD_USER_ID);
        
        assertNotNull(followings_3);
        assertEquals(1, followings_3.size());
        assertNull(followers_3);
        
        //2 details
        authenticateClientAsSecondUser();
        
        List<UserDto> followings_2 = client.getFollowings(SECOND_USER_ID);
        List<UserDto> followers_2 = client.getFollowers(SECOND_USER_ID);
        
        assertNotNull(followers_2);
        assertEquals(2, followers_2.size());
        assertNull(followings_2);
    }
    
    @Test
    public void followAlreadyFollowedUserTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        client.follow(SECOND_USER_ID);
        client.follow(SECOND_USER_ID);
        client.follow(SECOND_USER_ID);
        
        List<UserDto> followings_1 = client.getFollowings(FIRST_USER_ID);
        List<UserDto> followers_1 = client.getFollowers(FIRST_USER_ID);
        
        assertEquals(1, followings_1.size());
        assertNull(followers_1);
    }
    
    @Test(expected = UserNotFoundException.class)
    public void followUnexistingUserTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        client.follow(Long.MAX_VALUE);
    }
    
    // unfollow
    
    @Test
    public void unfollowTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        client.follow(SECOND_USER_ID);
        
        List<UserDto> before_followings_1 = client.getFollowings(FIRST_USER_ID);
        assertNotNull(before_followings_1);
        assertEquals(1, before_followings_1.size());
        
        client.unfollow(SECOND_USER_ID);
        
        List<UserDto> after_followings_1 = client.getFollowings(FIRST_USER_ID);
        assertNull(after_followings_1);
    }
    
    @Test
    public void unfollowAlreadyUnfollowedUserTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        client.unfollow(SECOND_USER_ID);
        client.unfollow(SECOND_USER_ID);
        
        List<UserDto> followings_1 = client.getFollowings(FIRST_USER_ID);
        assertNull(followings_1);
    }
    
    @Test(expected = UserNotFoundException.class)
    public void unfollowUnexistingUserTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        client.unfollow(Long.MAX_VALUE);
    }
    
    // review
    
    @Test
    public void addReviewTest() throws UserNotFoundException {
        authenticateClientAsFirstUser();
        
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setTo(new UserDto(getSecondUser()));
        reviewDto.setText("Trustful person");
        client.addReview(reviewDto);
        
        List<ReviewDto> reviews_1 = client.getReviews(FIRST_USER_ID);
        assertTrue(reviews_1 == null || reviews_1.isEmpty());
        
        List<ReviewDto> reviews_2 = client.getReviews(SECOND_USER_ID);
        assertEquals(1, reviews_2.size());
    }
    
    // helpers
    
    private static UserDto createUserDto(String name) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setFirstName("First name");
        userDto.setLastName("Last name");
        userDto.setEmail(name + "@mail.ru");
        userDto.setPhoneNumber("123" + name.hashCode());
        userDto.setDateOfBirth(new Date());
        userDto.setZipCode("123");
        return userDto;
    }
}
