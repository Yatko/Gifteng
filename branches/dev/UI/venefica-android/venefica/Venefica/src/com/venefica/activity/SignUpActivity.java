package com.venefica.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.venefica.activity.R;
import com.venefica.module.user.UserDto;
import com.venefica.services.ImageDto;
import com.venefica.services.User;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetUserContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.RegisterUserContext;
import com.venefica.services.AsyncServices.UpdateUserContext;
import com.venefica.services.ServicesManager.GetUserResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.RegisterUserResult;
import com.venefica.services.ServicesManager.UpdateUserResult;
import com.venefica.skining.*;
import com.venefica.utils.VeneficaApplication;
import com.venefica.utils.Utils;
@Deprecated
public class SignUpActivity extends ActivityLogOut
{
	public static final int DATE_DIALOG_ID = 1;
	public static final int AVATAR_DIALOG_ID = 2;
	public static final String USER_COMPLITE_MODE = "UserCompliteMode";
	public static final String EDIT_USER_MODE = "EditUserMode";

	SignUpTemplate T;

	SimpleDateFormat df;
	final Calendar cal = Calendar.getInstance();
	Date date = new Date(0, 0, 0);
	private final DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			date.setDate(dayOfMonth);
			date.setMonth(monthOfYear);
			date.setYear(year - 1900);

