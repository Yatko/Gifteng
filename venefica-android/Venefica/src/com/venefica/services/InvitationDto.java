/**
 * 
 */
package com.venefica.services;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.venefica.utils.Constants;

/**
 * Describes an invitation data transfer object.
 * @author avinash
 * 
 */
public class InvitationDto implements KvmSerializable{

	public enum UserType {
		GIVER, RECEIVER;	
	}
    private String email;
    private String zipCode;
    private String source;
    private UserType userType;
	/**
	 * default constructor
	 */
	public InvitationDto() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getProperty(int)
	 */
	@Override
	public Object getProperty(int index) {
		switch (index)
		{
			case 0:
				return email;
			case 1:
				return zipCode;
			case 2:
				return source;
			case 3:
				if (userType != null) {
					return userType.name();
				} else {
					return null;
				}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyCount()
	 */
	@Override
	public int getPropertyCount() {
		return 4;
	}
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#setProperty(int, java.lang.Object)
	 */
	@Override
	public void setProperty(int index, Object value) {
		switch (index)
		{
			case 0:
				email = String.valueOf(value);
				break;
			case 1:
				zipCode = String.valueOf(value);
				break;
			case 2:
				source = String.valueOf(value);
				break;
			case 3:
				userType = UserType.valueOf(value.toString());
				break;
		}
	}
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyInfo(int, java.util.Hashtable, org.ksoap2.serialization.PropertyInfo)
	 */
	@Override
	public void getPropertyInfo(int index,@SuppressWarnings ("rawtypes") Hashtable properties,
			PropertyInfo info) {
		switch (index)
		{
			case 0:
				info.name = "email";
				info.type = String.class;
				break;

			case 1:
				info.name = "zipCode";
				info.type = String.class;
				break;

			case 2:
				info.name = "source";
				info.type = String.class;
				break;

			case 3:
				info.name = "userType";
				info.type = String.class;
				break;

			default:
				break;
		}
	}

	public void register(SoapSerializationEnvelope envelope){
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}

	public void registerRead(SoapSerializationEnvelope envelope){
		envelope.addMapping(null, "InvitationDto", this.getClass());
	}
}
