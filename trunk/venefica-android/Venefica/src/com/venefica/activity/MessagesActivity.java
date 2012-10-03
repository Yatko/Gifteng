package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.venefica.activity.R;
import com.venefica.services.MessageDto;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetAllMessagesContext;
import com.venefica.services.AsyncServices.HideMessageContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.SendMessageToContext;
import com.venefica.services.ServicesManager.GetAllMessagesResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.*;
import com.venefica.utils.MessageListAdapter;
import com.venefica.utils.MyApp;

public class MessagesActivity extends ActivityEx
{
	MessageTemplate T;
	private boolean showInbox;
	private MessageListAdapter adapterInbox;
	private MessageListAdapter adapterSent;
	private MessageDto selectedMessage;
	private String sendMessageStr;

	private final Runnable updateListRunnable = new Runnable()
	{
		public void run()
		{
			updateList();
		}
	};

	private final Runnable deleteMessage = new Runnable()
	{
		public void run()
		{
			ShowLoadingDialog();

			MyApp.AsyncServices.HideMessage(new HideMessageContext(selectedMessage.id, new ICallback()
			{
				public CallbackReturn Callback(IResult<?> result)
				{
					HideLoadingDialog();

					if (result.SoapResult == SoapRequestResult.Ok)
					{
						ShowInfoDialog(GetStringResource(R.string.delete_message_successful), updateListRunnable);
					}
					else
					{
						ShowInfoDialog(GetStringResource(R.string.delete_message_error));
					}
					return CallbackReturn.Ok;
				}
			}));
		}
	};

	private final Runnable sendMessage = new Runnable()
	{
		public void run()
		{
			final Dialog dialog = new Dialog(MessagesActivity.this);
			dialog.getWindow().setFlags(Window.FEATURE_NO_TITLE, 1);
			final View view = LayoutInflater.from(MessagesActivity.this).inflate(R.layout.send_message_dialog, null);
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
						msg.toName = selectedMessage.fromName;

						MyApp.AsyncServices.SendMessageTo(new SendMessageToContext(msg, new ICallback()
						{
							public CallbackReturn Callback(IResult<?> result)
							{
								HideLoadingDialog();

								if (result.SoapResult == SoapRequestResult.Ok)
								{
									ShowInfoDialog(GetStringResource(R.string.send_message_successful), updateListRunnable);
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

	private final Runnable hideMessage = new Runnable()
	{
		public void run()
		{
			ShowLoadingDialog();

			MyApp.AsyncServices.HideMessage(new HideMessageContext(selectedMessage.id, new ICallback()
			{
				public CallbackReturn Callback(IResult<?> result)
				{
					HideLoadingDialog();

					if (result.SoapResult == SoapRequestResult.Ok)
					{
						ShowInfoDialog(GetStringResource(R.string.hide_message_successful), updateListRunnable);
					}
					else
					{
						ShowInfoDialog(GetStringResource(R.string.hide_message_error));
					}
					return CallbackReturn.Ok;
				}
			}));
		}
	};
	
	protected Runnable editMessage = new Runnable()
	{
		public void run()
		{
			goToEditComment(selectedMessage.id, selectedMessage.text);
		}
	};

	private final OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
		{
			selectedMessage = (MessageDto)paramAdapterView.getItemAtPosition(paramInt);

			if (selectedMessage.owner)
				ShowQuestionDialog(GetStringResource(R.string.select_action), editMessage, deleteMessage, hideMessage, GetStringResource(R.string.edit), GetStringResource(R.string.delete), GetStringResource(R.string.hide));
			else
				ShowQuestionDialog(GetStringResource(R.string.select_action), sendMessage, deleteMessage, hideMessage, GetStringResource(R.string.send_to), GetStringResource(R.string.delete), GetStringResource(R.string.hide));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new MessageSkinDef(this);

		showInbox = true;
		T.btnInbox.setSelected(true);
		T.btnSent.setSelected(false);

		T.btnInbox.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				T.btnInbox.setSelected(true);
				T.btnSent.setSelected(false);
				showInbox = true;
				updateUi();
			}
		});

		T.btnSent.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				T.btnInbox.setSelected(false);
				T.btnSent.setSelected(true);
				showInbox = false;
				updateUi();
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		TabMainActivity.ShowTabs();

		updateList();
	}

	private void updateList()
	{
		ShowLoadingDialog();
		MyApp.AsyncServices.GetAllMessages(new GetAllMessagesContext(new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				GetAllMessagesResult ret = (GetAllMessagesResult)result;

				switch (ret.SoapResult)
				{
					case Ok:
					{
						final List<MessageDto> inbox = new ArrayList<MessageDto>();
						final List<MessageDto> sent = new ArrayList<MessageDto>();

						adapterInbox = new MessageListAdapter(MessagesActivity.this, inbox);
						adapterSent = new MessageListAdapter(MessagesActivity.this, sent);

						for (MessageDto msg : ret.Return)
						{
							if (msg.owner)
								adapterSent.add(msg);
							else
								adapterInbox.add(msg);
						}

						updateUi();
					}
						break;

					case Fault:
						Log.d("MessagesActivity.Callback Warning", "Fault");
						ShowInfoDialog(GetStringResource(R.string.get_messages_error), FinishRunnable);
						break;

					case SoapProblem:
						Log.d("MessagesActivity.Callback Warning", "SoapProblem");
						ShowInfoDialog(GetStringResource(R.string.soap_problem));
						break;
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	private void updateUi()
	{
		if (showInbox)
			T.listMessage.setAdapter(adapterInbox);
		else
			T.listMessage.setAdapter(adapterSent);

		T.listMessage.setOnItemClickListener(itemClickListener);

		if (adapterInbox != null && adapterSent != null)
		{
			adapterInbox.notifyDataSetChanged();
			adapterSent.notifyDataSetChanged();
		}
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

	protected void goToEditComment(long messageId, String Message)
	{
		Intent intent = new Intent(this, EditMessageActivity.class);
		intent.putExtra(EditMessageActivity.MESSAGE_ID, messageId);
		intent.putExtra(EditMessageActivity.MESSAGE_TEXT, Message);
		intent.putExtra(EditMessageActivity.IS_MESSAGE, true);
		startActivityForResult(intent, 1);
	}
}
