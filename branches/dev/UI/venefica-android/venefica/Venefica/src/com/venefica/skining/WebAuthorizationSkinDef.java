package com.venefica.skining;

import android.util.Log;
import android.webkit.WebView;
import android.app.Activity;

import com.venefica.activity.R;
@Deprecated
public class WebAuthorizationSkinDef extends WebAuthorizationTemplate
{
	public WebAuthorizationSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.web_authorization);

			Browser = (WebView)mActivity.findViewById(R.id.Browser);
		}
		catch (Exception e)
		{
			Log.d("WebAuthorizationSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
