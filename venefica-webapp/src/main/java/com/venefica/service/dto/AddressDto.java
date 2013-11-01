/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.common.GeoUtils;
import com.venefica.model.Address;
import com.venefica.model.AddressWrapper;
import com.vividsolutions.jts.geom.Point;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Address and location (geo position) wrapper dto class.
 * 
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressDto {
    
    // in, out
    private Long id;
    // in, out
    private String name;
    // in, out
    private String address1;
    // in, out
    private String address2;
    // in, out
    private String city;
    // in, out
    private String county;
    // in, out
    private String country;
    // in, out
    private String stateAbbreviation;
    // in, out
    private String state;
    // in, out
    private String area;
    // in, out
    private String zipCode;
    
    // in, out
    private Double longitude;
    // in, out
    private Double latitude;
    
    // WARNING: required for JAX-WS
    public AddressDto() {
    }
    
    public AddressDto(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public AddressDto(Point location) {
        setLocation(location);
    }
    
    public AddressDto(Address address) {
        setAddress(address);
    }
    
    public AddressDto(Address address, Point location) {
        setAddress(address);
        setLocation(location);
    }
    
    public AddressDto(AddressWrapper addressWrapper) {
        setAddress(addressWrapper.getAddress());
        
        id = addressWrapper.getId();
        name = addressWrapper.getName();
    }
    
    // helper methods
    
    public Point getLocation() {
        if (latitude != null && longitude != null) {
            Point location = GeoUtils.createPoint(latitude, longitude);
            return location;
        }
        return null;
    }
    
    public Address getAddress() {
        Address address = new Address();
        address.setAddress1(address1);
        address.setAddress2(address2);
        address.setCity(city);
        address.setCounty(county);
        address.setCountry(country);
        address.setState(state);
        address.setStateAbbreviation(stateAbbreviation);
        address.setArea(area);
        address.setZipCode(zipCode);
        return address;
    }
    
    public AddressWrapper getAddressWrapper() {
        AddressWrapper addressWrapper = new AddressWrapper(getAddress());
        addressWrapper.setId(id);
        addressWrapper.setName(name);
        return addressWrapper;
    }
    
    // internal helpers
    
    private void setAddress(Address address) {
        if ( address != null ) {
            address1 = address.getAddress1();
            address2 = address.getAddress2();
            city = address.getCity();
            county = address.getCounty();
            country = address.getCountry();
            stateAbbreviation = address.getStateAbbreviation();
            state = address.getState();
            area = address.getArea();
            zipCode = address.getZipCode();
        }
    }
    
    private void setLocation(Point location) {
        if ( location != null ) {
            //the longitude-X and latitude-Y is not a mistake
            this.longitude = location.getX();
            this.latitude = location.getY();
        }
    }
    
    // getters/setters
    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }
}
