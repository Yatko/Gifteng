package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class EditAdPreviewTemplate extends ActivityTemplate
{
	public Button btnPost;
	public TextView lblTitle;
	public TextView lblPrice;
	public TextView lblMiles;
	public TextView lblStaticMiles;
	public TextView lblDesc;
	public Gallery galleryImage;
	public ImageView imgMapPreview;

	public EditAdPreviewTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnPost == null)
			btnPost = new Button(mActivity);

		if (imgMapPreview == null)
			imgMapPreview = new ImageView(mActivity);

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

		if (galleryImage == null)
			galleryImage = new Gallery(mActivity);
	}
}
