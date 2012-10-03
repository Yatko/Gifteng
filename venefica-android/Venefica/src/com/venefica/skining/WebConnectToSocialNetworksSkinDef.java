package com.venefica.skining;

import android.util.Log;
import android.webkit.WebView;
import android.app.Activity;

import com.venefica.activity.R;

public class WebConnectToSocialNetworksSkinDef extends WebConnectToSocialNetworksTemplate
{
	public WebConnectToSocialNetworksSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.web_connect_to_social_network);

			Browser = (WebView)mActivity.findViewById(R.id.Browser);
		}
		catch (Exception e)
		{
			Log.d("WebConnectToSocialNetworksSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
