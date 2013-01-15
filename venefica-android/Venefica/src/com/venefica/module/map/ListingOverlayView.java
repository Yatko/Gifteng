package com.venefica.module.map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.utils.ImageDownloadManager;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Custom overlay view to show in pop up on tap of overlay item
 */
public class ListingOverlayView <Item extends OverlayItem> extends BalloonOverlayView<ListingOverlayItem> {

	private TextView title;
	private TextView snippet;
	private ImageView image;
	
	/**
	 * Constructor for listing overlay view
	 * @param context
	 * @param balloonBottomOffset
	 */
	public ListingOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	/* (non-Javadoc)
	 * @see com.venefica.module.map.BalloonOverlayView#setupView(android.content.Context, android.view.ViewGroup)
	 */
	@Override
	protected void setupView(Context context, final ViewGroup parent) {		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.view_listing_overlay, parent);
		
		// setup our fields
		title = (TextView) v.findViewById(R.id.txtListingOverlayTitle);
		snippet = (TextView) v.findViewById(R.id.txtListingOverlaySnippet);
		image = (ImageView) v.findViewById(R.id.imgListingOverlayImage);
	}
	
	/* (non-Javadoc)
	 * @see com.venefica.module.map.BalloonOverlayView#setBalloonData(com.google.android.maps.OverlayItem, android.view.ViewGroup)
	 */
	@Override
	protected void setBalloonData(ListingOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		title.setText(item.getTitle());
		snippet.setText(item.getSnippet());
		
		// get remote image from network.
		// bitmap results would normally be cached, but this is good enough for demo purpose.
		image.setImageResource(R.drawable.ic_launcher);
		/*ImageDownloadManager.getImageDownloadManagerInstance()
			.loadDrawable(item.getImgURL(), image, getResources().getDrawable(R.drawable.ic_launcher));*/
		if (getContext() instanceof ListingDetailsActivity) {
			((VeneficaApplication) ((ListingDetailsActivity)getContext()).getApplication())
			.getImgManager().loadImage(item.getImgURL(), image, getResources().getDrawable(R.drawable.ic_launcher));
		} else if (getContext() instanceof SearchListingsActivity) {
			((VeneficaApplication) ((SearchListingsActivity)getContext()).getApplication())
			.getImgManager().loadImage(item.getImgURL(), image, getResources().getDrawable(R.drawable.ic_launcher));
		}
		
	}	
}
