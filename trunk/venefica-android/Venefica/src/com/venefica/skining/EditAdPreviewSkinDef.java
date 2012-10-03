package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class EditAdPreviewSkinDef extends EditAdPreviewTemplate
{
	public EditAdPreviewSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.edit_ad_preview);

			btnPost = (Button)mActivity.findViewById(R.id.btnPost);
			imgMapPreview = (ImageView)mActivity.findViewById(R.id.imgMapPreview);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
			lblPrice = (TextView)mActivity.findViewById(R.id.lblPrice);
			lblMiles = (TextView)mActivity.findViewById(R.id.lblMiles);
			lblStaticMiles = (TextView)mActivity.findViewById(R.id.lblStaticMiles);
			lblDesc = (TextView)mActivity.findViewById(R.id.lblDesc);
			galleryImage = (Gallery)mActivity.findViewById(R.id.galleryImage);
		}
		catch (Exception e)
		{
			Log.d("EditAdPreviewSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
