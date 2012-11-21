package com.venefica.module.listings.bookmarks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.venefica.activity.R;
import com.venefica.module.listings.ListingData;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.services.AdDto;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BookmarkListingsActivity extends Activity {
	/**
	 * List to show bookmark listings
	 */
	private ListView listViewListings;
	/**
	 * List adapter
	 */
	private ListingListAdapter listingsListAdapter;
	/**
	 * Listings list
	 */
	private ArrayList<AdDto> listings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_listings);
        listViewListings = (ListView) findViewById(R.id.listActBookmarkListings);
        listings = getBookmarkListings();
        
		listingsListAdapter = new ListingListAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
		listViewListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(BookmarkListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});
    }

	private ArrayList<AdDto> getBookmarkListings() {
		ArrayList<AdDto> listings = new ArrayList<AdDto>();
		AdDto listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		listing = new AdDto();
		listing.setId(1);
		listing.setTitle("Apartment");
		listing.setDescription("3 bed, kitchen, Living room");
		listing.setPrice(new BigDecimal(75000.00));
		listing.setExpiresAt(new Date());
//		listing.setCurrencyCode("USD");
		listings.add(listing);
		return listings;
	}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bookmark_listings, menu);
        return true;
    }*/
}
