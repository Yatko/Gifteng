package com.venefica.module.user;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.venefica.module.main.R;
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Class to perform social network authentication
 */
public class WebAuthorizationActivity extends Activity {
	public static final String AUTH_URL = "auth_url";
	/**
	 * Web view to show social network auth page
	 */
	private WebView webViewBrowser;
	/**
	 * Auth data received from social network
	 */
	private String authToken = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_authorization);
		webViewBrowser = (WebView) findViewById(R.id.Browser);

		String url = getIntent().getStringExtra(AUTH_URL);
		if (url == null){
			onBackPressed();
		}

		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webViewBrowser.getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		cookieSyncManager.sync();

		webViewBrowser.getSettings().setJavaScriptEnabled(true);
		webViewBrowser.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				WebAuthorizationActivity.this.setTitle(getResources().getString(R.string.msg_progress));
				WebAuthorizationActivity.this.setProgress(newProgress * 100);
                if (newProgress == 100) {
                	WebAuthorizationActivity.this.setTitle(getResources().getString(R.string.app_name));
				}
			}
		});
		webViewBrowser.setWebViewClient(new AuthWebViewClient());
		//Load social network url
		webViewBrowser.loadUrl(url);
	}

	/**
	 * @author avinash
	 * Client class for web view.
	 */
	private class AuthWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String UrlStr) {
			try {
				String token = null;
				URL url = new URL(UrlStr);

				Log.d("WebAuthorizationActivity", url.toString());

				if (url.getHost().startsWith(Constants.SERVER_URL)) {
					String file = url.getFile();
					if (file.contains("signin/error")) {
						//Set result on failure
						setResult(Constants.ERROR_RESULT_USER_AUTH);
						finish();
					} else {
						String urlQuery = url.getQuery();
						if (urlQuery != null) {
							String[] strArr = urlQuery.split("[=&]");
							if (strArr != null && strArr.length > 1) {
								for (int i = 0; i < strArr.length; i++) {
									if (strArr[i].equalsIgnoreCase("data")
											&& i + 1 < strArr.length) {
										token = strArr[i + 1];
										break;
									}
								}
							}
						}
					}
				}

				if (token != null) {
					//Set activity result after user authorization
					authToken = URLDecoder.decode(token);
					Intent resultIntent = new Intent();
					resultIntent.putExtra(Constants.PREFERENCES_AUTH_TOKEN, authToken);
					setResult(Constants.RESULT_USER_AUTHORISED, resultIntent);
					finish();
				}
			} catch (MalformedURLException e) {
				Log.e("WebAuthorizationActivity::onPageFinished: ",e.getLocalizedMessage());
			}
		}
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			//Set result on failure
			setResult(Constants.ERROR_NETWORK_CONNECT);
			finish();
		}
	}
}
