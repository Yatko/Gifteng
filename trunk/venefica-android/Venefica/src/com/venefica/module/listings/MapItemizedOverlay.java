/**
 * 
 */
package com.venefica.module.listings;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.venefica.activity.R;

/**
 * @author avinash
 *
 */
public class MapItemizedOverlay extends ItemizedOverlay {
	/**
	 * Overlay Items
	 */
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;
	/**
	 * @param defaultMarker
	 */
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
        super(boundCenterBottom(defaultMarker));
        this.context = context;
    }
	/**
	 * Add overlay item
	 * @param overlay
	 */
	public void addOverlay(OverlayItem overlay) {
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
	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}
	
	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
    public boolean onTap(GeoPoint p, MapView mapView) {
		/*mapView.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) mapView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popUp = inflater.inflate(R.layout.view_map_overlay_detail, mapView, false);
		MapView.LayoutParams mapParams = new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT,
                p,
                0,
                0,
                MapView.LayoutParams.BOTTOM_CENTER);
		mapView.addView(popUp, mapParams);*/
		return true;		
	};

    /* (non-Javadoc)
     * @see com.google.android.maps.ItemizedOverlay#onTouchEvent(android.view.MotionEvent, com.google.android.maps.MapView)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView){

        return false;
    }
}
