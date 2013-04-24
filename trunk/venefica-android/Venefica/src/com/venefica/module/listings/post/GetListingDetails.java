package com.venefica.module.listings.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.venefica.module.listings.MapItemizedOverlay;
import com.venefica.module.listings.browse.BrowseCatResultWrapper;
import com.venefica.module.listings.browse.CategoryListAdapter;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.OnSingleTapListener;
import com.venefica.module.map.TapControlledMapView;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.CategoryDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Activity class to collect listing details
 */
public class GetListingDetails extends VeneficaMapActivity implements LocationListener, OnClickListener, OnItemSelectedListener{

	/**
	 * Edit fields to collect listing data
	 */
	private EditText edtTitle, edtDescription,
			edtPrice, edtZip;
	private Spinner spinCategory;
	private Button btnNext;
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	/**
	 * List adapter
	 */
	private CategoryListAdapter categoriesListAdapter;
	/**
	 * Categories list
	 */
	private List<CategoryDto> categories;
	public static final int ACT_MODE_DOWNLOAD_CATEGORY = 1001;
	
	private int CURRENT_MODE = ACT_MODE_DOWNLOAD_CATEGORY;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_CATEGORY_ID = "category_id";
	public static final String KEY_CURRENT_VALUE = "current_value";
	public static final String KEY_ZIP_CODE = "zip_code";
	public static final String KEY_FREE_SHIPPING = "free_shipping";
	public static final String KEY_PICKUP = "pickup";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_COVER_IMAGE = "cover_image";
	public static final String KEY_IS_BACK_FROM_PREVIEW = "back_from_preview";
	public static final String KEY_IS_UPDATE_MODE = "update_mode";
	
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
	/**
	 * true when back from preview
	 */
	private boolean isBackFromPreview = false;
	private String selectedCategory = "";
	/**
	 * cover image position
	 */
	private int coverImagePosition = -1;
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
		
		spinCategory = (Spinner) findViewById(R.id.spinActPostListingCategory);
		categories = new ArrayList<CategoryDto>();
		categoriesListAdapter = new CategoryListAdapter(this, categories);
		spinCategory.setPrompt(getResources().getString(R.string.g_hint_choose_category));
		spinCategory.setAdapter(categoriesListAdapter);
		spinCategory.setOnItemSelectedListener(this);
		//set data if back from preview
		if (getIntent().getBooleanExtra(KEY_IS_BACK_FROM_PREVIEW, false) 
				|| getIntent().getBooleanExtra(KEY_IS_UPDATE_MODE, false)) {
			edtTitle.setText(getIntent().getStringExtra(GetListingDetails.KEY_TITLE));
			selectedCategory = getIntent().getStringExtra(GetListingDetails.KEY_CATEGORY);
			edtDescription.setText(getIntent().getStringExtra(GetListingDetails.KEY_DESCRIPTION));
			edtPrice.setText(getIntent().getStringExtra(GetListingDetails.KEY_CURRENT_VALUE));
			isBackFromPreview = getIntent().getBooleanExtra(GetListingDetails.KEY_IS_BACK_FROM_PREVIEW, isBackFromPreview);
		}
		coverImagePosition = getIntent().getIntExtra(KEY_COVER_IMAGE, coverImagePosition);
		btnNext = (Button) findViewById(R.id.btnActPostListingNextToPreview);
		btnNext.setOnClickListener(this);
		if (WSAction.isNetworkConnected(this)) {
			new CategoryTask().execute(ACT_MODE_DOWNLOAD_CATEGORY);
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
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
			ListingOverlayItem overlayItem = new ListingOverlayItem(currLoc, getResources().getString(R.string.g_hint_current_location), ""
					, -1, Constants.PHOTO_URL_PREFIX );
			overlayItems.clear();
			overlayItems.addOverlay(overlayItem);
			mapView.getOverlays().add(overlayItems);
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
		int id = view.getId();
		if (id == R.id.btnActPostListingNextToPreview && validateFields()) {
			// hide virtual keyboard
			InputMethodManager imm = (InputMethodManager) GetListingDetails.this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			Intent intent = new Intent();
			intent.putExtra(KEY_TITLE, edtTitle.getText().toString());
			intent.putExtra(KEY_DESCRIPTION, edtDescription.getText().toString());
			intent.putExtra(KEY_CATEGORY, categories.get(spinCategory.getSelectedItemPosition()).getName());
			intent.putExtra(KEY_CATEGORY_ID, categories.get(spinCategory.getSelectedItemPosition()).getId());
			intent.putExtra(KEY_CURRENT_VALUE, edtPrice.getText().toString());
			intent.putExtra(KEY_ZIP_CODE, edtPrice.getText().toString());
			if (location != null) {
				intent.putExtra(KEY_LATITUDE, location.getLatitude());
				intent.putExtra(KEY_LONGITUDE, location.getLongitude());
			}
			intent.putExtra(KEY_COVER_IMAGE, coverImagePosition);
			intent.putExtra(KEY_IS_BACK_FROM_PREVIEW, false);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}
	/**
     * Method to validate input fields
     * @return result of validation
     */
    private boolean validateFields(){
    	boolean result = true;
    	StringBuffer message = new StringBuffer();
    	if(vaildator == null){
    		vaildator = new InputFieldValidator();    		
    	}
    	
    	if(!vaildator.validateField(edtTitle, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_title).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}    	
    	if(!vaildator.validateField(edtDescription, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_description).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtPrice, Pattern.compile(InputFieldValidator.PRICE_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_price).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.g_msg_validation_price));
    		message.append("\n");
    	}    	
    	if(!vaildator.validateField(edtZip, Pattern.compile(InputFieldValidator.ZIP_PATTERN_REGX))){
    		result = false;
    		message.append(edtZip.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_zipcode));
    		message.append("\n");
    	}
    	if (!result) {
			Utility.showLongToast(this, message.toString());
		}
		return result;    	
    }
	
	/**
	 * 
	 * @author avinash
	 * Class to perform download operations
	 */
	class CategoryTask extends AsyncTask<Integer, Integer, BrowseCatResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			showDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected BrowseCatResultWrapper doInBackground(Integer... params) {
			BrowseCatResultWrapper wrapper = new BrowseCatResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_DOWNLOAD_CATEGORY)) {
					wrapper = wsAction.getCategories(((VeneficaApplication)getApplication()).getAuthToken());
				}
			}catch (IOException e) {
				Log.e("CategoryTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("CategoryTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		
		protected void onPostExecute(BrowseCatResultWrapper result) {
//			dismissDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.categories == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_CATEGORIES_SUCCESS && result.categories != null
					&& result.categories.size() > 0) {
				categories.clear();
				categories.addAll(result.categories);
				categoriesListAdapter.notifyDataSetChanged();
				for (int i = 0; i < categories.size(); i++) {
					if (selectedCategory.equals("")) {
						break;
					}
					if (categories.get(i).getName().equalsIgnoreCase(selectedCategory)) {
						spinCategory.setSelection(i, true);
						break;
					}
				}				
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,
			long id) {
		/*CategoryDto cat = categories.get(position);  
		if(cat.getSubcategories() != null && cat.getSubcategories().size() > 0){
			categories.clear();
			categories.addAll(cat.getSubcategories());
			categoriesListAdapter.notifyDataSetChanged();
		}*/
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
