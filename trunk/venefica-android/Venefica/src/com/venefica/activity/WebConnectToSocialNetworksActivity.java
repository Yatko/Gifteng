package com.venefica.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.venefica.activity.R;
import com.venefica.services.ServicesManager.eSocialNetworks;
import com.venefica.skining.WebConnectToSocialNetworksSkinDef;
import com.venefica.skining.WebConnectToSocialNetworksTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;

public class WebConnectToSocialNetworksActivity extends ActivityEx
{
	public static final String SOCIAL_NETWORK = "SocialNetwork";

	WebConnectToSocialNetworksTemplate T;
	private final Map<String, String> headers = new HashMap<String, String>();

	@SuppressLint ("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new WebConnectToSocialNetworksSkinDef(this);

		eSocialNetworks network = eSocialNetworks.valueOf(getIntent().getStringExtra(SOCIAL_NETWORK));
		if (network == null)
			onBackPressed();
		else
		{
			String url = null;

			//String auth = URLEncoder.encode(MyApp.AuthToken);
			switch (network)
			{				
				case facebook:
					url = Constants.CONNECT_TO_FACEBOOK_URL;
					break;
				case twitter:
					url = Constants.CONNECT_TO_TWITER_URL;
					break;
				case vkontakte:
					url = Constants.CONNECT_TO_VK_URL;
					break;
			}

			if (url == null)
				onBackPressed();

			CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(T.Browser.getContext());
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			cookieManager.removeSessionCookie();
			cookieSyncManager.sync();

			T.Browser.getSettings().setJavaScriptEnabled(true);
			T.Browser.setWebChromeClient(new WebChromeClient());
			T.Browser.setWebViewClient(new MyWebViewClient());

			headers.put("AuthToken", MyApp.AuthToken);

			T.Browser.loadUrl(url, headers);
		}
	}

	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String urlStr)
		{
			headers.put("AuthToken", MyApp.AuthToken);
			view.loadUrl(urlStr, headers);
			
			try
			{
				URL url = new URL(urlStr);

				Log.d("WebConnectToSocialNetworksActivity", url.toString());

				if (url.getHost().startsWith(Constants.SERVER_URL))
				{
					String file = url.getFile();
					if (file.contains("connect/error"))
					{
						ShowInfoDialog(GetStringResource(R.string.connect_social_networks_error), FinishRunnable);
						return false;
					}
					else if (file.contains("connect/ok"))
					{
						ShowInfoDialog(GetStringResource(R.string.connect_social_networks_successful), FinishRunnable);
						return false;
					}
				}
			}
			catch (MalformedURLException e)
			{
				Log.d("onPageFinished MalformedURLException:", e.getLocalizedMessage());
			}
			
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String urlStr)
		{			
			try
			{
				URL url = new URL(urlStr);

				Log.d("WebConnectToSocialNetworksActivity", url.toString());

				if (url.getHost().startsWith(Constants.SERVER_URL))
				{
					String file = url.getFile();
					if (file.contains("connect/error"))
					{
						ShowInfoDialog(GetStringResource(R.string.connect_social_networks_error), FinishRunnable);
						return;
					}
					else if (file.contains("connect/ok"))
					{
						ShowInfoDialog(GetStringResource(R.string.connect_social_networks_successful), FinishRunnable);
						return;
					}
				}
			}
			catch (MalformedURLException e)
			{
				Log.d("onPageFinished MalformedURLException:", e.getLocalizedMessage());
			}
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
		{
			//TODO dirty hack, SSL is always accepted
			handler.proceed();
		}
	}
}
