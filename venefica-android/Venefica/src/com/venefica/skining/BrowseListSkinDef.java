package com.venefica.skining;

import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.app.Activity;

import com.venefica.activity.R;

public class BrowseListSkinDef extends BrowseListTemplate
{
	public BrowseListSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.browse_list);

			listItem = (ListView)mActivity.findViewById(R.id.listItem);
			HeaderTap = new BrowseHeaderSkinDef(mActivity);
			filterLayout = (RelativeLayout)mActivity.findViewById(R.id.layoutFilter);
		}
		catch (Exception e)
		{
			Log.d("BrowseMapSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
