package com.venefica.module.listings;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.Overlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.venefica.activity.R;
import com.venefica.module.utils.Utility;

import android.location.LocationManager;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
     * Map 
     */
    private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private Overlay itemizedoverlay;
	private boolean showMap = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);
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
        setDetails();
    }
	private void setDetails() {
		txtTitle.setText("Apartment");
		txtPrice.setText("75000 USD");
		txtDescription.setText("3 bed, kitchen, Living room");
		txtAddress.setText("Miami, FL");
		txtListingDate.setText("11/05/2012");
		txtExpiaryDate.setText("11/15/2012");
		btnViewSeller.setText("propery_dealer");
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_listing_details, menu);
        return true;
    }*/
}
