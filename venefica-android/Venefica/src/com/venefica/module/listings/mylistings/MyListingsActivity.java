package com.venefica.module.listings.mylistings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.venefica.activity.R;
import com.venefica.activity.R.layout;
import com.venefica.activity.R.menu;
import com.venefica.module.listings.ListingData;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.bookmarks.BookmarkListingsActivity;
import com.venefica.module.messages.MessageData;
import com.venefica.module.messages.MessageDetailActivity;
import com.venefica.module.messages.MessageListActivity;
import com.venefica.module.messages.MessageListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyListingsActivity extends Activity {
	/**
	 * List to show my listings
	 */
	private ListView listViewListings;
	/**
	 * List adapter
	 */
	private ListingListAdapter listingsListAdapter;
	/**
	 * Listings list
	 */
	private ArrayList<ListingData> listings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        listViewListings = (ListView) findViewById(R.id.listActMyListings);
        listings = getDemoListings();
        
		listingsListAdapter = new ListingListAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
		listViewListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MyListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});
    }

	private ArrayList<ListingData> getDemoListings() {
		ArrayList<ListingData> listings = new ArrayList<ListingData>();
		ListingData listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new ListingData();
		listing.setListingId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
		listing.setCurrencyCode("USD");
		listings.add(listing);
		return listings;
	}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_listings, menu);
        return true;
    }*/
}
