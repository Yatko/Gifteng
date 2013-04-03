/**
 * 
 */
package com.venefica.module.listings.post;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.post.PostAct.PostListingTask;
import com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener;
import com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener;
import com.venefica.module.main.R;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 *
 */
public class PostListingActivity extends SherlockFragmentActivity implements OnPostImagesListener, OnPostPreivewListener{

	/**
	 * fragment to handle camera and gallery
	 */
	private PostImagesFragment imgFragment;
	private ArrayList<String> imageList;
	private ArrayList<Bitmap> images;
	/**
	 * listing to post
	 */
	private AdDto listing;
	private ImageDto coverImage;
	private WSAction wsAction;
	public long createdListingId;	
	/**
	 * flag processing (working) indicates post listing process is running
	 */
	private boolean uploadingData = false;
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
	public static final int ACT_MODE_PROCESS_BITMAP = 3004;
	public static final int  ACT_MODE_UPLOAD_IMAGES = 3005;
	private static int CURRENT_MODE = ACT_MODE_POST_LISTING;
	
	private static int REQ_GET_LISTING_DETAILS_TO_POST = 5001;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
		setProgressBarIndeterminateVisibility(false);
		
		setContentView(R.layout.activity_post_listing);
		//set mode
		CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_POST_LISTING);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		imgFragment = new PostImagesFragment();
		fragmentTransaction.add(R.id.layPostlistingMain, imgFragment);
		fragmentTransaction.commit();
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
					if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS
							|| ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE){
						finish();
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_POST_LISTING){
				message = (String) getResources().getText(R.string.error_postlisting);
			}else if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UPDATE_LISTING){
				message = (String) getResources().getText(R.string.error_update_listing);
			}else if(ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_update_success);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	

	@Override
	public void onSetCoverImage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraError(int errorCode) {
		ERROR_CODE = Constants.ERROR_START_CAMERA;
    	showDialog(D_ERROR);
	}

	@Override
	public void onNextButtonClick(ArrayList<String> imageList, ArrayList<Bitmap> images) {
		this.imageList = imageList;
		this.images = images;
		Intent intent = new Intent(this, GetListingDetails.class);
		startActivityForResult(intent, REQ_GET_LISTING_DETAILS_TO_POST);
	}
	
	/**
	 * get images from cache
	 * @param fileName
	 * @return Bitmap
	 */
	private Bitmap getImageFromCache(String fileName){
		return ((VeneficaApplication)getApplication()).getImgManager().getBitmapFromCache(fileName);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK) {
			//get listing data to post 
			if (requestCode == REQ_GET_LISTING_DETAILS_TO_POST) {
				Bundle data = intent.getExtras();
				
				if (listing == null) {
					listing = new AdDto();
				}		
				listing.setTitle(data.getString(GetListingDetails.KEY_TITLE));
				listing.setCategory(data.getString(GetListingDetails.KEY_CATEGORY));
				listing.setDescription(data.getString(GetListingDetails.KEY_DESCRIPTION));
				listing.setCategoryId(data.getLong(GetListingDetails.KEY_CATEGORY_ID));
				listing.setPrice(new BigDecimal(data.getString(GetListingDetails.KEY_CURRENT_VALUE)));
				listing.setLatitude(data.getDouble(GetListingDetails.KEY_LATITUDE));
				listing.setLongitude(data.getDouble(GetListingDetails.KEY_LONGITUDE));
				
				listing.setCanMarkAsSpam(true);
				listing.setCanRate(true);
				listing.setExpired(false);
				listing.setExpiresAt(Utility.shiftDate(new Date(), Constants.EXPIRE_AD_IN_DAYS));
				listing.setInBookmars(false);
				listing.setNumAvailProlongations(0);
				listing.setOwner(true);
				listing.setWanted(false);
				listing.setNumViews(0L);
				listing.setRating(1.0f);
				coverImage = new ImageDto(getImageFromCache(imageList.get(data.getInt(GetListingDetails.KEY_COVER_IMAGE))));
				listing.setImage(coverImage);
				
				PostPreviewFragment postPreviewFragment = new PostPreviewFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.layPostlistingMain, postPreviewFragment).addToBackStack(null).commit();
			}
		}
	}
	
	/**
	 * Helper method to upload images
	 * @param token
	 * @param listingId
	 * @param wsAction
	 * @return PostListingResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private PostListingResultWrapper uploadImages(String token, long listingId, WSAction wsAction) throws IOException, XmlPullParserException{
		PostListingResultWrapper result =  new PostListingResultWrapper();
		
		for (String imageName : imageList) {
			Bitmap img = getImageFromCache(imageName);
			result = wsAction.addImageToAd(token, listingId, img);
			img.recycle();
		}
		return result;
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
			setSupportProgressBarIndeterminateVisibility(true);
			uploadingData = true;
		}

		@Override
		protected PostListingResultWrapper doInBackground(Integer... params) {
			PostListingResultWrapper wrapper = new PostListingResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_POST_LISTING)) {
					wrapper = wsAction.postListing(((VeneficaApplication)getApplication()).getAuthToken(), listing);
				}/*else if (params[0].equals(ACT_MODE_GET_LISTING)) {
					ListingDetailsResultWrapper detailsWrapper = wsAction.getListingById(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId);
					wrapper.listing = detailsWrapper.listing;
					wrapper.result = detailsWrapper.result;
				}else if (params[0].equals(ACT_MODE_UPDATE_LISTING)) {
					wrapper = wsAction.updateListing(((VeneficaApplication)getApplication()).getAuthToken(), getListingDetails(selectedListing));
				}else*/ if (params[0].equals(ACT_MODE_UPLOAD_IMAGES)) {
					wrapper = uploadImages(((VeneficaApplication)getApplication()).getAuthToken(), createdListingId, wsAction);
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
			setSupportProgressBarIndeterminateVisibility(false);
			uploadingData = false;
			if(result.data == null && result.result == -1 && result.listing == null){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}/*else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				setListingDetails(result.listing);
			}*/else if (result.result ==  Constants.RESULT_POST_LISTING_SUCCESS && !result.data.equals("")) {
				createdListingId = Long.parseLong(result.data);
				coverImage = null;
				new PostListingTask().execute(ACT_MODE_UPLOAD_IMAGES);
			}else if (result.result ==  Constants.RESULT_ADD_IMAGE_TO_AD_SUCCESS) {
				ERROR_CODE = Constants.RESULT_POST_LISTING_SUCCESS;
				showDialog(D_ERROR);
			}else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);				
			}
		}
	}

	@Override
	public ArrayList<Bitmap> getImages() {		
		return images;
	}

	@Override
	public AdDto getListing() {
		return listing;
	}

	@Override
	public void onPostButtonClick() {
		if(WSAction.isNetworkConnected(PostListingActivity.this)){			
			if(CURRENT_MODE == ACT_MODE_UPDATE_LISTING){
				new PostListingTask().execute(ACT_MODE_UPDATE_LISTING);
			}else if (CURRENT_MODE == ACT_MODE_POST_LISTING) {
				new PostListingTask().execute(ACT_MODE_POST_LISTING);
			}
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}

	@Override
	public ArrayList<String> getImageList() {		
		return imageList;
	}
}
