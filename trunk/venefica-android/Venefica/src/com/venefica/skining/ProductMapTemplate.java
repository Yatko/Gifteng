package com.venefica.skining;

import com.google.android.maps.MapView;
import com.venefica.activity.R;

import android.app.Activity;
import android.util.Log;

public abstract class ProductMapTemplate extends ActivityTemplate
{
	public MapView MyMapView;

	public ProductMapTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		try
		{
			if (MyMapView == null)
				MyMapView = new MapView(mActivity, mActivity.getString(R.string.google_map_api_key));
		}
		catch (Exception e)
		{
			Log.d("ProductPreviewTemplate Exception:", e.getLocalizedMessage());
		}
	}
}
