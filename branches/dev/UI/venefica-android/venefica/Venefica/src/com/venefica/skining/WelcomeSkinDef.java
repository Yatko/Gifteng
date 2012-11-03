package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class WelcomeSkinDef extends WelcomeTemplate
{
	public WelcomeSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.welcome);

			btnSearch = (Button)mActivity.findViewById(R.id.btnSearch);
			btnBrowse = (Button)mActivity.findViewById(R.id.btnBrowse);
			btnPost = (Button)mActivity.findViewById(R.id.btnPost);
			btnSignUp = (Button)mActivity.findViewById(R.id.btnSignUp);
			btnSignIn = (Button)mActivity.findViewById(R.id.btnSignIn);
			lblBuyers = (TextView)mActivity.findViewById(R.id.lblBuyers);
			lblListings = (TextView)mActivity.findViewById(R.id.lblListings);
		}
		catch (Exception e)
		{
			Log.d("WelcomeSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