			UpdateDateBirth();
		}
	};

	/** Regex to validate fields */
	final static Pattern regLogin = Pattern.compile("[-0-9a-zA-Z_]{6,32}");
	final static Pattern regPassword = regLogin;
	final static Pattern regEmail = Pattern.compile("([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})");
	final static Pattern regPhone = Pattern.compile("[\\+0-9]{1,32}");
	final static Pattern regFName = Pattern.compile("[-_\\w]{1,50}");
	final static Pattern regLName = regFName;
	final static Pattern regZip = Pattern.compile("[-\\d]{1,32}");
	final static Pattern regCountry = Pattern.compile(".{1,50}");
	final static Pattern regCity = Pattern.compile(".{1,50}");
	final static Pattern regArea = Pattern.compile(".{1,50}");

	/**
	 * true - collection mode after missing infy social networking in some
	 * fields hides
	 */
	boolean UserCompliteMode = false;
	/**
	 * true - Edit mode profile
	 */
	boolean EditUserMode = false;
	final Runnable OnBackRunnable = new Runnable()
	{
		public void run()
		{
			onBackPressed();
		}
	};

	Bitmap AvatarImage;

	class FieldValidator
	{
		EditText field;
		Pattern pattern;
		TextView lblMessage;
		public String message;

		public FieldValidator(EditText _field, Pattern _pattern, TextView _lblMessage, String _message)
		{
			field = _field;
			pattern = _pattern;
			lblMessage = _lblMessage;
			message = _message;
		}

		public boolean Validate(boolean ChangeMessage)
		{
			boolean result = true;

			try
			{
				String text = field.getText().toString();
				if (pattern.matcher(text).matches() == false)
				{
					result = false;
				}

				if (ChangeMessage && lblMessage != null)
				{
					if (result)
					{
						HideHint();
					}
					else
					{
						ShowHint();
					}
				}
			}
			catch (Exception e)
			{
				Log.d("FieldValidator.Validate Exception:", e.getLocalizedMessage());
			}

			return result;
		}

		public void ShowHint()
		{
			lblMessage.setVisibility(View.VISIBLE);
			lblMessage.setText(message);
		}

		public void HideHint()
		{
			lblMessage.setVisibility(View.GONE);
		}
	}

	final OnFocusChangeListener OnFocusChangeEvent = new OnFocusChangeListener()
	{
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (hasFocus)
			{
				ShowHintField(v);
			}
			else
			{
				HideHintField(v);
				//ValidateField(v);
			}
		}
	};

	class CheckLoginAvailableTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
		}
	};

	class GetAdressTask extends AsyncTask<Location, Void, String[]>
	{
		@Override
		protected String[] doInBackground(Location... location)
		{
			String[] result = new String[3];
			Geocoder coder = new Geocoder(SignUpActivity.this);
			try
			{
				List<Address> ResultList = coder.getFromLocation(location[0].getLatitude(), location[0].getLongitude(), 1);
				if (ResultList.size() > 0)
				{
					result[0] = ResultList.get(0).getCountryName();
					result[1] = ResultList.get(0).getLocality();
					result[2] = ResultList.get(0).getAdminArea();
				}
			}
			catch (IOException e)
			{
				Log.d("GetAdressTask.doInBackground IOException:", e.getLocalizedMessage());
			}
			catch (Exception e)
			{
				Log.d("GetAdressTask.doInBackground Exception:", e.getLocalizedMessage());
			}

			return result;
		}

		protected void onPostExecute(String[] result)
		{
			if (result == null)
				return;

			if (result[0] != null)
				T.editCountry.setText(result[0]);
			if (result[0] != null)
				T.editCity.setText(result[1]);
			if (result[0] != null)
				T.editArea.setText(result[2]);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new SignUpSkinDef(this);

		ShowLoadingDialog();

		df = new SimpleDateFormat(GetStringResource(R.string.date_format));

		date.setDate(cal.get(Calendar.DAY_OF_MONTH));
		date.setMonth(cal.get(Calendar.MONTH));
		date.setYear(cal.get(Calendar.YEAR) - 1900);
		UpdateDateBirth();

		T.btnAvatar.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				showDialog(AVATAR_DIALOG_ID);
			}
		});

		T.btnSignUp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				if (ValidateAllField())
				{//��
					ShowLoadingDialog();

					UserDto user = new UserDto();

					user.setBusinessAcc(T.cbBusinessAccount.isChecked());
					user.setDateOfBirth(date);//T.btnBirth.getText().toString();
					user.setEmail(T.editEmail.getText().toString());
					user.setName(T.editFirstName.getText().toString());
					user.setLastName(T.editLastName.getText().toString());
					user.setName(T.editLogin.getText().toString());
					user.setPhoneNumber(T.editPhone.getText().toString());
					user.setZipCode(T.editZip.getText().toString());
					user.setCounty(T.editCountry.getText().toString());
					user.setCity(T.editCity.getText().toString());
					user.setArea(T.editArea.getText().toString());
					if (AvatarImage != null)
						user.setAvatar(new ImageDto(AvatarImage));

					if (UserCompliteMode || EditUserMode)
					{
						VeneficaApplication.asyncServices.UpdateUser(new UpdateUserContext(user, new ICallback()
						{
							public CallbackReturn Callback(IResult<?> result)
							{
								HideLoadingDialog();

								if (result instanceof UpdateUserResult)
								{
									UpdateUserResult res = (UpdateUserResult)result;

									switch (res.SoapResult)
									{
										case Ok:
											ShowInfoDialog(GetStringResource(R.string.update_user_successful), new Runnable()
											{
												public void run()
												{
													if (UserCompliteMode)
													{
														Intent intent = new Intent(SignUpActivity.this, TabMainActivity.class);
														intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivityForResult(intent, 0);
													}
													else
													{
														VeneficaApplication.user = null;
													}

													logOutAction();
													finish();
												}
											});
											break;

										case Fault:
											switch (res.Return)
											{
												case Error:
													ShowInfoDialog(GetStringResource(R.string.update_user_error));
													break;
												case DupeEmail:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_email));
													break;
												case DupeLogin:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_login));
													break;
												case DupePhone:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_phone));
													break;
											}
											break;

										case SoapProblem:
											ShowInfoDialog(GetStringResource(R.string.soap_problem));
											break;
									}

									return CallbackReturn.Ok;
								}
								else
								{
									return CallbackReturn.Error;
								}
							}
						}));
					}
					else
					{
						VeneficaApplication.asyncServices.RegisterUser(new RegisterUserContext(user, T.editPass.getText().toString(), new ICallback()
						{
							public CallbackReturn Callback(IResult<?> result)
							{
								HideLoadingDialog();

								if (result instanceof RegisterUserResult)
								{
									RegisterUserResult res = (RegisterUserResult)result;

									switch (res.SoapResult)
									{
										case Ok:
											Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
											//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivityForResult(intent, 0);

											logOutAction();
											finish();
											break;

										case Fault:
											switch (res.Return)
											{
												case Error:
													ShowInfoDialog(GetStringResource(R.string.register_error));
													break;
												case DupeEmail:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_email));
													break;
												case DupeLogin:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_login));
													break;
												case DupePhone:
													ShowInfoDialog(GetStringResource(R.string.user_dupe_phone));
													break;
											}
											break;

										case SoapProblem:
											ShowInfoDialog(GetStringResource(R.string.soap_problem));
											break;
									}

									return CallbackReturn.Ok;
								}
								else
								{
									return CallbackReturn.Error;
								}
							}
						}));
					}
				}
			}
		});

		T.btnBirth.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				showDialog(DATE_DIALOG_ID);
			}
		});

		T.editLogin.setTag(new FieldValidator(T.editLogin, regLogin, T.lblLoginValid, GetStringResource(R.string.user_pattern_hint)));
		T.editPass.setTag(new FieldValidator(T.editPass, regPassword, T.lblPassValid, GetStringResource(R.string.password_pattern_hint)));
		T.editEmail.setTag(new FieldValidator(T.editEmail, regEmail, T.lblEmailValid, GetStringResource(R.string.email_pattern_hint)));
		T.editPhone.setTag(new FieldValidator(T.editPhone, regPhone, T.lblPhoneValid, GetStringResource(R.string.phone_pattern_hint)));
		T.editFirstName.setTag(new FieldValidator(T.editFirstName, regFName, T.lblFirstNameValid, GetStringResource(R.string.standart_word_pattern_hint)));
		T.editLastName.setTag(new FieldValidator(T.editLastName, regLName, T.lblLastNameValid, GetStringResource(R.string.standart_word_pattern_hint)));
		T.editZip.setTag(new FieldValidator(T.editZip, regZip, T.lblZipValid, GetStringResource(R.string.zip_pattern_hint)));
		T.editCountry.setTag(new FieldValidator(T.editCountry, regCountry, T.lblCountryValid, GetStringResource(R.string.standart_word_pattern_hint)));
		T.editCity.setTag(new FieldValidator(T.editCity, regCity, T.lblCityValid, GetStringResource(R.string.standart_word_pattern_hint)));
		T.editArea.setTag(new FieldValidator(T.editArea, regArea, T.lblAreaValid, GetStringResource(R.string.standart_word_pattern_hint)));

		T.editLogin.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editPass.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editEmail.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editPhone.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editFirstName.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editLastName.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editZip.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editCountry.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editCity.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editArea.setOnFocusChangeListener(OnFocusChangeEvent);

		SetDefaultPhone();
		SetDefaultEmail();

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			UserCompliteMode = extras.getBoolean(USER_COMPLITE_MODE, false);
			EditUserMode = extras.getBoolean(EDIT_USER_MODE, false);

			if (UserCompliteMode || EditUserMode)
			{
				T.LoginRow.setVisibility(View.GONE);
				T.PassRow.setVisibility(View.GONE);

				if (EditUserMode)
					T.btnSignUp.setText(R.string.edit_accaunt);
				else
					T.btnSignUp.setText(R.string.update);

				if (UserCompliteMode)
					T.lblTitle.setText(GetStringResource(R.string.profile_not_complite));
				else
					T.lblTitle.setText(GetStringResource(R.string.edit_accaunt));

				VeneficaApplication.asyncServices.GetUser(new GetUserContext(new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();

						if (result instanceof GetUserResult)
						{
							GetUserResult res = (GetUserResult)result;

							switch (res.SoapResult)
							{
								case Ok:
									if (res.Return != null)
									{
										User user = res.Return;

										if (user.getAvatarUrl() != null && user.getAvatarUrl().length() > 0)
										{
//											VeneficaApplication.ImgLoader.displayImage(user.avatarUrl, T.imgAvatar, VeneficaApplication.ImgLoaderOptions);
										}

										T.editLogin.setText(user.getName());

										T.editEmail.setText(user.getEmail());

										T.editPhone.setText(user.getPhoneNumber());

										T.editFirstName.setText(user.getFirstName());
										T.editLastName.setText(user.getLastName());
										T.editZip.setText(user.getZipCode());
										T.editCountry.setText(user.getCounty());
										T.editCity.setText(user.getCity());
										T.editArea.setText(user.getArea());

										if (user.getDateOfBirth() != null)
											date = user.getDateOfBirth();
										UpdateDateBirth();
										T.cbBusinessAccount.setChecked(user.isBusinessAcc());
									}
									else
									{
										Log.d("GetUser Warning!", "res.Return == null");

										ShowInfoDialog(GetStringResource(R.string.get_user_error), OnBackRunnable);
									}
									break;

								case Fault:
									ShowInfoDialog(GetStringResource(R.string.get_user_error), OnBackRunnable);
									break;

								case SoapProblem:
									ShowInfoDialog(GetStringResource(R.string.soap_problem), OnBackRunnable);
									break;
							}

							return CallbackReturn.Ok;
						}
						else
						{
							ShowInfoDialog(GetStringResource(R.string.get_user_error), OnBackRunnable);
							return CallbackReturn.Error;
						}
					}
				}));
			}
			else
			{
				new GetAdressTask().execute(VeneficaApplication.myLocation);
				HideLoadingDialog();
			}
		}
		else
		{
			new GetAdressTask().execute(VeneficaApplication.myLocation);
			HideLoadingDialog();
		}
	}

	/**
	 * Updates the date on the form. The date value is taken from
	 * <b>this.date</b>
	 */
	protected void UpdateDateBirth()
	{
		String DateStr = df.format(date);

		if (DateStr != null)
			T.btnBirth.setText(DateStr);
	}

	protected void SetDefaultPhone()
	{
		TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String phone = tMgr.getLine1Number();
		if (phone != null)
			T.editPhone.setText(phone);
	}

	@TargetApi (8)
	protected void SetDefaultEmail()
	{
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(this).getAccounts();
		for (Account account : accounts)
		{
			if (account.name != null && emailPattern.matcher(account.name).matches())
			{
				T.editEmail.setText(account.name);
			}
		}
	}

	/**Check all fields, also checks username availability */
	protected boolean ValidateAllField()
	{
		boolean result = true;

		String login = T.editLogin.getText().toString();
		String pass = T.editPass.getText().toString();
		String email = T.editEmail.getText().toString();
		String phone = T.editPhone.getText().toString();
		String fname = T.editFirstName.getText().toString();
		String lname = T.editLastName.getText().toString();
		String zip = T.editZip.getText().toString();
		String country = T.editCountry.getText().toString();
		String city = T.editCity.getText().toString();
		String area = T.editArea.getText().toString();

		String desc = GetStringResource(R.string.incorrectly_filled_fields);
		if (!ValidateField(T.editLogin) && UserCompliteMode == false && EditUserMode == false)
		{
			Log.d("Warning", "Login(" + login + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.login) + "\"";
		}

		if (!ValidateField(T.editPass) && UserCompliteMode == false && EditUserMode == false)
		{
			Log.d("Warning", "Password(" + pass + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.password) + "\"";
		}

		if (!ValidateField(T.editEmail))
		{
			Log.d("Warning", "Email(" + email + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.email) + "\"";
		}

		if (!ValidateField(T.editPhone))
		{
			Log.d("Warning", "Phone(" + phone + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.phone) + "\"";
		}

		if (!ValidateField(T.editFirstName))
		{
			Log.d("Warning", "First name(" + fname + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.first_name) + "\"";
		}

		if (!ValidateField(T.editLastName))
		{
			Log.d("Warning", "Last name(" + lname + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.last_name) + "\"";
		}

		if (!ValidateField(T.editZip))
		{
			Log.d("Warning", "Zip(" + zip + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.zip) + "\"";
		}

		if (!ValidateField(T.editCountry))
		{
			Log.d("Warning", "Country(" + country + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.country) + "\"";
		}

		if (!ValidateField(T.editCity))
		{
			Log.d("Warning", "City(" + city + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.city) + "\"";
		}

		if (!ValidateField(T.editArea))
		{
			Log.d("Warning", "Area(" + area + ") not valid");
			result = false;
			desc += "\n\"" + GetStringResource(R.string.area) + "\"";
		}

		if (result == false)
			ShowInfoDialog(desc);

		return result;
	}

	/**
	 * @return true - field contains the correct data
	 */
	protected boolean ValidateField(View field)
	{
		boolean result = false;

		FieldValidator validator = GetValidator(field);
		if (validator == null)
		{
			result = false;
		}
		else
		{
			result = validator.Validate(true);
		}

		return result;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DATE_DIALOG_ID:
			{
				return new DatePickerDialog(this, pDateSetListener, date.getYear() + 1900, date.getMonth(), date.getDate());
			}

			case AVATAR_DIALOG_ID:
			{
				CharSequence[] ListItems = new CharSequence[3];
				ListItems[0] = GetStringResource(R.string.camera);
				ListItems[1] = GetStringResource(R.string.gallery);
				ListItems[2] = GetStringResource(R.string.cancel);

				AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
				dialog.setTitle(GetStringResource(R.string.add_photo));
				dialog.setItems(ListItems, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						if (which == 0)
						{
							Utils.StartActivityForResultGetCamera(SignUpActivity.this);
						}
						else if (which == 1)
						{
							Utils.StartActivityForResultGetGallery(SignUpActivity.this);
						}

						dialog.cancel();
					}
				});
				return dialog.create();
			}
		}
		return null;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		AvatarImage = Utils.GetImageActivityResult(this, requestCode, resultCode, data);
		if (AvatarImage != null)
			T.imgAvatar.setImageBitmap(AvatarImage);
	}

	protected void ShowHintField(View v)
	{
		FieldValidator validator = GetValidator(v);
		if (validator != null)
		{
			validator.ShowHint();
		}
	}

	protected void HideHintField(View v)
	{
		FieldValidator validator = GetValidator(v);
		if (validator != null)
		{
			validator.HideHint();
		}
	}

	protected FieldValidator GetValidator(View v)
	{
		FieldValidator result = null;

		if (v == null)
		{
			result = null;
		}
		else
		{
			Object obj = v.getTag();
			if (obj != null && obj.getClass() == FieldValidator.class)
			{
				result = (FieldValidator)obj;
			}
			else
			{
				result = null;
			}
		}

		return result;
	}
}
