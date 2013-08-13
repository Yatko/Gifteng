/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.PermissionDeniedException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author gyuszi
 */
@WebService(name = "AdminService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface AdminService {
    
    //***************
    //* approval    *
    //***************
    
    /**
     * 
     * @return
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "GetUnapprovedAds")
    @WebResult(name = "ad")
    List<AdDto> getUnapprovedAds() throws PermissionDeniedException;
    
    /**
     * 
     * @return
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "GetOfflineAds")
    @WebResult(name = "ad")
    List<AdDto> getOfflineAds() throws PermissionDeniedException;
    
    /**
     * 
     * @param adId
     * @return
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "GetApprovals")
    @WebResult(name = "approval")
    List<ApprovalDto> getApprovals(@WebParam(name = "adId") Long adId)
            throws PermissionDeniedException, AdNotFoundException;
    
    /**
     * 
     * @param adId
     * @param revision
     * @return
     * @throws PermissionDeniedException
     * @throws AdNotFoundException
     * @throws ApprovalNotFoundException 
     */
    @WebMethod(operationName = "GetApproval")
    @WebResult(name = "approval")
    ApprovalDto getApproval(@WebParam(name = "adId") Long adId, @WebParam(name = "revision") Integer revision)
            throws PermissionDeniedException, AdNotFoundException, ApprovalNotFoundException;
    
    /**
     * 
     * @param adId
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "ApproveAd")
    void approveAd(@WebParam(name = "adId") Long adId)
            throws PermissionDeniedException, AdNotFoundException, GeneralException;
    
    /**
     * 
     * @param adId
     * @param message
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "UnapproveAd")
    void unapproveAd(@WebParam(name = "adId") Long adId, @WebParam(name = "message") String message)
            throws PermissionDeniedException, AdNotFoundException, GeneralException;
    
    /**
     * 
     * @param adId
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "OnlineAd")
    void onlineAd(@WebParam(name = "adId") Long adId)
            throws PermissionDeniedException, AdNotFoundException;
    
    //***************
    //* user        *
    //***************
    
    /**
     * 
     * @return a list of available users
     */
    @WebMethod(operationName = "GetUsers")
    @WebResult(name = "user")
    public List<UserDto> getUsers() throws PermissionDeniedException;
    
}
