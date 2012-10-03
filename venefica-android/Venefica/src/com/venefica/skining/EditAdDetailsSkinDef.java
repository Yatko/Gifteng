package com.venefica.skining;

import android.util.Log;
import android.widget.EditText;
import android.app.Activity;

import com.venefica.activity.R;

public class EditAdDetailsSkinDef extends EditAdDetailsTemplate
{
	public EditAdDetailsSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.edit_ad_details);

			editTitle = (EditText)mActivity.findViewById(R.id.editTitle);
			editDesc = (EditText)mActivity.findViewById(R.id.editDesc);
			editPrice = (EditText)mActivity.findViewById(R.id.editPrice);
		}
		catch (Exception e)
		{
			Log.d("EditAdDetailsSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
