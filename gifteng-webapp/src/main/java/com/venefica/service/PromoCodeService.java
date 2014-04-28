/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.service.dto.PromoCodeProviderDto;
import com.venefica.service.fault.RequestNotFoundException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gyuszi
 */
@WebService(name = "PromoCodeService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PromoCodeService {
    
    @WebMethod(operationName = "GetProviders")
    List<PromoCodeProviderDto> getProviders();
    
    @WebMethod(operationName = "UpdateZenclusivePromoCode")
    void updateZenclusivePromoCode(
            @WebParam(name = "requestId") @NotNull Long requestId,
            @WebParam(name = "promoCode") @NotNull String promoCode,
            @WebParam(name = "expirationDate") String expirationDate,
            @WebParam(name = "value") String value,
            @WebParam(name = "supportPhone") String supportPhone,
            @WebParam(name = "supportUrl") String supportUrl,
            @WebParam(name = "incentiveName") String incentiveName,
            @WebParam(name = "incentiveUrl") String incentiveUrl
    ) throws RequestNotFoundException;
    
    /**
     * Not published via WS.
     * 
     * @param provider
     * @param request 
     */
    void processProvider(Long promoCodeProviderId, Long requestId);
    
}
