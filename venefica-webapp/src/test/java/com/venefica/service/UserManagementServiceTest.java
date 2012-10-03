package com.venefica.service;

import static org.junit.Assert.*;

import java.util.Date;

import javax.inject.Inject;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import com.venefica.service.UserManagementService;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/UserManagementServiceTest-context.xml")
public class UserManagementServiceTest extends ServiceTestBase<UserManagementService> {

	private static final String TEST_USER_NAME = "userManagementServiceTestUser2";

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

	// register -------

	@Test
	public void registerUserTest() throws UserAlreadyExistsException {
		UserDto testUserDto = createUserDto(TEST_USER_NAME);
		client.registerUser(testUserDto, "pa$$word");
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void registerUserWithTheSameNameTest() throws UserAlreadyExistsException {
		UserDto userWithRegisteredName = createUserDto("otherUser");
		userWithRegisteredName.setName(getFirstUser().getName());
		client.registerUser(userWithRegisteredName, "pa$$word");
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void registerUserWithTheSameEmailTest() throws UserAlreadyExistsException {
		UserDto userWithRegisteredEmail = createUserDto("otherUser");
		userWithRegisteredEmail.setEmail(getFirstUser().getEmail());
		client.registerUser(userWithRegisteredEmail, "pass$$word");
	}

	// update --------

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
