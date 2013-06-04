package com.venefica.module.listings;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.OnSingleTapListener;
import com.venefica.module.map.TapControlledMapView;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.ProfileDetailActivity;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.CommentDto;
import com.venefica.services.ImageDto;
import com.venefica.services.MessageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;
/**
 * 
 * @author avinash
 * Activity to show detail Listing  
 */
public class ListingDetailsActivity extends VeneficaMapActivity implements android.view.View.OnClickListener, LocationListener{
	/**
	 * Gallery to images
	 */
	private Gallery gallery;

    private long selectedListingId = 0;
    private AdDto listing;
    /**
     * Images
     */
    private List<ImageDto> images;
    /**
     * Adapter for gallery
     */
    private GalleryImageAdapter galImageAdapter;
    /**
     * Text view to show details
     */
    private TextView txtDescription;
    
    /**
     * Text view to show user details
     */
    private TextView txtUserName, txtMemberInfo, txtAddress;
    /**
     * Buttons
     */
    private ImageButton btnBookmark, btnFlag, btnSendMsg;
    private Button btnGetIt, btnFollow;
    private ImageView profImgView;
    /**
     * Modes
     */
    public static final int ACT_MODE_MY_LISTINGS_DETAILS = 4001;
    public static final int ACT_MODE_SEARCH_LISTINGS_DETAILS = 4002;
    public static final int ACT_MODE_DOWNLOAD_LISTINGS_DETAILS = 4003;
    public static final int ACT_MODE_END_LISTINGS = 4004;
    public static final int ACT_MODE_RELIST_LISTINGS = 4005;
    public static final int ACT_MODE_DELETE_LISTINGS = 4006;
    public static final int ACT_MODE_BOOKMARK_LISTINGS = 4007;
    public static final int ACT_MODE_REMOVE_BOOKMARK = 4008;
    public static final int ACT_MODE_DOWNLOAD_COMMENTS = 4009;
    public static final int ACT_MODE_SEND_MESSAGE  = 4010;
	public static final int ACT_MODE_ADD_COMMENT = 4011;
	public static final int ACT_MODE_FOLLOW_USER = 4012;
	public static final int ACT_MODE_UNFOLLOW_USER = 4013;
	public static final int ACT_MODE_MARK_AS_SPAM = 4014;
	public static final int ACT_MODE_UNMARK_AS_SPAM = 4015;
	public static final int ERROR_CONF_UNMARK_AS_SPAM = 4016;
	public static final int ERROR_CONF_MARK_AS_SPAM = 4017;
	public static final int ACT_MODE_REQUEST_AD = 4018;
	public static final int ACT_MODE_CANCEL_REQUEST_AD = 4019;
    /**
     * Current mode
     */
    public static int CURRENT_MODE = ACT_MODE_SEARCH_LISTINGS_DETAILS;
    /**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
     * Map 
     */
    private MapController mapController;
	private TapControlledMapView mapView;
	private WSAction wsAction;
	private boolean isMapShown = true;
	private MapItemizedOverlay<ListingOverlayItem> overlayItems;
	
	/**
	 * Comments
	 */
	private ListView listViewComments;
	private List<CommentDto> comments;
	private CommentListAdapter adapterComments;
	/**
	 * flag true if any view is expanded
	 */
	private boolean isViewExpanded = false;

	/**
	 * Send message/comments
	 */
	private ImageButton imgBtnSend;
	private EditText edtMessage;
	private LinearLayout laySend;
	private boolean isSendMsgVisible = false;
	private boolean isSendMessage = false;
	
	private LayoutInflater inflater;
	private PopupWindow popupWindow;
	private ViewGroup mapContainer;
	/**
	 * layout heights
	 */
	private final int viewHeightMin = 150, viewHeightMax = 250; // in pixels
	
	private ImageButton imgBtnMore, imgBtnFacebook, imgBtnTwitter;
	/**
	 * to get current location for distance calculation
	 */
	private LocationManager locationManager;
	private String locProvider;
	private Location location;
	/**
	 * text view on pop up
	 */
	private TextView txtMiles, txtLeft, txtDays;
	/**
	 * request button layout
	 */
	private LinearLayout layRequest;
	private FrameLayout layUserDetails;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        setContentView(R.layout.activity_listing_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);        
                
        inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mapContainer = (ViewGroup) inflater.inflate(R.layout.view_listing_details_map_container, null);
        
        //set activity mode
        CURRENT_MODE = getIntent().getExtras().getInt("act_mode");
        selectedListingId = getIntent().getExtras().getLong("ad_id");
        //user details
        layUserDetails = (FrameLayout) mapContainer.findViewById(R.id.layActListingDetailUserDetails);
		layUserDetails.setOnClickListener(this);
        txtUserName = (TextView) mapContainer.findViewById(R.id.txtUserViewUserName);
        txtMemberInfo = (TextView) mapContainer.findViewById(R.id.txtUserViewMemberInfo);
        txtAddress = (TextView) mapContainer.findViewById(R.id.txtUserViewAddress);
        profImgView = (ImageView) mapContainer.findViewById(R.id.imgUserViewProfileImg);
        btnSendMsg = (ImageButton) mapContainer.findViewById(R.id.imgBtnUserViewSendMsg);
        btnSendMsg.setOnClickListener(this);
        btnFollow = (Button) mapContainer.findViewById(R.id.imgBtnUserViewFollow);
        btnFollow.setOnClickListener(this);
        //Details
        txtDescription = (TextView) mapContainer.findViewById(R.id.txtActListingDesc);
        //Bookmark
		btnBookmark = (ImageButton) findViewById(R.id.btnActListingDetailsBookmark);
		btnBookmark.setOnClickListener(this);
		//Flag
		btnFlag = (ImageButton) findViewById(R.id.btnActListingDetailsFlag);
		btnFlag.setOnClickListener(this);
		//Getit
		btnGetIt = (Button) findViewById(R.id.btnActListingDetailsRequest);
		btnGetIt.setOnClickListener(this);
		//Details popup		
		View popupView = inflater.inflate(R.layout.view_listing_details_popup, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,  
	                     LayoutParams.WRAP_CONTENT); 
		txtMiles = (TextView) popupView.findViewById(R.id.txtListingDetailsPopUpMiles);
        txtLeft = (TextView) popupView.findViewById(R.id.txtListingDetailsPopUpBidsRem);
        txtDays = (TextView) popupView.findViewById(R.id.txtListingDetailsPopUpDaysRem);
        imgBtnMore = (ImageButton) popupView.findViewById(R.id.btnListingDetailsPopUpMore);
        imgBtnMore.setOnClickListener(this);
        imgBtnFacebook = (ImageButton) popupView.findViewById(R.id.btnListingDetailsPopUpShare);
        imgBtnFacebook.setOnClickListener(this);
        imgBtnTwitter = (ImageButton) popupView.findViewById(R.id.btnListingDetailsPopUpTweet);
        imgBtnTwitter.setOnClickListener(this);
		//Map
		mapView = (TapControlledMapView) mapContainer.findViewById(R.id.mapviewActListingDetails);
		// dismiss balloon upon single tap of MapView 
		mapView.setOnSingleTapListener(new OnSingleTapListener() {		
			@Override
			public boolean onSingleTap(MotionEvent e) {
				overlayItems.hideAllBalloons();
				popupWindow.dismiss();
				/*expandView(mapView);
				collapseView(listViewComments);*/
				return true;
			}
		});
		// satellite or 2d mode
		mapView.setSatellite(true);
		mapView.setTraffic(false);
		
