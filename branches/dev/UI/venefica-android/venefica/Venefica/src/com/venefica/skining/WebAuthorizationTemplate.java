package com.venefica.skining;

import android.app.Activity;
import android.webkit.WebView;

public abstract class WebAuthorizationTemplate extends ActivityTemplate
{
	public WebView Browser;


	public WebAuthorizationTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (Browser == null)
			Browser = new WebView(mActivity);
	}
}
