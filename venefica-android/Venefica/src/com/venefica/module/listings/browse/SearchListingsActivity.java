package com.venefica.module.listings.browse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.venefica.activity.R;
import com.venefica.activity.R.layout;
import com.venefica.activity.R.menu;
import com.venefica.module.listings.ListingData;
import com.venefica.module.listings.ListingListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchListingsActivity extends Activity {
	/**
	 * List to show listings
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
	/**
	 * Search edit box
	 */
	private EditText edtSearch;
	/**
	 * Search button
	 */
	private Button btnSearch;
	/**
	 * TItle
	 */
	private TextView txtTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_listings);
        edtSearch = (EditText) findViewById(R.id.edtActSearchListing);
        txtTitle = (TextView) findViewById(R.id.txtActSearchListingTitle);
        btnSearch = (Button) findViewById(R.id.btnActSearchListing);
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				listings.addAll(getDemoListings());
				listingsListAdapter.notifyDataSetChanged();
			}
		});
        listViewListings = (ListView) findViewById(R.id.listActSearchListings);
        listings = new ArrayList<ListingData>();
        
		listingsListAdapter = new ListingListAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
		listViewListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent intent = new Intent(MyListingsActivity.this, ListingDetailActivity.class);
//				startActivity(intent);
			}
		});
		//Hide searchbar and load listings for selected category
		if(getIntent().getBooleanExtra("hide_searchbar", false)){
			hideSearchBar();
			listings.addAll(getDemoListings());
			listingsListAdapter.notifyDataSetChanged();
			txtTitle.setText(getIntent().getStringExtra("category_name"));
		}
    }
	private void hideSearchBar() {
		edtSearch.setVisibility(View.GONE);
		btnSearch.setVisibility(View.GONE);
		txtTitle.setVisibility(View.VISIBLE);
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
        getMenuInflater().inflate(R.menu.activity_search_listings, menu);
        return true;
    }*/
}
