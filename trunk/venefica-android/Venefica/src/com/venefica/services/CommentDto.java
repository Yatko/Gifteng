package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.venefica.utils.Constants;

public class CommentDto implements KvmSerializable
{
	public long id;
	public String text;
	public boolean owner;
	public String publisherName;
	public String publisherAvatarUrl;
	public Date createdAt;
	public String publisherFullName;

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
				return publisherName;
			case 4:
				return publisherAvatarUrl;
			case 5:
				return createdAt != null ? createdAt.getTime() : null;
			case 6:
				return publisherFullName;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 7;
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
				info.name = "publisherName";
				info.type = String.class;
				break;

			case 4:
				info.name = "publisherAvatarUrl";
				info.type = String.class;
				break;

			case 5:
				info.name = "createdAt";
				info.type = Long.class;
				break;
				
			case 6:
				info.name = "publisherFullName";
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
				publisherName = String.valueOf(value);
				break;
			case 4:
				publisherAvatarUrl = String.valueOf(value);
				break;
			case 5:
				createdAt = new Date(Long.parseLong(value.toString()));
				break;
			case 6:
				publisherFullName = String.valueOf(value);
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
		envelope.addMapping(null, "CommentDto", this.getClass());
		new ImageDto().registerRead(envelope);
	}

}
