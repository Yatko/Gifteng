package com.venefica.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;


/**
 * Authenticates users by their login and password.
 * 
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "AuthService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface AuthService {

	/**
	 * Authenticates the user by his login and password and generates authorization token.
	 * 
	 * @param name
	 *            the name of the user
	 * @param password
	 *            the password of the user
	 * @return authorization token
	 * @throws AuthenticationException
	 *             is thrown when the user can't be authenticated.
	 */
	@WebMethod(operationName = "Authenticate")
	@WebResult(name = "AuthToken")
	public String authenticate(@WebParam(name = "name") String name,
			@WebParam(name = "password") String password) throws AuthenticationException;

	/**
	 * Changes user's password.
	 * 
	 * @param oldPassword
	 *            old password
	 * @param newPassword
	 *            new password
	 * @throws AuthorizationException
	 *             when the old password doesn't match the current one
	 */
	@WebMethod(operationName = "ChangePassword")
	public void changePassword(@WebParam(name = "oldPassword") String oldPassword,
			@WebParam(name = "newPassword") String newPassword) throws AuthorizationException;
}
