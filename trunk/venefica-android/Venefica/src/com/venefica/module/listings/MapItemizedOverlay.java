package com.venefica.module.listings;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.venefica.module.map.BalloonItemizedOverlay;
import com.venefica.module.map.BalloonOverlayView;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.ListingOverlayView;

/**
 * @author avinash 
 * Class to show overlay items on map.
 */
public class MapItemizedOverlay<Item extends OverlayItem> extends
		BalloonItemizedOverlay<ListingOverlayItem> {
	/**
	 * Overlay Items
	 */
	private ArrayList<ListingOverlayItem> mOverlays = new ArrayList<ListingOverlayItem>();
	private Context context;

	/**
	 * @param defaultMarker
	 */
	public MapItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
		this.context = mapView.getContext();
	}

	/**
	 * Add overlay item
	 * 
	 * @param overlay
	 */
	public void addOverlay(ListingOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	/**
	 * Clear all items
	 */
	public void clear() {

		mOverlays.clear();
		populate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected ListingOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}

	
	/* (non-Javadoc)
	 * @see com.venefica.module.map.BalloonItemizedOverlay#onBalloonTap(int, com.google.android.maps.OverlayItem)
	 */
	@Override
	protected boolean onBalloonTap(int index, ListingOverlayItem item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.map.BalloonItemizedOverlay#createBalloonOverlayView()
	 */
	@Override
	protected BalloonOverlayView<ListingOverlayItem> createBalloonOverlayView() {
		return new ListingOverlayView<OverlayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}
}
