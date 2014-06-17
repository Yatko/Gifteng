package com.venefica.service;

import com.venefica.service.dto.BusinessCategoryDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.dto.UserSettingDto;
import com.venefica.service.dto.UserStatisticsDto;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvalidInvitationException;
import com.venefica.service.fault.InvitationNotFoundException;
import com.venefica.service.fault.UserAlreadyExistsException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.validation.constraints.NotNull;

/**
 * Registers new users and updates information about them.
 *
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "UserManagement", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface UserManagementService {
    
    //*****************************
    //* user verification related *
    //*****************************
    
    @WebMethod(operationName = "VerifyUser")
    void verifyUser(@WebParam(name = "code") @NotNull String code) throws UserNotFoundException, GeneralException;
    
    @WebMethod(operationName = "ResendVerification")
    void resendVerification() throws UserNotFoundException, GeneralException;
    
    
    
    //**********************
    //* categories related *
    //**********************
    
    @WebMethod(operationName = "GetAllBusinessCategories")
    @WebResult(name = "category")
    List<BusinessCategoryDto> getAllBusinessCategories();
    
    
    
    //************************************
    //* user crud (create/update/delete) *
    //************************************
    
    /**
     * Registers a new business user.
     * 
     * @param userDto the user to be created
     * @param password
     * @return
     * @throws UserAlreadyExistsException when a user with the same email exists
     * @throws GeneralException when the provided category does not exists
     */
    @WebMethod(operationName = "RegisterBusinessUser")
    @WebResult(name = "userId")
    public Long registerBusinessUser(
            @WebParam(name = "user") @NotNull UserDto userDto,
            @WebParam(name = "password") @NotNull String password) throws UserAlreadyExistsException, GeneralException;
    
    /**
     * Registers new local user.
     *
     * @param userDto the user to register
     * @param password the password (may be a hash of the password in the future)
     * @param invitationCode the invitation code
     * @param referrerId the referrer user id
     * @param shareId the share id
     * @throws UserAlreadyExistsException is thrown when a user with the same
     * name and email already exists
     * @throws InvitationNotFoundException thrown when the provided invitation code
     * could not be found
     * @throws GeneralException if the user verification code could not be created
     */
    @WebMethod(operationName = "RegisterUser")
    @WebResult(name = "userId")
    public Long registerUser(
            @WebParam(name = "user") @NotNull UserDto userDto,
            @WebParam(name = "password") @NotNull String password,
            @WebParam(name = "invitationCode")  @NotNull String invitationCode,
            @WebParam(name = "referrerId")  Long referrerId,
            @WebParam(name = "shareId")  Long shareId)
            throws UserAlreadyExistsException, InvitationNotFoundException, InvalidInvitationException, GeneralException;
    
    /**
     * Updates user information.
     *
     * @param userDto updated user object
     * @return true if all required information is gathered.
     */
    @WebMethod(operationName = "UpdateUser")
    @WebResult(name = "complete")
    public boolean updateUser(@WebParam(name = "user") @NotNull UserDto userDto)
            throws UserNotFoundException, UserAlreadyExistsException;
    
    /**
     * Returns true if all required information is gathered for the current
     * user.
     */
    @WebMethod(operationName = "IsUserComplete")
    @WebResult(name = "complete")
    public boolean isUserComplete() throws UserNotFoundException;
    
    /**
     * Mark the currently logged user as deactivated (deleted). User and it's
     * gifts will not be show anymore.
     */
    @WebMethod(operationName = "DeactivateUser")
    public void deactivateUser() throws UserNotFoundException;

    
    
    //*******************
    //* user statistics *
    //*******************
    
    @WebMethod(operationName = "GetStatistics")
    @WebResult(name = "statistics")
    public UserStatisticsDto getStatistics(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    
    
    //***************
    //* user search *
    //***************
    
    /**
     * Retrieves a list of users having the highest score.
     * 
     * @param numberUsers
     * @return 
     */
    @WebMethod(operationName = "GetTopUsers")
    @WebResult(name = "users")
    public List<UserDto> getTopUsers(@WebParam(name = "numberUsers") int numberUsers) throws UserNotFoundException;
    
    /**
     * Retrieves information about the current (logged) user.
     *
     * @return user object
     * @throws UserNotFoundException is thrown when a user with the specified
     * token (id) not found
     */
    @WebMethod(operationName = "GetUser")
    @WebResult(name = "user")
    public UserDto getUser() throws UserNotFoundException;

    /**
     * Retrieves information about the user by his name.
     *
     * @param name name of the user
     * @return user object
     * @throws UserNotFoundException is thrown when a user with the specified
     * name not found
     */
    @WebMethod(operationName = "GetUserByName")
    @WebResult(name = "user")
    public UserDto getUserByName(@WebParam(name = "name") @NotNull String name) throws UserNotFoundException;
    
    /**
     * Retrieves information about the user by his email address.
     * 
     * @param email the email address of the user
     * @return user object
     * @throws UserNotFoundException when the user with the specified email
     * address not found
     */
    @WebMethod(operationName = "GetUserByEmail")
    @WebResult(name = "user")
    public UserDto getUserByEmail(@WebParam(name = "email") @NotNull String email) throws UserNotFoundException;
    
    /**
     * Retrieves information about the user by his phone number.
     * 
     * @param phone the phone number of the user
     * @return user object
     * @throws UserNotFoundException when the user with the specified phone
     * number not found
     */
    @WebMethod(operationName = "GetUserByPhone")
    @WebResult(name = "user")
    public UserDto getUserByPhone(@WebParam(name = "phone") @NotNull String phone) throws UserNotFoundException;
    
    /**
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetUserById")
    @WebResult(name = "user")
    public UserDto getUserById(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;

    
    
    //***************
    //* user follow *
    //***************
    
    /**
     * Adds the given user into the actual ones followers list.
     * 
     * @param userId the user identifier
     * @return the current user statistics
     * @throws UserNotFoundException the given user could not be found
     */
    @WebMethod(operationName = "Follow")
    @WebResult(name = "statistics")
    public UserStatisticsDto follow(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * Removes the given user from the followers list of the actual one.
     * 
     * @param userId the user identifier
     * @return the current user statistics
     * @throws UserNotFoundException the given user could not be found
     */
    @WebMethod(operationName = "Unfollow")
    @WebResult(name = "statistics")
    public UserStatisticsDto unfollow(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * Returns the list of users that are following the given one.
     * 
     * @param userId
     * @return list of followers
     * @throws UserNotFoundException if the given user could not be found
     */
    @WebMethod(operationName = "GetFollowers")
    @WebResult(name = "follower")
    public List<UserDto> getFollowers(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * Returns a list of users that the given one is following.
     * 
     * @param userId
     * @return list of followings
     * @throws UserNotFoundException if the given user could not be found
     */
    @WebMethod(operationName = "GetFollowings")
    @WebResult(name = "following")
    public List<UserDto> getFollowings(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    
    
    //*****************
    //* user settings *
    //*****************
    
    /**
     * Returns the current user settings.
     * 
     * @return
     * @throws GeneralException if the user is not member type
     */
    @WebMethod(operationName = "GetUserSetting")
    @WebResult(name = "setting")
    public UserSettingDto getUserSetting() throws UserNotFoundException, GeneralException;
    
    /**
     * Saves/updates the given settings for the given and current user.
     * 
     * @param userSettingDto
     * @throws GeneralException if the current user is business type, or the current
     * user is not the same as the updating one
     */
    @WebMethod(operationName = "SaveUserSetting")
    public void saveUserSetting(@WebParam(name = "setting") @NotNull UserSettingDto userSettingDto) throws UserNotFoundException, GeneralException;
}
