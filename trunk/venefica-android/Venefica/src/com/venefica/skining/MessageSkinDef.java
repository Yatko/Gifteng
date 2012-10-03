package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class MessageSkinDef extends MessageTemplate
{
	protected TextView lblNumUnread;
	
	public MessageSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.message_layout);
			
			listMessage = (ListView)mActivity.findViewById(R.id.listMessage);
			btnInbox = (Button)mActivity.findViewById(R.id.btnInbox);
			btnSent = (Button)mActivity.findViewById(R.id.btnSent);
		}
		catch (Exception e)
		{
			Log.d("MessageSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}

	public void SetUnReadMessage(int num)
	{
		if (lblNumUnread != null)
			lblNumUnread.setText("" + num);
	}
}
