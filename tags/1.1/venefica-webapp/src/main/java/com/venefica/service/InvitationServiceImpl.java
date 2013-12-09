/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.MailChimpSender;
import com.venefica.common.MailException;
import com.venefica.common.RandomGenerator;
import com.venefica.config.Constants;
import com.venefica.config.InvitationConfig;
import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.model.UserType;
import com.venefica.service.dto.InvitationDto;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvitationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
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

    private static final String INVITATION_REQUEST_TEMPLATE = "invitation-request/";
    
    //spcial invitation request message for users with defined zipcodes
    private static final String INVITATION_REQUEST_1_TEMPLATE = INVITATION_REQUEST_TEMPLATE + "1/";
    private static final String INVITATION_REQUEST_1_SUBJECT_TEMPLATE = INVITATION_REQUEST_1_TEMPLATE + "subject.vm";
    private static final String INVITATION_REQUEST_1_HTML_MESSAGE_TEMPLATE = INVITATION_REQUEST_1_TEMPLATE + "message.html.vm";
    private static final String INVITATION_REQUEST_1_PLAIN_MESSAGE_TEMPLATE = INVITATION_REQUEST_1_TEMPLATE + "message.txt.vm";
    
    //other type of invitation request message for users being outside of the defined zipcodes
    private static final String INVITATION_REQUEST_2_TEMPLATE = INVITATION_REQUEST_TEMPLATE + "2/";
    private static final String INVITATION_REQUEST_2_SUBJECT_TEMPLATE = INVITATION_REQUEST_2_TEMPLATE + "subject.vm";
    private static final String INVITATION_REQUEST_2_HTML_MESSAGE_TEMPLATE = INVITATION_REQUEST_2_TEMPLATE + "message.html.vm";
    private static final String INVITATION_REQUEST_2_PLAIN_MESSAGE_TEMPLATE = INVITATION_REQUEST_2_TEMPLATE + "message.txt.vm";
    
    @Inject
    private InvitationDao invitationDao;
    @Inject
    private MailChimpSender mailChimpSender;
    @Inject
    private InvitationConfig invitationConfig;
    
    private final boolean useEmailSender = true;
    private final boolean useMailChimp = false;
    
    @Override
    @Transactional
    public Long requestInvitation(InvitationDto invitationDto) throws InvitationException {
        Invitation invitation;
        
        if ( invitationDto.getIpAddress() == null || invitationDto.getIpAddress().trim().isEmpty() ) {
            invitationDto.setIpAddress(getIpAddress());
        }
        
        invitation = invitationDao.findByEmail(invitationDto.getEmail());
        if ( invitation != null ) {
            //use the same invitation if email already exists
            invitationDto.update(invitation);
            invitation.setExpiresAt(getExpirationDate());
            invitation.setExpired(false);
            invitationDao.update(invitation);
        } else {
            invitation = new Invitation();
            invitationDto.update(invitation);
            invitation.setExpiresAt(getExpirationDate());
            invitation.setExpired(false);
            invitation.setCode(generateCode());
            invitationDao.save(invitation);
        }
        
        String email = invitation.getEmail();
        String country = invitation.getCountry();
        String zipcode = invitation.getZipCode();
        UserType userType = invitation.getUserType();
        String source = invitation.getSource();
        String otherSource = invitation.getOtherSource();
        String code = invitation.getCode();
        
        if ( useEmailSender ) {
            try {
                Map<String, Object> vars = new HashMap<String, Object>(0);
                vars.put("invitationCode", code);
                vars.put("invitation", invitation);

                String subject;
                String htmlMessage;
                String plainMessage;
                boolean containsZipcode = false;
                
                if ( invitationConfig.getZipCodes() != null ) {
                    if ( invitationConfig.getZipCodes().contains(zipcode) ) {
                        containsZipcode = true;
                    } else if ( invitationConfig.getZipCodes().contains(StringUtils.stripStart(zipcode, "0")) ) {
                        containsZipcode = true;
                    }
                }
                        
                if ( containsZipcode ) {
                    subject = INVITATION_REQUEST_1_SUBJECT_TEMPLATE;
                    htmlMessage = INVITATION_REQUEST_1_HTML_MESSAGE_TEMPLATE;
                    plainMessage = INVITATION_REQUEST_1_PLAIN_MESSAGE_TEMPLATE;
                } else {
                    subject = INVITATION_REQUEST_2_SUBJECT_TEMPLATE;
                    htmlMessage = INVITATION_REQUEST_2_HTML_MESSAGE_TEMPLATE;
                    plainMessage = INVITATION_REQUEST_2_PLAIN_MESSAGE_TEMPLATE;
                }
                
                emailSender.sendHtmlEmailByTemplates(subject, htmlMessage, plainMessage, email, vars);
            } catch ( MailException ex ) {
                logger.error("Email exception", ex);
                throw new InvitationException(ex.getErrorCode(), "Could not send invitation mail!");
            } catch ( Exception ex ) {
                logger.error("Runtime exception", ex);
                throw new InvitationException(GeneralException.GENERAL_ERROR, ex.getMessage());
            }
        }
        
        if ( useMailChimp ) {
            try {
                String finalSource;
                if ( otherSource != null && !otherSource.trim().isEmpty() ) {
                    finalSource = otherSource;
                } else {
                    finalSource = source;
                }

                Map<String, Object> vars = new HashMap<String, Object>(0);
                vars.put("COUNTRY", country);
                vars.put("ZIPCODE", zipcode);
                vars.put("USERTYPE", userType.getDescription());
                vars.put("SOURCE", finalSource);

                mailChimpSender.listSubscribe(email, vars);
            } catch ( MailException ex ) {
                logger.error("MailChimp exception", ex);
                throw new InvitationException(ex.getErrorCode(), "Could not list subscribe to MailChimp!");
            }
        }
        
        return invitation.getId();
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
    
    private String generateCode() throws InvitationException {
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
        return code;
    }
    
    private Date getExpirationDate() {
        return DateUtils.addDays(new Date(), Constants.INVITATION_EXPIRATION_PERIOD_DAYS);
    }
}
