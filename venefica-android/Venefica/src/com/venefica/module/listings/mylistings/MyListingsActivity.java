package com.venefica.module.listings.mylistings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.browse.SearchListingResultWrapper;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class MyListingsActivity extends VeneficaActivity {
	public static final int ACT_MODE_DOWNLOAD_MY_LISTINGS = 3001;
	/**
	 * List to show my listings
	 */
	private ListView listViewListings;
	/**
	 * List adapter
	 */
	private MyListingsAdapter listingsListAdapter;
	/**
	 * Listings list
	 */
	private List<AdDto> listings;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	private WSAction wsAction;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
		getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		setContentView(R.layout.activity_my_listings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);
		listViewListings = (ListView) findViewById(R.id.listActMyListings);
		// listings = getDemoListings();
		listings = new ArrayList<AdDto>();
		listingsListAdapter = new MyListingsAdapter(this, listings);
		listViewListings.setAdapter(listingsListAdapter);
		/*
		 * listViewListings.setOnItemClickListener(new
		 * AdapterView.OnItemClickListener() {
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view, int
		 * position, long id) { Intent intent = new
		 * Intent(MyListingsActivity.this, ListingDetailsActivity.class);
		 * intent.putExtra("ad_id", listings.get(position).getId());
		 * intent.putExtra("mode",
		 * ListingDetailsActivity.ACT_MODE_MY_LISTINGS_DETAILS);
		 * startActivity(intent); } });
		 */
		registerForContextMenu(listViewListings);
	}

    @Override
    protected void onResume() {
    	super.onResume();
    	//Download my listings
		new MyListingsTask().execute(ACT_MODE_DOWNLOAD_MY_LISTINGS);
    }
   	
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(MyListingsActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MyListingsActivity.this);
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_MY_LISTINGS){
				message = (String) getResources().getText(R.string.error_get_my_listings);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
    
    /**
     * 
     * @author avinash
     * Class to handle my listings download 
     */
    class MyListingsTask extends AsyncTask<Integer, Integer, SearchListingResultWrapper>{
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
				if (params[0].equals(ACT_MODE_DOWNLOAD_MY_LISTINGS)) {
					wrapper = wsAction.getMyListings(((VeneficaApplication)getApplication()).getAuthToken());
				}
			}catch (IOException e) {
				Log.e("MyListingsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("MyListingsTask::doInBackground :", e.toString());
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
			}else if (result.result == Constants.RESULT_GET_MY_LISTINGS_SUCCESS && result.listings != null
					&& result.listings.size() > 0) {
				listings.clear();
				listings.addAll(result.listings);
				listingsListAdapter.notifyDataSetChanged();
			}
		}
    }
    
}
