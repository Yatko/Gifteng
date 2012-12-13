package com.venefica.module.listings.post;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.venefica.module.listings.GalleryImageAdapter;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.browse.BrowseCategoriesActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash 
 * Activity to post listings/adds
 */
public class PostListingActivity extends VeneficaMapActivity implements LocationListener {
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	/**
	 * Constants
	 */
	public static final int REQ_SELECT_CATEGORY = 1001;
	private static final int REQ_GET_IMAGE = 1002;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Activity MODE
	 */
	
	public static final int ACT_MODE_POST_LISTING = 3001;
	public static final int ACT_MODE_UPDATE_LISTING = 3002;
	public static final int ACT_MODE_GET_LISTING = 3003;
	private static int CURRENT_MODE = ACT_MODE_POST_LISTING;
	
	protected static final int ERROR_DATE_VALIDATION = 22;
	
	
	/**
	 * Edit fields to collect listing data
	 */
	private EditText edtTitle, edtSubTitle, edtDescription,
			edtCondition, edtPrice, edtZip, edtState, edtCounty, edtCity, edtArea,
			edtLatitude, edtLongitude;
	/**
	 * Text fields to collect listing data
	 */
	/*private TextView txtTitle, txtSubTitle, txtCategory, txtDescription,
			txtCondition, txtPrice, txtZip, txtState, txtCounty, txtCity, txtArea,
			txtLatitude, txtLongitude;*/
	private Spinner spinCurrency;
	/**
	 * Gallery to images
	 */
	private Gallery gallery;
	/**
	 * Images
	 */
	private List<Drawable> drawables;
	/**
	 * Adapter for gallery
	 */
	private GalleryImageAdapter galImageAdapter;

