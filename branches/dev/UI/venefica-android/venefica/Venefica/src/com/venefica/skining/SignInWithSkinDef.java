package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.app.Activity;

import com.venefica.activity.R;

public class SignInWithSkinDef extends SignInWithTemplate
{
	public SignInWithSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.sign_in_with);

			btnFacebook = (Button)mActivity.findViewById(R.id.btnFacebook);
			btnTwitter = (Button)mActivity.findViewById(R.id.btnTwitter);
			btnVK = (Button)mActivity.findViewById(R.id.btnVK);
			btnApp = (Button)mActivity.findViewById(R.id.btnApp);
		}
		catch (Exception e)
		{
			Log.d("SignInWithSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
