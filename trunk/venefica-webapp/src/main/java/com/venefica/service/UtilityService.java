/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.AddressDto;
import com.venefica.service.fault.UserNotFoundException;
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
@WebService(name = "UtilityService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface UtilityService {
    
    @WebMethod(operationName = "GetAddressByZipcode")
    @WebResult(name = "address")
    AddressDto getAddressByZipcode(@WebParam(name = "zipcode") @NotNull String zipcode);
    
    @WebMethod(operationName = "SendEmail")
    void sendEmail(@WebParam(name = "text") @NotNull String text) throws UserNotFoundException;
}
