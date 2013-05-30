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
    private String state;
    // in, out
    private String area;
    // in, out
    private String zipCode;
    
    // in,out
    private Double latitude;
    // in,out
    private Double longitude;
    
    // WARNING: required for JAX-WS
    public AddressDto() {
    }
    
    public AddressDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public AddressDto(Point location) {
        this.latitude = location.getX();
        this.longitude = location.getY();
    }
    
    public AddressDto(Address address) {
        address1 = address.getAddress1();
        address2 = address.getAddress2();
        city = address.getCity();
        county = address.getCounty();
        country = address.getCountry();
        state = address.getState();
        area = address.getArea();
        zipCode = address.getZipCode();
    }
    
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
        address.setArea(area);
        address.setZipCode(zipCode);
        return address;
    }
    
    public AddressWrapper getAddressWrapper() {
        return new AddressWrapper(getAddress());
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
}
