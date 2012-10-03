package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class BrowsePhotoSkinDef extends BrowsePhotoTemplate
{
	public BrowsePhotoSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.browse_photo);

			Gallery = (Gallery)mActivity.findViewById(R.id.PhotoGallery);
			btnLeft = (Button)mActivity.findViewById(R.id.btnLeft);
			btnRight = (Button)mActivity.findViewById(R.id.btnRight);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
			lblDescription = (TextView)mActivity.findViewById(R.id.lblDescription);
			HeaderTap = new BrowseHeaderSkinDef(mActivity);
			filterLayout = (RelativeLayout)mActivity.findViewById(R.id.layoutFilter);
		}
		catch (Exception e)
		{
			Log.d("WelcomeSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
