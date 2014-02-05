/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

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
}
