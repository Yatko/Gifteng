package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.Gallery;
import android.app.Activity;

import com.venefica.activity.R;

public class EditAdShowSkinDef extends EditAdShowTemplate
{
	public EditAdShowSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.edit_ad_show);

			btnTakePhoto = (Button)mActivity.findViewById(R.id.btnTakePhoto);
			btnChoosePhoto = (Button)mActivity.findViewById(R.id.btnChoosePhoto);
			galleryImage = (Gallery)mActivity.findViewById(R.id.galleryImage);
		}
		catch (Exception e)
		{
			Log.d("EditAdShowSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
