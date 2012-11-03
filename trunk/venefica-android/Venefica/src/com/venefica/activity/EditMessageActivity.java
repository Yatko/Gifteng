package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.venefica.activity.R;
import com.venefica.services.CommentDto;
import com.venefica.services.MessageDto;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.UpdateCommentContext;
import com.venefica.services.AsyncServices.UpdateMessageContext;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.UpdateCommentResult;
import com.venefica.services.ServicesManager.UpdateMessageResult;
import com.venefica.skining.*;
import com.venefica.utils.VeneficaApplication;

public class EditMessageActivity extends ActivityEx
{
	public static final String MESSAGE_ID = "MessageId";
	public static final String MESSAGE_TEXT = "MessageText";
	public static final String IS_MESSAGE = "IsMessage";

	EditMessageTemplate T;
	long id;
	String msg;
	/** true - edited MessageDto, false - edited CommentDto */
	boolean isMessage;

	public final Runnable FinishResultRunnable = new Runnable()
	{
		public void run()
		{
			Intent intent = new Intent();
			intent.putExtra("id", id);
			setResult(RESULT_OK, intent);

			finish();
		}
	};

	private final OnClickListener editCommentClick = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			if (T.editNewMessage.getText().length() > 0)
			{
				CommentDto comment = new CommentDto();
				comment.id = id;
				comment.text = T.editNewMessage.getText().toString();

				ShowLoadingDialog();
				VeneficaApplication.asyncServices.UpdateComment(new UpdateCommentContext(comment, new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();

						UpdateCommentResult ret = (UpdateCommentResult)result;

						switch (ret.SoapResult)
						{
							case Ok:
							{
								Intent intent = new Intent();
								intent.putExtra("id", id);
								setResult(RESULT_OK, intent);

								FinishResultRunnable.run();
								break;
							}

							case Fault:
							{
								ShowInfoDialog(GetStringResource(R.string.update_comment_error));
								Log.d("EditMessageActivity.Callback Warning", "SoapProblem");
								break;
							}

							case SoapProblem:
							{
								Log.d("EditMessageActivity.Callback Warning", "SoapProblem");
								break;
							}
						}

						return CallbackReturn.Ok;
					}
				}));
			}
			else
			{
				ShowInfoDialog(GetStringResource(R.string.short_message));
			}
		}
	};
	
	private final OnClickListener editMessageClick = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			if (T.editNewMessage.getText().length() > 0)
			{
				MessageDto message = new MessageDto();
				message.id = id;
				message.text = T.editNewMessage.getText().toString();

				ShowLoadingDialog();
				VeneficaApplication.asyncServices.UpdateMessage(new UpdateMessageContext(message, new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();

						UpdateMessageResult ret = (UpdateMessageResult)result;

						switch (ret.SoapResult)
						{
							case Ok:
							{
								Intent intent = new Intent();
								intent.putExtra("id", id);
								setResult(RESULT_OK, intent);

								FinishResultRunnable.run();
								break;
							}

							case Fault:
							{
								ShowInfoDialog(GetStringResource(R.string.update_message_error));
								Log.d("EditMessageActivity.Callback Warning", "SoapProblem");
								break;
							}

							case SoapProblem:
							{
								Log.d("EditMessageActivity.Callback Warning", "SoapProblem");
								break;
							}
						}

						return CallbackReturn.Ok;
					}
				}));
			}
			else
			{
				ShowInfoDialog(GetStringResource(R.string.short_message));
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new EditMessageSkinDef(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			id = extras.getLong(MESSAGE_ID);
			msg = extras.getString(MESSAGE_TEXT);
			isMessage = extras.getBoolean(IS_MESSAGE, false);
			if (id != 0 || msg != null)
			{
				T.editOldMessage.setText(msg);
				T.editNewMessage.setText(msg);
			}
			else
			{
				ShowInfoDialog("id == 0 || msg == null", FinishRunnable);
			}
		}
		else
		{
			ShowInfoDialog("extras ==null", FinishRunnable);
		}

		T.btnUpdateMessage.setOnClickListener(isMessage ? editMessageClick : editCommentClick);
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
}
