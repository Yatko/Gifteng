/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.service.dto.InvitationDto;
import java.util.Date;
//import java.util.UUID;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
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
        
        return invitationDao.save(invitation);
    }
    
    // internal helpers
    
    private String generateCode() {
        //String code = UUID.randomUUID().toString();
        String code = RandomStringUtils.randomAlphanumeric(CODE_LENGTH);
        return code;
    }
    
}
