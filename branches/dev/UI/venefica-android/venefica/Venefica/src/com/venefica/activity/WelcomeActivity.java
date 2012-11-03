package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.venefica.skining.WelcomeSkinDef;
import com.venefica.skining.WelcomeTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;
import com.venefica.utils.PreferencesManager;
@Deprecated
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
		
		PreferencesManager pref = new PreferencesManager(this, Constants.VENEFICA_PREFERENCES);
		String token = pref.GetString(Constants.PREFERENCES_AUTH_TOKEN, "");
		long saveTime = pref.GetLong(Constants.PREFERENCES_SESSION_IN_TIME, 0);
		
		if(System.currentTimeMillis() - saveTime < Constants.SESSION_TIME_OUT && token.equals("") == false)
		{			
			HideLoadingDialog();
			
//			VeneficaApplication.authToken  = data;
			pref.Put(Constants.PREFERENCES_SESSION_IN_TIME, System.currentTimeMillis());
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
