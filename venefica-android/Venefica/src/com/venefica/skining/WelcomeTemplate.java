package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public abstract class WelcomeTemplate extends ActivityTemplate
{
	public Button btnSearch;
	public Button btnBrowse;
	public Button btnPost;
	public Button btnSignUp;
	public Button btnSignIn;
	public TextView lblBuyers;
	public TextView lblListings;

	public WelcomeTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnSearch == null)
			btnSearch = new Button(mActivity);
		if (btnBrowse == null)
			btnBrowse = new Button(mActivity);
		if (btnPost == null)
			btnPost = new Button(mActivity);
		if (btnSignUp == null)
			btnSignUp = new Button(mActivity);
		if (btnSignIn == null)
			btnSignIn = new Button(mActivity);
		if (lblBuyers == null)
			lblBuyers = new TextView(mActivity);
		if (lblListings == null)
			lblListings = new TextView(mActivity);
	}
}
