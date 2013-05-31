package com.venefica.module.listings.browse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.venefica.module.dashboard.ISlideMenuCallback;
import com.venefica.module.dashboard.SlideMenuView;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.MapItemizedOverlay;
import com.venefica.module.listings.ListingListAdapter.TileButtonClickListener;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.OnSingleTapListener;
import com.venefica.module.map.TapControlledMapView;
import com.venefica.module.network.WSAction;
import com.venefica.module.settings.SettingsActivity;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.AdDto.AdType;
import com.venefica.services.CommentDto;
import com.venefica.services.FilterDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class SearchListingsActivity extends VeneficaMapActivity implements
ISlideMenuCallback, LocationListener, TileButtonClickListener, OnClickListener{
	/**
	 * List to show listings
	 */
	private GridView gridViewListings;
	/**
	 * List adapter
	 */
	private ListingListAdapter listingsListAdapter;
	/**
	 * Listings list
	 */
	private List<AdDto> listings;
	/**
	 * Used for paging
	 */
	private long lastAdId = -1;
	/**
	 * List page size
	 */
	private int LIST_PAGE_SIZE = 11; 
	/**
	 * set true to  load items on scroll
	 */
	private boolean isLoadOnScroll = false;
	/**
	 * set true to  if available more items to download
	 */
	private boolean hasMoreListings = false;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Activity modes
	 */
	public static final int ACT_MODE_SEARCH = 3001;
	public static final int ACT_MODE_SEARCH_BY_CATEGORY = 3002;
	public static final int ACT_MODE_DOWNLOAD_BOOKMARKS = 3003;
	public static final int ACT_MODE_REMOVE_BOOKMARK = 3004;
	public static final int ACT_MODE_DOWNLOAD_MY_LISTINGS = 3006;
	public static final int ACT_MODE_BOOKMARK_LISTINGS = 3007;
	public static final int ACT_MODE_ADD_COMMENT = 3008;
	private static int CURRENT_MODE;

	private WSAction wsAction;
	/**
	 * Slide menu
	 */
	private SlideMenuView slideMenuView;
	/**
	 * Search view in actionbar
	 */
	private EditText searchView;
	/**
	 * Groups to show hide
	 */
	private RelativeLayout mapLayout;
	private RelativeLayout tileLayout;
	private ImageButton toggleButtonTile;
	private ImageButton toggleButtonMap;
	private TextView txtTitleTile;
	private boolean isMapShown;
	/**
	 * Map 
	 */
	private TapControlledMapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private String locProvider;
	private Location location;
	/*
	 * business
	 */
	private MapItemizedOverlay<?> businessOverlayItems;
	/**
	 * members
	 */
	private MapItemizedOverlay<?> membersOverlayItems;
	/**
	 * Current location
	 */
	private MapItemizedOverlay<?> overlayItems;
	/**
	 * Shared Preferences
	 */
	SharedPreferences prefs;
	/**
	 * Filter settings
	 */
	private FilterDto filter;
	/**
	 * flag to use location from
	 */
	private boolean useCurrentLocation;
	/**
	 * selected listing id
	 */
	private long selectedListingId;
	/**
	 * comment layout
	 */
	private LinearLayout laySend;
	private boolean isSendMsgVisible = false;
	private boolean isOwner;
	private EditText edtComment;
	private ImageButton imgBtnSendComment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        setContentView(R.layout.activity_search_listings);
        //get preferences
        prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
        //set mode 
        CURRENT_MODE = getIntent().getExtras().getInt("act_mode");
        //slide menu
        slideMenuView = (SlideMenuView) findViewById(R.id.sideNavigationViewActSearchListing);
		slideMenuView.setMenuItems(R.menu.slide_menu);
		slideMenuView.setMenuClickCallback(this);
		slideMenuView.setUserDetails(((VeneficaApplication)getApplication()).getUser());
        super.setSlideMenuView(slideMenuView);
        
        //comment 
        laySend = (LinearLayout) findViewById(R.id.layActSearchListingsSend);
        edtComment = (EditText) findViewById(R.id.edtActSearchListingsMessage);
        imgBtnSendComment = (ImageButton) findViewById(R.id.imgBtnActSearchListingsSend);
        imgBtnSendComment.setOnClickListener(this);
		//Create the search view
        searchView = (EditText) getLayoutInflater().inflate(R.layout.view_searchbar, null);
        searchView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 
                LayoutParams.FILL_PARENT));
        searchView.requestFocusFromTouch();
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					getCurrentLocation();
					if (locProvider != null) {
						getFilterOptions();
						lastAdId = -1;
						isLoadOnScroll = false;
						// hide virtual keyboard
						InputMethodManager imm = (InputMethodManager) SearchListingsActivity.this
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
						new SearchListingTask().execute(CURRENT_MODE);
					}					
		            return true;
		        }
				return false;
			}
		});
        //Map 
        mapLayout = (RelativeLayout) findViewById(R.id.layActSearchListingsMap);
        mapView = (TapControlledMapView) findViewById(R.id.mapviewActSearchListings);
		// dismiss balloon upon single tap of MapView (iOS behavior)
		mapView.setOnSingleTapListener(new OnSingleTapListener() {
			@Override
			public boolean onSingleTap(MotionEvent e) {
				businessOverlayItems.hideAllBalloons();
				membersOverlayItems.hideAllBalloons();
				overlayItems.hideAllBalloons();
				return true;
			}
		});
        mapView.setBuiltInZoomControls(true);
        mapView.setTraffic(true);
        mapView.setSatellite(false);
        mapController = mapView.getController();
        mapController.setZoom(16);
        
        //Toggle Button to view Tiles
        txtTitleTile = (TextView) findViewById(R.id.txtActSearchListingsTitleTile);
        txtTitleTile.setText(getResources().getString(R.string.label_dashboard_browse));
        toggleButtonTile = (ImageButton) findViewById(R.id.btnActSearchListingsTile);
        toggleButtonTile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tileLayout.getVisibility() == ViewGroup.VISIBLE){
					tileLayout.setVisibility(ViewGroup.GONE);
					mapLayout.setVisibility(ViewGroup.VISIBLE);
					isMapShown = true;										
				}else {
					tileLayout.setVisibility(ViewGroup.VISIBLE);
					mapLayout.setVisibility(ViewGroup.GONE);
					isMapShown = false;
				}
			}
		});
      //Toggle Button to view Map
        toggleButtonMap = (ImageButton) findViewById(R.id.btnActSearchListingsMap);
        toggleButtonMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tileLayout.getVisibility() == ViewGroup.VISIBLE){
					tileLayout.setVisibility(ViewGroup.GONE);
					mapLayout.setVisibility(ViewGroup.VISIBLE);
					isMapShown = true;
					if (listings != null && listings.size() > 0) {
						updateMap(listings);
					}
				}else {
					tileLayout.setVisibility(ViewGroup.VISIBLE);
					mapLayout.setVisibility(ViewGroup.GONE);
					isMapShown = false;
				}
			}
		});
        
        //List and tile view
        tileLayout = (RelativeLayout) findViewById(R.id.layActSearchListingsTile);
        listings = new ArrayList<AdDto>();
        gridViewListings = (GridView) findViewById(R.id.listActSearchListings);
        
		listingsListAdapter = new ListingListAdapter(this, listings, true);
		gridViewListings.setAdapter(listingsListAdapter);
		gridViewListings.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(SearchListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});		
		businessOverlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.red_dot), mapView){
			@Override
			public boolean onTouchEvent(MotionEvent event, MapView mapView) {
				//get location when user lifts the finger
				if (event.getAction() == MotionEvent.ACTION_UP) {
					GeoPoint touchedPoint = mapView.getProjection().fromPixels(
							(int) event.getX(), (int) event.getY());
					if (location != null && !useCurrentLocation) {
						location.setLatitude(touchedPoint.getLatitudeE6() / 1E6);
						location.setLongitude(touchedPoint.getLongitudeE6() / 1E6);											
					}
				}
				return false;
			}
		}; 
		businessOverlayItems.setShowClose(false);
		businessOverlayItems.setShowDisclosure(false);
