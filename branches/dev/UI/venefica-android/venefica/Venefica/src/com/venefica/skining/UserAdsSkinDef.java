package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;

import com.venefica.activity.R;

public class UserAdsSkinDef extends UserAdsTemplate
{	
	public UserAdsSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.user_ads_layout);
			
			btnActive = (Button)mActivity.findViewById(R.id.btnActive);
			btnExpired = (Button)mActivity.findViewById(R.id.btnExpired);
			listItem = (ListView)mActivity.findViewById(R.id.listItem);
			
		}
		catch (Exception e)
		{
			Log.d("UserAdsSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
