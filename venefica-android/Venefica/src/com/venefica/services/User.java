package com.venefica.services;

import java.util.Date;

import com.venefica.utils.Constants;

public class User
{
	public boolean businessAcc;
	public Date dateOfBirth;
	public String email = "";
	public String firstName = "";
	public String lastName = "";
	public String name = "";
	public String phoneNumber = "";
	public String zipCode = "";
	public String country = "";
	public String city = "";
	public String area = "";
	public String avatarUrl;
	public boolean useMiles = true; 

	public User()
	{

	}

	public User(UserDto dto)
	{
		businessAcc = dto.businessAcc;
		dateOfBirth = dto.dateOfBirth;
		email = dto.email;
		firstName = dto.firstName;
		lastName = dto.lastName;
		name = dto.name;
		phoneNumber = dto.phoneNumber;
		zipCode = dto.zipCode;
		country = dto.country;
		city = dto.city;
		area = dto.area;

		if (dto.avatar != null && dto.avatar.url != null && dto.avatar.url.equals("") == false)
		{
			avatarUrl = Constants.PHOTO_URL_PREFIX + dto.avatar.url;
		}
		else
		{
			avatarUrl = "";
		}
		
		useMiles = dto.useMiles;
	}
}
