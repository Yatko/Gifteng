/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.ShareDto;
import com.venefica.service.dto.UserConnectionDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.ShareNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.List;
import java.util.Set;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gyuszi
 */
@WebService(name = "SocialService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface SocialService {
    
    //******************
    //* social network *
    //******************
    
    /**
     * Returns a list of social network names connected to the current user
     * account.
     */
    @WebMethod(operationName = "GetConnectedSocialNetworks")
    @WebResult(name = "network")
    public Set<String> getConnectedSocialNetworks();

    /**
     * Removes a connection between the current user account and an account of
     * the specified social network.
     *
     * @param networkName the name of the social network
     */
    @WebMethod(operationName = "DisconnectFromNetwork")
    public void disconnectFromNetwork(@WebParam(name = "networkName") String networkName);
    
    /**
     * Returns the user connection on the given network for the current user.
     * 
     * @param networkName the name of the social network
     * @return null if the user is not connected with the given social network,
     * otherwise the user connection
     */
    @WebMethod(operationName = "GetUserConnection")
    @WebResult(name = "userConnection")
    public UserConnectionDto getUserConnection(@WebParam(name = "networkName") String networkName);
    
    /**
     * 
     * @return 
     */
    @WebMethod(operationName = "CalculateSocialPoints")
    @WebResult(name = "socialPoints")
    public int calculateSocialPoints() throws UserNotFoundException;
    
    
    
    //*********
    //* share *
    //*********
    
    /**
     * Places the message on the walls of the connected social networks.
     *
     * @param message text of the message
     */
    @WebMethod(operationName = "ShareOnSocialNetworks")
    void shareOnSocialNetworks(@WebParam(name = "message") @NotNull String message);
    
    @WebMethod(operationName = "GetShare")
    @WebResult(name = "share")
    ShareDto getShare(@WebParam(name = "shareId") @NotNull Long shareId) throws ShareNotFoundException;
    
    @WebMethod(operationName = "CreateShare")
    @WebResult(name = "shareId")
    Long createShare(@WebParam(name = "share") @NotNull ShareDto shareDto) throws AdNotFoundException, UserNotFoundException;
    
    
    
    //***********
    //* friends *
    //***********
    
    @WebMethod(operationName = "GetFriendList")
    @WebResult(name = "friend")
    List<UserConnectionDto> getFriendList(
            @WebParam(name = "networkName") String networkName,
            @WebParam(name = "offset") int offset,
            @WebParam(name = "limit") int limit);
}
