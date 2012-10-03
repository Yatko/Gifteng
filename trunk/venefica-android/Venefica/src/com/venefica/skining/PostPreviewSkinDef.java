package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class PostPreviewSkinDef extends PostPreviewTemplate
{
	public PostPreviewSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.post_preview_layout);

			btnPost = (Button)mActivity.findViewById(R.id.btnPost);
			imgPostPreview = (ImageView)mActivity.findViewById(R.id.imgPostPreview);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
			lblPrice = (TextView)mActivity.findViewById(R.id.lblPrice);
			lblMiles = (TextView)mActivity.findViewById(R.id.lblMiles);
			lblStaticMiles = (TextView)mActivity.findViewById(R.id.lblStaticMiles);
			lblDesc = (TextView)mActivity.findViewById(R.id.lblDesc);
			imgMapPreview = (ImageView)mActivity.findViewById(R.id.imgMapPreview);
		}
		catch (Exception e)
		{
			Log.d("PostPreviewTemplate.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
