package com.venefica.module.listings;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author avinash Adapter class for image gallery
 */
public class GalleryImageAdapter extends BaseAdapter {
	private Context context;

	private static ImageView imageView;

	private List<Drawable> plotsImages;

	private static ViewHolder holder;

	public GalleryImageAdapter(Context context, List<Drawable> plotsImages) {

		this.context = context;
		this.plotsImages = plotsImages;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return plotsImages.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			holder = new ViewHolder();

			imageView = new ImageView(this.context);

			imageView.setPadding(3, 3, 3, 3);

			convertView = imageView;

			holder.imageView = imageView;

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageDrawable(plotsImages.get(position));
		holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.imageView.setLayoutParams(new Gallery.LayoutParams(150, 120));
		return imageView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}

}
