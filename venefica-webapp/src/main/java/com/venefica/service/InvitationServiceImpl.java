/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.service.dto.InvitationDto;
import java.util.Date;
import javax.annotation.Resource;
//import java.util.UUID;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.stereotype.Service;

/**
 *
 * @author gyuszi
 */
@Service("invitationService")
@WebService(endpointInterface = "com.venefica.service.InvitationService")
public class InvitationServiceImpl extends AbstractService implements InvitationService {

    private static final int EXPIRATION_PERIOD_DAYS = 3;
    private static final int CODE_LENGTH = 8;
    
    @Resource
    private WebServiceContext jaxwsContext;
    
    @Inject
    private InvitationDao invitationDao;
    
    @Override
    public Long requestInvitation(InvitationDto invitationDto) {
        Invitation invitation = new Invitation();
        invitationDto.update(invitation);
        
        String code = generateCode();
        Date expiresAt = DateUtils.addDays(new Date(), EXPIRATION_PERIOD_DAYS);
        invitation.setExpiresAt(expiresAt);
        invitation.setExpired(false);
        invitation.setCode(code);
        invitation.setIpAddress(getIpAddress());
        
        return invitationDao.save(invitation);
    }
    
    @Override
    public boolean isInvitationValid(String code) {
        Invitation invitation = invitationDao.findByCode(code);
        
        if ( invitation == null ) {
            logger.debug("Invitation (code: " + code + ") could not be found.");
            return false;
        } else if ( !invitation.isValid()) {
            logger.warn("Invitation (code: " + code + ") is not valid anymore.");
            return false;
        }
        
        return true;
    }
    
    // internal helpers
    
    private String generateCode() {
        //String code = UUID.randomUUID().toString();
        String code = RandomStringUtils.randomAlphanumeric(CODE_LENGTH);
        return code;
    }
    
    private String getIpAddress() { 
        String ipAddress = null;
        HttpServletRequest request;
        String header = "X-Forwarded-For";
        
        if ( jaxwsContext != null ) {
            request = (HttpServletRequest) jaxwsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        } else {
            Message message = PhaseInterceptorChain.getCurrentMessage();
            request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        }

        if ( request != null ) {
            if ( StringUtils.isNotBlank(request.getHeader(header)) ) {
                ipAddress = request.getHeader(header);
            } else {
                ipAddress = request.getRemoteAddr();
            }
        }

        logger.info("IP ADDRESS: " + ipAddress);
        return ipAddress; 
    }
    
}
