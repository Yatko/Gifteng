/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.config.EmailConfig;
import com.venefica.dao.LocationDao;
import com.venefica.model.Location;
import com.venefica.model.NotificationType;
import com.venefica.model.User;
import com.venefica.service.dto.AddressDto;
import com.venefica.service.fault.UserNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.stereotype.Service;

/**
 *
 * @author gyuszi
 */
@Service("utilityService")
@WebService(endpointInterface = "com.venefica.service.UtilityService")
public class UtilityServiceImpl extends AbstractService implements UtilityService {
    
    @Inject
    private EmailConfig emailConfig;
    
    @Inject
    private LocationDao locationDao;

    @Override
    public AddressDto getAddressByZipcode(String zipcode) {
        Location location = locationDao.findByZipcode(zipcode);
        if ( location == null ) {
            return null;
        }
        
        AddressDto address = new AddressDto();
        address.setCity(location.getCity());
        address.setState(location.getState());
        address.setStateAbbreviation(location.getStateAbbreviation());
        address.setZipCode(location.getZipcode());
        address.setLatitude(location.getLatitude());
        address.setLongitude(location.getLongitude());
        return address;
    }

    @Override
    public void sendEmail(String text) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("from", currentUser);
        vars.put("text", text == null ? "-" : text.trim());
        
        emailSender.sendNotification(NotificationType.EMAIL_NEW, emailConfig.getIssueEmailAddress(), vars);
    }
    
    @Override
    public void sendInvitationEmail(String text, List<String> emails) throws UserNotFoundException {
        if ( text == null || text.trim().isEmpty() ) {
            logger.warn("Could not send empty text invitation emails");
            return;
        } else if ( emails == null || emails.isEmpty() ) {
            logger.warn("Could not send empty addressed invitation emails");
            return;
        }
        
        User currentUser = getCurrentUser();
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("from", currentUser);
        vars.put("text", text.trim());
        
        for ( String email : emails ) {
            emailSender.sendNotification(NotificationType.INVITATION_EMAIL_NEW, email, vars);
        }
    }
}
