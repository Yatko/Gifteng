package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.venefica.utils.Constants;

public class MessageDto implements KvmSerializable
{
	public long id;
	public String text;
	public boolean owner;
	public String toName;
	public String toAvatarUrl;
	public Date createdAt;
	public boolean read;
	public String fromName;
	public String fromAvatarUrl;
	public String toFullName;
	public String fromFullName;

	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return id;
			case 1:
				return text;
			case 2:
				return owner;
			case 3:
				return toName;
			case 4:
				return toAvatarUrl;
			case 5:
				return createdAt != null ? createdAt.getTime() : null;
			case 6:
				return read;
			case 7:
				return fromName;
			case 8:
				return fromAvatarUrl;
			case 9:
				return toFullName;
			case 10:
				return fromFullName;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 11;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.name = "id";
				info.type = Long.TYPE;
				break;

			case 1:
				info.name = "text";
				info.type = String.class;
				break;

			case 2:
				info.name = "owner";
				info.type = Boolean.class;
				break;

			case 3:
				info.name = "toName";
				info.type = String.class;
				break;

			case 4:
				info.name = "toAvatarUrl";
				info.type = String.class;
				break;

			case 5:
				info.name = "createdAt";
				info.type = Long.class;
				break;

			case 6:
				info.name = "read";
				info.type = Boolean.class;
				break;

			case 7:
				info.name = "fromName";
				info.type = String.class;
				break;

			case 8:
				info.name = "fromAvatarUrl";
				info.type = String.class;
				break;
				
			case 9:
				info.name = "toFullName";
				info.type = String.class;
				break;

			case 10:
				info.name = "fromFullName";
				info.type = String.class;
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
				id = Long.parseLong(value.toString());
				break;
			case 1:
				text = String.valueOf(value);
				break;
			case 2:
				owner = Boolean.parseBoolean(value.toString());
				break;
			case 3:
				toName = String.valueOf(value);
				break;
			case 4:
				toAvatarUrl = String.valueOf(value);
				break;
			case 5:
				createdAt = new Date(Long.parseLong(value.toString()));
				break;
			case 6:
				read = Boolean.parseBoolean(value.toString());
				break;
			case 7:
				fromName = String.valueOf(value);
				break;
			case 8:
				fromAvatarUrl = String.valueOf(value);
				break;
			case 9:
				toFullName = String.valueOf(value);
				break;
			case 10:
				fromFullName = String.valueOf(value);
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
		envelope.addMapping(null, "MessageDto", this.getClass());
		new ImageDto().registerRead(envelope);
	}

}
