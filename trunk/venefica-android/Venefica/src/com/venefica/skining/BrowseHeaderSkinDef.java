package com.venefica.skining;

import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;

import com.venefica.activity.R;

public class BrowseHeaderSkinDef extends BrowseHeaderTemplate
{
	protected Button btnList;
	protected Button btnMap;
	protected Button btnPhoto;

	protected Button btnFilter;
	protected EditText editSearch;
	protected Button btnRefresh;

	public BrowseHeaderSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			btnList = (Button)mActivity.findViewById(R.id.btnList);
			btnMap = (Button)mActivity.findViewById(R.id.btnMap);
			btnPhoto = (Button)mActivity.findViewById(R.id.btnPhoto);

			btnFilter = (Button)mActivity.findViewById(R.id.btnFilter);
			editSearch = (EditText)mActivity.findViewById(R.id.editSearch);
			btnRefresh = (Button)mActivity.findViewById(R.id.btnRefresh);

			editSearch.clearFocus();
		}
		catch (Exception e)
		{
			Log.d("BrowseMapSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}

	@Override
	public void SetOnClickToList(OnClickListener listener)
	{
		if (btnList != null && listener != null)
			btnList.setOnClickListener(listener);
	}

	@Override
	public void SetOnClickToMap(OnClickListener listener)
	{
		if (btnMap != null && listener != null)
			btnMap.setOnClickListener(listener);
	}

	@Override
	public void SetOnClickToPhoto(OnClickListener listener)
	{
		if (btnPhoto != null && listener != null)
			btnPhoto.setOnClickListener(listener);
	}

	@Override
	public void SetOnClickToFilter(OnClickListener listener)
	{
		if (btnFilter != null && listener != null)
			btnFilter.setOnClickListener(listener);
	}

	@Override
	public void SetOnClickToRefresh(OnClickListener listener)
	{
		if (btnRefresh != null && listener != null)
			btnRefresh.setOnClickListener(listener);
	}

	@Override
	public String GetSearch()
	{
		String result = "";
		if (editSearch != null)
			result = editSearch.getText().toString();

		return result;
	}

	@Override
	public void SetSearch(String search)
	{
		if (editSearch != null)
			editSearch.setText(search);
	}
}
