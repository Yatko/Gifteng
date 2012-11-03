package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

import com.google.android.maps.MapView;
import com.venefica.activity.R;

public class EditAdLocationSkinDef extends EditAdLocationTemplate
{
	public EditAdLocationSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.edit_ad_locate);
			
			btnMyLocation = (Button)mActivity.findViewById(R.id.btnMyLocation);
			Map = (MapView)mActivity.findViewById(R.id.MyMapView);
			post_locate_instructions = (LinearLayout)mActivity.findViewById(R.id.post_locate_instructions);
			lblAddress = (TextView)mActivity.findViewById(R.id.lblAddress);
		}
		catch (Exception e)
		{
			Log.d("EditAdLocationSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
