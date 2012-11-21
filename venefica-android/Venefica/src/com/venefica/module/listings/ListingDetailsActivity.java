package com.venefica.module.listings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.Overlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.venefica.activity.R;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 
 * @author avinash
 * Activity to show detail Listing  
 */
public class ListingDetailsActivity extends MapActivity {
	/**
	 * Gallery to images
	 */
	private Gallery gallery;

    private int selectedImagePosition = 0;
    private long selectedListingId = 0;
    /**
     * Images
     */
    private List<Drawable> drawables;
    /**
     * Adapter for gallery
     */
    private GalleryImageAdapter galImageAdapter;
    /**
     * Text view to show details
     */
    private TextView txtTitle, txtPrice, txtDescription, txtAddress, txtListingDate, 
    			txtExpiaryDate;
    /**
     * Buttons
     */
    private Button btnMakeOffer, btnViewSeller;
    private ImageButton btnBookmark, btnShowOnMap;
    /**
     * Modes
     */
    public static final int ACT_MODE_MY_LISTINGS_DETAILS = 4001;
    public static final int ACT_MODE_SEARCH_LISTINGS_DETAILS = 4002;
    public static final int ACT_MODE_DOWNLOAD_LISTINGS_DETAILS = 4003;
    /**
     * Current mode
     */
    public static int CURRENT_MODE = ACT_MODE_SEARCH_LISTINGS_DETAILS;
    /**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
    /**
     * Map 
     */
    private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private Overlay itemizedoverlay;
	private boolean showMap = true;

	private WSAction wsAction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);
        //set activity mode
        CURRENT_MODE = getIntent().getExtras().getInt("mode");
        selectedListingId = getIntent().getExtras().getLong("ad_id");
        //Details
        txtTitle = (TextView) findViewById(R.id.txtActListingDetailsTitle);
        txtPrice = (TextView) findViewById(R.id.txtActListingPrice);
        txtDescription = (TextView) findViewById(R.id.txtActListingDesc);
        txtAddress = (TextView) findViewById(R.id.txtActListingAddress);
        txtListingDate = (TextView) findViewById(R.id.txtActListingListedOn);
		txtExpiaryDate = (TextView) findViewById(R.id.txtActListingExpiresOn);
        //Seller
		btnViewSeller = (Button) findViewById(R.id.btnActListingDetailsSeller);
		//Bookmark
		btnBookmark = (ImageButton) findViewById(R.id.btnActListingDetailsBookmark);
		btnBookmark.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Utility.showShortToast(ListingDetailsActivity.this, getResources().getString(R.string.msg_detail_listing_add_fav_success));
			}
		});
		btnMakeOffer = (Button) findViewById(R.id.btnActListingDetailsMakeOffer);
		//Map
		mapView = (MapView) findViewById(R.id.mapviewActListingDetails);
		mapView.setBuiltInZoomControls(true);
		// satellite or 2d mode
		mapView.setSatellite(true);
		mapController = mapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view
		btnShowOnMap = (ImageButton) findViewById(R.id.btnActListingDetailsLocateOnMap);
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {
			
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
        //Gallery
        gallery = (Gallery) findViewById(R.id.galleryActListingDetailsPhotos);
        gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent galleryIntent = new Intent(ListingDetailsActivity.this, GalleryActivity.class);
				startActivity(galleryIntent);
			}
		});
        drawables = getImages();
        galImageAdapter = new GalleryImageAdapter(this, drawables);
        gallery.setAdapter(galImageAdapter);
        if (CURRENT_MODE == ACT_MODE_MY_LISTINGS_DETAILS) {
			btnBookmark.setVisibility(View.GONE);
			btnViewSeller.setClickable(false);
			btnMakeOffer.setVisibility(View.GONE);
		}
        new ListingDetailsTask().execute(ACT_MODE_DOWNLOAD_LISTINGS_DETAILS);
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(ListingDetailsActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(ListingDetailsActivity.this);
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
				message = (String) getResources().getText(R.string.error_get_listing_details);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	private void setDetails(AdDto listing) {
		txtTitle.setText(listing.getTitle());
		txtPrice.setText(listing.getPrice()+"");
		txtDescription.setText(listing.getDescription());
//		txtAddress.setText(listing.get);
		txtListingDate.setText(listing.getCreatedAt());
		txtExpiaryDate.setText(Utility.convertShortDateToString(listing.getExpiresAt()));
		btnViewSeller.setText(listing.getCreator().getName());
	}
	private List<Drawable> getImages() {
		drawables = new ArrayList<Drawable>();
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
        drawables.add(getResources().getDrawable(R.drawable.ic_launcher));
		return drawables;
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_listing_details, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	switch (CURRENT_MODE) {
		case ACT_MODE_MY_LISTINGS_DETAILS:
			menu.findItem(R.id.menu_listing_delete).setVisible(true);
			menu.findItem(R.id.menu_listing_end).setVisible(true);
			menu.findItem(R.id.menu_listing_relist).setVisible(true);
			menu.findItem(R.id.menu_listing_update).setVisible(true);
			break;

		default:
			menu.findItem(R.id.menu_listing_delete).setVisible(false);
			menu.findItem(R.id.menu_listing_end).setVisible(false);
			menu.findItem(R.id.menu_listing_relist).setVisible(false);
			menu.findItem(R.id.menu_listing_update).setVisible(false);
			break;
		}
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_listing_update:
			Intent intent = new Intent(ListingDetailsActivity.this, PostListingActivity.class);
			intent.putExtra("ad_id", selectedListingId);
			intent.putExtra("mode",PostListingActivity.MODE_UPDATE_LISTING);
			startActivityForResult(intent, PostListingActivity.MODE_UPDATE_LISTING);
			break;

		default:
			break;
		}
    	return true;
    }
    /**
     * 
     * @author avinash
     * Task to handle listing download.
     */
    class ListingDetailsTask extends AsyncTask<Integer, Integer, ListingDetailsResultWrapper>{
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		showDialog(D_PROGRESS);
    	}
		@Override
		protected ListingDetailsResultWrapper doInBackground(Integer... params) {
			ListingDetailsResultWrapper wrapper = new ListingDetailsResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_DOWNLOAD_LISTINGS_DETAILS)) {
					wrapper = wsAction.getListingById(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
				}
			}catch (IOException e) {
				Log.e("ListingDetailsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("ListingDetailsTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
    	@Override
    	protected void onPostExecute(ListingDetailsResultWrapper result) {
    		super.onPostExecute(result);
    		dismissDialog(D_PROGRESS);
    		if(result.listing == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				setDetails(result.listing);
			}
    	}
    }
}
