package com.venefica.skining;

import android.app.Activity;
import android.widget.ListView;

public abstract class BookmarksTemplate extends ActivityTemplate
{
	public ListView listBookmarks;

	public BookmarksTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (listBookmarks == null)
			listBookmarks = new ListView(mActivity);
	}
}
