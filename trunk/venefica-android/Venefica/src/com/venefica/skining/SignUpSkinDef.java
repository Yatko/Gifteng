package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class SignUpSkinDef extends SignUpTemplate
{
	public SignUpSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.sign_up);

			imgAvatar = (ImageView)mActivity.findViewById(R.id.imgAvatar);
			btnAvatar = (Button)mActivity.findViewById(R.id.btnAvatar);
			imgAvailable = (ImageView)mActivity.findViewById(R.id.checkboxAvailable);
			lblAvailable = (TextView)mActivity.findViewById(R.id.lblAvailable);
			btnSignUp = (Button)mActivity.findViewById(R.id.btnSignIn);

			editLogin = (EditText)mActivity.findViewById(R.id.editLogin);
			editPass = (EditText)mActivity.findViewById(R.id.editPass);
			editEmail = (EditText)mActivity.findViewById(R.id.editEmail);
			editPhone = (EditText)mActivity.findViewById(R.id.editPhone);
			editFirstName = (EditText)mActivity.findViewById(R.id.editFirstName);
			editLastName = (EditText)mActivity.findViewById(R.id.editLastName);
			editZip = (EditText)mActivity.findViewById(R.id.editZip);
			editCountry = (EditText)mActivity.findViewById(R.id.editCountry);
			editCity = (EditText)mActivity.findViewById(R.id.editCity);
			editArea = (EditText)mActivity.findViewById(R.id.editArea);
			btnBirth = (Button)mActivity.findViewById(R.id.btnBirth);
			cbBusinessAccount = (CheckBox)mActivity.findViewById(R.id.cbBusinessAccount);

			lblLoginValid = (TextView)mActivity.findViewById(R.id.lblLoginValid);
			lblPassValid = (TextView)mActivity.findViewById(R.id.lblPassValid);
			lblEmailValid = (TextView)mActivity.findViewById(R.id.lblEmailValid);
			lblPhoneValid = (TextView)mActivity.findViewById(R.id.lblPhoneValid);
			lblFirstNameValid = (TextView)mActivity.findViewById(R.id.lblFirstNameValid);
			lblLastNameValid = (TextView)mActivity.findViewById(R.id.lblLastNameValid);
			lblZipValid = (TextView)mActivity.findViewById(R.id.lblZipValid);
			lblCountryValid = (TextView)mActivity.findViewById(R.id.lblCountryValid);
			lblCityValid = (TextView)mActivity.findViewById(R.id.lblCityValid);
			lblAreaValid = (TextView)mActivity.findViewById(R.id.lblAreaValid);

			LoginRow = mActivity.findViewById(R.id.row2);
			PassRow = mActivity.findViewById(R.id.row3);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
		}
		catch (Exception e)
		{
			Log.d("CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
