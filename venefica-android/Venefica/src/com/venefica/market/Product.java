package com.venefica.market;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.venefica.module.user.UserDto;
import com.venefica.services.AdDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.ImageAd;
import com.venefica.utils.VeneficaApplication;

public class Product
{
	public long Id;
	public String Title = "";
	public String Desc = "";
	public String Price = "";
	public long Category;
	public ImageAd image;
	public ImageAd imageThumbnail;
	public GeoPoint Location;
	public Date PublicDate;
	public boolean owner;
	public boolean inBookmars;
	public Date expiresAt;
	public boolean wanted;
	public long numViews;
	public UserDto creator;
	public boolean expired;
	public long numAvailProlongations;
	public boolean canMarkAsSpam;
	public float rating;
	public boolean canRate;
	public List<ImageAd> images;

	//The following variables are calculated by hand on site
	/** When was published */
	public PostTime postTime = PostTime.None;
	/**
	 * Distance in kilometers to the product <b> WARNING! memorable that we can
	 * Be and miles, do not forget to convert into what must </ b>
	 */
	public float KMeters;

	public enum PostTime
	{
		None, Day, Week, Last
	}

	public Product()
	{
		;
	}

	public Product(Product clone)
	{
		Id = clone.Id;
		Title = clone.Title;
		Desc = clone.Desc;
		Price = clone.Price;
		Category = clone.Category;
		image = clone.image;
		imageThumbnail = clone.imageThumbnail;
		Location = clone.Location;
		PublicDate = clone.PublicDate;
		owner = clone.owner;
		inBookmars = clone.inBookmars;
		expiresAt = clone.expiresAt;
		wanted = clone.wanted;
		numViews = clone.numViews;
		creator = clone.creator;
		expired = clone.expired;
		numAvailProlongations = clone.numAvailProlongations;
		canMarkAsSpam = clone.canMarkAsSpam;
		rating = clone.rating;
		canRate = clone.canRate;
	}

	public Product(AdDto dto)
	{
		/*try
		{
			Id = (int)dto.id;
			Title = dto.title;
			Desc = dto.description;
			Price = String.format("%.2f", dto.price != null ? dto.price.floatValue() : 0.0f);
			Category = (int)dto.categoryId;

			if (dto.image != null && dto.image.url != null && dto.image.url.equals("") == false)
			{
				image = new ImageAd(dto.image.id, Constants.PHOTO_URL_PREFIX + dto.image.url, null);
			}

			if (dto.imageThumbnail != null && dto.imageThumbnail.url != null && dto.imageThumbnail.url.equals("") == false)
			{
				imageThumbnail = new ImageAd(dto.image.id, Constants.PHOTO_URL_PREFIX + dto.imageThumbnail.url, null);
			}

			Location loc = new Location("gps");
			loc.setLatitude(dto.latitude);
			loc.setLongitude(dto.longitude);
			Location = GeoLocation.LocToGeo(loc);

			if (dto.createdAt != null)
				PublicDate = new Date(Long.parseLong(dto.createdAt));

			//are counting their own variables
			CalcPostTime();
			KMeters = VeneficaApplication.myLocation.distanceTo(GeoLocation.GeoToLoc(Location)) / 1000.0f;

			owner = dto.owner;
			inBookmars = dto.inBookmars;
			expiresAt = dto.expiresAt;
			wanted = dto.wanted;
			numViews = dto.numViews;
			creator = dto.creator;
			expired = dto.expired;
			numAvailProlongations = dto.numAvailProlongations;
			canMarkAsSpam = dto.canMarkAsSpam;
			rating = dto.rating;
			canRate = dto.canRate;

			images = new ArrayList<ImageAd>();
			if (dto.images != null)
			{
				for (ImageDto it : dto.images)
				{
					images.add(new ImageAd(it.id, Constants.PHOTO_URL_PREFIX + it.url, null));
				}
			}
		}
		catch (Exception e)
		{
			Log.d("Product(adDto dto) Exception:", e.getLocalizedMessage());
		}
*/
	}

	public void CalcPostTime()
	{
		if (PublicDate == null)
			return;

		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		int Day = cl.get(Calendar.DAY_OF_YEAR);
		int Week = cl.get(Calendar.WEEK_OF_YEAR);
		int Year = cl.get(Calendar.YEAR);

		cl.setTime(PublicDate);
		if (Year != cl.get(Calendar.YEAR))
		{
			postTime = PostTime.Last;
		}
		else if (Week != cl.get(Calendar.WEEK_OF_YEAR))
		{
			postTime = PostTime.Last;
		}
		else if (Day != cl.get(Calendar.DAY_OF_YEAR))
		{
			postTime = PostTime.Week;
		}
		else
		{
			postTime = PostTime.Day;
		}
	}
}
