package com.venefica.module.listings.bookmarks;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.venefica.activity.R;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.network.WSAction;
import com.venefica.services.AdDto;
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
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
	
	private WSAction wsAction;
	/**
	 * selected listing
	 */
	private AdDto selectedListing;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_listings);
        listViewListings = (ListView) findViewById(R.id.listActBookmarkListings);
        listings = new ArrayList<AdDto>();
		listingsListAdapter = new ListingListAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_get_bookmarks);
			}else if(ERROR_CODE == ERROR_NO_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_no_bookmarks);
			}else if(ERROR_CODE == ERROR_CONFIRM_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.msg_bookmark_confirm_delete)
						+" " +selectedListing.getTitle() +" ?";
			}else if(ERROR_CODE == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS){
				message = (String) getResources().getText(R.string.msg_bookmark_delete_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_remove_bookmarks);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }	

    /**
     * 
     * @author avinash
     * Class to handle bookmark tasks
     */
	class BookmarkTask extends AsyncTask<Integer, Integer, BookmarksResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(D_PROGRESS);
		}
		@Override
		protected BookmarksResultWrapper doInBackground(Integer... params) {
			BookmarksResultWrapper wrapper = new BookmarksResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_DOWNLOAD_BOOKMARKS)) {
					wrapper = wsAction.getBookmarkedListings(((VeneficaApplication)getApplication()).getAuthToken());
				}else if (params[0].equals(ACT_MODE_REMOVE_BOOKMARK)) {
					wrapper = wsAction.removeBookmarkedListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListing.getId());
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
		protected void onPostExecute(BookmarksResultWrapper result) {
			super.onPostExecute(result);
			dismissDialog(D_PROGRESS);
			if(result.bookmarks == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_BOOKMARKS_SUCCESS && result.bookmarks != null
					&& result.bookmarks.size() > 0) {
				listings.clear();
				listings.addAll(result.bookmarks);
				listingsListAdapter.notifyDataSetChanged();
			}else if (result.result == Constants.RESULT_GET_BOOKMARKS_SUCCESS 
					&& (result.bookmarks == null || result.bookmarks.size() == 0)) {
				ERROR_CODE = ERROR_NO_BOOKMARKS;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS 
					|| result.result == Constants.ERROR_RESULT_REMOVE_BOOKMARKS) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bookmark_listings, menu);
        return true;
    }*/
}
