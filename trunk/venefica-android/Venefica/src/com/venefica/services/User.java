package com.venefica.services;

import java.util.Date;

import com.venefica.module.user.UserDto;
import com.venefica.utils.Constants;
/**
 * 
 * @author avinash
 * Class to hold user data
 */
public class User {
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
	private String avatarUrl;
	private boolean useMiles = true;
	private ImageDto avatar;
	public User() {

	}

	public User(UserDto dto) {
		businessAcc = dto.isBusinessAcc();
		dateOfBirth = dto.getDateOfBirth();
		email = dto.getEmail();
		firstName = dto.getFirstName();
		lastName = dto.getLastName();
		name = dto.getName();
		phoneNumber = dto.getPhoneNumber();
		zipCode = dto.getZipCode();
		county = dto.getCounty();
		city = dto.getCity();
		area = dto.getArea();

		if (dto.getAvatar() != null && dto.getAvatar().url != null
				&& dto.getAvatar().url.equals("") == false) {
			avatarUrl = Constants.PHOTO_URL_PREFIX + dto.getAvatar().url;
			setAvatar(dto.getAvatar());
		} else {
			avatarUrl = "";
		}

		useMiles = dto.useMiles;
	}

	/**
	 * @return the businessAcc
	 */
	public boolean isBusinessAcc() {
		return businessAcc;
	}

	/**
	 * @param businessAcc
	 *            the businessAcc to set
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
	 * @param dateOfBirth
	 *            the dateOfBirth to set
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
	 * @param email
	 *            the email to set
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
	 * @param firstName
	 *            the firstName to set
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
	 * @param lastName
	 *            the lastName to set
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
	 * @param name
	 *            the name to set
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
	 * @param phoneNumber
	 *            the phoneNumber to set
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
	 * @param zipCode
	 *            the zipCode to set
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
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
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
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl
	 *            the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the useMiles
	 */
	public boolean isUseMiles() {
		return useMiles;
	}

	/**
	 * @param useMiles
	 *            the useMiles to set
	 */
	public void setUseMiles(boolean useMiles) {
		this.useMiles = useMiles;
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
}
