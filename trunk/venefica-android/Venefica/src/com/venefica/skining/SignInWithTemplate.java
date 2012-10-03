package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;

public abstract class SignInWithTemplate extends ActivityTemplate
{
	public Button btnFacebook;
	public Button btnTwitter;
	public Button btnVK;
	public Button btnApp;

	public SignInWithTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnFacebook == null)
			btnFacebook = new Button(mActivity);
		if (btnTwitter == null)
			btnTwitter = new Button(mActivity);
		if (btnVK == null)
			btnVK = new Button(mActivity);
		if (btnApp == null)
			btnApp = new Button(mActivity);
	}
}