	/**
	 * Buttons
	 */
	private Button btnSelCategory, btnAddPhotos, btnPost, btnExpiary;
	/**
	 * Map button
	 */
	private ImageButton btnLocateOnMap;
	/**
	 * Calendar for current date
	 */
	private Calendar calendar = Calendar.getInstance();
	/**
	 * Map
	 */
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private Overlay itemizedoverlay;
	private boolean showMap = true;
	private String locProvider;
	/**
	 * Selected category
	 */
	private long categoryId;
	private String categoryName;
	/**
	 * Selected listing
	 */
	private long selectedListingId;
	private WSAction wsAction;	
	private AdDto selectedListing;
	private ImageDto image;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_listing);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//set mode
		CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_POST_LISTING);
		//location manager 
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Gallery
		gallery = (Gallery) findViewById(R.id.galleryActPostListingPhotos);
		drawables = new ArrayList<Drawable>();
		galImageAdapter = new GalleryImageAdapter(this, drawables);
		gallery.setAdapter(galImageAdapter);

		// Map
		mapView = (MapView) findViewById(R.id.mapviewActPostListingMapLocate);
		mapView.setBuiltInZoomControls(true);
		// satellite or 2d mode
		mapView.setSatellite(true);
		mapController = mapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view
		btnLocateOnMap = (ImageButton) findViewById(R.id.btnActPostListingLocateAddress);
		btnLocateOnMap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (showMap) {
					mapView.setVisibility(MapView.VISIBLE);
					showMap = false;
				} else {
					mapView.setVisibility(MapView.GONE);
					showMap = true;
				}

			}
		});
		// Add photos
		btnAddPhotos = (Button) findViewById(R.id.btnActPostListingAddPhotos);
		btnAddPhotos.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				pickImage();
			}
		});
		
		btnPost = (Button) findViewById(R.id.btnActPostListingPost);
		btnPost.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (validateFields()) {
					if(CURRENT_MODE == ACT_MODE_UPDATE_LISTING){
						new PostListingTask().execute(ACT_MODE_UPDATE_LISTING);
					}else if (CURRENT_MODE == ACT_MODE_POST_LISTING) {
						new PostListingTask().execute(ACT_MODE_POST_LISTING);
					}
				}
			}
		});
		btnSelCategory = (Button) findViewById(R.id.btnActPostListingCategory);
		btnSelCategory.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent selCatIntent = new Intent(PostListingActivity.this, BrowseCategoriesActivity.class);
				selCatIntent.putExtra("act_mode", BrowseCategoriesActivity.ACT_MODE_GET_CATEGORY);
				startActivityForResult(selCatIntent, REQ_SELECT_CATEGORY);
			}
		});
		btnExpiary = (Button) findViewById(R.id.btnActPostListingAddExpiary);
		btnExpiary.setText(Utility.convertShortDateToString(new Date()));
		btnExpiary.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(D_DATE);
			}
		});
		//data
		edtTitle = (EditText) findViewById(R.id.edtActPostListingListTitle);
		edtSubTitle = (EditText) findViewById(R.id.edtActPostListingSubTitle);
		edtDescription = (EditText) findViewById(R.id.edtActPostListingDescription);
		edtCondition = (EditText) findViewById(R.id.edtActPostListingCondition);
		edtPrice = (EditText) findViewById(R.id.edtActPostListingPrice);
		edtZip = (EditText) findViewById(R.id.edtActPostListingZip);
		edtState = (EditText) findViewById(R.id.edtActPostListingState);
		edtCounty = (EditText) findViewById(R.id.edtActPostListingCounty);
		edtCity = (EditText) findViewById(R.id.edtActPostListingCity);
		edtArea = (EditText) findViewById(R.id.edtActPostListingArea);
		edtLatitude =  (EditText) findViewById(R.id.edtActPostListingLatitude);
		edtLongitude = (EditText) findViewById(R.id.edtActPostListingLongitude);
		
		/*txtTitle = (TextView) findViewById(R.id.txtActPostListingListTitle);
		txtSubTitle = (TextView) findViewById(R.id.txtActPostListingSubTitle);
		txtCategory = (TextView) findViewById(R.id.txtActPostListingCategory);
		txtDescription = (TextView) findViewById(R.id.txtActPostListingDescription);
		txtCondition = (TextView) findViewById(R.id.txtActPostListingCondition);
		txtPrice = (TextView) findViewById(R.id.txtActPostListingPrice);
		txtZip = (TextView) findViewById(R.id.txtActPostListingZip);
		txtState = (TextView) findViewById(R.id.txtActPostListingState);
		txtCounty = (TextView) findViewById(R.id.txtActPostListingCounty);
		txtCity = (TextView) findViewById(R.id.txtActPostListingCity);
		txtArea = (TextView) findViewById(R.id.txtActPostListingArea);
		txtLatitude = (TextView) findViewById(R.id.txtActPostListingLatitude);
		txtLongitude = (TextView) findViewById(R.id.txtActPostListingLongitude);*/
		
		if (CURRENT_MODE == ACT_MODE_UPDATE_LISTING) {
			selectedListingId = getIntent().getLongExtra("ad_id", 0);
			btnPost.setText(getResources().getString(R.string.label_update));
			new PostListingTask().execute(ACT_MODE_GET_LISTING);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);
		locProvider = locationManager.getBestProvider(criteria, false);
		final boolean locProviderEnabled = locationManager.isProviderEnabled(locProvider);

	    if (!locProviderEnabled) {
	    	ERROR_CODE = Constants.ERROR_ENABLE_LOCATION_PROVIDER;
	    	showDialog(D_ERROR);	        
	    }
	    Location location = locationManager.getLastKnownLocation(locProvider);
	    // Initialize the location fields
	    if (location != null) {
	      Utility.showLongToast(this, locProvider + getResources().getString(R.string.msg_postlisting_provider_selected));
	      onLocationChanged(location);
	    } else {
	      edtLatitude.setHint(getResources().getString(R.string.hint_post_listing_location_unavailable));
	      edtLongitude.setHint(getResources().getString(R.string.hint_post_listing_location_unavailable));
	    }
	}
	/* Request updates at startup */
	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(locProvider, 400, 1, this);
	  }

	  /* Remove the locationlistener updates when Activity is paused */
	  @Override
	  protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	  }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_SELECT_CATEGORY) {
			if (resultCode == Activity.RESULT_OK) {
				 categoryId = data.getLongExtra("cat_id", -1);
				 categoryName = data.getStringExtra("category_name").trim().
						 equalsIgnoreCase("")?getResources().getString(R.string.code_category_other): data.getStringExtra("category_name");
				if (categoryName != null) {
					btnSelCategory.setText(categoryName);
				}
			} else {

			}
		} else if (requestCode == REQ_GET_IMAGE && resultCode == Activity.RESULT_OK){
            try {               
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                image = new ImageDto(bitmap);
                drawables.clear();
                drawables.add(new BitmapDrawable(getResources(), bitmap));
                galImageAdapter.notifyDataSetChanged();
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(PostListingActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(PostListingActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS || ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS){
						finish();
					}else if (ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER) {
						enableLocationSettings();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	if(id == D_DATE){
    		DatePickerDialog dateDg = new DatePickerDialog(PostListingActivity.this, new OnDateSetListener() {
				
				public void onDateSet(DatePicker arg0, int year, int month, int date) {
					if (year >= calendar.get(Calendar.YEAR) 
							&& (year > calendar.get(Calendar.YEAR) || month >= calendar.get(Calendar.MONTH))
							&& (year > calendar.get(Calendar.YEAR) || month > calendar.get(Calendar.MONTH) 
							|| date > calendar.get(Calendar.DAY_OF_MONTH))) {
						btnExpiary.setText((month>9? (month+1): "0"+(month+1))+"/"+(date>9? date: "0"+date)+"/"+year);
					} else {
						ERROR_CODE = ERROR_DATE_VALIDATION;
						showDialog(D_ERROR);
					}
										
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			return dateDg;
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_POST_LISTING){
				message = (String) getResources().getText(R.string.error_postlisting);
			}else if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LOCATION){
				message = (String) getResources().getText(R.string.error_postlisting_get_location);
			}else if(ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER){
				message = (String) getResources().getText(R.string.msg_postlisting_enable_provider);
			}else if(ERROR_CODE == ERROR_DATE_VALIDATION){
				message = (String) getResources().getText(R.string.msg_validation_date_higher);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UPDATE_LISTING){
				message = (String) getResources().getText(R.string.error_update_listing);
			}else if(ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_update_success);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/**
	 * 
	 * @author avinash
	 * Class to handle post listing server communication
	 */
	class PostListingTask extends AsyncTask<Integer, Integer, PostListingResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(D_PROGRESS);
		}

		@Override
		protected PostListingResultWrapper doInBackground(Integer... params) {
			PostListingResultWrapper wrapper = new PostListingResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_POST_LISTING)) {
					wrapper = wsAction.postListing(((VeneficaApplication)getApplication()).getAuthToken(), getListingDetails(null));
				}else if (params[0].equals(ACT_MODE_GET_LISTING)) {
					ListingDetailsResultWrapper detailsWrapper = wsAction.getListingById(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId);
					wrapper.listing = detailsWrapper.listing;
					wrapper.result = detailsWrapper.result;
				}else if (params[0].equals(ACT_MODE_UPDATE_LISTING)) {
					wrapper = wsAction.updateListing(((VeneficaApplication)getApplication()).getAuthToken(), getListingDetails(selectedListing));
				}
			}catch (IOException e) {
				Log.e("PostListingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("PostListingTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		
		@Override
		protected void onPostExecute(PostListingResultWrapper result) {
			super.onPostExecute(result);
			dismissDialog(D_PROGRESS);
			if(result.data == null && result.result == -1 && result.listing == null){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				setListingDetails(result.listing);
			}else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);				
			}
		}
	}

	/**
	 * Method to get listing data from edit fields 
	 * @return AdDto
	 */
	private AdDto getListingDetails(AdDto listing) {
		if (listing == null) {
			listing = new AdDto();
		}		
		listing.setTitle(edtTitle.getText().toString());
		listing.setCategory(categoryName);
		listing.setDescription(edtDescription.getText().toString());
		listing.setCategoryId(categoryId);
		listing.setPrice(new BigDecimal(edtPrice.getText().toString()));
		listing.setLatitude(Math.round(Double.parseDouble(edtLatitude.getText().toString())));
		listing.setLongitude(Math.round(Double.parseDouble(edtLongitude.getText().toString())));
		listing.setCanMarkAsSpam(true);
		listing.setCanRate(true);
//		listing.setCreatedAt(Utility.converDateToString(new Date()));
		listing.setExpired(false);
		listing.setExpiresAt(new Date(btnExpiary.getText().toString()));
		listing.setInBookmars(false);
		listing.setNumAvailProlongations(0);
		listing.setOwner(true);
		listing.setWanted(false);
		listing.setNumViews(0L);
		listing.setRating(1.0f);
		if (image != null) {
			listing.setImage(image);
		}
		return listing;
	}

	public void onLocationChanged(Location location) {
		edtLatitude.setText(location.getLatitude()+"");
		edtLongitude.setText(location.getLongitude()+"");
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
	 * Method to show options to enable location provider
	 */
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
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
    	
    	if(!vaildator.validateField(edtTitle, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_title).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	/*if(!vaildator.validateField(edtSubTitle, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtSubTitle.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}*/
    	if(categoryId == 0L){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_category).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_category));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtDescription, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_description).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	/*if(!vaildator.validateField(edtCondition, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtCondition.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}*/
    	if(!vaildator.validateField(edtPrice, Pattern.compile(InputFieldValidator.phonePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_postlisting_price).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_phone));
    		message.append("\n");
    	}
    	/*if(!vaildator.validateField(edtLatitude, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(txtLatitude.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtLongitude, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(txtLongitude.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}*/
    	/*if(!vaildator.validateField(edtZip, Pattern.compile(InputFieldValidator.zipCodePatternRegx))){
    		result = false;
    		message.append(txtZip.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_zipcode));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtState, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtState.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCounty, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtCounty.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCity, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtCity.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtArea, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(txtArea.getText().toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}*/
    	if (!result) {
			Utility.showLongToast(this, message.toString());
		}/*else{
			getListingDetails();
		}*/
		return result;    	
    }
    /**
     * Get image
     */
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQ_GET_IMAGE);
    }
    private void setListingDetails(AdDto listing) {
    	selectedListing = listing;
		edtTitle.setText(listing.getTitle());
		btnSelCategory.setText(listing.getCategory());
		edtDescription.setText(listing.getDescription());
		edtPrice.setText(listing.getPrice().toString());
		edtLatitude.setText(listing.getLatitude()+"");
		edtLongitude.setText(listing.getLongitude()+"");
		btnExpiary.setText(Utility.convertShortDateToString(listing.getExpiresAt()));
		categoryName = listing.getCategory();
		categoryId = listing.getCategoryId();
	}
}
