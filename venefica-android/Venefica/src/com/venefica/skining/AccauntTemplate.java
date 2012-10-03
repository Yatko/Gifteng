package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class AccauntTemplate extends ActivityTemplate
{
	public ImageView imgAvatar;
	public TextView lblLogin;
	public TextView lblEmail;
	public Button btnMyAds;
	public Button btnBookmarks;
	public Button btnEditAccaunt;
	public Button btnChangesPassword;
	public Button btnSettings;

	public AccauntTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if(btnMyAds == null)
			btnMyAds = new Button(mActivity);
		
		if(btnBookmarks == null)
			btnBookmarks = new Button(mActivity);
		
		if(imgAvatar == null)
			imgAvatar = new ImageView(mActivity);
		
		if(lblLogin == null)
			lblLogin = new TextView(mActivity);
		
		if(lblEmail == null)
			lblEmail = new TextView(mActivity);
		
		if(btnEditAccaunt == null)
			btnEditAccaunt = new Button(mActivity);
		
		if(btnChangesPassword == null)
			btnChangesPassword = new Button(mActivity);
		
		if(btnSettings == null)
			btnSettings = new Button(mActivity);
	}
}
