package com.venefica.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.utils.Constants;

public class FilterDto implements KvmSerializable
{
	private static int staticRev = 0;
	
	public int rev;
	public List<Long> categories;

	public long distance = 50 * 1609;
	public double latitude;
	public double longitude;

	public BigDecimal minPrice;
	public BigDecimal maxPrice;
	public boolean hasPhoto;

	public boolean wanted = false;
	public String searchString;

	public FilterDto()
	{
		rev = staticRev++;
	}

	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return categories != null ? new Vector<Long>(categories) : null;
			case 1:
				return distance;
			case 2:
				return String.valueOf(latitude);
			case 3:
				return String.valueOf(longitude);
			case 4:
				return minPrice != null ? minPrice.toString() : null;
			case 5:
				return maxPrice != null ? maxPrice.toString() : null;
			case 6:
				return hasPhoto;
			case 7:
				return wanted;
			case 8:
				return searchString;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 9;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.name = "categories";
				info.type = new Vector<Long>().getClass();
				break;

			case 1:
				info.name = "distance";
				info.type = Long.class;
				break;

			case 2:
				info.name = "latitude";
				info.type = Double.class;
				break;

			case 3:
				info.name = "longitude";
				info.type = Double.class;
				break;

			case 4:
				info.name = "minPrice";
				info.type = BigDecimal.class;
				break;

			case 5:
				info.name = "maxPrice";
				info.type = BigDecimal.class;
				break;

			case 6:
				info.name = "hasPhoto";
				info.type = Boolean.TYPE;
				break;
				
			case 7:
				info.name = "wanted";
				info.type = Boolean.class;
				break;

			case 8:
				info.name = "searchString";
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
					categories = new ArrayList<Long>((Vector<Long>)value);
					break;
				case 1:
					distance = Long.valueOf(value.toString());
					break;
				case 2:
					latitude = Double.valueOf(value.toString());
					break;
				case 3:
					longitude = Double.valueOf(value.toString());
					break;
				case 4:
					minPrice = BigDecimal.valueOf(Double.valueOf(String.valueOf(value)));
					break;
				case 5:
					maxPrice = BigDecimal.valueOf(Double.valueOf(String.valueOf(value)));
					break;
				case 6:
					hasPhoto = Boolean.valueOf(value.toString());
					break;
				case 7:
					wanted = Boolean.valueOf(value.toString());
					break;
				case 8:
					searchString = value.toString();
					break;
			}
		}
		catch (Exception e)
		{
			Log.d("FilterDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "FilterDto", this.getClass());
	}
}
