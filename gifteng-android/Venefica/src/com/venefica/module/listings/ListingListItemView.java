package com.venefica.module.listings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;

/**
 * @author avinash
 * View for listing list items
 */
public class ListingListItemView extends LinearLayout {
	private TextView txtTitle, txtDesc, txtPrice, txtDaysToExp;
	private CheckBox chkSelListing;
	private ImageView imgListingPhoto;
	private AdDto listing;
	/**
	 * @param context
	 */
	public ListingListItemView(Context context, AdDto listing) {
		super(context);
		this.listing = listing;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_listing_list_item, this, false);
		txtTitle = (TextView) view.findViewById(R.id.txtListingTitle);
		txtDesc = (TextView) view.findViewById(R.id.txtListingDesc);
		txtPrice = (TextView) view.findViewById(R.id.txtListingPrice);
		txtDaysToExp = (TextView) view.findViewById(R.id.txtListingExpDays);
		imgListingPhoto = (ImageView) view.findViewById(R.id.imgListingImage);
//		chkSelListing = (CheckBox) view.findViewById(R.id.chkListingSelect);
		setListing(listing);
		this.addView(view);
	}

	public AdDto getListing(){
		return listing;
	}
	
	public void setListing(AdDto listing){
		txtTitle.setText(listing.getTitle());
		txtDesc.setText(listing.getDescription());
		txtPrice.setText(listing.getPrice().toString());
		txtPrice.append(" ");
//		txtPrice.append(listing.getCurrencyCode());
		txtDaysToExp.setText(Utility.convertShortDateToString(listing.getExpiresAt()));
//		imgListingPhoto.setImageBitmap(listing.getImageThumbnail());
	}
	
	/**
	 * Select listing
	 *//*
	public void setSelected(boolean selected){
		chkSelListing.setChecked(selected);
	}
	
	*//**
	 * Get listing selection
	 *//*
	public boolean isSelected(){
		return chkSelListing.isChecked();
	}*/
}