//		businessOverlayItems.setSnapToCenter(true);
		membersOverlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.icon_location), mapView){
			@Override
			public boolean onTouchEvent(MotionEvent event, MapView mapView) {
				//get location when user lifts the finger
				if (event.getAction() == MotionEvent.ACTION_UP) {
					GeoPoint touchedPoint = mapView.getProjection().fromPixels(
							(int) event.getX(), (int) event.getY());
					if (location != null && !useCurrentLocation) {
						location.setLatitude(touchedPoint.getLatitudeE6() / 1E6);
						location.setLongitude(touchedPoint.getLongitudeE6() / 1E6);											
					}
				}
				return false;
			}
		}; 
		membersOverlayItems.setShowClose(false);
		membersOverlayItems.setShowDisclosure(false);
//		membersOverlayItems.setSnapToCenter(true);
		overlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.blue_dot), mapView);
		overlayItems.setShowClose(false);
		overlayItems.setShowDisclosure(false);
		overlayItems.setSnapToCenter(true);
		toggleMapView(false);
    }
        
    /**
     * Toggle map view and tile view
     * @param showMap
     */
    private void toggleMapView(boolean showMap){
    	if (showMap) {
    		mapLayout.setVisibility(ViewGroup.VISIBLE);
        	tileLayout.setVisibility(ViewGroup.GONE);
		} else {
			mapLayout.setVisibility(ViewGroup.GONE);
	    	tileLayout.setVisibility(ViewGroup.VISIBLE);
		}
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	//get location service
    	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	if (CURRENT_MODE == ACT_MODE_SEARCH_BY_CATEGORY) {
    		getCurrentLocation();
        	//Get Filter settings
    		getFilterOptions();
		}
    	
	    if(WSAction.isNetworkConnected(this)){
	    	if (CURRENT_MODE == ACT_MODE_DOWNLOAD_BOOKMARKS || CURRENT_MODE == ACT_MODE_DOWNLOAD_MY_LISTINGS) {
	    		isLoadOnScroll = false;
	    		new SearchListingTask().execute(CURRENT_MODE);
	    		toggleButtonMap.setVisibility(View.GONE);
			} else if (CURRENT_MODE == ACT_MODE_SEARCH_BY_CATEGORY && locProvider != null && lastAdId == -1) {
				isLoadOnScroll = false;
				new SearchListingTask().execute(CURRENT_MODE);
			}
	    }else{
	    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
	    	showDialog(D_ERROR);	 
	    }
	    if (CURRENT_MODE == ACT_MODE_DOWNLOAD_BOOKMARKS) {
	    	txtTitleTile.setText(getResources().getText(R.string.label_bookmark_listing_title));
		}else if (CURRENT_MODE == ACT_MODE_DOWNLOAD_MY_LISTINGS) {
	    	txtTitleTile.setText(getResources().getText(R.string.label_my_listing_title));
		}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//Get current location	
    	if(useCurrentLocation && (locProvider != null)
    			&& !(CURRENT_MODE == ACT_MODE_DOWNLOAD_BOOKMARKS || CURRENT_MODE == ACT_MODE_DOWNLOAD_MY_LISTINGS)){
    		locationManager.requestLocationUpdates(locProvider, Constants.LOCATION_UPDATE_PERIOD, Constants.LOCATION_UPDATE_MIN_DISTANCE, this);
    	}	    
    }
    @Override
    protected void onPause() {    	
    	super.onPause();
        //Flush image cache
        ((VeneficaApplication)getApplication()).getImgManager().flushCache();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	//Stop location updates
        locationManager.removeUpdates(this);
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
					if (ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER) {
						showLocationSettings();
					} else if (ERROR_CODE == Constants.ERROR_SIGN_OUT_APPLICATION) {
						//clear image cache before exit
						((VeneficaApplication)getApplication()).getImgManager().reset();
						System.exit(0);
					}
					dismissDialog(D_ERROR);
				}
			});	
			builder.setNegativeButton(R.string.label_btn_cancel, new DialogInterface.OnClickListener() {
				@Override
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
    		((AlertDialog) dialog).getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LISTINGS){
				message = (String) getResources().getText(R.string.error_search_listings);
			}else if(ERROR_CODE ==Constants.ERROR_NO_DATA){
				clearUIWhenNoData();
				message = (String) getResources().getText(R.string.error_search_listings_no_data);
			}else if(ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER){
				((AlertDialog) dialog).getButton(Dialog.BUTTON_POSITIVE)
					.setText(getResources().getText(R.string.label_btn_yes));
				((AlertDialog) dialog).getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
				message = (String) getResources().getText(R.string.msg_enable_location_provider);				
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_get_bookmarks);
			}else if(ERROR_CODE == Constants.ERROR_NO_BOOKMARKS){
				clearUIWhenNoData();
				message = (String) getResources().getText(R.string.error_no_bookmarks);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_MY_LISTINGS){
				message = (String) getResources().getText(R.string.error_get_my_listings);
			}else if(ERROR_CODE == Constants.ERROR_SIGN_OUT_APPLICATION){
				((AlertDialog) dialog).getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
				message = (String) getResources().getText(R.string.msg_app_exit);
			}else if (ERROR_CODE == Constants.RESULT_BOOKMARKS_LISTING_SUCCESS) {
				message = (String) getResources().getText(R.string.msg_detail_listing_add_fav_success);
			}else if (ERROR_CODE == Constants.ERROR_RESULT_BOOKMARKS_LISTING) {
				message = (String) getResources().getText(R.string.msg_detail_listing_add_fav_failed);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_ADD_COMMENT){
				message = (String) getResources().getText(R.string.error_add_comment);
			}else if(ERROR_CODE == Constants.RESULT_ADD_COMMENT_SUCCESS){
				setMessageLayoutVisiblity(false);
				message = (String) getResources().getText(R.string.msg_add_comment_success);
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
			setSupportProgressBarIndeterminateVisibility(true);
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
							, lastAdId, LIST_PAGE_SIZE, filter);
				} else if (params[0].equals(ACT_MODE_DOWNLOAD_BOOKMARKS)) {
					wrapper = wsAction.getBookmarkedListings(((VeneficaApplication)getApplication()).getAuthToken());
				} else if (params[0].equals(ACT_MODE_DOWNLOAD_MY_LISTINGS)) {
					wrapper = wsAction.getMyListings(((VeneficaApplication)getApplication()).getAuthToken());
				} else if (params[0].equals(ACT_MODE_BOOKMARK_LISTINGS)) {
					ListingDetailsResultWrapper res = 
					wsAction.bookmarkListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
					wrapper.result = res.result; 
				}else if (params[0].equals(ACT_MODE_ADD_COMMENT)) {
					ListingDetailsResultWrapper res = wsAction.addCommentToListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId, getComment());
					wrapper.result = res.result;
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
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.listings == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTINGS_SUCCESS && result.listings != null
					&& result.listings.size() > 0) {
				if (!isLoadOnScroll && !hasMoreListings) {
					listings.clear();
				}
				if(result.listings.size() >= LIST_PAGE_SIZE && CURRENT_MODE == ACT_MODE_SEARCH_BY_CATEGORY){
					hasMoreListings = true;
				}else {
					hasMoreListings = false;
				}
				
				listings.addAll(result.listings);
				updateMap(listings);
				listingsListAdapter.notifyDataSetChanged();
				lastAdId = result.listings.get(result.listings.size()-1).getId();
				isLoadOnScroll = false;
			}else if(!isLoadOnScroll){
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}
	/**
	 * Method to set Filter for search listings
	 * @return
	 */
	private void getFilterOptions(){
		useCurrentLocation = prefs.getBoolean(getResources().getString(R.string.pref_key_use_current_location), true);
		filter = new FilterDto();		
		filter.setDistance(prefs.getInt(getResources().getString(R.string.pref_key_use_miles), Constants.PREF_DEF_VAL_MILES));
		if (location != null) {
			filter.setLatitude(new Double(location.getLatitude()));
			filter.setLongitude(new Double(location.getLongitude()));
		}		
		filter.setMaxPrice(new BigDecimal(
				Double.parseDouble(prefs.getString(getResources().getString(R.string.pref_key_price_max), Constants.PREF_DEF_VAL_MAX_PRICE))));
		filter.setMinPrice(new BigDecimal(
				Double.parseDouble(prefs.getString(getResources().getString(R.string.pref_key_price_min), Constants.PREF_DEF_VAL_MIN_PRICE))));
//		filter.setWanted(false);
		filter.setSearchString(searchView.getText().toString());
		filter.setHasPhoto(true);
		Long cat = prefs.getLong(Constants.PREF_KEY_CATEGORY_ID, Constants.PREF_DEF_VAL_CATEGORY);
		//Load all if no category set
		if (cat == Constants.PREF_DEF_VAL_CATEGORY) {
			filter.setCategories(null);
		} else {
			List<Long> cats = new ArrayList<Long>();
			cats.add(cat);		
			filter.setCategories(cats);
		}		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getResources().getString(R.string.label_search))
            .setIcon(R.drawable.icon_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        menu.add(getResources().getString(R.string.label_search_setting))
        .setIcon(R.drawable.settings_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().equalsIgnoreCase(getResources().getString(R.string.label_search_setting))) {
			Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
			settingsIntent.putExtra("act_mode",SettingsActivity.ACT_MODE_FILTER_SETTINGS);
	    	startActivity(settingsIntent);
		} 
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}	
	
	/**
	 * Show current location on map
	 * @param location
	 */
	private void updateMap(List<AdDto> listings){
		if (isMapShown) {
			businessOverlayItems.clear();
			membersOverlayItems.clear();
			MapItemizedOverlay<?> allOverlays = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.icon_location), mapView);
			for (AdDto adDto : listings) {				
				if (adDto.getType() == AdType.BUSINESS) {
					businessOverlayItems.addOverlay(new ListingOverlayItem(new GeoPoint((int)(adDto.getAddress().getLatitude() * 1E6)
							, (int)(adDto.getAddress().getLongitude() * 1E6)), adDto.getTitle()
							, adDto.getDescription(), adDto.getId()
							, Constants.PHOTO_URL_PREFIX + adDto.getImage().getUrl()));
				} else {				
					membersOverlayItems.addOverlay(new ListingOverlayItem(new GeoPoint((int)(adDto.getAddress().getLatitude() * 1E6)
							, (int)(adDto.getAddress().getLongitude() * 1E6)), adDto.getTitle()
							, adDto.getDescription(), adDto.getId()
							, Constants.PHOTO_URL_PREFIX + adDto.getImage().getUrl()));
				}
				allOverlays.addOverlay(new ListingOverlayItem(new GeoPoint((int)(adDto.getAddress().getLatitude() * 1E6)
						, (int)(adDto.getAddress().getLongitude() * 1E6)), adDto.getTitle()
						, adDto.getDescription(), adDto.getId()
						, Constants.PHOTO_URL_PREFIX + adDto.getImage().getUrl()));
			}
			mapView.getOverlays().add(businessOverlayItems);
			mapView.getOverlays().add(membersOverlayItems);
			updateMap(this.location);
			mapController.zoomToSpan(allOverlays.getLatSpanE6(), allOverlays.getLonSpanE6());
