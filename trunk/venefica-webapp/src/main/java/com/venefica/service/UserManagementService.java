package com.venefica.service;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserNotFoundException;


/**
 * Registers new users and updates information about them.
 * 
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "UserManagement", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface UserManagementService {

	/**
	 * Registers new local user not connected to any social network.
	 * 
	 * @param userDto
	 *            the user to register
	 * @param password
	 *            the password (may be a hash of the password in the future)
	 * @throws UserAlreadyExistsException
	 *             is thrown when a user with the same name already exists
	 */
	@WebMethod(operationName = "RegisterUser")
	@WebResult(name = "userId")
	public Long registerUser(@WebParam(name = "user") UserDto userDto,
			@WebParam(name = "password") String password) throws UserAlreadyExistsException;

	/**
	 * Updates user information.
	 * 
	 * @param userDto
	 *            updated user object
	 * @return true if all required information is gathered.
	 */
	@WebMethod(operationName = "UpdateUser")
	@WebResult(name = "complete")
	public boolean updateUser(@WebParam(name = "user") UserDto userDto)
			throws UserAlreadyExistsException;

	/**
	 * Retrieves information about the user.
	 * 
	 * @return user object
	 * @throws UserNotFoundException
	 *             is thrown when a user with the specified token (id) not found
	 */
	@WebMethod(operationName = "GetUser")
	@WebResult(name = "user")
	public UserDto getUser() throws UserNotFoundException;

	/**
	 * Retrieves information about the user by his name.
	 * 
	 * @param name
	 *            name of the user
	 * @return user object
	 * @throws UserNotFoundException
	 *             is thrown when a user with the specified name not found
	 */
	@WebMethod(operationName = "GetUserByName")
	@WebResult(name = "user")
	public UserDto getUserByName(@WebParam(name = "name") String name) throws UserNotFoundException;

	/**
	 * Returns true if all required information is gathered for the current user.
	 */
	@WebMethod(operationName = "IsUserComplete")
	@WebResult(name = "complete")
	public boolean isUserComplete();

	/**
	 * Returns a list of social network names connected to the current user account.
	 */
	@WebMethod(operationName = "GetConnectedSocialNetworks")
	@WebResult(name = "network")
	public Set<String> getConnectedSocialNetworks();

	/**
	 * Removes a connection between the current user account and an account of the specified social
	 * network.
	 * 
	 * @param networkName
	 *            the name of the social network
	 */
	@WebMethod(operationName = "DisconnectFromNetwork")
	public void disconnectFromNetwork(@WebParam(name = "networkName") String networkName);
}
