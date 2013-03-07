package com.venefica.module.listings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Class to represent tile
 */
public class ListingTileView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener{
	private TextView txtTitle, /*txtDesc,*/ txtPrice;
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
//		txtDesc = (TextView) view.findViewById(R.id.txtListingTileDesc);
		txtPrice = (TextView) view.findViewById(R.id.txtListingTilePrice);
		imgBtnShare = (ImageButton) view.findViewById(R.id.imgBtnListingTileShare);
		imgBtnShare.setOnClickListener(this);
		imgView = (ImageView) view.findViewById(R.id.imgListingTileBg);
		imgView.setOnClickListener(this);
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
//		txtDesc.setText(listing.getDescription());
		txtPrice.setText("USD ");
		txtPrice.append(listing.getPrice().toString());
//		txtPrice.append(" ");
//		txtPrice.append(listing.getCurrencyCode());
		if (this.listing.getImage() != null) {
			((VeneficaApplication) ((SearchListingsActivity)getContext()).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.listing.getImage().getUrl()
						, imgView, getResources().getDrawable(R.drawable.icon_picture_white));
		}else {
			((VeneficaApplication) ((SearchListingsActivity)getContext()).getApplication())
				.getImgManager().loadImage("", imgView, getResources().getDrawable(R.drawable.icon_picture_white));
		}	
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgBtnListingTileShare) {			
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, this.listing.getTitle()+ " "+ this.listing.getDescription());
			sendIntent.setType("text/plain");
			getContext().startActivity(sendIntent);
		}else{
			Intent intent = new Intent(getContext(), ListingDetailsActivity.class);
			intent.putExtra("ad_id", this.listing.getId());
			int mode = SearchListingsActivity.getCURRENT_MODE();
			if (mode == SearchListingsActivity.ACT_MODE_DOWNLOAD_BOOKMARKS) {
				mode = ListingDetailsActivity.ACT_MODE_BOOKMARK_LISTINGS;
			} else if (mode == SearchListingsActivity.ACT_MODE_DOWNLOAD_MY_LISTINGS) {
				mode = ListingDetailsActivity.ACT_MODE_MY_LISTINGS_DETAILS;
			}else if (mode == SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY) {
				mode = ListingDetailsActivity.ACT_MODE_DOWNLOAD_LISTINGS_DETAILS;
			}
			intent.putExtra("act_mode", mode);
			getContext().startActivity(intent);
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
