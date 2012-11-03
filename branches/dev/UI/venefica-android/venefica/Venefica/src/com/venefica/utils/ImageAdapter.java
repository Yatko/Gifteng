package com.venefica.utils;

import java.util.List;

import com.venefica.activity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<ImageAd>
{
	private List<ImageAd> Images;

	class ViewHolder
	{
		public ImageView imgProduct;
	}

	public ImageAdapter(Context context, List<ImageAd> images)
	{
		super(context, R.layout.gallery_image_row, images);
		this.Images = images;
	}

	@Override
	public long getItemId(int position)
	{
		return Images.get(position).id;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		View row = convertView;

		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater)super.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.gallery_image_row, parent, false);

			holder = new ViewHolder();

			holder.imgProduct = (ImageView)row.findViewById(R.id.imgProduct);
			holder.imgProduct.setScaleType(ImageView.ScaleType.FIT_CENTER);

			row.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)row.getTag();
		}

		ImageAd item = Images.get(position);
		if (item != null)
		{
			if(item.url != null)
			{
//				VeneficaApplication.ImgLoader.displayImage(item.url, holder.imgProduct, VeneficaApplication.ImgLoaderOptions);
			}
			else if(item.bitmap != null)
			{
				holder.imgProduct.setImageBitmap(item.bitmap);
			}
			else
			{
				holder.imgProduct.setImageResource(R.drawable.default_photo);
			}
		}
		else
			Log.d("ImageAdapter.getView", "value == null");

		return row;
	}
}
