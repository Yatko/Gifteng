/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.InvitationDto;
import com.venefica.service.fault.InvitationException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author gyuszi
 */
@WebService(name = "InvitationService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface InvitationService {
    
    /**
     * Creates a new invitation.
     * 
     * @param invitationDto creating invitation
     * @return id of the created invitation
     */
    @WebMethod(operationName = "RequestInvitation")
    @WebResult(name = "invitationId")
    Long requestInvitation(@WebParam(name = "invitation") InvitationDto invitationDto) throws InvitationException;
    
    /**
     * Checks if the invitation identified with the given code exists and is valid.
     * Valid invitation means:
     * - is not expired
     * - the maximum allowed number of use is not exceeded
     * 
     * @param code the invitation code
     * @return 
     */
    @WebMethod(operationName = "IsInvitationValid")
    @WebResult(name = "valid")
    boolean isInvitationValid(@WebParam(name = "code") String code);
    
}
