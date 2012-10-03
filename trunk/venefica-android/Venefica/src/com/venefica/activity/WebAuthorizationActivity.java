package com.venefica.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.venefica.activity.R;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.IsUserCompleteContext;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.IsUserCompleteResult;
import com.venefica.skining.WebAuthorizationSkinDef;
import com.venefica.skining.WebAuthorizationTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;

public class WebAuthorizationActivity extends ActivityLogOut
{
	public static final String URL_INTENT_NAME = "AUTH_URL";

	WebAuthorizationTemplate T;

	@SuppressLint ("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new WebAuthorizationSkinDef(this);

		String url = getIntent().getStringExtra(URL_INTENT_NAME);
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

		T.Browser.loadUrl(url);
	}

	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public void onPageFinished(WebView view, String UrlStr)
		{
			try
			{
				String AuthToken = null;
				URL url = new URL(UrlStr);

				Log.d("WebAuthorizationActivity", url.toString());

				if (url.getHost().startsWith(Constants.SERVER_URL))
				{
					String file = url.getFile();
					if (file.contains("signin/error"))
					{
						ShowInfoDialog(GetStringResource(R.string.authorization_error), FinishRunnable);
					}
					else
					{
						String Query = url.getQuery();
						if (Query != null)
						{
							String[] strArr = Query.split("[=&]");
							if (strArr != null && strArr.length > 1)
							{
								for (int i = 0; i < strArr.length; i++)
								{
									if (strArr[i].equalsIgnoreCase("token") && i + 1 < strArr.length)
									{
										AuthToken = strArr[i + 1];
										break;
									}
								}
							}
						}
					}
				}

				if (AuthToken != null)
				{
					String token = URLDecoder.decode(AuthToken);
					MyApp.SetAuthToken(token);
					GoToBrowseList();
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

	void GoToBrowseList()
	{
		T.Browser.setVisibility(View.INVISIBLE);
		ShowLoadingDialog();

		MyApp.AsyncServices.IsUserComplete(new IsUserCompleteContext(new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				if (result instanceof IsUserCompleteResult)
				{
					HideLoadingDialog();

					IsUserCompleteResult Res = (IsUserCompleteResult)result;
					if (Res.Return == false)
					{
						Intent intent = new Intent(WebAuthorizationActivity.this, SignUpActivity.class);
						intent.putExtra(SignUpActivity.USER_COMPLITE_MODE, true);
						startActivityForResult(intent, 0);
						T.Browser.setVisibility(View.VISIBLE);

						logOutAction();
						finish();
					}
					else
					{
						Intent intent = new Intent(WebAuthorizationActivity.this, TabMainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivityForResult(intent, 0);
						T.Browser.setVisibility(View.VISIBLE);

						logOutAction();
						finish();
					}
					return CallbackReturn.Ok;
				}
				else
				{
					Log.d("GoToBrowseList Alert!", "result instanceof IsUserCompleteResult fail");
					return CallbackReturn.Error;
				}
			}
		}));
	}

}
