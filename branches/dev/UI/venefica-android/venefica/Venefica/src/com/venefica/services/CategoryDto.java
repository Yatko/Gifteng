package com.venefica.services;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.utils.Constants;

public class CategoryDto implements KvmSerializable
{
	long id;
	long parentId;
	Vector<CategoryDto> subcategories;
	String name;

	public CategoryDto()
	{

	}

	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return id;
			case 1:
				return parentId;
			case 2:
				return subcategories;
			case 3:
				return name;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 4;
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
				info.name = "parentId";
				info.type = Long.TYPE;
				break;

			case 2:
				info.name = "subcategories";
				info.type = new Vector<CategoryDto>().getClass();
				break;

			case 3:
				info.name = "name";
				info.type = String.class;
				break;

			default:
				break;
		}
	}


	@SuppressWarnings ("unchecked")
	public void setProperty(int index, Object value)
	{
		try
		{
			switch (index)
			{
				case 0:
					id = Long.valueOf(value.toString());
					break;
				case 1:
					parentId = Long.valueOf(value.toString());
					break;
				case 2:
				{
					subcategories = (Vector<CategoryDto>)value;
					break;
				}
				case 3:
				{
					name = value.toString();
					break;
				}
			}
		}
		catch (Exception e)
		{
			Log.d("categoryDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "CategoryDto", new CategoryDto().getClass());
	}
}
