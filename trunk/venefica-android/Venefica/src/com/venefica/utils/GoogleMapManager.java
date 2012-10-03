package com.venefica.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.*;

public class GoogleMapManager
{
	MapView Map;
	MapController Controller;
	List<Overlay> Overlay;

	public List<Overlay> GetOverlay()
	{
		return Overlay;
	}

	public void AddOverlay(Overlay item)
	{
		GetOverlay().add(item);
	}

	public GoogleMapManager(MapView map)
	{
		this.Map = map;
		Controller = map.getController();
		Overlay = map.getOverlays();
	}

	public void GoToLocation(GeoPoint point, int zoom)
	{
		Controller.animateTo(point);
		Controller.setZoom(zoom);
		Map.invalidate();
	}

	public void GoToLocation(Location location, int zoom)
	{
		Controller.animateTo(GeoLocation.LocToGeo(location));
		Controller.setZoom(zoom);
	}

	public static class MyLocationPinPoint extends ItemizedOverlay<OverlayItem>
	{
		ArrayList<OverlayItem> Overlays = new ArrayList<OverlayItem>();
		Context Context;

		public MyLocationPinPoint(Drawable defaultMarker, Context context)
		{
			super(boundCenterBottom(defaultMarker));
			this.Context = context;
		}

		public int addOverlay(OverlayItem overlay)
		{
			Overlays.add(overlay);
			populate();
			return Overlays.size() - 1;
		}

		@Override
		protected OverlayItem createItem(int i)
		{
			return Overlays.get(i);
		}

		@Override
		public int size()
		{
			return Overlays.size();
		}

		protected boolean onTap(int index)
		{
			Log.d("onTap", "index: " + index);
			return true;
		}
	}
}