        mapController = mapView.getController();
		mapController.setZoom(19); // Zoom 1 is world view
		overlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.icon_location), mapView);
		overlayItems.setShowClose(false);
		overlayItems.setShowDisclosure(false);
		overlayItems.setSnapToCenter(true);
		//Gallery
        gallery = (Gallery) mapContainer.findViewById(R.id.galleryActListingDetailsPhotos);        
        gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				/*Intent galleryIntent = new Intent(ListingDetailsActivity.this, GalleryActivity.class);
				startActivity(galleryIntent);*/				
				popupWindow.showAsDropDown(gallery);
			}
		});
        images = new ArrayList<ImageDto>();
        galImageAdapter = new GalleryImageAdapter(this, images, null, false, false, false);
        gallery.setAdapter(galImageAdapter);
        
        //Comments
        listViewComments = (ListView) findViewById(R.id.listListingDetailsComments);
        listViewComments.addHeaderView(mapContainer);
        TextView textViewFooter = new TextView(this);
        textViewFooter.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, Utility.convertDpToPixel(this, 60)));
        listViewComments.addFooterView(textViewFooter);
        comments = getEmptyComment();
        adapterComments = new CommentListAdapter(this, comments);
        listViewComments.setAdapter(adapterComments);        
             
        //Send Messages        
        edtMessage = (EditText) findViewById(R.id.edtActListingDetailsMessage);
        imgBtnSend = (ImageButton) findViewById(R.id.imgBtnActListingDetailsSend);
        imgBtnSend.setOnClickListener(this);
        laySend = (LinearLayout) findViewById(R.id.layActListingDetailsSend);
        
                
        layRequest = (LinearLayout) findViewById(R.id.layActListingDetailsRequestItem);
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
    @Override
    protected void onResume() {
    	super.onResume();
    	locationManager.requestLocationUpdates(locProvider, 400, 1, this);
    	//Get listing details
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
					if (ERROR_CODE == Constants.RESULT_END_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_DELETE_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.ERROR_RESULT_GET_LISTING_DETAILS
							|| ERROR_CODE == Constants.ERROR_NETWORK_CONNECT) {
						finish();
					}else if (ERROR_CODE == Constants.RESULT_RELIST_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_FOLLOW_USER_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_UNFOLLOW_USER_SUCCESS) {
						new ListingDetailsTask().execute(ACT_MODE_DOWNLOAD_LISTINGS_DETAILS);
					} 
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}if(id == D_CONFIRM){
    		AlertDialog.Builder builder = new AlertDialog.Builder(ListingDetailsActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			
			builder.setPositiveButton(R.string.label_btn_yes, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					//Delete when code id confirm to delete
					if (ERROR_CODE == Constants.ERROR_CONFIRM_REMOVE_BOOKMARKS /*&& selectedListing != null*/) {
						new ListingDetailsTask().execute(ACT_MODE_REMOVE_BOOKMARK);
					} else if (ERROR_CODE == ERROR_CONF_MARK_AS_SPAM) {
						new ListingDetailsTask().execute(ACT_MODE_MARK_AS_SPAM);
					} else if (ERROR_CODE == ERROR_CONF_UNMARK_AS_SPAM){
						new ListingDetailsTask().execute(ACT_MODE_UNMARK_AS_SPAM);
					}
					dismissDialog(D_CONFIRM);					
				}
			});
			
			builder.setNegativeButton(R.string.label_btn_no, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_CONFIRM);
				}
			});
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	String message = "";
    	if(id == D_ERROR) {    		
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LISTING_DETAILS){
				message = (String) getResources().getText(R.string.error_get_listing_details);
			}else if (ERROR_CODE == Constants.RESULT_END_LISTING_SUCCESS) {
				message = (String) getResources().getText(R.string.msg_end_listing_success);
			}else if (ERROR_CODE == Constants.ERROR_RESULT_END_LISTING) {
				message = (String) getResources().getText(R.string.error_end_listing);
			}else if (ERROR_CODE == Constants.RESULT_RELIST_LISTING_SUCCESS) {
				message = (String) getResources().getText(R.string.msg_relist_listing_success);
			}else if (ERROR_CODE == Constants.ERROR_RESULT_RELIST_LISTING) {
				message = (String) getResources().getText(R.string.error_relist_listing);
			}else if (ERROR_CODE == Constants.RESULT_DELETE_LISTING_SUCCESS) {
				message = (String) getResources().getText(R.string.msg_delete_listing_success);
			}else if (ERROR_CODE == Constants.ERROR_RESULT_DELETE_LISTING) {
				message = (String) getResources().getText(R.string.error_delete_listing);
			}else if (ERROR_CODE == Constants.RESULT_BOOKMARKS_LISTING_SUCCESS) {
				message = (String) getResources().getText(R.string.msg_detail_listing_add_fav_success);
				listing.setInBookmars(true);
			}else if (ERROR_CODE == Constants.ERROR_RESULT_BOOKMARKS_LISTING) {
				message = (String) getResources().getText(R.string.msg_detail_listing_add_fav_failed);
			}else if(ERROR_CODE == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS){
				message = (String) getResources().getText(R.string.msg_bookmark_delete_success);
				listing.setInBookmars(false);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.error_remove_bookmarks);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_SEND_MESSAGE){
				message = (String) getResources().getText(R.string.error_send_message);
			}else if(ERROR_CODE == Constants.RESULT_SEND_MESSAGE_SUCCESS){
				setMessageLayoutVisiblity(false);
				isSendMessage = false;
				message = (String) getResources().getText(R.string.msg_send_message_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_ADD_COMMENT){
				message = (String) getResources().getText(R.string.error_add_comment);
			}else if(ERROR_CODE == Constants.RESULT_ADD_COMMENT_SUCCESS){
				setMessageLayoutVisiblity(false);
				message = (String) getResources().getText(R.string.msg_add_comment_success);
				//Download comments
				new ListingDetailsTask().execute(ACT_MODE_DOWNLOAD_COMMENTS);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_FOLLOW_USER || ERROR_CODE == Constants.ERROR_RESULT_UNFOLLOW_USER){
				message = (String) getResources().getText(R.string.g_error);
			}else if(ERROR_CODE == Constants.RESULT_FOLLOW_USER_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_follow_success);
			}else if(ERROR_CODE == Constants.RESULT_UNFOLLOW_USER_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_unfollow_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_LOCATION){
				message = (String) getResources().getText(R.string.error_postlisting_get_location);
			}else if(ERROR_CODE == Constants.ERROR_ENABLE_LOCATION_PROVIDER){
				message = (String) getResources().getText(R.string.msg_postlisting_enable_provider);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_MARK_AS_SPAM){
				message = (String) getResources().getText(R.string.g_error);
			}else if(ERROR_CODE == Constants.RESULT_MARK_AS_SPAM_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_mark_spam_success);
				listing.setCanMarkAsSpam(false);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UNMARK_AS_SPAM){
				message = (String) getResources().getText(R.string.g_error);
			}else if(ERROR_CODE == Constants.RESULT_UNMARK_AS_SPAM_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_unmark_spam_success);
				listing.setCanMarkAsSpam(true);
			}else if(ERROR_CODE == Constants.RESULT_REQUEST_AD_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_request_ad_success);
			}else if(ERROR_CODE == Constants.RESULT_CANCEL_REQUEST_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_cancel_request_ad_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_REQUEST_AD || ERROR_CODE == Constants.ERROR_RESULT_CANCEL_REQUEST){
				message = (String) getResources().getText(R.string.g_error);
			}else if (ERROR_CODE == Constants.ERROR_AD_NO_MORE_AVAILABLE) {
				message = (String) getResources().getText(R.string.g_error_ad_no_more_available);
			}
		} else if(id == D_CONFIRM){
			if(ERROR_CODE == ERROR_CONF_MARK_AS_SPAM){
				message = (String) getResources().getText(R.string.g_msg_mark_spam_conf);
			}else if(ERROR_CODE == ERROR_CONF_UNMARK_AS_SPAM){
				message = (String) getResources().getText(R.string.g_msg_unmark_spam_conf);
			}else if(ERROR_CODE == Constants.ERROR_CONFIRM_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.msg_bookmark_confirm_delete);
			}
		}
    	((AlertDialog) dialog).setMessage(message);
    }
    /**
     * Set details on screen
     * @param listing
     */
	private void setDetails(AdDto listing) {
		if (!listing.isOwner()) {
			layRequest.setVisibility(ViewGroup.VISIBLE);
			btnFollow.setVisibility(View.VISIBLE);
			btnSendMsg.setVisibility(View.VISIBLE);
		} else {
			btnFollow.setVisibility(View.GONE);
			btnSendMsg.setVisibility(View.GONE);
		}
		//Show on map
		updateMap(listing.getAddress().getLatitude(), listing.getAddress().getLongitude()
				, listing.getTitle(), listing.getDescription(), listing.getId()
				, listing.getImage()!= null? listing.getImage().getUrl(): "");
		if (listing.getCreator() != null && listing.getCreator().getAvatar() != null) {
			//set user info
			((VeneficaApplication) getApplication()).getImgManager().loadImage(
					Constants.PHOTO_URL_PREFIX
							+ listing.getCreator().getAvatar().getUrl(),
					profImgView,
					getResources().getDrawable(R.drawable.icon_picture_white));
		}
		
		/*if (listing.isRequested()) {
			btnGetIt.setText(getResources().getString(R.string.g_label_cancel_request));
		} else {*/
			btnGetIt.setText(getResources().getString(R.string.label_detail_listing_get_it));
//		}
		images.clear();
//		images.add(listing.getImage());
		images.addAll(listing.getImages());
		galImageAdapter.notifyDataSetChanged();
		txtUserName.setText(listing.getCreator().getFirstName()+" "+(listing.getCreator().getLastName()));
		txtMemberInfo.setText(getResources().getText(R.string.label_detail_listing_member_since).toString());
		txtMemberInfo.append(" ");
		txtMemberInfo.append(Utility.convertShortDateToString(listing.getCreator().getJoinedAt()));
		if (listing.getCreator().getAddress() != null) {
			txtAddress.setText(listing.getCreator().getAddress().getCity() +", "+listing.getCreator().getAddress().getCounty());
		}
		if (listing.getCreator().isInFollowings()) {
			btnFollow.setText(getResources().getString(R.string.g_label_unfollow));
		} else {
			btnFollow.setText(getResources().getString(R.string.label_follow));
		}
		
		//set listing details
		StringBuffer text = new StringBuffer();
		text.append("<b>"+listing.getTitle()+" </b>");
		text.append("<b>"+listing.getPrice().toString()+"</b>");
		text.append("<br>");
		text.append(listing.getDescription());
		txtDescription.setText(Html.fromHtml(text.toString()));
	}
	/**
	 * method to show listing location on map
	 * @param latitude
	 * @param longitude
	 * @param title
	 * @param description
	 */
	private void updateMap(double latitude, double longitude, String title, String description, long listingId, String imgURL){
		if (isMapShown ) {
			GeoPoint currLoc = new GeoPoint((int)(latitude * 1E6), (int)(longitude * 1E6));
			mapController.animateTo(currLoc);
			ListingOverlayItem overlayItem = new ListingOverlayItem(currLoc, title, description
					, listingId, Constants.PHOTO_URL_PREFIX + imgURL);
			overlayItems.clear();
			overlayItems.addOverlay(overlayItem);
			mapView.getOverlays().add(overlayItems);
			//scroll map to view overlay in visiblee area
			mapView.scrollBy(0, -mapView.getHeight()/4);
		}	
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(this);
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_listing_details, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	switch (CURRENT_MODE) {
		case ACT_MODE_MY_LISTINGS_DETAILS:
			menu.findItem(R.id.menu_listing_delete).setVisible(true);
			if (listing != null && listing.isExpired()) {
				menu.findItem(R.id.menu_listing_end).setVisible(false);
				menu.findItem(R.id.menu_listing_relist).setVisible(true);
				menu.findItem(R.id.menu_listing_update).setVisible(false);
			} else {
				menu.findItem(R.id.menu_listing_end).setVisible(true);
				menu.findItem(R.id.menu_listing_relist).setVisible(false);
				menu.findItem(R.id.menu_listing_update).setVisible(true);
			}			
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
    	int itemId = item.getItemId();
		if (itemId == R.id.menu_listing_update) {
			Intent intent = new Intent(ListingDetailsActivity.this, PostListingActivity.class);
			intent.putExtra("ad_id", selectedListingId);
			intent.putExtra("act_mode",PostListingActivity.ACT_MODE_UPDATE_LISTING);
			startActivityForResult(intent, PostListingActivity.ACT_MODE_UPDATE_LISTING);
		} else if (itemId == R.id.menu_listing_end) {
			new ListingDetailsTask().execute(ACT_MODE_END_LISTINGS);
		} else if (itemId == R.id.menu_listing_relist) {
			new ListingDetailsTask().execute(ACT_MODE_RELIST_LISTINGS);
		} else if (itemId == R.id.menu_listing_delete) {
			new ListingDetailsTask().execute(ACT_MODE_DELETE_LISTINGS);
		} else if (itemId == android.R.id.home) {
			if (isSendMsgVisible) {
				setMessageLayoutVisiblity(false);
				isSendMessage = false;
			}else if (isViewExpanded) {
				/*collapseAllViews();*/
			} else{
				finish();
			}			
		}else if (itemId == R.id.menu_listing_share) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
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
//    		showDialog(D_PROGRESS);
    		setSupportProgressBarIndeterminateVisibility(true);
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
				}else if (params[0].equals(ACT_MODE_END_LISTINGS)) {
					wrapper = wsAction.endListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
				}else if (params[0].equals(ACT_MODE_RELIST_LISTINGS)) {
					wrapper = wsAction.relistListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
				}else if (params[0].equals(ACT_MODE_DELETE_LISTINGS)) {
					wrapper = wsAction.deleteListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
				}else if (params[0].equals(ACT_MODE_BOOKMARK_LISTINGS)) {
					wrapper = wsAction.bookmarkListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId);
				}else if (params[0].equals(ACT_MODE_REMOVE_BOOKMARK)) {
					wrapper = wsAction.removeBookmarkedListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId);
				}else if (params[0].equals(ACT_MODE_DOWNLOAD_COMMENTS)) {
					wrapper = wsAction.getCommentsByListing(((VeneficaApplication)getApplication()).getAuthToken(), selectedListingId, -1, 10);
				}else if (params[0].equals(ACT_MODE_SEND_MESSAGE)) {
					wrapper = wsAction.sendMessageTo(((VeneficaApplication)getApplication()).getAuthToken()
							, getMessage());
				}else if (params[0].equals(ACT_MODE_ADD_COMMENT)) {
					wrapper = wsAction.addCommentToListing(((VeneficaApplication)getApplication()).getAuthToken()
							, selectedListingId, getComment());
				}else if (params[0].equals(ACT_MODE_FOLLOW_USER)) {
					wrapper = wsAction.followUser(((VeneficaApplication)getApplication()).getAuthToken()
							, listing.getCreator().getId());
				}else if (params[0].equals(ACT_MODE_UNFOLLOW_USER)) {
					wrapper = wsAction.unfollowUser(((VeneficaApplication)getApplication()).getAuthToken()
							, listing.getCreator().getId());
				}else if (params[0].equals(ACT_MODE_MARK_AS_SPAM)) {
					wrapper = wsAction.markListingAsSpam(((VeneficaApplication)getApplication()).getAuthToken()
							, listing.getId());
				}else if (params[0].equals(ACT_MODE_UNMARK_AS_SPAM)) {
					wrapper = wsAction.unmarkListingAsSpam(((VeneficaApplication)getApplication()).getAuthToken()
							, listing.getId());
				}else if (params[0].equals(ACT_MODE_REQUEST_AD)) {
					wrapper = wsAction.requestListing(((VeneficaApplication)getApplication()).getAuthToken()
							, listing.getId());
				}else if (params[0].equals(ACT_MODE_CANCEL_REQUEST_AD)) {
//					wrapper = wsAction.cancelRequestForListing(((VeneficaApplication)getApplication()).getAuthToken()
//							, listing.getId());
				}
			}catch (IOException e) {
				Log.e("ListingDetailsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("ListingDetailsTask::doInBackground :", e.toString());
			} catch (Exception e) {
				Log.e("ListingDetailsTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
    	@Override
    	protected void onPostExecute(ListingDetailsResultWrapper result) {
    		super.onPostExecute(result);
//    		dismissDialog(D_PROGRESS);
    		setSupportProgressBarIndeterminateVisibility(false);
    		if(result.listing == null && result.comments == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_LISTING_DETAILS_SUCCESS && result.listing != null) {
				setDetails(result.listing);
				listing = result.listing;
				//Download comments
				new ListingDetailsTask().execute(ACT_MODE_DOWNLOAD_COMMENTS);
				if (location != null) {
					onLocationChanged(location);
				}
				if (listing != null && listing.getExpiresAt() != null) {
					showDaysRemainingToExpiary(listing.getExpiresAt());
					showItemsRemaining(listing.getQuantity());
				}
			}else if(result.result == Constants.RESULT_GET_COMMENTS_SUCCESS /*&& result.comments != null*/){
				if (result.comments != null) {
					comments.clear();
					comments.addAll(result.comments) ;
					adapterComments.notifyDataSetChanged();
				} 				
			}else if(result.result != Constants.ERROR_RESULT_GET_COMMENTS){
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
    	}
    }
    
    @Override
    protected void onPause() {
    	//Flush image cache
    	((VeneficaApplication)getApplication()).getImgManager().flushCache();
    	super.onPause();
    }
    @Override
    public void onBackPressed() {
    	if (isSendMsgVisible) {
			setMessageLayoutVisiblity(false);
			isSendMessage = false;
		} else if (isViewExpanded) {
			/*collapseAllViews();*/
		} else {
			super.onBackPressed();
		}    	
    }
    
	/**
	 * Get comments to send
	 * @return commentDto
	 */
	public CommentDto getComment() {
		CommentDto commentDto = new CommentDto();
		commentDto.setCreatedAt(new Date());		
		commentDto.setOwner(listing.isOwner());
		commentDto.setText(edtMessage.getText().toString());
		/*commentDto.setPublisherName(publisherName);
		commentDto.setPublisherFullName(publisherFullName);
		commentDto.setPublisherAvatarUrl(publisherAvatarUrl);*/
		return commentDto;
	}

	/**
	 * Get message to send
	 * @return messageDto
	 */
	public MessageDto getMessage() {
		MessageDto messageDto = new MessageDto();
		messageDto.setCreatedAt(new Date());
		messageDto.setOwner(true);
		messageDto.setText(edtMessage.getText().toString());
		messageDto.setToName(listing.getCreator().getName());
		messageDto.setToFullName(listing.getCreator().getFirstName() +" "+listing.getCreator().getLastName());
		messageDto.setToAvatarUrl(listing.getCreator().getAvatar() != null && listing.getCreator().getAvatar().getUrl()!= null 
				?listing.getCreator().getAvatar().getUrl() : "/url_null");
		return messageDto;
	}
	
	@Override
	public void onClick(View v) {
		popupWindow.dismiss();
		int id = v.getId();
		if (id == R.id.btnActListingDetailsBookmark) {
			if (listing != null && !listing.isOwner() && !listing.isInBookmars()) {
				new ListingDetailsTask().execute(ACT_MODE_BOOKMARK_LISTINGS);
			} else if(!listing.isOwner()) {
				ERROR_CODE = Constants.ERROR_CONFIRM_REMOVE_BOOKMARKS;
				showDialog(D_CONFIRM);
			}
		} else if (id == R.id.imgBtnUserViewSendMsg && listing != null && !listing.isOwner()) {
			setMessageLayoutVisiblity(true);
			isSendMessage = true;
		} else if(id == R.id.imgBtnActListingDetailsSend && listing != null){
			//Check for empty message
			if (edtMessage.getText().toString().trim().length() > 0) {
				// hide virtual keyboard
				InputMethodManager imm = (InputMethodManager) ListingDetailsActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(imgBtnSend.getWindowToken(), 0);
				if (isSendMessage) {
					new ListingDetailsTask().execute(ACT_MODE_SEND_MESSAGE);
				} else {
					new ListingDetailsTask().execute(ACT_MODE_ADD_COMMENT);
				}
			}						
		} else if (id == R.id.imgBtnUserViewFollow) {
			if (listing.getCreator().isInFollowings()) {
				new ListingDetailsTask().execute(ACT_MODE_UNFOLLOW_USER);
			} else {
				new ListingDetailsTask().execute(ACT_MODE_FOLLOW_USER);
			}			
		} else if (id == R.id.btnActListingDetailsFlag && listing != null) {
			if (listing.isCanMarkAsSpam()) {
				ERROR_CODE = ERROR_CONF_MARK_AS_SPAM;				
			} else {
				ERROR_CODE = ERROR_CONF_UNMARK_AS_SPAM;
			}
			showDialog(D_CONFIRM);
		} else if (id == R.id.btnActListingDetailsRequest) {
			if (listing.isRequested()) {
				Utility.showLongToast(this, getResources().getString(R.string.g_msg_request_placed_already));
//				new ListingDetailsTask().execute(ACT_MODE_CANCEL_REQUEST_AD);
			} else {
				new ListingDetailsTask().execute(ACT_MODE_REQUEST_AD);
			}			
		} else if (id == R.id.btnListingDetailsPopUpMore) {
			//start rating activity
			Intent reviewIntent = new Intent(this, RatingActivity.class);
			reviewIntent.putExtra("ad_id", selectedListingId);
			reviewIntent.putExtra("to_user_id", listing.getCreator().getId());
			startActivity(reviewIntent);
		} else if (id == R.id.layActListingDetailUserDetails && listing!= null) {
			Intent accountIntent = new Intent(ListingDetailsActivity.this, ProfileDetailActivity.class);
			accountIntent.putExtra("act_mode",ProfileDetailActivity.ACT_MODE_VIEW_PROFILE);
			accountIntent.putExtra("user_name", listing.getCreator().getName());
			startActivity(accountIntent);
		} else if (id == R.id.btnListingDetailsPopUpShare) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			String shareText = listing.getTitle()+" "
					+ listing.getDescription()+" "
					+	Constants.PHOTO_URL_PREFIX + listing.getImage().getUrl();
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
			PackageManager pm = getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
			for (final ResolveInfo app : activityList) {
			    if ((app.activityInfo.name).contains("facebook")) {
			        final ActivityInfo activity = app.activityInfo;
			        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
			        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			        shareIntent.setComponent(name);
			        startActivity(shareIntent);
			        break;
			   }
			}
		} else if (id == R.id.btnListingDetailsPopUpTweet) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			String shareText = listing.getTitle()+" "
					+ listing.getDescription()+" "
					+	Constants.PHOTO_URL_PREFIX + listing.getImage().getUrl();
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
			PackageManager pm = getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
			for (final ResolveInfo app : activityList) {
			    if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
			        final ActivityInfo activity = app.activityInfo;
			        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
			        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			        shareIntent.setComponent(name);
			        startActivity(shareIntent);
			        break;
			   }
			}
		}
	}
	
	/**
	 * Set send message layout visibility
	 * @param isVisible
	 */
	public void setMessageLayoutVisiblity(boolean isVisible){
		if (isVisible) {
			laySend.setVisibility(ViewGroup.VISIBLE);
		} else {
			laySend.setVisibility(ViewGroup.GONE);
		}		
		isSendMsgVisible = isVisible;
	}
	
	/**
	 * Method to get dummy comment object when no comments are available
	 * @return
	 */
	private ArrayList<CommentDto> getEmptyComment(){
		ArrayList<CommentDto> comments = new ArrayList<CommentDto>();
		CommentDto comment = new CommentDto();
		comment.setPublisherAvatarUrl("");
		comment.setOwner(true);
		comment.setText("");
		comments.add(comment);
		return comments;
	}
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;
			showDistance(location);
		}
	}
	@Override
	public void onProviderDisabled(String provider) {
		Utility.showLongToast(this, provider + getResources().getString(R.string.msg_postlisting_provider_disabled));
	}
	@Override
	public void onProviderEnabled(String provider) {
		Utility.showLongToast(this, provider + getResources().getString(R.string.msg_postlisting_provider_selected));
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Helper method to show distance from current location on popup in miles
	 * @param location
	 */
	private void showDistance(Location location){
		if (listing != null && location != null) {
			Location location2 = new Location(location);
			location2.setLatitude(listing.getAddress().getLatitude());
			location2.setLongitude(listing.getAddress().getLongitude());
			float distance = location.distanceTo(location2);
			if (txtMiles != null) {
				DecimalFormat df = new DecimalFormat("#.##");
				txtMiles.setText(df.format(distance * 0.00062137119));
			}
		}		
	}
	
	/**
	 * show days to expire on popup
	 * @param expiaryDate
	 */
	private void showDaysRemainingToExpiary(Date expiaryDate){
		int diffInDays = (int)( (expiaryDate.getTime() - System.currentTimeMillis()) 
                / (1000 * 60 * 60 * 24) );
		txtDays.setText(diffInDays +" " + getResources().getString(R.string.g_label_days));
	}
	
	/**
	 * show remaining gifts quantity on popup
	 * @param itemsRemaining
	 */
	private void showItemsRemaining(int itemsRemaining){
		txtLeft.setText(listing.getQuantity()+"");
	}
}
