package com.venefica.module.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * @author avinash
 * Overlay Item for listing to show on map
 */
public class ListingOverlayItem extends OverlayItem {
	private long listingId;
	private String imgURL;
	/**
	 * @param point
	 * @param title
	 * @param snippet
	 */
	public ListingOverlayItem(GeoPoint point, String title, String snippet, long listingId, String imgURL) {
		super(point, title, snippet);
		this.listingId = listingId;
		this.imgURL = imgURL;
	}
	/**
	 * @return the listingId
	 */
	public long getListingId() {
		return listingId;
	}
	/**
	 * @param listingId the listingId to set
	 */
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}
	/**
	 * @return the imgURL
	 */
	public String getImgURL() {
		return imgURL;
	}
	/**
	 * @param imgURL the imgURL to set
	 */
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

}
