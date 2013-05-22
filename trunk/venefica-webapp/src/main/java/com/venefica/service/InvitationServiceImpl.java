/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.EmailSender;
import com.venefica.common.MailChimpSender;
import com.venefica.common.MailException;
import com.venefica.common.RandomGenerator;
import com.venefica.config.Constants;
import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.service.dto.InvitationDto;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvitationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import java.util.UUID;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Service("invitationService")
@WebService(endpointInterface = "com.venefica.service.InvitationService")
public class InvitationServiceImpl extends AbstractService implements InvitationService {

    private static final String INVITATION_REQUEST_SUBJECT_TEMPLATE = "invitation-request-subject.vm";
    private static final String INVITATION_REQUEST_HTML_MESSAGE_TEMPLATE = "invitation-request-message.html.vm";
    private static final String INVITATION_REQUEST_PLAIN_MESSAGE_TEMPLATE = "invitation-request-message.txt.vm";
    
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private EmailSender emailSender;
    @Inject
    private MailChimpSender mailChimpSender;
    
    @Override
    @Transactional
    public Long requestInvitation(InvitationDto invitationDto) throws InvitationException {
        String code;
        int generationTried = 0;
        while ( true ) {
            code = RandomGenerator.generateNumeric(Constants.INVITATION_DEFAULT_CODE_LENGTH);
            generationTried++;
            if ( invitationDao.findByCode(code) == null ) {
                //the generated code does not exists, found an unused (free) one
                break;
            } else if ( generationTried >= 10 ) {
                throw new InvitationException("Cannot generate valid invitation code!");
            }
        }
        
        Invitation invitation = new Invitation();
        invitationDto.update(invitation);
        invitation.setExpiresAt(DateUtils.addDays(new Date(), Constants.INVITATION_EXPIRATION_PERIOD_DAYS));
        invitation.setExpired(false);
        invitation.setCode(code);
        invitation.setIpAddress(getIpAddress());
        
        Long invitationId = invitationDao.save(invitation);
        
        try {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("invitationCode", code);

            emailSender.sendHtmlEmailByTemplates(
                    INVITATION_REQUEST_SUBJECT_TEMPLATE,
                    INVITATION_REQUEST_HTML_MESSAGE_TEMPLATE,
                    INVITATION_REQUEST_PLAIN_MESSAGE_TEMPLATE,
                    invitationDto.getEmail(),
                    vars);
        } catch ( MailException ex ) {
            logger.error("Email exception", ex);
            throw new InvitationException(ex.getErrorCode(), "Could not send invitation mail!");
        } catch ( Exception ex ) {
            logger.error("Runtime exception", ex);
            throw new InvitationException(GeneralException.GENERAL_ERROR, ex.getMessage());
        }
        
        try {
            String source;
            if ( invitationDto.getOtherSource() != null && !invitationDto.getOtherSource().trim().isEmpty() ) {
                source = invitationDto.getOtherSource();
            } else {
                source = invitationDto.getSource();
            }

            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("COUNTRY", invitationDto.getCountry());
            vars.put("ZIPCODE", invitationDto.getZipCode());
            vars.put("USERTYPE", invitationDto.getUserType().getDescription());
            vars.put("SOURCE", source);

            mailChimpSender.listSubscribe(invitationDto.getEmail(), vars);
        } catch ( MailException ex ) {
            logger.error("MailChimp exception", ex);
            throw new InvitationException(ex.getErrorCode(), "Could not list subscribe to MailChimp!");
        }
        
        return invitationId;
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
}
