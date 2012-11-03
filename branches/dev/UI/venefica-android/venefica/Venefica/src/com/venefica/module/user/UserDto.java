package com.venefica.module.user;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Class to hold and transfer user data using soap. 
 */
public class UserDto implements KvmSerializable
{
	private boolean businessAcc;
	private Date dateOfBirth;
	private String email = "";
	private String firstName = "";
	private String lastName = "";
	private String name = "";
	private String phoneNumber = "";
	private String zipCode = "";
	private String county = "";
	private String city = "";
	private String area = "";
	private ImageDto avatar;

	//no services
	public boolean useMiles = true;

	public Object getProperty(int index)
	{
		//final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		switch (index)
		{
		case 0:
			return businessAcc;
		case 1:
			return dateOfBirth.getTime();
		case 2:
			return email;
		case 3:
			return firstName;
		case 4:
			return lastName;
		case 5:
			return name;
		case 6:
			return phoneNumber;
		case 7:
			return zipCode;
		case 8:
			return county;
		case 9:
			return city;
		case 10:
			return area;
		case 11:
			return avatar;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 12;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
		case 0:
			info.name = "businessAcc";
			info.type = Boolean.TYPE;
			break;

		case 1:
			info.name = "dateOfBirth";
			info.type = Long.class;
			break;

		case 2:
			info.name = "email";
			info.type = String.class;
			break;

		case 3:
			info.name = "firstName";
			info.type = String.class;
			break;

		case 4:
			info.name = "lastName";
			info.type = String.class;
			break;

		case 5:
			info.name = "name";
			info.type = String.class;
			break;

		case 6:
			info.name = "phoneNumber";
			info.type = String.class;
			break;

		case 7:
			info.name = "zipCode";
			info.type = String.class;
			break;

		case 8:
			info.name = "country";
			info.type = String.class;
			break;

		case 9:
			info.name = "city";
			info.type = String.class;
			break;

		case 10:
			info.name = "area";
			info.type = String.class;
			break;

		case 11:
			info.name = "avatar";
			info.type = ImageDto.class;
			break;

		default:
			break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{
		case 0:
			businessAcc = Boolean.parseBoolean(value.toString());
			break;
		case 1:
			dateOfBirth = new Date(Long.parseLong(value.toString()));
			break;
		case 2:
			email = String.valueOf(value);
			break;
		case 3:
			firstName = String.valueOf(value);
			break;
		case 4:
			lastName = String.valueOf(value);
			break;
		case 5:
			name = String.valueOf(value);
			break;
		case 6:
			phoneNumber = String.valueOf(value);
			break;
		case 7:
			zipCode = String.valueOf(value);
			break;
		case 8:
			county = String.valueOf(value);
			break;
		case 9:
			city = String.valueOf(value);
			break;
		case 10:
			area = String.valueOf(value);
			break;
		case 11:
			avatar = (ImageDto)value;
			break;
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
		new ImageDto().register(envelope);
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "UserDto", this.getClass());
		new ImageDto().registerRead(envelope);
	}

	/**
	 * @return the businessAcc
	 */
	public boolean isBusinessAcc() {
		return businessAcc;
	}

	/**
	 * @param businessAcc the businessAcc to set
	 */
	public void setBusinessAcc(boolean businessAcc) {
		this.businessAcc = businessAcc;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String country) {
		this.county = country;
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
	 * @return the avatar
	 */
	public ImageDto getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(ImageDto avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the useMiles
	 */
	public boolean isUseMiles() {
		return useMiles;
	}

	/**
	 * @param useMiles the useMiles to set
	 */
	public void setUseMiles(boolean useMiles) {
		this.useMiles = useMiles;
	}
}
