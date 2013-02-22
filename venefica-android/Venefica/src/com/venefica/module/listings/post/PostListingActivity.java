package com.venefica.module.listings.post;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.venefica.module.listings.GalleryImageAdapter;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.browse.BrowseCategoriesActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.CameraPreview;
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
public class PostListingActivity extends VeneficaMapActivity implements LocationListener, OnClickListener, ViewFactory {
	/**
	 * main layout
	 */
	private LinearLayout layMain;
	/**
	 * images layout for step1 and step2
	 */
	private RelativeLayout layImagesView;
	/**
	 * Controls(buttons) layouts
	 */
	private LinearLayout layControlsStepOne, layControlsStepTwo;
	/**
	 * layout inflater
	 */
	private LayoutInflater infleter;
	/**
	 * surface view for camera in step 1
	 */
	private FrameLayout cameraPreview;
	private Camera camera;
	private CameraPreview camPreview;
	/**
	 * ImageSwitcher for preview of images in step 2
	 */
	private ImageSwitcher imgSwitcher;
	/**
	 * Gallery view
	 */
	private Gallery gallery;
	/**
	 * Images taken
	 */
	private ArrayList<Bitmap> images;
	/**
	 * Adapter for gallery
	 */
	private GalleryImageAdapter galImageAdapter;
	/**
	 * buttons for step one
	 */
	private ImageButton imgBtnPickGallery, imgBtnPickCamera, imgBtnNextToStep2;
	/**
	 * buttons for step two
	 */
	private ImageButton imgBtnBackToStepOne, imgBtnDelete, imgBtnContrast,
			imgBtnCrop, imgBtnNextToStepThree;
	/**
	 * is step two 
	 */
	private boolean isStepTwo = false;
	/**
	 * is step three
	 */
	private boolean isStepThree = false;
	/**
	 * is first image selected
	 */
	private boolean isFirstImage = true;
	/**
	 * selected image
	 */
	private int seletedImagePosition = 0;
	/**
	 * cover image position
	 */
	private int coverImagePosition = -1;
	/**
	 * layout details
	 */
	private RelativeLayout layDetails;
	/**
	 * image list
	 */
	private ArrayList<String> imageList;
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	/**
	 * Constants
	 */
	public static final int REQ_SELECT_CATEGORY = 1001;
	private static final int REQ_GET_IMAGE = 1002;
	private static final int REQ_IMAGE_CROP = 1003;
	private static final int REQ_GET_CAMERA_IMAGE = 1004;
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
	
	protected static final int ERROR_DATE_VALIDATION = 22;
	protected static final int ERROR_REMOVE_SEL_IMAGE = 23;
	
	
	
	/**
	 * Edit fields to collect listing data
	 */
	private EditText edtTitle, edtSubTitle, edtDescription,
			edtCondition, edtPrice, edtZip, edtState, edtCounty, edtCity, edtArea,
			edtLatitude, edtLongitude;
	/**
	 * Text fields to collect listing data
	 */
	private TextView txtSetAsCoverImg;/*txtTitle, txtSubTitle, txtCategory, txtDescription,
			txtCondition, txtPrice, txtZip, txtState, txtCounty, txtCity, txtArea,
			txtLatitude, txtLongitude;*/
	private Button btnSelCategory;
	private Spinner spinCurrency;
	/**
	 * Gallery to images
	 *//*
	private Gallery gallery;
	*//**
	 * Images
	 *//*
	private List<Bitmap> drawables;
	*//**
	 * Adapter for gallery
	 *//*
	private GalleryImageAdapter galImageAdapter;
	*/
	private Uri selectedImageUri;

	/**
	 * Buttons
	 */
	private Button btnListItem, btnExpiary;
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
	private Location location;
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
	private ImageDto coverImage;
	private List<ImageDto> imageDtos;
	
	/**
	 * Low resolution flag
	 */
	private boolean isLowResolution = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
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
		//location manager 
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// layout inflater
		infleter = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		// main layout
		layMain = (LinearLayout) findViewById(R.id.layPostlistingMain);
		// images layout
		layImagesView = (RelativeLayout) infleter.inflate(R.layout.view_post_listing_images, null);
		layMain.addView(layImagesView);
		txtSetAsCoverImg = (TextView) layImagesView.findViewById(R.id.txtActPostListingDefaultImg);
		
