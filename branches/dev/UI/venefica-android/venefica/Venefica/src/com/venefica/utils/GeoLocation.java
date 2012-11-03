package com.venefica.utils;

import java.util.Timer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class GeoLocation
{
	LocationManager LocManager;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	Location CurrentLocation = new Location(LocationManager.GPS_PROVIDER);
	Location GPSLocation = new Location(LocationManager.GPS_PROVIDER);
	Location NetLocation = new Location(LocationManager.NETWORK_PROVIDER);
	Timer UpdateTimer;
	GeoLocationResult Callback;

	LocationListener locationListenerGps = new LocationListener()
	{
		public void onLocationChanged(Location location)
		{
			GPSLocation = location;
			UpdateCurrentLocation();
		}

		public void onProviderDisabled(String arg0)
		{

		}

		public void onProviderEnabled(String arg0)
		{

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{

		}
	};

	LocationListener locationListenerNetwork = new LocationListener()
	{
		public void onLocationChanged(Location location)
		{
			NetLocation = location;
			UpdateCurrentLocation();
		}

		public void onProviderDisabled(String arg0)
		{

		}

		public void onProviderEnabled(String arg0)
		{

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2)
		{

		}
	};

	public static GeoPoint LocToGeo(Location location)
	{
		Double latitude = location.getLatitude() * 1E6;
		Double longitude = location.getLongitude() * 1E6;

		return new GeoPoint(latitude.intValue(), longitude.intValue());
	}
	
	public static Location GeoToLoc(GeoPoint point)
	{
		Double latitude = point.getLatitudeE6() / 1E6;
		Double longitude = point.getLongitudeE6() / 1E6;

		Location result = new Location(LocationManager.GPS_PROVIDER);
		result.setLatitude(latitude);
		result.setLongitude(longitude);
		
		return result;
	}
	
	void UpdateCurrentLocation()
	{
		if ((GPSLocation != null) && (NetLocation != null))
		{
			if (GPSLocation.getTime() > NetLocation.getTime())
			{
				CurrentLocation = GPSLocation;
			}
			else
			{
				CurrentLocation = NetLocation;
			}
		}

		StopUpdate();

		if (Callback != null)
			Callback.ChangeLocation(GetLocation());

	}

	public GeoLocation(Context context, GeoLocationResult callback)
	{
		try
		{
			if (LocManager == null)
				LocManager = ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE));

			gps_enabled = LocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			network_enabled = LocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			StartUpdate();

			this.Callback = callback;
		}
		catch (Exception e)
		{
			Log.d("GetLocation Exception:", e.getLocalizedMessage());
		}
	}

	public Location GetLocation()
	{
		return CurrentLocation;
	}

	public void StartUpdate()
	{
		LocManager.removeUpdates(locationListenerGps);
		LocManager.removeUpdates(locationListenerNetwork);

		if (gps_enabled)
			LocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0F, locationListenerGps);
		if (network_enabled)
			LocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0F, locationListenerNetwork);
	}

	public void StopUpdate()
	{
		LocManager.removeUpdates(locationListenerGps);
		LocManager.removeUpdates(locationListenerNetwork);
	}
	
	public static abstract class GeoLocationResult
	{
		public abstract void ChangeLocation(Location location);
	}
}
