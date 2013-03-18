package com.venefica.service;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.model.User;
import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authService")
@WebService(endpointInterface = "com.venefica.service.AuthService")
public class AuthServiceImpl extends AbstractService implements AuthService {

    @Inject
    private TokenEncryptor tokenEncryptor;

    @Override
    public String authenticate(String name, String password) throws AuthenticationException {
        User user = userDao.findUserByName(name);

        if (user == null) {
            throw new AuthenticationException("Wrong user name!");
        }

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
        if (oldPassword == null) {
            throw new NullPointerException("oldPassword is null");
        }
        if (newPassword == null) {
            throw new NullPointerException("newPassword is null");
        }

        User currentUser = securityContextHolder.getContext().getUser();

        User user = userDao.get(currentUser.getId());

        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
        } else {
            throw new AuthorizationException("Old passwod is wrong!");
        }
    }
}
