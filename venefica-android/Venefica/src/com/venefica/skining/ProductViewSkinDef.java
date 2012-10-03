package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class ProductViewSkinDef extends ProductViewTemplate
{

	public ProductViewSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.product_view);
			
			lblAdExpired = (TextView)mActivity.findViewById(R.id.lblAdExpired);
			btnBookmark = (Button)mActivity.findViewById(R.id.btnBookmark);
			btnEditAd = (Button)mActivity.findViewById(R.id.btnEditAd);
			layoutOtherButton = (LinearLayout)mActivity.findViewById(R.id.layoutOtherButton);
			btnRelist = (Button)mActivity.findViewById(R.id.btnRelist);
			btnEndAd = (Button)mActivity.findViewById(R.id.btnEndAd);
			btnDeleteAd = (Button)mActivity.findViewById(R.id.btnDeleteAd);
			btnShare = (Button)mActivity.findViewById(R.id.btnShare);			
			btnOwner = (Button)mActivity.findViewById(R.id.btnOwner);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
			lblPrice = (TextView)mActivity.findViewById(R.id.lblPrice);
			lblMiles = (TextView)mActivity.findViewById(R.id.lblMiles);
			lblStaticMiles = (TextView)mActivity.findViewById(R.id.lblStaticMiles);
			lblDesc = (TextView)mActivity.findViewById(R.id.lblDesc);
			layoutTotalViews = (LinearLayout)mActivity.findViewById(R.id.layoutTotalViews);
			lblTotalViews = (TextView)mActivity.findViewById(R.id.lblTotalViews);
			btnSpam = (Button)mActivity.findViewById(R.id.btnSpam);
			lblRating = (TextView)mActivity.findViewById(R.id.lblRating);
			btnRating = (Button)mActivity.findViewById(R.id.btnRating);
			galleryImage = (Gallery)mActivity.findViewById(R.id.galleryImage);
		}
		catch (Exception e)
		{
			Log.d("ProductPreviewSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
