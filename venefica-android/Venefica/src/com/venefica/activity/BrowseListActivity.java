package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.skining.*;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;
import com.venefica.utils.ProductListAdapter;

public class BrowseListActivity extends ActivityEx
{
	BrowseListTemplate T;
	ProductListAdapter itemAdapter;
	FilterPanel filterPanel;
	final Runnable filterCloseCallback = new Runnable()
	{
		public void run()
		{
			if (filterPanel.isChange())
			{
				MarketEx.getInstance().setFilter(filterPanel.getFilter());
				itemAdapter.notifyDataSetChanged();
				HideKeyboard();
			}
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new BrowseListSkinDef(this);

		T.HeaderTap.SetOnClickToMap(new View.OnClickListener()
		{
			public void onClick(final View v)
			{
				GoToMap();
			}
		});

		T.HeaderTap.SetOnClickToPhoto(new View.OnClickListener()
		{
			public void onClick(final View v)
			{
				GoToPhoto();
			}
		});

		T.listItem.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				Product item = (Product)arg0.getItemAtPosition(position);
				if (item != null)
				{
					GoToPreview(item.Id);
				}
			}
		});

		filterPanel = new FilterPanel(getParent(), null, T.filterLayout, filterCloseCallback);
		filterPanel.setFilter(MarketEx.getInstance().getFilter());
		T.HeaderTap.SetOnClickToFilter(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				filterPanel.show();
				HideKeyboard();
			}
		});

		T.HeaderTap.SetOnClickToRefresh(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String str = T.HeaderTap.GetSearch();
				MarketEx.getInstance().setFilterSearchString(str.length() > 0 ? str : null);
				itemAdapter.notifyDataSetChanged();
				HideKeyboard();
			}
		});

		itemAdapter = new ProductListAdapter(this, R.layout.product_row);
		T.listItem.setAdapter(itemAdapter);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		TabMainActivity.ShowTabs();
		itemAdapter.notifyDataSetChanged();

		T.HeaderTap.SetSearch(MarketEx.getInstance().getFilter().searchString);

		MarketEx.getInstance().getFilter().latitude = MyApp.MyLocation.getLatitude();
		MarketEx.getInstance().getFilter().longitude = MyApp.MyLocation.getLongitude();

		/*if (MarketEx.getInstance().GetNumProduct() == 0)
		{
			MarketEx.getInstance().UpdateProducts(new UpdateProductsCallback()
			{
				public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
				{
					itemAdapter.notifyDataSetChanged();
				}
			});
		}*/
	}

	public void GoToMap()
	{
		final Intent intent = new Intent(this, BrowseMapActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	public void GoToPhoto()
	{
		final Intent intent = new Intent(this, BrowsePhotoActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	public void GoToPreview(long productId)
	{
		final Intent intent = new Intent(BrowseListActivity.this, TabProductViewActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		startActivity(intent);
	}

	@Override
	public void onBackPressed()
	{
		if (getParent() instanceof BrowseGroupActivity)
		{
			BrowseGroupActivity parent = (BrowseGroupActivity)getParent();
			parent.onBackPressed();
		}
		return;
	}

	protected void ShowFilter()
	{

	}
}
