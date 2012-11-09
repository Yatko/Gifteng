package com.venefica.module.listings;

import java.util.ArrayList;
import java.util.List;

import com.venefica.activity.R;
import com.venefica.module.utils.Utility;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 
 * @author avinash
 * Activity to show detail Listing  
 */
public class ListingDetailsActivity extends Activity {
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
    
    private Button btnMakeOffer, btnViewSeller;
    private ImageButton btnBookmark;
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
        
		btnViewSeller = (Button) findViewById(R.id.btnActListingDetailsSeller);
		btnBookmark = (ImageButton) findViewById(R.id.btnActListingDetailsBookmark);
		btnBookmark.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Utility.showShortToast(ListingDetailsActivity.this, getResources().getString(R.string.msg_detail_listing_add_fav_success));
			}
		});
        //Gallery
        gallery = (Gallery) findViewById(R.id.galleryActListingDetailsPhotos);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_listing_details, menu);
        return true;
    }*/
}