		// Controls layout
		layControlsStepOne = (LinearLayout) layImagesView.findViewById(R.id.layPostListingStep1Controls);
		imgBtnPickGallery = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingPickGallery);
		imgBtnPickGallery.setOnClickListener(this);
		imgBtnPickCamera = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingPickCamera);
		imgBtnPickCamera.setOnClickListener(this);
		imgBtnNextToStep2 = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingNextToStep2);
		imgBtnNextToStep2.setOnClickListener(this);

		layControlsStepTwo = (LinearLayout) layImagesView.findViewById(R.id.layPostListingStep2Controls);
		imgBtnBackToStepOne = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingBackToStep1);
		imgBtnBackToStepOne.setOnClickListener(this);
		imgBtnDelete = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingDelete);
		imgBtnDelete.setOnClickListener(this);
		imgBtnContrast = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingContrast);
		imgBtnContrast.setOnClickListener(this);
		imgBtnCrop = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingCrop);
		imgBtnCrop.setOnClickListener(this);
		imgBtnNextToStepThree = (ImageButton) layImagesView.findViewById(R.id.imgBtnPostListingNextToStep3);
		imgBtnNextToStepThree.setOnClickListener(this);

		cameraPreview = (FrameLayout) layImagesView.findViewById(R.id.layViewPostListingCameraPreview);
		imgSwitcher = (ImageSwitcher) layImagesView.findViewById(R.id.imgSwitcherActPostListing);
		imgSwitcher.setFactory(this);
		gallery = (Gallery) layImagesView.findViewById(R.id.galleryActPostListing);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				if (isStepTwo && images != null && images.size() > 0) {
					seletedImagePosition = position;
					if (seletedImagePosition == coverImagePosition) {
						txtSetAsCoverImg.setVisibility(View.VISIBLE);
					} else {
						txtSetAsCoverImg.setVisibility(View.GONE);
					}
					imgSwitcher.setImageDrawable(new BitmapDrawable(getImageFromCache(imageList.get(position))));
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		registerForContextMenu(gallery);
		setStepOneUIVisiblity(ViewGroup.VISIBLE);
		
		camPreview = new CameraPreview(this);
		camPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		cameraPreview.addView(camPreview);
		
		//images
		images = new ArrayList<Bitmap>();
		images.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_camera));
		galImageAdapter = new GalleryImageAdapter(this, null, images, true, true);
		gallery.setAdapter(galImageAdapter);
		
		
		//layout details
		layDetails = (RelativeLayout) infleter.inflate(R.layout.view_post_listing_details, null);
		layMain.addView(layDetails);
		//data
		edtTitle = (EditText) layDetails.findViewById(R.id.edtActPostListingListTitle);
		edtDescription = (EditText) layDetails.findViewById(R.id.edtActPostListingDescription);
		edtPrice = (EditText) layDetails.findViewById(R.id.edtActPostListingPrice);
		edtLatitude =  (EditText) layDetails.findViewById(R.id.edtActPostListingLatitude);
		edtLongitude = (EditText) layDetails.findViewById(R.id.edtActPostListingLongitude);
		btnListItem = (Button) layDetails.findViewById(R.id.btnActPostListingPost);
		btnListItem.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (validateFields()) {
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
			}
		});
		btnSelCategory = (Button) layDetails.findViewById(R.id.btnActPostListingCategory);
		btnSelCategory.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent selCatIntent = new Intent(PostListingActivity.this, BrowseCategoriesActivity.class);
				selCatIntent.putExtra("act_mode", BrowseCategoriesActivity.ACT_MODE_GET_CATEGORY);
				startActivityForResult(selCatIntent, REQ_SELECT_CATEGORY);
			}
		});
		/*
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
		
		btnExpiary = (Button) findViewById(R.id.btnActPostListingAddExpiary);
		btnExpiary.setText(Utility.convertShortDateToString(new Date()));
		btnExpiary.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showDialog(D_DATE);
			}
		});
		
		if (CURRENT_MODE == ACT_MODE_UPDATE_LISTING) {
			selectedListingId = getIntent().getLongExtra("ad_id", 0);
			btnListItem.setText(getResources().getString(R.string.label_update));
			new PostListingTask().execute(ACT_MODE_GET_LISTING);
		}*/
	}

	@Override
	protected void onStart() {
		super.onStart();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(false);
		locProvider = locationManager.getBestProvider(criteria, true);		

	    if (locProvider != null) {
	    	Log.d("PostListingActivity :", locProvider);
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
		if (Utility.checkCameraHardware(this)) {
			camera = Utility.getCameraInstance();
			camPreview.setCamera(camera);
		}
	}

	/* Remove the locationlistener updates and release camera when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
		releaseCamera();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (isStepThree) {
				showStepThreeUI(false);
				isStepThree = false;
			}else {
				finish();
			}			
		}
		return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_postlisting_gallery_context_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.menu_set_as_default_img:
	        	coverImage = new ImageDto(getImageFromCache(imageList.get(gallery.getSelectedItemPosition())));
	        	coverImagePosition = gallery.getSelectedItemPosition();
	        	galImageAdapter.setCoverImagePosition(coverImagePosition);
	        	galImageAdapter.notifyDataSetChanged();
	        	gallery.setSelection(coverImagePosition);
	            return true;	        
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnPostListingPickGallery) {
			pickImageFromGallery();
		} else if (view.getId() == R.id.imgBtnPostListingPickCamera){
			camera.takePicture(null, null, pictureCallback);
		} else if (view.getId() == R.id.imgBtnPostListingNextToStep2){
			if (!isFirstImage) {
				setStepOneUIVisiblity(ViewGroup.GONE);
				setStepTwoUIVisiblity(ViewGroup.VISIBLE);
				showStepThreeUI(false);
				isStepTwo = true;
				gallery.setSelection(0);
				if ( images != null && images.size() > 0) {
					imgSwitcher.setImageDrawable(new BitmapDrawable(images.get(0)));
				}
			}else {
				Utility.showLongToast(this, getResources().getString(R.string.msg_postlisting_sel_image));
			}			
		} else if (view.getId() == R.id.imgBtnPostListingBackToStep1){
			setStepOneUIVisiblity(ViewGroup.VISIBLE);
			setStepTwoUIVisiblity(ViewGroup.GONE);
			showStepThreeUI(false);
			isStepTwo = false;
		} else if (view.getId() == R.id.imgBtnPostListingDelete){			
			ERROR_CODE = ERROR_REMOVE_SEL_IMAGE;
			showDialog(D_ERROR);
		} else if (view.getId() == R.id.imgBtnPostListingContrast){
			
		} else if (view.getId() == R.id.imgBtnPostListingCrop){
			
		} else if (view.getId() == R.id.imgBtnPostListingNextToStep3){
			if (coverImage == null) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_postlisting_sel_cover_image));
			} else {
				isStepTwo = false;
				showStepThreeUI(true);
				((VeneficaApplication)getApplication()).getImgManager().flushCache();
			}			
		}
	}
	/**
	 * helper method to show hide step one UI
	 * @param visibility
	 */
	private void setStepOneUIVisiblity(int visibility) {
		layControlsStepOne.setVisibility(visibility);
		cameraPreview.setVisibility(visibility);		
	}

	/**
	 * helper method to show hide step two UI
	 * @param visibility
	 */
	private void setStepTwoUIVisiblity(int visibility) {
		layControlsStepTwo.setVisibility(visibility);
		imgSwitcher.setVisibility(visibility);
	}
	/**
	 * helper method to show hide step three UI
	 * @param visibility
	 */
	private void showStepThreeUI(boolean show) {
		if (show) {
			layDetails.setVisibility(ViewGroup.VISIBLE);
			layImagesView.setVisibility(ViewGroup.GONE);
			isStepThree = true;
		}else {
			layDetails.setVisibility(ViewGroup.GONE);
			layImagesView.setVisibility(ViewGroup.VISIBLE);
			isStepThree = false;
		}
		
	}
	/**
	 * release the camera for other applications
	 */
	private void releaseCamera(){
        if (camera != null){
            camera.release();
            camera = null;
        }
    }
	@Override
	public void onBackPressed() {
		if (isStepThree) {
			showStepThreeUI(false);
			isStepThree = false;
		}else {
			super.onBackPressed();
		}		
	}
	/**
	 * Handle picture callback for camera
	 */
	private PictureCallback pictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
			putImageInCache(Bitmap.createScaledBitmap(image, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y, false));
			if (isFirstImage) {
				images.clear();
			}
			images.add(Bitmap.createScaledBitmap(image, Constants.IMAGE_THUMBNAILS_WIDTH, Constants.IMAGE_THUMBNAILS_HEIGHT, false));
			galImageAdapter.notifyDataSetChanged();
			image.recycle();
			camera.startPreview();
			isFirstImage = false;
			gallery.setSelection(images.size()-1);
		}
	};
	public long createdListingId;	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQ_SELECT_CATEGORY) {
				 categoryId = data.getLongExtra("cat_id", -1);
				 categoryName = data.getStringExtra("category_name").trim().
						 equalsIgnoreCase("")?getResources().getString(R.string.code_category_other): data.getStringExtra("category_name");
				if (categoryName != null) {
					btnSelCategory.setText(categoryName);
				}
				
			} else if (requestCode == REQ_GET_IMAGE){
				final Bundle extras = data.getExtras();
	            if (extras != null) {            		
	    			selectedImageUri = data.getData();
//	    			performCrop();
	    			String[] filePathColumn = { MediaStore.Images.Media.DATA };
	     
	                Cursor cursor = getContentResolver().query(selectedImageUri,
	                        filePathColumn, null, null, null);
	                cursor.moveToFirst();
	     
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                String picturePath = cursor.getString(columnIndex);
	                cursor.close();
	                if (isFirstImage) {
	    				images.clear();
	    			}
	                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	                putImageInCache(Bitmap.createScaledBitmap(bitmap, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y, false));
	                images.add(Bitmap.createScaledBitmap(bitmap, Constants.IMAGE_THUMBNAILS_WIDTH, Constants.IMAGE_THUMBNAILS_HEIGHT, false));
	    			galImageAdapter.notifyDataSetChanged();
	    			bitmap.recycle();
	    			isFirstImage = false;
	    			gallery.setSelection(images.size()-1);
	            }			           
	        } else if (requestCode == REQ_IMAGE_CROP) {
	        	new PostListingTask().execute(ACT_MODE_PROCESS_BITMAP);
			}
		}
	}
	
	/**
	 * Method to resize bitmap with specified size
	 * @return bitmap
	 */
	private Bitmap resizeBitmap() {
		Rect rect = new Rect(0, 0, Constants.IMAGE_MAX_SIZE_X, Constants.IMAGE_MAX_SIZE_Y);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inInputShareable = false;
	    opts.inSampleSize = 1;
	    opts.inScaled = false;
	    opts.inDither = false;
	    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(Utility.getTempUri()));
			if (Constants.IMAGE_MAX_SIZE_X > bm.getWidth() && Constants.IMAGE_MAX_SIZE_Y > bm.getHeight()) {
				isLowResolution = true;				
				bm.recycle();
				return null;
			}else{
				isLowResolution = false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) Constants.IMAGE_MAX_SIZE_X) / width;
	    float scaleHeight = ((float) Constants.IMAGE_MAX_SIZE_Y) / height;

	    // create a matrix for the manipulation
	    Matrix matrix = new Matrix();

	    // resize the bit map
	    matrix.postScale(scaleWidth, scaleHeight);

	    // recreate the new Bitmap
	    return  Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
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
					}else if (ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER) {
						enableLocationSettings();
					}else if (ERROR_CODE == ERROR_REMOVE_SEL_IMAGE) {
						images.remove(seletedImagePosition);
						imageList.remove(seletedImagePosition);
						if (seletedImagePosition == coverImagePosition) {
							coverImage = null;
						}
						galImageAdapter.resetCoverImagePosition();
						galImageAdapter.notifyDataSetChanged();
						gallery.setSelection(seletedImagePosition - 1);
						
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
			}else if(ERROR_CODE == Constants.ERROR_LOW_RESOLUTION_CROP){
				message = (String) getResources().getText(R.string.msg_postlisting_low_resolution);
			}else if (ERROR_CODE == ERROR_REMOVE_SEL_IMAGE) {
				message = (String) getResources().getText(R.string.msg_postlisting_remove_sel_image);
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
//			showDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected PostListingResultWrapper doInBackground(Integer... params) {
			PostListingResultWrapper wrapper = new PostListingResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if(params[0].equals(ACT_MODE_PROCESS_BITMAP)){
					wrapper.image = resizeBitmap();
				} else if (params[0].equals(ACT_MODE_POST_LISTING)) {
					wrapper = wsAction.postListing(((VeneficaApplication)getApplication()).getAuthToken(), getListingDetails(null));
				}else if (params[0].equals(ACT_MODE_GET_LISTING)) {
					ListingDetailsResultWrapper detailsWrapper = wsAction.getListingById(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId);
					wrapper.listing = detailsWrapper.listing;
					wrapper.result = detailsWrapper.result;
				}else if (params[0].equals(ACT_MODE_UPDATE_LISTING)) {
					wrapper = wsAction.updateListing(((VeneficaApplication)getApplication()).getAuthToken(), getListingDetails(selectedListing));
				}else if (params[0].equals(ACT_MODE_UPLOAD_IMAGES)) {
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
//			dismissDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(false);
			if (isLowResolution && result.image == null) {
				ERROR_CODE = Constants.ERROR_LOW_RESOLUTION_CROP;
				showDialog(D_ERROR);
			} else if(result.image != null){
				if (imageDtos == null) {
					imageDtos = new ArrayList<ImageDto>();
				}
				coverImage = new ImageDto(result.image);
				imageDtos.add(coverImage);
								
//                drawables.add(result.image);
                galImageAdapter.notifyDataSetChanged();
			} else if(result.data == null && result.result == -1 && result.listing == null){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				setListingDetails(result.listing);
			}else if (result.result ==  Constants.RESULT_POST_LISTING_SUCCESS && !result.data.equals("")) {
				createdListingId = Long.parseLong(result.data);
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
		if (location != null) {
			listing.setLatitude(location.getLatitude());
			listing.setLongitude(location.getLongitude());
		}
		
		listing.setCanMarkAsSpam(true);
		listing.setCanRate(true);
//		listing.setCreatedAt(Utility.converDateToString(new Date()));
		listing.setExpired(false);
//		listing.setExpiresAt(new Date(btnExpiary.getText().toString()));
		listing.setInBookmars(false);
		listing.setNumAvailProlongations(0);
		listing.setOwner(true);
		listing.setWanted(false);
		listing.setNumViews(0L);
		listing.setRating(1.0f);
		if (coverImage != null) {
			listing.setImage(coverImage);
		}		
		return listing;
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;
			edtLatitude.setText(location.getLatitude()+"");
			edtLongitude.setText(location.getLongitude()+"");
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
    private void pickImageFromGallery() {
        // Gallery.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQ_GET_IMAGE);
    }
    
    /**
     * Set listing details for editing
     * @param listing
     */
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
    
    /**
     * Helper method to carry out crop operation
     */
    private void performCrop(){
    	//take care of exceptions
    	try {
    		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP");
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(selectedImageUri, "image/*");
	    	//set crop properties
	    	cropIntent.putExtra("crop", "true");
	    	//indicate aspect of desired crop
	    	cropIntent.putExtra("aspectX", Constants.IMAGE_ASPECT_X);
	    	cropIntent.putExtra("aspectY", Constants.IMAGE_ASPECT_Y);
	    	//retrieve data on return
	    	cropIntent.putExtra("return-data", false);
	    	cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Utility.getTempUri());
	    	cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	    	cropIntent.putExtra("noFaceDetection",true);
	    	//start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, REQ_IMAGE_CROP);  
    	}
    	//respond to users whose devices do not support the crop action
    	catch(ActivityNotFoundException anfe){
    		Log.d("PostListingActivity::performCrop: ", anfe.toString());
    	}
    }

	/* (non-Javadoc)
	 * @see android.widget.ViewSwitcher.ViewFactory#makeView()
	 */
	@Override
	public View makeView() {
		ImageView img = new ImageView(this);
		img.setBackgroundColor(0xFF000000);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		img.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return img;
	}
	
	/**
	 * Store images in cache
	 * @param bitmap
	 */
	private void putImageInCache(Bitmap bitmap){
		String fileName = "IMG" + System.currentTimeMillis();
		if (imageList == null) {
			imageList = new ArrayList<String>();
		}
		((VeneficaApplication)getApplication()).getImgManager().putBitmapInCache(fileName, bitmap);
		imageList.add(fileName);
	}
	
	/**
	 * get images from cache
	 * @param fileName
	 * @return Bitmap
	 */
	private Bitmap getImageFromCache(String fileName){
		return ((VeneficaApplication)getApplication()).getImgManager().getBitmapFromCache(fileName);
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
			result = wsAction.addImageToAd(token, listingId, getImageFromCache(imageName));		
		}
		return result;
	}
}
