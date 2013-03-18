package com.venefica.module.listings;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.main.R;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash 
 * Adapter class for image gallery
 */
public class GalleryImageAdapter extends BaseAdapter {
	private Context context;

	private static ImageView imageView;

	private List<ImageDto> plotsImages;
	private List<Bitmap> images;
	private boolean useDrawables;
	private static ViewHolder holder;
	private boolean showThumbnails;

	private int coverPosition = -1;

	public GalleryImageAdapter(Context context, List<ImageDto> plotsImages, List<Bitmap> images, boolean useDrawables, boolean showThumbnails) {

		this.context = context;
		this.plotsImages = plotsImages;
		this.images = images;
		this.useDrawables = useDrawables;
		this.showThumbnails = showThumbnails;
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

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_postlisting_image, null);
			if (showThumbnails) {
				holder.imageView = (ImageView) convertView.findViewById(R.id.imgPostListingSwitcherImage);				
			} else {
				holder.imageView = (ImageView) convertView.findViewById(R.id.imgListingDetailSwitcherImage);
			}
			holder.imageView.setVisibility(View.VISIBLE);
			holder.txtCoverMark = (TextView) convertView.findViewById(R.id.txtPostListingCoverTicker);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (useDrawables) {			
			holder.imageView.setImageBitmap(/*Utility.resizeBitmap(*/images.get(position)
					/*, Constants.IMAGE_THUMBNAILS_WIDTH, Constants.IMAGE_THUMBNAILS_HEIGHT)*/);			
		} else {			
			if(this.context instanceof ListingDetailsActivity){
				((VeneficaApplication) ((ListingDetailsActivity)this.context).getApplication())
				.getImgManager().loadImage(plotsImages.get(position) != null 
				? Constants.PHOTO_URL_PREFIX + plotsImages.get(position).getUrl():"", 
						holder.imageView, this.context.getResources().getDrawable(R.drawable.icon_picture_white));
			} else if (this.context instanceof PostListingActivity){
				((VeneficaApplication) ((PostListingActivity)this.context).getApplication())
				.getImgManager().loadImage(plotsImages.get(position) != null 
				? Constants.PHOTO_URL_PREFIX + plotsImages.get(position).getUrl():"", 
						holder.imageView, this.context.getResources().getDrawable(R.drawable.icon_picture_white));
			}			
		}	
		if (coverPosition == position) {
			holder.txtCoverMark.setVisibility(View.VISIBLE);
		}else {
			holder.txtCoverMark.setVisibility(View.GONE);
		}
				
		return convertView;
	}

	private static class ViewHolder {
		TextView txtCoverMark;
		ImageView imageView;
	}
	
	/**
	 *  set cover image position for post layout gallery
	 * @param coverPosition
	 */
	public void setCoverImagePosition(int coverPosition){
		this.coverPosition  = coverPosition;
	}
	/**
	 * Reset cover image position for post layout gallery
	 */
	public void resetCoverImagePosition(){
		this.coverPosition  = -1;
	}
}
