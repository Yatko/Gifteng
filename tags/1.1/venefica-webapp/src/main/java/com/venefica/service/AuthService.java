package com.venefica.service;

import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.UserNotFoundException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

/**
 * Authenticates users by their login and password.
 *
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "AuthService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface AuthService {

    /**
     * Authenticates the user by his login and password and generates
     * authorization token.
     *
     * @param name the name of the user
     * @param password the password of the user
     * @param userAgent the visiting users browser details
     * @return authorization token
     * @throws AuthenticationException is thrown when the user can't be
     * authenticated.
     */
    @WebMethod(operationName = "Authenticate")
    @WebResult(name = "AuthToken")
    public String authenticate(
            @WebParam(name = "name") String name,
            @WebParam(name = "password") String password,
            @WebParam(name = "userAgent") String userAgent) throws AuthenticationException;
    
    /**
     * Authenticates the user by his email address and password and generates
     * authorization token.
     *
     * @param name the email address of the user
     * @param password the password of the user
     * @param userAgent the visiting users browser details
     * @return authorization token
     * @throws AuthenticationException is thrown when the user can't be
     * authenticated.
     */
    @WebMethod(operationName = "AuthenticateEmail")
    @WebResult(name = "AuthToken")
    public String authenticateEmail(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password,
            @WebParam(name = "userAgent") String userAgent) throws AuthenticationException;
    
    /**
     * Authenticates the user by his phone number and password and generates
     * authorization token.
     *
     * @param name the phone number of the user
     * @param password the password of the user
     * @param userAgent the visiting users browser details
     * @return authorization token
     * @throws AuthenticationException is thrown when the user can't be
     * authenticated.
     */
    @WebMethod(operationName = "AuthenticatePhone")
    @WebResult(name = "AuthToken")
    public String authenticatePhone(
            @WebParam(name = "phone") String phone,
            @WebParam(name = "password") String password,
            @WebParam(name = "userAgent") String userAgent) throws AuthenticationException;

    /**
     * Changes user's password.
     *
     * @param oldPassword old password
     * @param newPassword new password
     * @throws AuthorizationException when the old password doesn't match the
     * current one
     */
    @WebMethod(operationName = "ChangePassword")
    public void changePassword(@WebParam(name = "oldPassword") String oldPassword,
            @WebParam(name = "newPassword") String newPassword) throws AuthorizationException;
    
    /**
     * Change password into the given new one via forgot password request.
     * 
     * @param newPassword
     * @param code the forgot password request code
     * @throws GeneralException in case the code is invalid
     */
    @WebMethod(operationName = "ChangeForgottenPassword")
    public void changeForgottenPassword(@WebParam(name = "newPassword") String newPassword,
            @WebParam(name = "code") String code) throws UserNotFoundException, GeneralException;
    
    /**
     * 
     * @param email 
     * @param ipAddress 
     */
    @WebMethod(operationName = "ForgotPasswordEmail")
    public void forgotPasswordEmail(@WebParam(name = "email") String email, @WebParam(name = "ipAddress") String ipAddress) throws UserNotFoundException, GeneralException;
}
