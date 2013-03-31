/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.dao.InvitationDao;
import com.venefica.model.Invitation;
import com.venefica.model.UserType;
import com.venefica.service.dto.InvitationDto;
import javax.inject.Inject;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author gyuszi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/InvitationServiceTest-context.xml")
public class InvitationServiceTest extends ServiceTestBase<InvitationService> {
    
    @Inject
    private InvitationDao invitationDao;
    
    public InvitationServiceTest() {
        super(InvitationService.class);
    }
    
    @Test
    public void requestInvitationTest() {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setEmail("xxx@gmail.com");
        invitationDto.setSource("other");
        invitationDto.setUserType(UserType.GIVER);
        invitationDto.setZipCode("00000");
        
        Long invitationId = client.requestInvitation(invitationDto);
        assertNotNull("The id of the invitation must be returned!", invitationId);
        
        Invitation invitation = invitationDao.get(invitationId);
        assertNotNull("The invitation with id = " + invitationId + " not found!", invitation);
        assertNotNull("Expiration date must be set!", invitation.getExpiresAt());
    }
}
