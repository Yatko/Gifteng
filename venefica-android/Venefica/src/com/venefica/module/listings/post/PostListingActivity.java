/**
 * 
 */
package com.venefica.module.listings.post;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener;
import com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener;
import com.venefica.module.listings.post.UpdateListingDialogFragment.UpdateListingDialogListener;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.AddressDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Activity to post new listing 
 */
public class PostListingActivity extends VeneficaActivity implements OnPostImagesListener, OnPostPreivewListener, UpdateListingDialogListener{

	/**
	 * fragments 
	 */
	private PostImagesFragment imgFragment;
	private UpdateListingDialogFragment updateListingDialogFragment;
	private ArrayList<String> imageList;
	private ArrayList<Bitmap> images;
	private ArrayList<Long> imagesTodeleteFromServer;
	
	/**
	 * listing to post
	 */
	private AdDto listing;
	private ImageDto coverImage;
	private int coverImagePosition = -1;
	private WSAction wsAction;
	public long createdListingId = -1;	
	/**
	 * flag processing (working) indicates post listing process is running
	 */
	private boolean uploadingData = false;
	/**
	 * true if displaying preview fragment
	 */
	private boolean isPreviewShown = false;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	public long selectedListingId;
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
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
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
		
		if (CURRENT_MODE == ACT_MODE_UPDATE_LISTING) {
			selectedListingId = getIntent().getLongExtra("ad_id", 0);
			updateListingDialogFragment = new UpdateListingDialogFragment();
			updateListingDialogFragment.show(getSupportFragmentManager(), "fragment_update_listing");
		} else if (CURRENT_MODE == ACT_MODE_POST_LISTING) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			imgFragment = new PostImagesFragment();
			fragmentTransaction.add(R.id.layPostlistingMain, imgFragment);
			fragmentTransaction.commit();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			if (!uploadingData) {
				onBackPressed();
			} else {
				Utility.showLongToast(this, getResources().getString(R.string.msg_working_in_background));
			}			
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
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
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
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

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener#onCameraError(int)
	 */
	@Override
	public void onCameraError(int errorCode) {
		ERROR_CODE = Constants.ERROR_START_CAMERA;
    	showDialog(D_ERROR);
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener#onNextButtonClick(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public void onNextButtonClick(ArrayList<String> imageList
			, ArrayList<Bitmap> images, int coverImagePosition, ArrayList<Long> imagesTodeleteFromServer) {
		this.imageList = imageList;
		this.images = images;
		this.coverImagePosition = coverImagePosition;
		this.imagesTodeleteFromServer = imagesTodeleteFromServer;
		Intent intent = new Intent(this, GetListingDetails.class);
		if ( listing != null) {
			intent.putExtra(GetListingDetails.KEY_TITLE, listing.getTitle());
			intent.putExtra(GetListingDetails.KEY_DESCRIPTION, listing.getDescription());
			intent.putExtra(GetListingDetails.KEY_CATEGORY, listing.getCategory());
			intent.putExtra(GetListingDetails.KEY_CATEGORY_ID, listing.getCategoryId());
			intent.putExtra(GetListingDetails.KEY_CURRENT_VALUE, listing.getPrice().toString());
//			intent.putExtra(GetListingDetails.KEY_ZIP_CODE, listing.get);
//			intent.putExtra(GetListingDetails.KEY_LATITUDE, listing.getLatitude());
//			intent.putExtra(GetListingDetails.KEY_LONGITUDE, listing.getLongitude());
			intent.putExtra(GetListingDetails.KEY_COVER_IMAGE, coverImagePosition);	
			intent.putExtra(GetListingDetails.KEY_IS_BACK_FROM_PREVIEW, isPreviewShown);	
			intent.putExtra(GetListingDetails.KEY_IS_UPDATE_MODE
					, CURRENT_MODE == ACT_MODE_UPDATE_LISTING ? true : false);
			intent.putExtra(GetListingDetails.KEY_FREE_SHIPPING, listing.getFreeShipping());
			intent.putExtra(GetListingDetails.KEY_PICKUP, listing.getPickUp());
		} else {			
			intent.putExtra(GetListingDetails.KEY_IS_BACK_FROM_PREVIEW, isPreviewShown);	
		}
		startActivityForResult(intent, REQ_GET_LISTING_DETAILS_TO_POST);
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener#isBackFromPreview()
	 */
	@Override
	public boolean isBackFromPreview() {		
		return isPreviewShown;
	}
	/**
	 * get images from cache
	 * @param fileName
	 * @return Bitmap
	 */
	private Bitmap getImageFromCache(String fileName){
		return ((VeneficaApplication)getApplication()).getImgManager().getBitmapFromCache(fileName);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		isPreviewShown = false;
		if (resultCode == Activity.RESULT_OK) {
			//get listing data to post 
			if (requestCode == REQ_GET_LISTING_DETAILS_TO_POST) {
				Bundle data = intent.getExtras();
				
				if (listing == null) {
					listing = new AdDto();
					listing.setAddress(new AddressDto());
				}		
				listing.setTitle(data.getString(GetListingDetails.KEY_TITLE));
				listing.setCategory(data.getString(GetListingDetails.KEY_CATEGORY));
				listing.setDescription(data.getString(GetListingDetails.KEY_DESCRIPTION));
				listing.setCategoryId(data.getLong(GetListingDetails.KEY_CATEGORY_ID));
				listing.setPrice(new BigDecimal(data.getString(GetListingDetails.KEY_CURRENT_VALUE)));
				AddressDto addressDto = new AddressDto();
				addressDto.setLatitude(data.getDouble(GetListingDetails.KEY_LATITUDE));
				addressDto.setLongitude(data.getDouble(GetListingDetails.KEY_LONGITUDE));
				addressDto.setZipCode(data.getString(GetListingDetails.KEY_ZIP_CODE));
				listing.setAddress(addressDto);
//				listing.getAddress().setLatitude(data.getDouble(GetListingDetails.KEY_LATITUDE));
//				listing.getAddress().setLongitude(data.getDouble(GetListingDetails.KEY_LONGITUDE));
				listing.setFreeShipping(data.getBoolean(GetListingDetails.KEY_FREE_SHIPPING));
				listing.setPickUp(data.getBoolean(GetListingDetails.KEY_PICKUP));
				
				listing.setCanMarkAsSpam(true);
				listing.setCanRate(true);
				listing.setExpired(false);
				listing.setExpiresAt(Utility.shiftDate(new Date(), Constants.EXPIRE_AD_IN_DAYS));
				listing.setInBookmars(false);
				listing.setNumAvailProlongations(0);
				listing.setOwner(true);
				listing.setQuantity(1);
				listing.setNumViews(0L);
				listing.setRating(1.0f);
				if (coverImagePosition == -1 && CURRENT_MODE == ACT_MODE_UPDATE_LISTING) {
//					coverImage = new ImageDto(getImageFromCache(Constants.PHOTO_URL_PREFIX + listing.getImage().getUrl()));
				} else if (coverImagePosition >= getImagesToUpdate().size()) {
					coverImage = new ImageDto(getImageFromCache(imageList.get(coverImagePosition - getImagesToUpdate().size())));
				} else {
					coverImage = new ImageDto(getImageFromCache(Constants.PHOTO_URL_PREFIX + getImagesToUpdate().get(coverImagePosition).getUrl()));
				}				
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
		if (imagesTodeleteFromServer != null && imagesTodeleteFromServer.size() > 0) {
			result = wsAction.deleteImagesFromListing(token, listingId,
					imagesTodeleteFromServer);
		} else {
			result.result = Constants.RESULT_ADD_IMAGE_TO_AD_SUCCESS;
		}
		
		if (result.result == Constants.RESULT_DELETE_IMAGES_SUCCESS) {
			result.result = Constants.RESULT_ADD_IMAGE_TO_AD_SUCCESS;
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#getImages()
	 */
	@Override
	public ArrayList<Bitmap> getImages() {		
		return images;
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#getListing()
	 */
	@Override
	public AdDto getListing() {
		return listing;
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#onPostButtonClick()
	 */
	@Override
	public void onPostButtonClick() {
		if(WSAction.isNetworkConnected(PostListingActivity.this)){	
			if (!uploadingData) {
				if(CURRENT_MODE == ACT_MODE_UPDATE_LISTING){
					new PostListingTask().execute(ACT_MODE_UPDATE_LISTING);
				}else if (CURRENT_MODE == ACT_MODE_POST_LISTING) {
					new PostListingTask().execute(ACT_MODE_POST_LISTING);
				}
			} else {
				Utility.showLongToast(this, getResources().getString(R.string.msg_working_in_background));
			}
			
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#getImageList()
	 */
	@Override
	public ArrayList<String> getImageList() {		
		return imageList;
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
				} else if (params[0].equals(ACT_MODE_GET_LISTING)) {
					ListingDetailsResultWrapper detailsWrapper = wsAction.getListingById(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId);
					wrapper.listing = detailsWrapper.listing;
					wrapper.result = detailsWrapper.result;
				} else if (params[0].equals(ACT_MODE_UPDATE_LISTING)) {
					wrapper = wsAction.updateListing(((VeneficaApplication)getApplication()).getAuthToken(), listing);
				} else if (params[0].equals(ACT_MODE_UPLOAD_IMAGES)) {
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
			} else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				listing = result.listing;
				//show camera view with existing images at bottom
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				imgFragment = new PostImagesFragment();
				fragmentTransaction.add(R.id.layPostlistingMain, imgFragment);
				fragmentTransaction.commit();
			} else if (result.result ==  Constants.RESULT_POST_LISTING_SUCCESS && !result.data.equals("")) {
				createdListingId = Long.parseLong(result.data);
				coverImage = null;
				new PostListingTask().execute(ACT_MODE_UPLOAD_IMAGES);
			} else if (result.result ==  Constants.RESULT_UPDATE_LISTING_SUCCESS) {
				createdListingId = listing.getId();
				coverImage = null;
				new PostListingTask().execute(ACT_MODE_UPLOAD_IMAGES);
			} else if (result.result ==  Constants.RESULT_ADD_IMAGE_TO_AD_SUCCESS) {
				if (CURRENT_MODE == ACT_MODE_UPDATE_LISTING) {
					ERROR_CODE = Constants.RESULT_UPDATE_LISTING_SUCCESS;
				} else if (CURRENT_MODE == ACT_MODE_POST_LISTING){
					ERROR_CODE = Constants.RESULT_POST_LISTING_SUCCESS;
				}				
				showDialog(D_ERROR);
			} else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);				
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#setPreviewVisible(boolean)
	 */
	@Override
	public void setPreviewVisible(boolean isPreviewVisible) {
		this.isPreviewShown = isPreviewVisible;
	}

	/* (non-Javadoc)
	 * @see com.venefica.module.listings.post.PostPreviewFragment.OnPostPreivewListener#getCoverImagePosition()
	 */
	@Override
	public int getCoverImagePosition() {
		return this.coverImagePosition;
	}

	@Override
	public void onUpdate() {
		updateListingDialogFragment.dismiss();
		//get details
		new PostListingTask().execute(ACT_MODE_GET_LISTING);
	}

	@Override
	public void onCancel() {
		updateListingDialogFragment.dismiss();
		onBackPressed();
	}

	/**
	 * @return the cURRENT_MODE
	 */
	public static int getCURRENT_MODE() {
		return CURRENT_MODE;
	}	

	@Override
	public List<ImageDto> getImagesToUpdate() {
		if (listing != null && listing.getImages() != null) {
			return listing.getImages();			
		} else {
			return new ArrayList<ImageDto>();
		}		
	}

	@Override
	public List<ImageDto> getImageDtosToUpdate() {
		return getImagesToUpdate() ;
	}	
		
	@Override
	public ArrayList<Long> getImagesTodeleteFromServer() {
		return this.imagesTodeleteFromServer;
	}
}
