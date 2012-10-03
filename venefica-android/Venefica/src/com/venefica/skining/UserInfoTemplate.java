package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class UserInfoTemplate extends ActivityTemplate
{
	public ImageView imgAvatar;
	public TextView lblLogin;
	public TextView lblFName;
	public TextView lblLName;
	public TextView lblCountry;
	public Button btnSendMessage;

	public UserInfoTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (imgAvatar == null)
			imgAvatar = new ImageView(mActivity);

		if (lblLogin == null)
			lblLogin = new TextView(mActivity);

		if (lblFName == null)
			lblFName = new TextView(mActivity);
		
		if (lblLName == null)
			lblLName = new TextView(mActivity);
		
		if (lblCountry == null)
			lblCountry = new TextView(mActivity);
		
		if (btnSendMessage == null)
			btnSendMessage = new Button(mActivity);
	}
}
