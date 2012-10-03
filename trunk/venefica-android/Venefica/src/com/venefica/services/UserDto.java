package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.venefica.utils.Constants;

public class UserDto implements KvmSerializable
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
	public ImageDto avatar;
	
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
				return country;
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
				country = String.valueOf(value);
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
}
