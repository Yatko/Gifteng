package com.venefica.module.listings.post;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.venefica.module.listings.MapItemizedOverlay;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.OnSingleTapListener;
import com.venefica.module.map.TapControlledMapView;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Activity class to collect listing details
 */
public class GetListingDetails extends VeneficaMapActivity implements LocationListener, OnClickListener{

	/**
	 * Edit fields to collect listing data
	 */
	private EditText edtTitle, edtDescription,
			edtPrice, edtZip;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
     * Map 
     */
    private MapController mapController;
	private TapControlledMapView mapView;
	private WSAction wsAction;
	private boolean isMapShown = true;
	private MapItemizedOverlay<ListingOverlayItem> overlayItems;
	private LocationManager locationManager;
	private String locProvider;
	private Location location;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_post_listing_enter_details);
		//data
		edtTitle = (EditText) findViewById(R.id.edtActPostListingTitle);
		edtDescription = (EditText) findViewById(R.id.edtActPostListingDescription);
		edtPrice = (EditText) findViewById(R.id.edtActPostListingPriceValue);
		edtZip = (EditText) findViewById(R.id.edtActPostListingZipCode);
		
		//Map
		mapView = (TapControlledMapView) findViewById(R.id.mapViewActPostListing);
		// dismiss balloon upon single tap of MapView 
		mapView.setOnSingleTapListener(new OnSingleTapListener() {		
			@Override
			public boolean onSingleTap(MotionEvent e) {
				overlayItems.hideAllBalloons();						
				return true;
			}
		});
		// satellite or 2d mode
		mapView.setSatellite(false);
		mapView.setTraffic(true);
		
        mapController = mapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view
		overlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.icon_location), mapView);
		overlayItems.setShowClose(false);
		overlayItems.setShowDisclosure(false);
		overlayItems.setSnapToCenter(true);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return true;
	}
	@Override
	protected void onStart() {
		super.onStart();
		// get locaton service
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(false);
		locProvider = locationManager.getBestProvider(criteria, true);		

	    if (locProvider != null) {
	    	Log.d("GetListingDetails :", locProvider);
	    	location = locationManager.getLastKnownLocation(locProvider);
			onLocationChanged(location);
	    } else {
	    	ERROR_CODE = Constants.ERROR_ENABLE_LOCATION_PROVIDER;
	    	showDialog(D_ERROR);	        
	    }	    
	   
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(locProvider, 400, 1, this);		
	}
	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(this);
	}
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(GetListingDetails.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(GetListingDetails.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS
							|| ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE){
						finish();
					}else if (ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER) {
						enableLocationSettings();
					}
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LOCATION){
				message = (String) getResources().getText(R.string.error_postlisting_get_location);
			}else if(ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER){
				message = (String) getResources().getText(R.string.msg_postlisting_enable_provider);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;
			updateMap(location);
		}
	}

	public void onProviderDisabled(String provider) {
		Utility.showLongToast(this, provider + getResources().getString(R.string.msg_postlisting_provider_disabled));
	}

	public void onProviderEnabled(String provider) {
		Utility.showLongToast(this, provider + getResources().getString(R.string.msg_postlisting_provider_selected));
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * method to show listing location on map
	 * @param latitude
	 * @param longitude
	 * @param title
	 * @param description
	 */
	private void updateMap(Location location){
		if (isMapShown ) {
			GeoPoint currLoc = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
			mapController.animateTo(currLoc);			
		}	
	}
	/**
	 * Method to show options to enable location provider
	 */
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onClick(View view) {
		
	}
}
