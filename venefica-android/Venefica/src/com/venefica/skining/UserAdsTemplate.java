package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;

public abstract class UserAdsTemplate extends ActivityTemplate
{
	public Button btnActive;
	public Button btnExpired;
	public ListView listItem;
	
	public UserAdsTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if(btnActive==null)
			btnActive = new Button(mActivity);
		
		if(btnExpired==null)
			btnExpired = new Button(mActivity);
		
		if(listItem==null)
			listItem = new ListView(mActivity);
	}
}
