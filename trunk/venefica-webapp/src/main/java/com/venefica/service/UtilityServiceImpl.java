/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.dao.LocationDao;
import com.venefica.model.Location;
import com.venefica.service.dto.AddressDto;
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
    
}
