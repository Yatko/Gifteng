package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;

public abstract class MessageTemplate extends ActivityTemplate
{
	public ListView listMessage;
	public Button btnInbox;
	public Button btnSent;
	
	public MessageTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if(listMessage==null)
			listMessage = new ListView(mActivity);
		
		if(btnInbox==null)
			btnInbox = new Button(mActivity);
		
		if(btnSent==null)
			btnSent = new Button(mActivity);
	}
}
