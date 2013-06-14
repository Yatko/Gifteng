package com.venefica.module.listings.bookmarks;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.bookmarks.BookmarkListAdapter.BookmarkButtonClickListener;
import com.venefica.module.listings.browse.SearchListingResultWrapper;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class BookmarkListingsActivity extends VeneficaActivity implements BookmarkButtonClickListener {
	/**
	 * List to show bookmark listings
	 */
	private ListView listViewListings;
	/**
	 * List adapter
	 */
	private BookmarkListAdapter bookmarksListAdapter;
	/**
	 * Listings list
	 */
	private ArrayList<AdDto> listings;
	/**
	 * selected listing
	 */
	private long selectedAdId = -1;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	public static final int ACT_MODE_DOWNLOAD_BOOKMARKS = 3001;
	public static final int ACT_MODE_REMOVE_BOOKMARK = 3002;
	public static final int ERROR_NO_BOOKMARKS = 4001;
	public static final int ERROR_CONFIRM_REMOVE_BOOKMARKS = 4002;
	public static final int ACT_MODE_REQUEST_AD = 4003;
	
	private WSAction wsAction;
	/**
	 * selected listing
	 */
	private AdDto selectedListing;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        setContentView(R.layout.activity_bookmark_listings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  
        setSupportProgressBarIndeterminateVisibility(false);
        
        listViewListings = (ListView) findViewById(R.id.listActBookmarkListings);
        listings = new ArrayList<AdDto>();
		bookmarksListAdapter = new BookmarkListAdapter(this, listings);
		bookmarksListAdapter.setBookmarkButtonClickListener(this);
		listViewListings.setAdapter(bookmarksListAdapter);
		listViewListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(BookmarkListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});
		listViewListings.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
					long id) {
				selectedListing = listings.get(position);
				ERROR_CODE = ERROR_CONFIRM_REMOVE_BOOKMARKS;
				showDialog(D_CONFIRM);
				return false;
			}
		});
    }

    @Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(BookmarkListingsActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkListingsActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == ERROR_NO_BOOKMARKS) {
						finish();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	if(id == D_CONFIRM){
    		AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkListingsActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			
			builder.setPositiveButton(R.string.label_btn_yes, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					//Delete when code id confirm to delete
					if (ERROR_CODE == ERROR_CONFIRM_REMOVE_BOOKMARKS && selectedListing != null) {
						new BookmarkTask().execute(ACT_MODE_REMOVE_BOOKMARK);
					}						
					dismissDialog(D_CONFIRM);					
				}
			});
			
			builder.setNegativeButton(R.string.label_btn_no, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_CONFIRM);
				}
			});
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	return null;
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	new BookmarkTask().execute(ACT_MODE_DOWNLOAD_BOOKMARKS);
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
			} else if(ERROR_CODE == Constants.ERROR_RESULT_GET_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_get_bookmarks);
			} else if(ERROR_CODE == ERROR_NO_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_no_bookmarks);
			} else if(ERROR_CODE == ERROR_CONFIRM_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.msg_bookmark_confirm_delete)
						+" " +selectedListing.getTitle() +" ?";
			} else if(ERROR_CODE == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS){
				message = (String) getResources().getText(R.string.msg_bookmark_delete_success);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_remove_bookmarks);
			} else if(ERROR_CODE == Constants.RESULT_REQUEST_AD_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_request_ad_success);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_REQUEST_AD || ERROR_CODE == Constants.ERROR_RESULT_CANCEL_REQUEST){
				message = (String) getResources().getText(R.string.g_msg_request_placed_already);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		finish();
    	}
    	return true;
    }
    /**
     * 
     * @author avinash
     * Class to handle bookmark tasks
     */
	class BookmarkTask extends AsyncTask<Integer, Integer, SearchListingResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected SearchListingResultWrapper doInBackground(Integer... params) {
			SearchListingResultWrapper wrapper = new SearchListingResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_DOWNLOAD_BOOKMARKS)) {
					wrapper = wsAction.getBookmarkedListings(((VeneficaApplication)getApplication()).getAuthToken());
				} else if (params[0].equals(ACT_MODE_REMOVE_BOOKMARK)) {
					ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();
					result = wsAction.removeBookmarkedListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListing.getId());
					wrapper.result = result.result;
				} else if (params[0].equals(ACT_MODE_REQUEST_AD)) {
					ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();
					result = wsAction.requestListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedAdId);
					wrapper.result = result.result;
				}
			}catch (IOException e) {
				Log.e("BookmarkTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("BookmarkTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(SearchListingResultWrapper result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.listings == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_BOOKMARKS_SUCCESS && result.listings != null
					&& result.listings.size() > 0) {
				listings.clear();
				listings.addAll(result.listings);
				bookmarksListAdapter.notifyDataSetChanged();
			}else if (result.result == Constants.RESULT_GET_BOOKMARKS_SUCCESS 
					&& (result.listings == null || result.listings.size() == 0)) {
				ERROR_CODE = ERROR_NO_BOOKMARKS;
				showDialog(D_ERROR);
			}else/* if (result.result == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS 
					|| result.result == Constants.ERROR_RESULT_REMOVE_BOOKMARKS)*/ {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}
    

	@Override
	public long onRequestButtonClick(long adId, boolean isRequested) {
		if (isRequested) {
			Utility.showLongToast(
					this,
					getResources().getString(
							R.string.g_msg_request_placed_already));
		} else {
			selectedAdId = adId;
			new BookmarkTask().execute(ACT_MODE_REQUEST_AD);
			return adId;
		}
		return 0;
	}

	@Override
	public void onImageClick(long adId) {
		Intent intent = new Intent(this, ListingDetailsActivity.class);
		intent.putExtra("ad_id", adId);
		intent.putExtra("act_mode", ListingDetailsActivity.ACT_MODE_BOOKMARK_LISTINGS);
		startActivity(intent);
	}
}
