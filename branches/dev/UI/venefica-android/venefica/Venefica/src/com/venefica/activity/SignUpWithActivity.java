package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.venefica.module.user.WebAuthorizationActivity;
import com.venefica.skining.*;
import com.venefica.utils.Constants;

public class SignUpWithActivity extends ActivityLogOut
{
	SignUpWithTemplate T;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new SignUpWithSkinDef(this);

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
				GoToWebAuthorization(Constants.SIGN_IN_TWITTER_URL);
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
				GoToSignUp();
			}
		});

		T.btnSignIn.setOnClickListener(new View.OnClickListener()
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

	void GoToSignUp()
	{
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivityForResult(intent, 0);
	}

	void GoToWebAuthorization(String url)
	{
		Intent intent = new Intent(this, WebAuthorizationActivity.class);
		intent.putExtra(WebAuthorizationActivity.AUTH_URL, url);
		startActivityForResult(intent, 0);
	}
}
