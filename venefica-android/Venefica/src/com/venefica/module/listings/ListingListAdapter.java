/**
 * 
 */
package com.venefica.module.listings;

import java.util.List;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.services.AdDto;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author avinash
 * Adapter class for Listing List Adapter
 */
public class ListingListAdapter extends BaseAdapter {
	private List<AdDto> listings;
	private Context context;
	private boolean isGridView;
	public ListingListAdapter(Context context, List<AdDto> listings, boolean isGridView) {
		this.context = context;
		this.listings = listings;
		this.isGridView = isGridView;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return listings.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (isGridView) {
				convertView = new ListingTileView(context);
			} else {
				convertView = new ListingListItemView(context, listings.get(position));
			}			
		}
		if (isGridView) {
			((ListingTileView)convertView).setListing(listings.get(position));
		} else {
			((ListingListItemView)convertView).setListing(listings.get(position));
		}
		if (position == getCount()-3) {
			Log.d("last Item", ""+position);
			if(this.context instanceof SearchListingsActivity){
				((SearchListingsActivity)this.context).getMoreListings();
			}
		}
		return convertView;
	}

}
