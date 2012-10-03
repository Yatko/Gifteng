package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public abstract class SignInTemplate extends ActivityTemplate
{
	public EditText editLogin;
	public EditText editPass;
	public Button btnSignIn;
	public Button btnForgotPass;
	public TextView lblLoginValid;
	public TextView lblPassValid;
	public CheckBox cbRemember;

	public SignInTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (editLogin == null)
			editLogin = new EditText(mActivity);
		if (editPass == null)
			editPass = new EditText(mActivity);
		if (btnSignIn == null)
			btnSignIn = new Button(mActivity);
		if (btnForgotPass == null)
			btnForgotPass = new Button(mActivity);
		if (lblLoginValid == null)
			lblLoginValid = new Button(mActivity);
		if (lblPassValid == null)
			lblPassValid = new Button(mActivity);
		if (cbRemember == null)
			cbRemember = new CheckBox(mActivity);
	}
}
