package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.venefica.activity.R;
import com.venefica.market.Category;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.skining.*;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;
import com.venefica.utils.ProductPhotoAdapter;

public class BrowsePhotoActivity extends ActivityEx
{
	BrowsePhotoTemplate T;
	ProductPhotoAdapter itemAdapter;
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new BrowsePhotoSkinDef(this);
		TabMainActivity.ShowTabs();

		T.HeaderTap.SetOnClickToMap(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				GoToMap();
			}
		});
		T.HeaderTap.SetOnClickToList(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				GoToList();
			}
		});

		T.Gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong)
			{
				UpdateUIFromProduct(position);
			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
			}
		});

		T.Gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				Product item = (Product)arg0.getItemAtPosition(position);
				if (item != null)
					GoToPreview(item.Id);
			}
		});

		T.btnLeft.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				int currentPos = T.Gallery.getSelectedItemPosition();
				if (currentPos != 0)
					T.Gallery.setSelection(currentPos - 1, true);
			}
		});

		T.btnRight.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				int currentPos = T.Gallery.getSelectedItemPosition();
				if (currentPos != T.Gallery.getCount() - 1)
					T.Gallery.setSelection(currentPos + 1, true);
			}
		});

		itemAdapter = new ProductPhotoAdapter(this, R.layout.gallery_product_row);

		T.Gallery.setAdapter(itemAdapter);
		itemAdapter.setUpdateListCallback(new Runnable()
		{
			public void run()
			{
				UpdateUIFromProduct(T.Gallery.getSelectedItemPosition());
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

	void GoToMap()
	{
		final Intent intent = new Intent(this, BrowseMapActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	void GoToList()
	{
		final Intent intent = new Intent(this, BrowseListActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	public void GoToPreview(long productId)
	{
		final Intent intent = new Intent(this, TabProductViewActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		startActivity(intent);
	}

	void UpdateUIFromProduct(int CurrentPosition)
	{
		if (CurrentPosition == AdapterView.INVALID_POSITION || CurrentPosition > T.Gallery.getCount())
			return;

		Product product = MarketEx.getInstance().GetProducts().get(CurrentPosition);

		T.lblTitle.setText(product.Title);

		float dist = product.KMeters;
		String unit;
		if (MyApp.user.useMiles)
		{
			dist = dist * 0.621371192f; //мили
			unit = GetStringResource(R.string.miles);
		}
		else
		{
			unit = GetStringResource(R.string.km);
		}

		StringBuffer desc = new StringBuffer();
		desc.append(Category.GetCategoryById(product.Category).Desc);
		desc.append(" | " + product.Price);
		desc.append(" | " + String.format("%.2f %s", dist, unit));
		T.lblDescription.setText(desc);

		T.btnLeft.setVisibility(View.VISIBLE);
		T.btnRight.setVisibility(View.VISIBLE);

		if (CurrentPosition == 0)
		{
			T.btnLeft.setVisibility(View.INVISIBLE);
		}
		if (CurrentPosition == MarketEx.getInstance().GetProducts().size() - 1)
		{
			T.btnRight.setVisibility(View.INVISIBLE);
		}
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
}
