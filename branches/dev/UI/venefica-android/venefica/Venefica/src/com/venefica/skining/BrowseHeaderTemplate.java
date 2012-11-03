package com.venefica.skining;

import android.app.Activity;
import android.view.View;

/**
 * Templates are not complete, at least should not be set * ContentView
 */
public abstract class BrowseHeaderTemplate extends ActivityTemplate
{
	public BrowseHeaderTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		;
	}

	public abstract void SetOnClickToList(View.OnClickListener listener);

	public abstract void SetOnClickToMap(View.OnClickListener listener);

	public abstract void SetOnClickToPhoto(View.OnClickListener listener);

	public abstract void SetOnClickToFilter(View.OnClickListener listener);

	public abstract void SetOnClickToRefresh(View.OnClickListener listener);

	public abstract String GetSearch();

	public abstract void SetSearch(String search);
}
