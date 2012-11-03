package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.app.Activity;

import com.venefica.activity.R;

public class SignUpWithSkinDef extends SignUpWithTemplate
{
	public SignUpWithSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.sign_up_with);

			btnFacebook = (Button)mActivity.findViewById(R.id.btnFacebook);
			btnTwitter = (Button)mActivity.findViewById(R.id.btnTwitter);
			btnVK = (Button)mActivity.findViewById(R.id.btnVK);
			btnApp = (Button)mActivity.findViewById(R.id.btnApp);
			btnSignIn = (Button)mActivity.findViewById(R.id.btnSignIn);
		}
		catch (Exception e)
		{
			Log.d("SignUpWithSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
