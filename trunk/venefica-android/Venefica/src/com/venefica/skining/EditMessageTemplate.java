package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

public abstract class EditMessageTemplate extends ActivityTemplate
{
	public EditText editOldMessage;
	public EditText editNewMessage;
	public Button btnUpdateMessage;
	
	public EditMessageTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if(editOldMessage==null)
			editOldMessage = new EditText(mActivity);
		if(editNewMessage==null)
			editNewMessage = new EditText(mActivity);
		if(btnUpdateMessage==null)
			btnUpdateMessage = new Button(mActivity);
	}
}
