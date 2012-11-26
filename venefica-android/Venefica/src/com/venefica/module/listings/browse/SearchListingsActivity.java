package com.venefica.module.listings.browse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.venefica.activity.R;
import com.venefica.activity.R.layout;
import com.venefica.activity.R.menu;
import com.venefica.module.listings.ListingData;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.bookmarks.BookmarksResultWrapper;
import com.venefica.module.listings.mylistings.MyListingsActivity;
import com.venefica.module.network.WSAction;
import com.venefica.services.AdDto;
import com.venefica.services.CategoryDto;
import com.venefica.services.FilterDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
	private List<AdDto> listings;
	/**
	 * Search edit box
	 */
	private EditText edtSearch;
	/**
	 * Search button
	 */
	private Button btnSearch;
	/**
	 * Title
	 */
	private TextView txtTitle;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	private static final int ERROR_NO_RESULTS = 4001;
	/**
	 * Activity modes
	 */
	public static final int ACT_MODE_SEARCH = 3001;
	public static final int ACT_MODE_SEARCH_BY_CATEGORY = 3002;
	/**
	 * Selected category
	 */
	private long selectedCategoryId;
	private String selectedCategory;
	private WSAction wsAction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_listings);
        edtSearch = (EditText) findViewById(R.id.edtActSearchListing);
        txtTitle = (TextView) findViewById(R.id.txtActSearchListingTitle);
        btnSearch = (Button) findViewById(R.id.btnActSearchListing);
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				listings.addAll(getDemoListings());
//				listingsListAdapter.notifyDataSetChanged();
			}
		});
        listViewListings = (ListView) findViewById(R.id.listActSearchListings);
        listings = new ArrayList<AdDto>();
        
		listingsListAdapter = new ListingListAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
		listViewListings.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(SearchListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});
		
		//Hide search options and load listings for selected category
		if(getIntent().getIntExtra("act_mode", ACT_MODE_SEARCH) == ACT_MODE_SEARCH_BY_CATEGORY){
			hideSearchBar();
			selectedCategory = getIntent().getStringExtra("category_name");
			selectedCategoryId = getIntent().getLongExtra("category_id", 0);
			txtTitle.setText(selectedCategory);
			new SearchListingTask().execute(ACT_MODE_SEARCH_BY_CATEGORY);
		}
    }
    /**
     * To hide search options
     */
	private void hideSearchBar() {
		edtSearch.setVisibility(View.GONE);
		btnSearch.setVisibility(View.GONE);
		txtTitle.setVisibility(View.VISIBLE);
	}
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(SearchListingsActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(SearchListingsActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	if(id == D_ERROR) {
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LISTINGS){
				message = (String) getResources().getText(R.string.error_search_listings);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/**
	 * 
	 * @author avinash
	 * Class to handle searchlisting task
	 */
	class SearchListingTask extends AsyncTask<Integer, Integer, SearchListingResultWrapper>{		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(D_PROGRESS);
		}
		@Override
		protected SearchListingResultWrapper doInBackground(Integer... params) {
			SearchListingResultWrapper wrapper = new SearchListingResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_SEARCH_BY_CATEGORY)) {
					wrapper = wsAction.searchListings(((VeneficaApplication)getApplication()).getAuthToken()
							, 0, 5, getFilterOptions());
				}
			}catch (IOException e) {
				Log.e("SearchListingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("SearchListingTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(SearchListingResultWrapper result) {
			super.onPostExecute(result);
			dismissDialog(D_PROGRESS);
			if(result.listings == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTINGS_SUCCESS && result.listings != null
					&& result.listings.size() > 0) {
//				listings.clear();
				listings.addAll(result.listings);
				listingsListAdapter.notifyDataSetChanged();
			}else if (result.result == Constants.RESULT_GET_LISTINGS_SUCCESS 
					&& (result.listings == null || result.listings.size() == 0)) {
				ERROR_CODE = ERROR_NO_RESULTS;
				showDialog(D_ERROR);
			}else if (result.result == Constants.ERROR_RESULT_GET_LISTINGS) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}
	private FilterDto getFilterOptions(){
		FilterDto filter = new FilterDto();
		filter.setDistance(50);
		filter.setLatitude(new Double(77.00));
		filter.setLongitude(new Double(21.00));
		filter.setMaxPrice(new BigDecimal(5000.00));
		filter.setMinPrice(new BigDecimal(0));
		filter.setWanted(true);
		filter.setSearchString("");
		filter.setHasPhoto(false);
		List<Long> cats = new ArrayList<Long>();
		cats.add(new Long(1));
		filter.setCategories(cats);
		return filter;		
	}
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_listings, menu);
        return true;
    }*/
}
