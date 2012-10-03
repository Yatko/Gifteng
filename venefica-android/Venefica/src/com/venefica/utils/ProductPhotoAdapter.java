package com.venefica.utils;

import java.util.List;

import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.market.MarketEx.UpdateProductsCallback;
import com.venefica.services.ServicesManager.SoapRequestResult;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ProductPhotoAdapter extends ArrayAdapter<Product>
{
	private List<Product> Products;
	private Runnable UpdateListCallback;

	class ViewHolder
	{
		public ImageView imgProduct;
		public View progressBar;
	}

	public ProductPhotoAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId, MarketEx.getInstance().GetProducts());
		this.Products = MarketEx.getInstance().GetProducts();
	}

	@Override
	public int getCount()
	{
		int sz = Products.size();

		if (sz == 0)
		{
			MarketEx.getInstance().UpdateProductsList(new UpdateProductsCallback()
			{
				public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
				{
					ProductPhotoAdapter.this.notifyDataSetChanged();
					UpdateListCallback.run();
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
			row = inflater.inflate(R.layout.gallery_product_row, parent, false);

			holder = new ViewHolder();

			holder.imgProduct = (ImageView)row.findViewById(R.id.imgProduct);
			holder.progressBar = row.findViewById(R.id.progressBar);

			holder.imgProduct.setScaleType(ImageView.ScaleType.FIT_CENTER);

			row.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)row.getTag();
		}

		if (position < Products.size() && position >= 0)
		{
			if (position == Products.size() - 1)
			{//last element
				if (MarketEx.getInstance().ExistLastProduct() == false)
				{//still have that shipping
					holder.imgProduct.setVisibility(View.GONE);
					holder.progressBar.setVisibility(View.VISIBLE);

					MarketEx.getInstance().UpdateProductsList(new UpdateProductsCallback()
					{
						public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
						{
							ProductPhotoAdapter.this.notifyDataSetChanged();

							if (UpdateListCallback != null)
								UpdateListCallback.run();
						}
					});
				}
				else
				{
					holder.imgProduct.setVisibility(View.VISIBLE);
					holder.progressBar.setVisibility(View.GONE);
				}
			}
			else
			{
				holder.imgProduct.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.GONE);
			}

			Product item = Products.get(position);
			if (item != null)
			{
				if (item.image != null && item.image.url != null)
				{
					MyApp.ImgLoader.displayImage(item.image.url, holder.imgProduct, MyApp.ImgLoaderOptions);
				}
				else
				{
					holder.imgProduct.setImageResource(R.drawable.default_photo);
				}
			}
			else
				Log.d("ProductPhotoAdapter.getView", "value == null");
		}
		else
			Log.d("ProductPhotoAdapter.getView", "position(" + position + ") out of range(0 - " + Products.size() + ")");

		return row;
	}

	public void setUpdateListCallback(Runnable updateListCallback)
	{
		UpdateListCallback = updateListCallback;
	}
}
