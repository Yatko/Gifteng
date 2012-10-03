package com.venefica.skining;

import android.app.Activity;
import android.widget.EditText;

public abstract class EditAdDetailsTemplate extends ActivityTemplate
{
	public EditText editTitle;
	public EditText editDesc;
	public EditText editPrice;

	public EditAdDetailsTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		 if(editTitle==null)
			 editTitle = new EditText(mActivity);
		 
		 if(editDesc==null)
			 editDesc = new EditText(mActivity);
		  
		 if(editPrice==null)
			 editPrice = new EditText(mActivity);
	}
}
