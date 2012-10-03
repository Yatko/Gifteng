package com.venefica.utils;

import com.venefica.activity.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

public class CustomLoadingDialog extends Dialog
{
	public CustomLoadingDialog(Activity activity)
	{
		super(activity);
		
		getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.custom_loading_dialog);
	}

}
