package com.venefica.skining;

import android.util.Log;
import android.widget.RelativeLayout;
import android.app.Activity;

import com.google.android.maps.MapView;
import com.venefica.activity.R;

public class BrowseMapSkinDef extends BrowseMapTemplate
{
	public BrowseMapSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.browse_map);

			Map = (MapView)mActivity.findViewById(R.id.MyMapView);
			HeaderTap = new BrowseHeaderSkinDef(mActivity);
			filterLayout = (RelativeLayout)mActivity.findViewById(R.id.layoutFilter);
		}
		catch (Exception e)
		{
			Log.d("BrowseMapSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
