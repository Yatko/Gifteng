package com.venefica.skining;

import android.app.Activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpTemplate extends ActivityTemplate
{
	public ImageView imgAvatar;
	public Button btnAvatar;
	public ImageView imgAvailable;
	public TextView lblAvailable;
	public Button btnSignUp;

	public EditText editLogin;
	public EditText editPass;
	public EditText editEmail;
	public EditText editPhone;
	public EditText editFirstName;
	public EditText editLastName;
	public EditText editZip;
	public EditText editCountry;
	public EditText editCity;
	public EditText editArea;
	public Button btnBirth;
	public CheckBox cbBusinessAccount;

	public TextView lblLoginValid;
	public TextView lblPassValid;
	public TextView lblEmailValid;
	public TextView lblPhoneValid;
	public TextView lblFirstNameValid;
	public TextView lblLastNameValid;
	public TextView lblZipValid;
	public TextView lblCountryValid;
	public TextView lblCityValid;
	public TextView lblAreaValid;

	public View LoginRow;
	public View PassRow;
	public TextView lblTitle;

	public SignUpTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void CreateWidgets()
	{
		;
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (imgAvatar == null)
			imgAvatar = new ImageView(mActivity);

		if (btnAvatar == null)
			btnAvatar = new Button(mActivity);

		if (imgAvailable == null)
			imgAvailable = new ImageView(mActivity);

		if (lblAvailable == null)
			lblAvailable = new TextView(mActivity);

		if (btnSignUp == null)
			btnSignUp = new Button(mActivity);

		if (editLogin == null)
			editLogin = new EditText(mActivity);

		if (editPass == null)
			editPass = new EditText(mActivity);

		if (editEmail == null)
			editEmail = new EditText(mActivity);

		if (editPhone == null)
			editPhone = new EditText(mActivity);

		if (editFirstName == null)
			editFirstName = new EditText(mActivity);

		if (editLastName == null)
			editLastName = new EditText(mActivity);

		if (editZip == null)
			editZip = new EditText(mActivity);
		
		if (editCountry == null)
			editCountry = new EditText(mActivity);
		
		if (editCity == null)
			editCity = new EditText(mActivity);
		
		if (editArea == null)
			editArea = new EditText(mActivity);

		if (btnBirth == null)
			btnBirth = new Button(mActivity);

		if (cbBusinessAccount == null)
			cbBusinessAccount = new CheckBox(mActivity);

		if (lblLoginValid == null)
			lblLoginValid = new TextView(mActivity);

		if (lblPassValid == null)
			lblPassValid = new TextView(mActivity);

		if (lblEmailValid == null)
			lblEmailValid = new TextView(mActivity);

		if (lblPhoneValid == null)
			lblPhoneValid = new TextView(mActivity);

		if (lblFirstNameValid == null)
			lblFirstNameValid = new TextView(mActivity);

		if (lblLastNameValid == null)
			lblLastNameValid = new TextView(mActivity);

		if (lblZipValid == null)
			lblZipValid = new TextView(mActivity);
		
		if (lblCountryValid == null)
			lblCountryValid = new TextView(mActivity);
		
		if (lblCityValid == null)
			lblCityValid = new TextView(mActivity);
		
		if (lblAreaValid == null)
			lblAreaValid = new TextView(mActivity);

		if (LoginRow == null)
			LoginRow = new View(mActivity);

		if (PassRow == null)
			PassRow = new View(mActivity);
		
		if (lblTitle == null)
			lblTitle = new TextView(mActivity);
	}

}
