package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class UserInfoSkinDef extends UserInfoTemplate
{
	public UserInfoSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.user_info_layout);

			imgAvatar = (ImageView)mActivity.findViewById(R.id.imgAvatar);
			lblLogin = (TextView)mActivity.findViewById(R.id.lblLogin);
			lblFName = (TextView)mActivity.findViewById(R.id.lblFName);
			lblLName = (TextView)mActivity.findViewById(R.id.lblLName);
			lblCountry = (TextView)mActivity.findViewById(R.id.lblCountry);
			btnSendMessage = (Button)mActivity.findViewById(R.id.btnSendMessage);
		}
		catch (Exception e)
		{
			Log.d("UserInfoSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
