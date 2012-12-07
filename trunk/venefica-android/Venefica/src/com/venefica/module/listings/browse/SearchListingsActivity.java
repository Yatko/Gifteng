package com.venefica.module.listings.browse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.OverlayItem;
import com.venefica.activity.R;
import com.venefica.module.dashboard.ISlideMenuCallback;
import com.venefica.module.dashboard.SlideMenuView;
import com.venefica.module.listings.ListingDetailsActivity;
import com.venefica.module.listings.ListingListAdapter;
import com.venefica.module.listings.MapItemizedOverlay;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.ImageDownloadManager;
import com.venefica.services.AdDto;
import com.venefica.services.FilterDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchListingsActivity extends VeneficaMapActivity implements
ISlideMenuCallback, LocationListener{
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
	/**
	 * Slide menu
	 */
	private SlideMenuView slideMenuView;
	/**
	 * Search view in actionbar
	 */
	private SearchView searchView;
	/**
	 * Groups to show hide
	 */
	private RelativeLayout mapLayout;
	private RelativeLayout tileLayout;
	private LinearLayout toggleLayout;
	private ImageButton toggleButtonTile;
	private LinearLayout toggleLayoutMap;
	private ImageButton toggleButtonMap;
	private boolean isMapShown;
	/**
	 * Map 
	 */
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private String locProvider;
	private Location location;
	private MapItemizedOverlay overlayItems;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_listings);
        //slide menu
        slideMenuView = (SlideMenuView) findViewById(R.id.sideNavigationViewActSearchListing);
		slideMenuView.setMenuItems(R.menu.slide_menu);
		slideMenuView.setMenuClickCallback(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.setSlideMenuView(slideMenuView);
        
		//Create the search view
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint(getResources().getString(R.string.hint_search_listing));
        
        //Map 
        mapLayout = (RelativeLayout) findViewById(R.id.layActSearchListingsMap);
        mapView = (MapView) findViewById(R.id.mapviewActSearchListings);
        mapView.setBuiltInZoomControls(true);
        mapView.setTraffic(true);
        mapView.setSatellite(false);
        mapController = mapView.getController();
        mapController.setZoom(12);
        
        //Toggle Button to view Tiles
        toggleButtonTile = new ImageButton(this);
        toggleButtonTile.setClickable(true);
        toggleButtonTile.setImageResource(R.drawable.picture_view);
        toggleLayout = (LinearLayout) findViewById(R.id.layActSearchListingsToggle);
        toggleLayout.addView(toggleButtonTile,  new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT));
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
        toggleButtonMap = new ImageButton(this);
        toggleButtonMap.setClickable(true);
        toggleButtonMap.setImageResource(R.drawable.map_view);
        toggleLayoutMap = (LinearLayout) findViewById(R.id.layActSearchListingsToggleMap);
        toggleLayoutMap.addView(toggleButtonMap,  new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT));
        toggleButtonMap.setOnClickListener(new View.OnClickListener() {
			
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
        
        //List and tile view
        tileLayout = (RelativeLayout) findViewById(R.id.layActSearchListingsTile);
        gridViewListings = (GridView) findViewById(R.id.listActSearchListings);
        listings = getDemoListings();
        
		listingsListAdapter = new ListingListAdapter(this, listings, true);
		gridViewListings.setAdapter(listingsListAdapter);
		gridViewListings.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(SearchListingsActivity.this, ListingDetailsActivity.class);
				startActivity(intent);
			}
		});
		overlayItems = new MapItemizedOverlay(getResources().getDrawable(R.drawable.locate_place), this);     				
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
    	//Get location provider 
    	Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locProvider = locationManager.getBestProvider(criteria, false);
		final boolean locProviderEnabled = locationManager.isProviderEnabled(locProvider);

	    if (!locProviderEnabled) {
	    	ERROR_CODE = Constants.ERROR_ENABLE_LOCATION_PROVIDER;
	    	showDialog(D_ERROR);	        
	    }
	    //Get last location
	    location = locationManager.getLastKnownLocation(locProvider);
	    updateMap(location);
//	    new SearchListingTask().execute(ACT_MODE_SEARCH_BY_CATEGORY);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//Get current location	    
	    locationManager.requestLocationUpdates(locProvider, Constants.LOCATION_UPDATE_PERIOD, 0, this);
	    updateMap(location);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        ImageDownloadManager.getImageDownloadManagerInstance().reset();
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
			}else if(ERROR_CODE == ERROR_NO_RESULTS){
				message = (String) getResources().getText(R.string.error_search_listings_no_data);
			}else if(ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER){
				message = (String) getResources().getText(R.string.msg_postlisting_enable_provider);
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
							, 1, 5, getFilterOptions());
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
	/**
	 * Method to set Filter for search listings
	 * @return
	 */
	private FilterDto getFilterOptions(){
		FilterDto filter = new FilterDto();
		filter.setDistance(150);
		filter.setLatitude(new Double(location.getLatitude()));
		filter.setLongitude(new Double(location.getLongitude()));
		filter.setMaxPrice(new BigDecimal(5000000.00));
		filter.setMinPrice(new BigDecimal(0));
		filter.setWanted(true);
		filter.setSearchString("benz");
		filter.setHasPhoto(false);
		List<Long> cats = new ArrayList<Long>();
		cats.add(new Long(3));
		filter.setCategories(cats);
		return filter;		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(getResources().getString(R.string.label_filter))
            .setIcon(R.drawable.browse_dark)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        menu.add(getResources().getString(R.string.label_filter_setting))
        .setIcon(R.drawable.settings_dark)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private List<AdDto> getDemoListings(){
		List<AdDto> listings= new ArrayList<AdDto>();
		AdDto listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		ImageDto imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://shrimprg.stanford.edu/sample2.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://i.space.com/images/i/22194/iFF/mars_sample_return.jpg?1348688790");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://2.imimg.com/data2/VF/HW/MY-1206776/spectro-sample-polishers-250x250.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Apollo_15_Genesis_Rock.jpg/300px-Apollo_15_Genesis_Rock.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://samplesally.com/wp-content/uploads/2012/12/Tess-Giberson-Sample-Sale_2.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://media.purex.com/fsp/hero-both2.png");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://epswww.unm.edu/iom/sims/sample.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://www.penofin.com/img/free-penofin-samples.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://dailysavings.files.wordpress.com/2012/12/free-loreal-everpure.jpg?w=500");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://2.imimg.com/data2/BJ/JM/MY-5141725/plastic-sample-bottle-250x250.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		
		listing = new AdDto();
		listing.setTitle("Demo Listing");
		listing.setPrice(new BigDecimal(1234));
		listing.setDescription("Demo description");
		imgDto = new ImageDto();
		imgDto.setUrl("http://gimp.open-source-solution.org/manual/images/filters/examples/color-taj-sample-colorize.jpg");
		listing.setImage(imgDto);
		listings.add(listing);
		return listings;
	}

	/**
	 * Show current location on map
	 * @param location
	 */
	private void updateMap(Location location){
		if (isMapShown) {
			GeoPoint currLoc = new GeoPoint((int)(location.getLatitude() * 1E6), (int)(location.getLongitude() * 1E6));
			mapController.animateTo(currLoc);
			OverlayItem overlayItem = new OverlayItem(currLoc, "My Location", "");
			overlayItems.clear();
			overlayItems.addOverlay(overlayItem);
			mapView.getOverlays().add(overlayItems);
			mapController.zoomToSpan(overlayItems.getLatSpanE6(), overlayItems.getLonSpanE6());
			mapView.invalidate();
		}	
	}
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		updateMap(location);
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
}
