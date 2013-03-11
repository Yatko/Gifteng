package com.venefica.service;

import com.venefica.dao.ImageDao;
import com.venefica.model.User;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserField;
import com.venefica.service.fault.UserNotFoundException;
import java.util.HashSet;
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
    
    @Override
    @Transactional
    public Long registerUser(UserDto userDto, String password) throws UserAlreadyExistsException {
        User user = userDto.toUser(imageDao);
        user.setPassword(password);

        // Check for existing users
        if (userDao.findUserByName(user.getName()) != null) {
            throw new UserAlreadyExistsException(UserField.NAME,
                    "User with the same name already exists!");
        }
        if (userDao.findUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException(UserField.EMAIL,
                    "User with the specified email already exists!");
        }

        return userDao.save(user);
    }

    @Override
    @Transactional
    public UserDto getUser() throws UserNotFoundException {
        try {
            Long currentUserId = securityContextHolder.getContext().getUserId();
            User user = userDao.get(currentUserId);
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
    public boolean isUserComplete() {
        User user = securityContextHolder.getContext().getUser();
        return user.isComplete();
    }

    @Override
    @Transactional
    public boolean updateUser(UserDto userDto) throws UserAlreadyExistsException {
        try {
            Long currentUserId = securityContextHolder.getContext().getUserId();
            User user = userDao.get(currentUserId);

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
            return user.isComplete();
        } catch (NumberFormatException e) {
            logger.error("Update user failed", e);
            throw new RuntimeException(e);
        }
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
