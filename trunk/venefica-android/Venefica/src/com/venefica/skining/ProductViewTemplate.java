package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ProductViewTemplate extends ActivityTemplate
{
	public TextView lblAdExpired;
	public Button btnBookmark;
	public Button btnEditAd;
	public LinearLayout layoutOtherButton;
	public Button btnRelist;
	public Button btnEndAd;
	public Button btnDeleteAd;
	public Button btnShare;
	public Button btnOwner;
	public TextView lblTitle;
	public TextView lblPrice;
	public TextView lblMiles;
	public TextView lblStaticMiles;
	public TextView lblDesc;
	public LinearLayout layoutTotalViews;
	public TextView lblTotalViews;
	public Button btnSpam;
	public TextView lblRating;
	public Button btnRating;
	public Gallery galleryImage;

	public ProductViewTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (lblAdExpired == null)
			lblAdExpired = new TextView(mActivity);
		
		if (btnBookmark == null)
			btnBookmark = new Button(mActivity);

		if (btnEditAd == null)
			btnEditAd = new Button(mActivity);

		if (layoutOtherButton == null)
			layoutOtherButton = new LinearLayout(mActivity);

		if (btnRelist == null)
			btnRelist = new Button(mActivity);
		
		if (btnEndAd == null)
			btnEndAd = new Button(mActivity);

		if (btnDeleteAd == null)
			btnDeleteAd = new Button(mActivity);

		if (btnShare == null)
			btnShare = new Button(mActivity);

		if (btnOwner == null)
			btnOwner = new Button(mActivity);

		if (lblTitle == null)
			lblTitle = new TextView(mActivity);

		if (lblPrice == null)
			lblPrice = new TextView(mActivity);

		if (lblMiles == null)
			lblMiles = new TextView(mActivity);

		if (lblStaticMiles == null)
			lblStaticMiles = new TextView(mActivity);

		if (lblDesc == null)
			lblDesc = new TextView(mActivity);

		if (layoutTotalViews == null)
			layoutTotalViews = new LinearLayout(mActivity);

		if (lblTotalViews == null)
			lblTotalViews = new TextView(mActivity);

		if (btnSpam == null)
			btnSpam = new Button(mActivity);
		
		if (lblRating == null)
			lblRating = new TextView(mActivity);

		if (btnRating == null)
			btnRating = new Button(mActivity);
		
		if (galleryImage == null)
			galleryImage = new Gallery(mActivity);
	}
}
