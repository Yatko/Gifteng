package com.venefica.service;

import javax.inject.Inject;
import javax.jws.WebService;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;

@Service("authService")
@WebService(endpointInterface = "com.venefica.service.AuthService")
public class AuthServiceImpl implements AuthService {

	@Inject
	private UserDao userDao;
	@Inject
	private ThreadSecurityContextHolder securityContextHolder;
	@Inject
	private TokenEncryptor tokenEncryptor;

	@Override
	public String authenticate(String name, String password) throws AuthenticationException {
		User user = userDao.findUserByName(name);

		if (user == null)
			throw new AuthenticationException("Wrong user name or password!");

		try {
			if (user.getPassword().equals(password)) {
				Token token = new Token(user.getId());
				return tokenEncryptor.encrypt(token);
			}
		} catch (TokenEncryptionException e) {
			throw new AuthenticationException("Internal error!");
		}

		throw new AuthenticationException("Wrong user name or password!");
	}

	@Override
	@Transactional
	public void changePassword(String oldPassword, String newPassword)
			throws AuthorizationException {
		if (oldPassword == null)
			throw new NullPointerException("oldPassword is null");
		if (newPassword == null)
			throw new NullPointerException("newPassword is null");

		User currentUser = securityContextHolder.getContext().getUser();

		User user = userDao.get(currentUser.getId());

		if (user.getPassword().equals(oldPassword))
			user.setPassword(newPassword);
		else
			throw new AuthorizationException("Old passwod is wrong!");
	}
}
