package com.venefica.activity;

import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.venefica.activity.R;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ChangePasswordContext;
import com.venefica.services.AsyncServices.GetUserContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.ChangePasswordResult;
import com.venefica.services.ServicesManager.GetUserResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.AccauntSkinDef;
import com.venefica.skining.AccauntTemplate;
import com.venefica.utils.VeneficaApplication;

public class AccauntActivity extends ActivityEx
{
	/** The form template */
	private AccauntTemplate T;
	private String oldPass = "";
	private String newPass = "";

	private final ICallback GetUserCallback = new ICallback()
	{
		public CallbackReturn Callback(final IResult<?> result)
		{
			HideLoadingDialog();

			if (result.SoapResult != SoapRequestResult.Ok)
			{
				ShowInfoDialog(GetStringResource(R.string.get_user_error), FinishRunnable);
			}
			else
			{
				GetUserResult ret = (GetUserResult)result;
				VeneficaApplication.user = ret.Return;

				UpdateUserInfo();
			}

			return CallbackReturn.Ok;
		}
	};

	final Runnable ShowChangePasswordDialogRunnable = new Runnable()
	{
		public void run()
		{
			ShowChangePasswordDialog();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new AccauntSkinDef(this);

		UpdateUserInfo();		
		
		T.btnMyAds.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				Intent intent = new Intent(AccauntActivity.this, UserAdsActivity.class);
				startActivity(intent);
			}
		});
		
		T.btnBookmarks.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				Intent intent = new Intent(AccauntActivity.this, BookmarksActivity.class);
				startActivity(intent);
			}
		});
		
		T.btnEditAccaunt.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				Intent intent = new Intent(AccauntActivity.this, SignUpActivity.class);
				intent.putExtra(SignUpActivity.EDIT_USER_MODE, true);
				startActivity(intent);
			}
		});

		T.btnChangesPassword.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				ShowChangePasswordDialog();
			}
		});
		
		T.btnSettings.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				Intent intent = new Intent(AccauntActivity.this, AccauntSettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	protected void ShowChangePasswordDialog()
	{
		final View changesPassView = LayoutInflater.from(AccauntActivity.this).inflate(R.layout.chage_password_dialog, null);
		final EditText editOldPass = (EditText)changesPassView.findViewById(R.id.editOldPass);
		final EditText editNewPass = (EditText)changesPassView.findViewById(R.id.editNewPass);
		final EditText editConfirmPass = (EditText)changesPassView.findViewById(R.id.editConfirmPass);

		editOldPass.setText(oldPass);
		editNewPass.setText(newPass);
		editConfirmPass.setText(newPass);

		AlertDialog dialog = new AlertDialog.Builder(AccauntActivity.this).setTitle(R.string.changes_password).setView(changesPassView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				final Pattern regPassword = Pattern.compile("[-0-9a-zA-Z_]{6,32}");

				oldPass = editOldPass.getText().toString();
				newPass = editNewPass.getText().toString();

				if (editNewPass.getText().toString().equals(editConfirmPass.getText().toString()) == false)
				{
					ShowInfoDialog(GetStringResource(R.string.possword_and_confirm_not_equals), ShowChangePasswordDialogRunnable);
				}
				else if (regPassword.matcher(editNewPass.getText().toString()).matches() == false)
				{
					ShowInfoDialog(GetStringResource(R.string.password_pattern_hint), ShowChangePasswordDialogRunnable);
				}
				else
				{
					dialog.dismiss();
					ShowLoadingDialog();

					VeneficaApplication.asyncServices.ChangePassword(new ChangePasswordContext(oldPass, newPass, new ICallback()
					{
						public CallbackReturn Callback(IResult<?> result)
						{
							HideLoadingDialog();

							ChangePasswordResult res = (ChangePasswordResult)result;

							switch (res.SoapResult)
							{
								case Ok:
									oldPass = "";
									newPass = "";
									ShowInfoDialog(GetStringResource(R.string.changes_password_successful));
									break;

								case Fault:
									ShowInfoDialog(GetStringResource(R.string.changes_password_error), ShowChangePasswordDialogRunnable);
									break;

								case SoapProblem:
									ShowInfoDialog(GetStringResource(R.string.soap_problem), ShowChangePasswordDialogRunnable);
									break;
							}

							return CallbackReturn.Ok;
						}
					}));
				}

			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.dismiss();
			}
		}).setCancelable(false).create();

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
	}
	/** Get data about the user and update View  */
	protected void UpdateUserInfo()
	{
		if (VeneficaApplication.user == null)
		{
			ShowLoadingDialog();
			VeneficaApplication.asyncServices.GetUser(new GetUserContext(GetUserCallback));
		}
		else
		{
			T.lblLogin.setText(VeneficaApplication.user.getName());
			T.lblEmail.setText(VeneficaApplication.user.getEmail());

			if (VeneficaApplication.user.getAvatarUrl() != null && VeneficaApplication.user.getAvatarUrl().length() > 0)
			{
//				VeneficaApplication.ImgLoader.displayImage(VeneficaApplication.user.avatarUrl, T.imgAvatar, VeneficaApplication.ImgLoaderOptions);
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		TabMainActivity.ShowTabs();

		UpdateUserInfo();
		
		oldPass = "";
		newPass = "";
	}

	@Override
	public void onBackPressed()
	{
		Object parent = getParent();
		if (parent == null)
		{
			finish();
		}
		else if (parent instanceof TabMainActivity)
		{
			((TabMainActivity)parent).GoToPrevious();
		}
		else
		{
			finish();
		}
	}
}
