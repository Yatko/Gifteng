package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;

import com.venefica.activity.R;

public class EditMessageSkinDef extends EditMessageTemplate
{
	public EditMessageSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.edit_message);

			editOldMessage = (EditText)mActivity.findViewById(R.id.editOldMessage);
			editNewMessage = (EditText)mActivity.findViewById(R.id.editNewMessage);
			btnUpdateMessage = (Button)mActivity.findViewById(R.id.btnUpdateMessage);
		}
		catch (Exception e)
		{
			Log.d("EditMessageSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}

}
