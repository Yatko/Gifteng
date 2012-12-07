package com.venefica.module.listings;

import com.venefica.activity.R;
import com.venefica.module.utils.ImageDownloadManager;
import com.venefica.services.AdDto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author avinash
 * Class to represent tile
 */
public class ListingTileView extends LinearLayout implements View.OnClickListener{
	private TextView txtTitle, txtDesc, txtPrice;
	private ImageButton imgBtnShare;
	private ImageView imgView;
	private AdDto listing;
	/**
	 * Constructor
	 * @param context
	 * @param listing
	 */
	public ListingTileView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_listing_tile_item, this, false);
		txtTitle = (TextView) view.findViewById(R.id.txtListingTileTitle);
		txtDesc = (TextView) view.findViewById(R.id.txtListingTileDesc);
		txtPrice = (TextView) view.findViewById(R.id.txtListingTilePrice);
		imgBtnShare = (ImageButton) view.findViewById(R.id.imgBtnListingTileShare);
		imgBtnShare.setOnClickListener(this);
		imgView = (ImageView) view.findViewById(R.id.imgListingTileBg);
		this.addView(view);
	}
	
	/**
	 * @return listing
	 */
	public AdDto getListing(){
		return listing;
	}
	
	/**
	 * Set listing 
	 * @param listing
	 */
	public void setListing(AdDto listing){
		this.listing = listing;
		txtTitle.setText(listing.getTitle());
		txtDesc.setText(listing.getDescription());
		txtPrice.setText("USD ");
		txtPrice.append(listing.getPrice().toString());
//		txtPrice.append(" ");
//		txtPrice.append(listing.getCurrencyCode());
		ImageDownloadManager.getImageDownloadManagerInstance().loadDrawable(this.listing.getImage().getUrl()
				, imgView, getResources().getDrawable(R.drawable.ic_launcher));
	}
	/**
	 * load image from url
	 */
	public void loadImage(){		
		ImageDownloadManager.getImageDownloadManagerInstance().loadDrawable(this.listing.getImage().getUrl()
				, imgView, getResources().getDrawable(R.drawable.ic_launcher));
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgBtnListingTileShare) {			
			
		}
	}
}
