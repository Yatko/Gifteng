package com.venefica.skining;


import com.google.android.maps.MapView;
import com.venefica.activity.R;

import android.app.Activity;
import android.util.Log;
import android.widget.RelativeLayout;

public abstract class BrowseMapTemplate extends ActivityTemplate
{
	public MapView Map;
	public BrowseHeaderTemplate HeaderTap;
	public RelativeLayout filterLayout;

	public BrowseMapTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		try
		{
			if (Map == null)
				Map = new MapView(mActivity, mActivity.getString(R.string.google_map_api_key));
		}
		catch (Exception e)
		{
			Log.d("BrowseMapTemplate Exception:", e.getLocalizedMessage());
		}

		if (HeaderTap == null)
			HeaderTap = new BrowseHeaderSkinDef(mActivity);

		if (filterLayout == null)
			filterLayout = new RelativeLayout(mActivity);
	}
}
