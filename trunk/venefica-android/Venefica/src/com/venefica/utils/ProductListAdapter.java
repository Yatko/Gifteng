package com.venefica.utils;

import java.util.List;

import com.venefica.activity.R;
import com.venefica.market.Category;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.market.MarketEx.UpdateProductsCallback;
import com.venefica.market.Product.PostTime;
import com.venefica.services.ServicesManager.SoapRequestResult;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product>
{
	private List<Product> Products;
	private boolean useMarket;  

	private final Runnable UpdateNotifyRunnable = new Runnable()
	{
		public void run()
		{
			notifyDataSetChanged();
		}
	};

	class ViewHolder
	{
		public ImageView imgDayBar;
		public ImageView imgWeekBar;
		public ImageView imgLastBar;

		public ImageView imgProduct;
		public TextView lblTitle;
		public TextView lblCategory;
		public TextView lblPrice;
		public TextView lblMiles;
		public TextView lblUnitDistance;

		public View layoutProductInfo;
		public View layoutFooter;
		public View layoutDontFind;
	}

	public ProductListAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId, MarketEx.getInstance().GetProducts());
		this.Products = MarketEx.getInstance().GetProducts();
		useMarket = true;
	}
	
	public ProductListAdapter(Context context, int textViewResourceId, List<Product> products)
	{
		super(context, textViewResourceId, products);
		this.Products = products;
		useMarket = false;
	}

	@Override
	public int getCount()
	{
		int sz = Products.size();

		if (sz == 0 && useMarket)
		{
			MarketEx.getInstance().UpdateProductsList(new UpdateProductsCallback()
			{
				public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
				{
					UpdateNotifyRunnable.run();
				}
			});
		}

		return sz;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		View row = convertView;

		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater)super.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.product_row, parent, false);

			holder = new ViewHolder();

			holder.imgDayBar = (ImageView)row.findViewById(R.id.imgDayBar);
			holder.imgWeekBar = (ImageView)row.findViewById(R.id.imgWeekBar);
			holder.imgLastBar = (ImageView)row.findViewById(R.id.imgLastBar);

			holder.imgProduct = (ImageView)row.findViewById(R.id.imgProduct);
			holder.lblTitle = (TextView)row.findViewById(R.id.lblTitle);
			holder.lblCategory = (TextView)row.findViewById(R.id.lblCategory);
			holder.lblPrice = (TextView)row.findViewById(R.id.lblPrice);
			holder.lblMiles = (TextView)row.findViewById(R.id.lblMiles);
			holder.lblUnitDistance = (TextView)row.findViewById(R.id.lblUnitDistance);

			holder.layoutProductInfo = row.findViewById(R.id.layoutProductInfo);
			holder.layoutFooter = row.findViewById(R.id.layoutFooter);
			holder.layoutDontFind = row.findViewById(R.id.layoutDontFind);

			row.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)row.getTag();
		}

		if (position < Products.size() && position >= 0)
		{
			if (position == Products.size() - 1 && useMarket)
			{//last element
				if (MarketEx.getInstance().ExistLastProduct())
				{
					holder.layoutProductInfo.setVisibility(View.VISIBLE);
					holder.layoutFooter.setVisibility(View.GONE);
					holder.layoutDontFind.setVisibility(View.GONE);
				}
				else
				{
					holder.layoutProductInfo.setVisibility(View.GONE);
					holder.layoutFooter.setVisibility(View.VISIBLE);
					holder.layoutDontFind.setVisibility(View.GONE);

					MarketEx.getInstance().UpdateProductsList(new UpdateProductsCallback()
					{
						public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
						{
							UpdateNotifyRunnable.run();
						}
					});
				}
			}
			else
			{
				holder.layoutProductInfo.setVisibility(View.VISIBLE);
				holder.layoutFooter.setVisibility(View.GONE);
				holder.layoutDontFind.setVisibility(View.GONE);
			}

			Product item = Products.get(position);
			if (item != null)
			{
				switch (item.postTime)
				{
					case None:
						SetBarNone(holder);
						break;

					case Day:
						if (position == 0)
						{
							SetBarDay(holder);
						}
						else
						{
							SetBarNone(holder);
						}
						break;

					case Week:
						if (position - 1 >= 0)
						{
							if (Products.get(position - 1).postTime == PostTime.Week)
							{
								SetBarNone(holder);
							}
							else
							{
								SetBarWeek(holder);
							}
						}
						else
						{
							SetBarWeek(holder);
						}
						break;

					case Last:
						if (position - 1 >= 0)
						{
							if (Products.get(position - 1).postTime == PostTime.Last)
							{
								SetBarNone(holder);
							}
							else
							{
								SetBarLast(holder);
							}
						}
						else
						{
							SetBarLast(holder);
						}
						break;
				}

				if(item.wanted)
				{
					holder.layoutProductInfo.setBackgroundResource(R.drawable.chatslist_wanted_selector);
				}
				else
				{
					holder.layoutProductInfo.setBackgroundResource(R.drawable.chatslist_selector);
				}
				
				if (item.imageThumbnail != null && item.imageThumbnail.url != null)
				{
//					VeneficaApplication.ImgLoader.displayImage(item.imageThumbnail.url, holder.imgProduct, VeneficaApplication.ImgLoaderOptions);
				}
				else
				{
					holder.imgProduct.setImageResource(R.drawable.default_photo);
				}

				holder.lblTitle.setText(item.Title);
				holder.lblCategory.setText(Category.GetCategoryById(item.Category).Desc);
				holder.lblPrice.setText(item.Price);

				float dist = item.KMeters;
				if (VeneficaApplication.user.isUseMiles())
				{
					dist = dist * 0.621371192f;
					holder.lblUnitDistance.setText(R.string.miles);
				}
				else
				{
					holder.lblUnitDistance.setText(R.string.km);
				}

				holder.lblMiles.setText(String.format("%.2f", dist));
			}
			else
				Log.d("ProductAdapter.getView", "value == null");
		}
		else
			Log.d("ProductAdapter.getView", "position(" + position + ") out of range(0 - " + Products.size() + ")");

		return row;
	}

	void SetBarNone(ViewHolder holder)
	{
		holder.imgDayBar.setVisibility(View.GONE);
		holder.imgWeekBar.setVisibility(View.GONE);
		holder.imgLastBar.setVisibility(View.GONE);
	}

	void SetBarDay(ViewHolder holder)
	{
		holder.imgDayBar.setVisibility(View.VISIBLE);
		holder.imgWeekBar.setVisibility(View.GONE);
		holder.imgLastBar.setVisibility(View.GONE);
	}

	void SetBarWeek(ViewHolder holder)
	{
		holder.imgDayBar.setVisibility(View.GONE);
		holder.imgWeekBar.setVisibility(View.VISIBLE);
		holder.imgLastBar.setVisibility(View.GONE);
	}

	void SetBarLast(ViewHolder holder)
	{
		holder.imgDayBar.setVisibility(View.GONE);
		holder.imgWeekBar.setVisibility(View.GONE);
		holder.imgLastBar.setVisibility(View.VISIBLE);
	}
}
