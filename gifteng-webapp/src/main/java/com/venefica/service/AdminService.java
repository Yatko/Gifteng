/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.ShippingDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.PermissionDeniedException;
import com.venefica.service.fault.ShippingNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.List;
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
    List<AdDto> getUnapprovedAds() throws UserNotFoundException, PermissionDeniedException;
    
    /**
     * 
     * @return
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "GetOfflineAds")
    @WebResult(name = "ad")
    List<AdDto> getOfflineAds() throws UserNotFoundException, PermissionDeniedException;
    
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
            throws UserNotFoundException, PermissionDeniedException, AdNotFoundException;
    
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
            throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, ApprovalNotFoundException;
    
    /**
     * 
     * @param adId
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "ApproveAd")
    void approveAd(@WebParam(name = "adId") Long adId)
            throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, GeneralException;
    
    /**
     * 
     * @param adId
     * @param message
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "UnapproveAd")
    void unapproveAd(@WebParam(name = "adId") Long adId, @WebParam(name = "message") String message)
            throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, GeneralException;
    
    /**
     * 
     * @param adId
     * @throws PermissionDeniedException
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "OnlineAd")
    void onlineAd(@WebParam(name = "adId") Long adId)
            throws UserNotFoundException, PermissionDeniedException, AdNotFoundException;
    
    //***************
    //* user        *
    //***************
    
    /**
     * 
     * @return a list of available users
     */
    @WebMethod(operationName = "GetUsers")
    @WebResult(name = "user")
    public List<UserDto> getUsers() throws UserNotFoundException, PermissionDeniedException;
    
    //***************
    //* shipping    *
    //***************
    
    /**
     * Get all the available and not deleted shippings.
     * 
     * @return
     * @throws UserNotFoundException
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "GetShippings")
    @WebResult(name = "shipping")
    public List<ShippingDto> getShippings() throws UserNotFoundException, PermissionDeniedException;
    
    /**
     * Updates the given shipping and persists changes in the database.
     * Also the shipping label (if provided) will be uploaded into storage.
     * 
     * @param shippingDto
     * @throws UserNotFoundException
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "UpdateShipping")
    public void updateShipping(@WebParam(name = "shipping") @NotNull ShippingDto shippingDto)
            throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException;
    
    /**
     * 
     * @param shippingId
     * @throws UserNotFoundException
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "SendMailToCreator")
    @WebResult(name = "sent")
    public boolean sendMailToCreator(@WebParam(name = "shippingId") @NotNull Long shippingId)
            throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException;
    
    /**
     * 
     * @param shippingId
     * @throws UserNotFoundException
     * @throws PermissionDeniedException 
     */
    @WebMethod(operationName = "SendMailToReceiver")
    @WebResult(name = "sent")
    public boolean sendMailToReceiver(@WebParam(name = "shippingId") @NotNull Long shippingId)
            throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException;
    
    /**
     * 
     * @param shippingId
     * @throws UserNotFoundException
     * @throws PermissionDeniedException 
     */
    //delete/hide shipping (only if it's 'processed')
    @WebMethod(operationName = "DeleteShipping")
    public void deleteShipping(@WebParam(name = "shippingId") @NotNull Long shippingId)
            throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException;
    
}