//			mapController.setCenter(currLoc);
			mapView.invalidate();
		}	
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
	@Override
	public void onLocationChanged(Location location) {
		if (location != null && Utility.isBetterLocation(location, this.location)) {
			this.location = location;
			updateMap(location);
		}		
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * @return the CURRENT_MODE
	 */
	public static int getCURRENT_MODE() {
		return CURRENT_MODE;
	}
	/**
	 * Method to remove tiles when no data
	 */
	private void clearUIWhenNoData(){
		listings.clear();			
		listingsListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Helper method to enable GPS
	 */
	private void getCurrentLocation() {
		// Get location provider
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
	
	/**
	 * Method to start location settings 
	 */
	private void showLocationSettings(){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, Constants.ERROR_ENABLE_LOCATION_PROVIDER);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.ERROR_ENABLE_LOCATION_PROVIDER) {
			getCurrentLocation();
			if (locProvider != null) {
				getFilterOptions();
				isLoadOnScroll = false;
				new SearchListingTask().execute(CURRENT_MODE);
			}			
		}
	}
	
	@Override
	public void onSideNavigationItemClick(int itemId) {
		super.onSideNavigationItemClick(itemId);
		switch (itemId) {
		case R.id.slideMenuSignOut:
			ERROR_CODE = Constants.ERROR_SIGN_OUT_APPLICATION;
			showDialog(D_ERROR);
    		break;
		}
	}
	
	/**
	 * helper method to load more items in list view 
	 */
	public void getMoreListings(){
		if (CURRENT_MODE == ACT_MODE_SEARCH_BY_CATEGORY && hasMoreListings) {
			isLoadOnScroll = true;
			if (locProvider != null) {
				getFilterOptions();
				new SearchListingTask().execute(CURRENT_MODE);
			}
		}		
	}
	
	@Override
	public void onBackPressed() {
		if (isSendMsgVisible) {
			setMessageLayoutVisiblity(false);
		}
		// disable app exit on back button press
	}

	@Override
	public void onCommentButtonClick(long adId, boolean isOwner) {
		selectedListingId = adId;
		this.isOwner = isOwner;
		setMessageLayoutVisiblity(true);
	}

	@Override
	public void onFavoriteButtonClick(long adId) {
		selectedListingId = adId;
		new SearchListingTask().execute(ACT_MODE_BOOKMARK_LISTINGS);
	}
	/**
	 * Set send comment layout visibility
	 * @param isVisible
	 */
	private void setMessageLayoutVisiblity(boolean isVisible){
		if (isVisible) {
			laySend.setVisibility(ViewGroup.VISIBLE);
		} else {
			laySend.setVisibility(ViewGroup.GONE);
		}		
		isSendMsgVisible = isVisible;
	}
	/**
	 * Get comments to send
	 * @return commentDto
	 */
	public CommentDto getComment() {
		CommentDto commentDto = new CommentDto();
		commentDto.setCreatedAt(new Date());		
		commentDto.setOwner(this.isOwner);
		commentDto.setText(edtComment.getText().toString());
		return commentDto;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.imgBtnActSearchListingsSend && !edtComment.getText().toString().trim().equals("")) {
			new SearchListingTask().execute(ACT_MODE_ADD_COMMENT);
		}
	}
}
