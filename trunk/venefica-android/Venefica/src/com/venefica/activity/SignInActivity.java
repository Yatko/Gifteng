package com.venefica.activity;

import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.venefica.activity.R;
import com.venefica.services.AsyncServices.AuthenticateContext;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.AuthenticateResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.skining.*;
import com.venefica.utils.VeneficaApplication;
@Deprecated
public class SignInActivity extends ActivityLogOut
{
	SignInTemplate T;

	final static Pattern regLogin = Pattern.compile("[-0-9a-zA-Z_]{6,32}");
	final static Pattern regPassword = regLogin;

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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new SignInSkinDef(this);

		T.btnForgotPass.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				;
			}
		});

		T.btnSignIn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ValidateAllField())
				{
					ShowLoadingDialog();
					VeneficaApplication.asyncServices.Authenticate(new AuthenticateContext(T.editLogin.getText().toString(), T.editPass.getText().toString(), new ICallback()
					{
						public CallbackReturn Callback(IResult<?> result)
						{
							HideLoadingDialog();
							if (result instanceof AuthenticateResult)
							{
								AuthenticateResult res = (AuthenticateResult)result;

								switch (res.SoapResult)
								{
									case Ok:
//										VeneficaApplication.authToken = res.Return.toString();
										((VeneficaApplication)getApplication()).setAuthToken(res.Return.toString());
										GoToBrowseList();
										break;

									case Fault:
										ShowInfoDialog(GetStringResource(R.string.authenticate_error));
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
		});

		T.editLogin.setTag(new FieldValidator(T.editLogin, regLogin, T.lblLoginValid, GetStringResource(R.string.user_pattern_hint)));
		T.editPass.setTag(new FieldValidator(T.editPass, regPassword, T.lblPassValid, GetStringResource(R.string.password_pattern_hint)));

		T.editLogin.setOnFocusChangeListener(OnFocusChangeEvent);
		T.editPass.setOnFocusChangeListener(OnFocusChangeEvent);
	}

	void GoToBrowseList()
	{
		VeneficaApplication.rememberMe = T.cbRemember.isChecked();
		
		Intent intent = new Intent(SignInActivity.this, TabMainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 32768);
		startActivityForResult(intent, 0);
		
		logOutAction();
		finish();
	}

	/** Check all fields, also checks username availability */
	protected boolean ValidateAllField()
	{
		boolean result = true;

		String login = T.editLogin.getText().toString();
		String pass = T.editPass.getText().toString();

		if (!ValidateField(T.editLogin))
		{
			Log.d("Warning", "Login(" + login + ") not valid");
			result = false;
		}

		if (!ValidateField(T.editPass))
		{
			Log.d("Warning", "Password(" + pass + ") not valid");
			result = false;
		}
		return result;
	}

	/**
	 * @return true -field contains the correct data
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
