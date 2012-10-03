package com.venefica.skining;

import android.util.Log;
import android.app.Activity;

import com.google.android.maps.MapView;
import com.venefica.activity.R;

public class ProductMapSkinDef extends ProductMapTemplate
{
	public ProductMapSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.product_map);

			MyMapView = (MapView)mActivity.findViewById(R.id.MyMapView);
		}
		catch (Exception e)
		{
			Log.d("ProductPreviewSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
