package com.venefica.skining;

import com.google.android.maps.MapView;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class EditAdLocationTemplate extends ActivityTemplate
{
	public Button btnMyLocation;
	public MapView Map;
	public LinearLayout post_locate_instructions;
	public TextView lblAddress;

	public EditAdLocationTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnMyLocation == null)
			btnMyLocation = new Button(mActivity);

		if (Map == null)
			Map = new MapView(mActivity, "");

		if (post_locate_instructions == null)
			post_locate_instructions = new LinearLayout(mActivity);

		if (lblAddress == null)
			lblAddress = new TextView(mActivity);
	}
}
