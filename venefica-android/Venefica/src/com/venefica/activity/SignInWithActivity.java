package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.venefica.skining.*;
import com.venefica.utils.Constants;

public class SignInWithActivity extends ActivityLogOut
{
	SignInWithTemplate T;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new SignInWithSkinDef(this);

		T.btnFacebook.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToWebAuthorization(Constants.SIGN_IN_FACEBOOK_URL);
			}
		});

		T.btnTwitter.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToWebAuthorization(Constants.SIGN_IN_TWITER_URL);
			}
		});

		T.btnVK.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToWebAuthorization(Constants.SIGN_IN_VK_URL);
			}
		});

		T.btnApp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToSignIn();
			}
		});
	}

	void GoToSignIn()
	{
		Intent intent = new Intent(this, SignInActivity.class);
		startActivityForResult(intent, 0);
	}

	void GoToWebAuthorization(String url)
	{
		Intent intent = new Intent(this, WebAuthorizationActivity.class);
		intent.putExtra(WebAuthorizationActivity.URL_INTENT_NAME, url);
		startActivityForResult(intent, 0);
	}
}
