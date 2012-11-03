package com.venefica.skining;

import android.util.Log;
import android.widget.ListView;
import android.app.Activity;

import com.venefica.activity.R;

public class BookmarksSkinDef extends BookmarksTemplate
{
	public BookmarksSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.bookmarks_layout);

			listBookmarks = (ListView)mActivity.findViewById(R.id.listBookmarks);
		}
		catch (Exception e)
		{
			Log.d("AccauntSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
