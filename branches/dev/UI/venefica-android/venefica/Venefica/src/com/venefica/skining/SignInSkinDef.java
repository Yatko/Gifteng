package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class SignInSkinDef extends SignInTemplate
{
	public SignInSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.sign_in);

			editLogin = (EditText)mActivity.findViewById(R.id.editLogin);
			editPass = (EditText)mActivity.findViewById(R.id.editPass);
			btnSignIn = (Button)mActivity.findViewById(R.id.btnSignIn);
			btnForgotPass = (Button)mActivity.findViewById(R.id.btnForgotPass);
			lblLoginValid = (TextView)mActivity.findViewById(R.id.lblLoginValid);
			lblPassValid = (TextView)mActivity.findViewById(R.id.lblPassValid);
			cbRemember = (CheckBox)mActivity.findViewById(R.id.cbRemember);
		}
		catch (Exception e)
		{
			Log.d("SignInSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
