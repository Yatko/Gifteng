/**
 * 
 */
package com.venefica.module.listings;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author avinash
 * Adapter class for Listing List Adapter
 */
public class ListingListAdapter extends BaseAdapter {
	private ArrayList<ListingData> listings;
	private Context context;
	public ListingListAdapter(Context context, ArrayList<ListingData> listings) {
		this.context = context;
		this.listings = listings;
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
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ListingListItemView(context, listings.get(position));
		}
		((ListingListItemView)convertView).setListing(listings.get(position));
		return convertView;
	}

}
