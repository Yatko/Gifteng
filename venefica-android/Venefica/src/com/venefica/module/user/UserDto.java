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
	private Date joinedAt;	
	private Long id;
	private boolean inFollowers;
    private boolean inFollowings;
	public Object getProperty(int index)
	{
		//final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		switch (index)
		{
		case 0:
			return dateOfBirth.getTime();
		case 1:
			return email;
		case 2:
			return firstName;
		case 3:
			return lastName;
		case 4:
			return name;
		case 5:
			return phoneNumber;
		case 6:
			return zipCode;
		case 7:
			return county;
		case 8:
			return city;
		case 9:
			return area;		
		case 10:
			return avatar;
		case 11:
			return joinedAt.getTime();
		case 12:
			return id;
		case 13:
			return inFollowers;
		case 14:
			return inFollowings;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 15;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
		case 0:
			info.name = "dateOfBirth";
			info.type = Long.class;
			break;

		case 1:
			info.name = "email";
			info.type = String.class;
			break;

		case 2:
			info.name = "firstName";
			info.type = String.class;
			break;

		case 3:
			info.name = "lastName";
			info.type = String.class;
			break;

		case 4:
			info.name = "name";
			info.type = String.class;
			break;

		case 5:
			info.name = "phoneNumber";
			info.type = String.class;
			break;

		case 6:
			info.name = "zipCode";
			info.type = String.class;
			break;

		case 7:
			info.name = "country";
			info.type = String.class;
			break;

		case 8:
			info.name = "city";
			info.type = String.class;
			break;

		case 9:
			info.name = "area";
			info.type = String.class;
			break;
			
		case 10:
			info.name = "avatar";
			info.type = ImageDto.class;
			break;
			
		case 11:
			info.name = "joinedAt";
			info.type = Long.class;
			break;
		case 12:
			info.name = "id";
			info.type = Long.class;
			break;
		case 13:
			info.name = "inFollowers";
			info.type = Boolean.class;
			break;
		case 14:
			info.name = "inFollowings";
			info.type = Boolean.class;
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
			dateOfBirth = new Date(Long.parseLong(value.toString()));
			break;
		case 1:
			email = String.valueOf(value);
			break;
		case 2:
			firstName = String.valueOf(value);
			break;
		case 3:
			lastName = String.valueOf(value);
			break;
		case 4:
			name = String.valueOf(value);
			break;
		case 5:
			phoneNumber = String.valueOf(value);
			break;
		case 6:
			zipCode = String.valueOf(value);
			break;
		case 7:
			county = String.valueOf(value);
			break;
		case 8:
			city = String.valueOf(value);
			break;
		case 9:
			area = String.valueOf(value);
			break;		
		case 10:
			avatar = (ImageDto)value;
			break;
		case 11:
			joinedAt = new Date(Long.parseLong(value.toString()));
			break;
		case 12:
			id = Long.valueOf(value.toString());
			break;
		case 13:
			inFollowers = Boolean.parseBoolean(value.toString());
			break;
		case 14:
			inFollowings = Boolean.parseBoolean(value.toString());
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
	 * @return the joinedAt
	 */
	public Date getJoinedAt() {
		return joinedAt;
	}

	/**
	 * @param joinedAt the joinedAt to set
	 */
	public void setJoinedAt(Date joinedAt) {
		this.joinedAt = joinedAt;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the inFollowers
	 */
	public boolean isInFollowers() {
		return inFollowers;
	}

	/**
	 * @param inFollowers the inFollowers to set
	 */
	public void setInFollowers(boolean inFollowers) {
		this.inFollowers = inFollowers;
	}

	/**
	 * @return the inFollowings
	 */
	public boolean isInFollowings() {
		return inFollowings;
	}

	/**
	 * @param inFollowings the inFollowings to set
	 */
	public void setInFollowings(boolean inFollowings) {
		this.inFollowings = inFollowings;
	}
}
