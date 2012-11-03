package com.venefica.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.venefica.activity.R;
import com.venefica.services.MessageDto;
import com.venefica.services.User;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetUserByNameContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.SendMessageToContext;
import com.venefica.services.ServicesManager.GetUserByNameResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.UserInfoSkinDef;
import com.venefica.skining.UserInfoTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class UserInfoActivity extends ActivityEx
{
	private UserInfoTemplate T;
	private User user;
	protected String sendMessageStr;

	private final Runnable sendMessage = new Runnable()
	{
		public void run()
		{
			final Dialog dialog = new Dialog(UserInfoActivity.this);
			dialog.getWindow().setFlags(Window.FEATURE_NO_TITLE, 1);
			final View view = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.send_message_dialog, null);
			dialog.setContentView(view);

			final EditText editText = (EditText)view.findViewById(R.id.editText);
			final Button btnSend = (Button)view.findViewById(R.id.btnSend);
			final Button btnCancel = (Button)view.findViewById(R.id.btnCancel);

			if (sendMessageStr != null)
				editText.setText(sendMessageStr);

			btnCancel.setOnClickListener(new OnClickListener()
			{
				public void onClick(View paramView)
				{
					dialog.dismiss();
				}
			});

			btnSend.setOnClickListener(new OnClickListener()
			{
				public void onClick(View paramView)
				{
					sendMessageStr = editText.getText().toString();
					if (sendMessageStr.length() > 0)
					{
						ShowLoadingDialog();
						dialog.dismiss();

						MessageDto msg = new MessageDto();
						msg.text = sendMessageStr;
						msg.toName = user.getName();

						VeneficaApplication.asyncServices.SendMessageTo(new SendMessageToContext(msg, new ICallback()
						{
							public CallbackReturn Callback(IResult<?> result)
							{
								HideLoadingDialog();

								if (result.SoapResult == SoapRequestResult.Ok)
								{
									ShowInfoDialog(GetStringResource(R.string.send_message_successful));
									sendMessageStr = "";
								}
								else
								{
									ShowInfoDialog(GetStringResource(R.string.send_message_error));
								}
								return CallbackReturn.Ok;
							}
						}));
					}
				}
			});

			dialog.show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new UserInfoSkinDef(this);

		final String name = getIntent().getStringExtra(Constants.USER_NAME_PARAM_NAME);
		if (name != null)
		{
			VeneficaApplication.asyncServices.GetUserByName(new GetUserByNameContext(name, new ICallback()
			{
				public CallbackReturn Callback(IResult<?> result)
				{
					GetUserByNameResult ret = (GetUserByNameResult)result;

					if (ret.SoapResult == SoapRequestResult.Ok)
					{
						user = ret.Return;
						updateUi();
					}
					else
					{
						ShowInfoDialog(GetStringResource(R.string.get_user_error), FinishRunnable);
					}

					return CallbackReturn.Ok;
				}
			}));
		}
		else
		{
			ShowInfoDialog("Error extras == null", FinishRunnable);
			return;
		}

		T.btnSendMessage.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				sendMessage.run();
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		updateUi();
	}

	public void updateUi()
	{
		if (user == null)
			return;

		T.lblLogin.setText(user.getName());
		T.lblFName.setText(user.getFirstName());
		T.lblLName.setText(user.getLastName());
		T.lblCountry.setText(user.getCounty());

		/*if (user.avatarUrl != null && user.avatarUrl.length() > 0 && VeneficaApplication.ImgLoader != null)
		{
			VeneficaApplication.ImgLoader.displayImage(user.avatarUrl, T.imgAvatar, VeneficaApplication.ImgLoaderOptions);
		}
		else
		{*/
			T.imgAvatar.setImageResource(R.drawable.default_photo);
//		}
	}
}
