package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.venefica.skining.WelcomeSkinDef;
import com.venefica.skining.WelcomeTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;
import com.venefica.utils.PreferencesManager;

public class WelcomeActivity extends ActivityLogOut
{
	WelcomeTemplate T;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new WelcomeSkinDef(this);

		T.btnBrowse.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				;
			}
		});

		T.btnPost.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				;
			}
		});

		T.btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToSignUpWith();
			}
		});

		T.btnSignIn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				GoToSignInWith();
			}
		});
		
		ShowLoadingDialog();
		
		PreferencesManager pref = new PreferencesManager(this, Constants.PREFERENCES_STORAGE_NAME);
		String token = pref.GetString(Constants.PREFERENCES_TOKEN_NAME, "");
		long saveTime = pref.GetLong(Constants.PREFERENCES_TOKEN_SAVE_TIME_NAME, 0);
		
		if(System.currentTimeMillis() - saveTime < Constants.TOKEN_VALID_TIME && token.equals("") == false)
		{			
			HideLoadingDialog();
			
			MyApp.AuthToken  = token;
			pref.Put(Constants.PREFERENCES_TOKEN_SAVE_TIME_NAME, System.currentTimeMillis());
			pref.Commit();
			
			Intent intent = new Intent(this, TabMainActivity.class);
			startActivity(intent);
			
			logOutAction();
			finish();
		}
		
		HideLoadingDialog();
	}

	void GoToSignUpWith()
	{
		Intent intent = new Intent(this, SignUpWithActivity.class);
		startActivityForResult(intent, 0);
	}

	void GoToSignInWith()
	{
		Intent intent = new Intent(this, SignInWithActivity.class);
		startActivityForResult(intent, 0);
	}
}
