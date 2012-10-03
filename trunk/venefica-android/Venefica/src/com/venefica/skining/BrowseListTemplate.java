package com.venefica.skining;

import android.app.Activity;
import android.widget.ListView;
import android.widget.RelativeLayout;

public abstract class BrowseListTemplate extends ActivityTemplate
{
	public ListView listItem;
	public BrowseHeaderTemplate HeaderTap;
	public RelativeLayout filterLayout;

	public BrowseListTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (listItem == null)
			listItem = new ListView(mActivity);

		if (HeaderTap == null)
			HeaderTap = new BrowseHeaderSkinDef(mActivity);

		if (filterLayout == null)
			filterLayout = new RelativeLayout(mActivity);
	}
}
