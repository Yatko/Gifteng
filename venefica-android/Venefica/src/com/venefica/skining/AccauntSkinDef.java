package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class AccauntSkinDef extends AccauntTemplate
{
	public AccauntSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.accaunt_layout);

			imgAvatar = (ImageView)mActivity.findViewById(R.id.imgAvatar);
			lblLogin = (TextView)mActivity.findViewById(R.id.lblLogin);
			lblEmail = (TextView)mActivity.findViewById(R.id.lblEmail);
			btnMyAds = (Button)mActivity.findViewById(R.id.btnMyAds);
			btnBookmarks = (Button)mActivity.findViewById(R.id.btnBookmarks);
			btnEditAccaunt = (Button)mActivity.findViewById(R.id.btnEditAccaunt);
			btnChangesPassword = (Button)mActivity.findViewById(R.id.btnChangesPassword);
			btnSettings = (Button)mActivity.findViewById(R.id.btnSettings);
		}
		catch (Exception e)
		{
			Log.d("AccauntSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
