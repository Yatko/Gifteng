package com.venefica.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.location.Location;
import android.util.Log;

import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.module.user.UserDto;
import com.venefica.utils.Constants;
import com.venefica.utils.GeoLocation;

public class AdDto implements KvmSerializable
{
	public long id;
	public long categoryId;
	public String category = "";
	public String title = "";
	public String description = "";
	public BigDecimal price = new BigDecimal(0.0f);
	public double latitude;
	public double longitude;
	public ImageDto image;
	/** takes milliseconds */
	public String createdAt = "";
	/** true - the current user is the owner of ad*/
	public boolean owner;
	public ImageDto imageThumbnail;
	public boolean inBookmars;
	public Date expiresAt;
	public boolean wanted;
	public long numViews;
	public UserDto creator;
	public boolean expired;
	public int numAvailProlongations;
	public boolean canMarkAsSpam;
	public float rating;
	public boolean canRate;
	public List<ImageDto> images;

	public AdDto()
	{

	}

	public AdDto(PostData Post)
	{
		if (Post == null)
		{
			Log.d("AdDto Alert!", "post == null");
			return;
		}

		id = Post.id;
		categoryId = Post.Category;
		category = "";
		title = Post.Title;
		description = Post.Desc;

		try
		{
			price = BigDecimal.valueOf(Double.valueOf(String.valueOf(Post.Price.trim().replace(",", "."))));
		}
		catch (Exception e)
		{
			price = BigDecimal.valueOf(0.0);
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}

		Location loc = GeoLocation.GeoToLoc(Post.GeoLocation);
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();

		if (Post.ImageShow != null)
		{
			image = new ImageDto(Post.ImageShow);
			imageThumbnail = new ImageDto(Post.ImageShow, true);
		}

		expiresAt = Post.useExpires ? Post.expirate : null;
		wanted = Post.wanted;
	}

	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return id;
			case 1:
				return categoryId;
			case 2:
				return category;
			case 3:
				return title;
			case 4:
				return description;
			case 5:
				return String.valueOf(price);
			case 6:
				return String.valueOf(latitude);
			case 7:
				return String.valueOf(longitude);
			case 8:
				return image;
			case 9:
				return createdAt;
			case 10:
				return owner;
			case 11:
				return imageThumbnail;
			case 12:
				return inBookmars;
			case 13:
				return expiresAt != null ? expiresAt.getTime() : 0;
			case 14:
				return wanted;
			case 15:
				return numViews;
			case 16:
				return creator;
			case 17:
				return expired;
			case 18:
				return numAvailProlongations;
			case 19:
				return canMarkAsSpam;
			case 20:
				return String.valueOf(rating);
			case 21:
				return canRate;
			case 22:
				return null; //images no send
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 23;
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
				info.name = "categoryId";
				info.type = Long.TYPE;
				break;

			case 2:
				info.name = "category";
				info.type = String.class;
				break;

			case 3:
				info.name = "title";
				info.type = String.class;
				break;

			case 4:
				info.name = "description";
				info.type = String.class;
				break;

			case 5:
				info.name = "price";
				info.type = BigDecimal.class;
				break;

			case 6:
				info.name = "latitude";
				info.type = Double.TYPE;
				break;

			case 7:
				info.name = "longitude";
				info.type = Double.TYPE;
				break;

			case 8:
				info.name = "image";
				info.type = ImageDto.class;
				break;

			case 9:
				info.name = "createdAt";
				info.type = String.class;
				break;

			case 10:
				info.name = "owner";
				info.type = Boolean.class;
				break;

			case 11:
				info.name = "imageThumbnail";
				info.type = ImageDto.class;
				break;

			case 12:
				info.name = "inBookmars";
				info.type = Boolean.class;
				break;

			case 13:
				info.name = "expiresAt";
				info.type = Long.class;
				break;

			case 14:
				info.name = "wanted";
				info.type = Boolean.class;
				break;

			case 15:
				info.name = "numViews";
				info.type = Long.class;
				break;

			case 16:
				info.name = "creator";
				info.type = UserDto.class;
				break;

			case 17:
				info.name = "expired";
				info.type = Boolean.class;
				break;

			case 18:
				info.name = "numAvailProlongations";
				info.type = Integer.class;
				break;

			case 19:
				info.name = "canMarkAsSpam";
				info.type = Boolean.class;
				break;

			case 20:
				info.name = "rating";
				info.type = Float.class;
				break;

			case 21:
				info.name = "canRate";
				info.type = Boolean.class;
				break;

			case 22:
				info.name = "images";
				info.type = new Vector<ImageDto>().getClass();
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
					categoryId = Long.valueOf(value.toString());
					break;
				case 2:
					category = String.valueOf(value);
					break;
				case 3:
					title = String.valueOf(value);
					break;
				case 4:
					description = String.valueOf(value);
					break;
				case 5:
					price = BigDecimal.valueOf(Double.valueOf(String.valueOf(value)));
					break;
				case 6:
					latitude = Double.valueOf(value.toString());
					break;
				case 7:
					longitude = Double.valueOf(value.toString());
					break;
				case 8:
					image = (ImageDto)value;
					break;
				case 9:
					createdAt = String.valueOf(value);
					break;
				case 10:
					owner = Boolean.parseBoolean(value.toString());
					break;
				case 11:
					imageThumbnail = (ImageDto)value;
					break;
				case 12:
					inBookmars = Boolean.parseBoolean(value.toString());
					break;
				case 13:
					expiresAt = new Date(Long.parseLong(value.toString()));
					break;
				case 14:
					wanted = Boolean.parseBoolean(value.toString());
					break;
				case 15:
					numViews = Long.valueOf(value.toString());
					break;
				case 16:
					creator = (UserDto)value;
					break;
				case 17:
					expired = Boolean.parseBoolean(value.toString());
					break;
				case 18:
					numAvailProlongations = Integer.valueOf(value.toString());
					break;
				case 19:
					canMarkAsSpam = Boolean.parseBoolean(value.toString());
					break;
				case 20:
					rating = Float.valueOf(value.toString());
					break;
				case 21:
					canRate = Boolean.parseBoolean(value.toString());
					break;
				case 22:
					images = (Vector<ImageDto>)value;
					
					/*if(images == null)
						images = new ArrayList<ImageDto>();
					
					images.add((ImageDto)value);*/
					break;
			}
		}
		catch (Exception e)
		{
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
		new ImageDto().register(envelope);
		new UserDto().register(envelope);
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "AdDto", this.getClass());
		new ImageDto().registerRead(envelope);
		new UserDto().registerRead(envelope);
	}
}
