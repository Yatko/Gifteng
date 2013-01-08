package com.venefica.module.listings;

import java.util.List;

import com.google.android.maps.MapView.LayoutParams;
import com.venefica.module.main.R;
import com.venefica.module.utils.ImageDownloadManager;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author avinash Adapter class for image gallery
 */
public class GalleryImageAdapter extends BaseAdapter {
	private Context context;

	private static ImageView imageView;

	private List<ImageDto> plotsImages;
	private List<Drawable> images;
	private boolean useDrawables;
	private static ViewHolder holder;

	public GalleryImageAdapter(Context context, List<ImageDto> plotsImages, List<Drawable> images, boolean useDrawables) {

		this.context = context;
		this.plotsImages = plotsImages;
		this.images = images;
		this.useDrawables = useDrawables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		if (useDrawables) {
			return this.images.size();
		} else {
			return this.plotsImages.size();
		}
		
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
			imageView.setScaleType(ScaleType.CENTER_CROP);
			convertView = imageView;

			holder.imageView = imageView;

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		if (useDrawables) {
			holder.imageView.setImageDrawable(images.get(position));
		} else {
			ImageDownloadManager.getImageDownloadManagerInstance()
			.loadDrawable(plotsImages.get(position) != null 
				? Constants.PHOTO_URL_PREFIX + plotsImages.get(position).getUrl():"", 
					holder.imageView, this.context.getResources().getDrawable(R.drawable.icon_picture_white));
		}	
		
		holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		holder.imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}

}
