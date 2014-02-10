/**
 * 
 */
package com.venefica.services;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.utils.Constants;

/**
 * @author avinash
 * class to handle address data
 */
public class AddressDto implements KvmSerializable {

	private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String county = "";
    private String country = "";
    private String state = "";
    private String area = "";
    private String zipCode = "";
    private Double latitude = 0.0;
    private Double longitude = 0.0;
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getProperty(int)
	 */
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return address1;
		case 1:
			return address2;
		case 2:
			return city;
		case 3:
			return county;
		case 4:
			return country;
		case 5:
			return state;
		case 6:
			return area;
		case 7:
			return zipCode;
		case 8:
			return String.valueOf(latitude);
		case 9:
			return String.valueOf(longitude);		
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyCount()
	 */
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#setProperty(int, java.lang.Object)
	 */
	@Override
	public void setProperty(int index, Object value) {
		try
		{
			switch (index)
			{
				case 0:
					address1 = String.valueOf(value.toString());
					break;
				case 1:
					address2 = String.valueOf(value.toString());
					break;
				case 2:
					city = String.valueOf(value);
					break;
				case 3:
					county = String.valueOf(value);
					break;
				case 4:
					country = String.valueOf(value);
					break;
				case 5:
					state = String.valueOf(value);
					break;
				case 6:
					area = String.valueOf(value);
					break;
				case 7:
					zipCode = String.valueOf(value);
					break;
				case 8:
					latitude = Double.valueOf(value.toString());
					break;
				case 9:
					longitude = Double.valueOf(value.toString());
					break;				
			}
		}catch (Exception e){
			Log.d("AddressDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope){
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}
	public void registerRead(SoapSerializationEnvelope envelope){
		envelope.addMapping(null, "AddressDto", this.getClass());
	}
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyInfo(int, java.util.Hashtable, org.ksoap2.serialization.PropertyInfo)
	 */
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info) {
		switch (index)
		{
			case 0:
				info.name = "address1";
				info.type = String.class;
				break;

			case 1:
				info.name = "address2";
				info.type = String.class;
				break;

			case 2:
				info.name = "city";
				info.type = String.class;
				break;

			case 3:
				info.name = "county";
				info.type = String.class;
				break;

			case 4:
				info.name = "country";
				info.type = String.class;
				break;

			case 5:
				info.name = "state";
				info.type = String.class;
				break;

			case 6:
				info.name = "area";
				info.type = String.class;
				break;

			case 7:
				info.name = "zipCode";
				info.type = String.class;
				break;

			case 8:
				info.name = "latitude";
				info.type = Double.TYPE;
				break;

			case 9:
				info.name = "longitude";
				info.type = Double.TYPE;
				break;

			default:
				break;
		}
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
