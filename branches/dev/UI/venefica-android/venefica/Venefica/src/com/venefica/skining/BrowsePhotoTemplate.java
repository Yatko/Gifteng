package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BrowsePhotoTemplate extends ActivityTemplate
{
	public Gallery Gallery;
	public Button btnLeft;
	public Button btnRight;
	public TextView lblTitle;
	public TextView lblDescription;
	public BrowseHeaderTemplate HeaderTap;
	public RelativeLayout filterLayout;

	public BrowsePhotoTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (Gallery == null)
			Gallery = new Gallery(mActivity);

		if (btnLeft == null)
			btnLeft = new Button(mActivity);

		if (btnRight == null)
			btnRight = new Button(mActivity);

		if (lblTitle == null)
			lblTitle = new TextView(mActivity);

		if (lblDescription == null)
			lblDescription = new TextView(mActivity);

		if (HeaderTap == null)
			HeaderTap = new BrowseHeaderSkinDef(mActivity);

		if (filterLayout == null)
			filterLayout = new RelativeLayout(mActivity);
	}
}
