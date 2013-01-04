package com.venefica.module.listings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.internal.nineoldandroids.animation.AnimatorSet;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.venefica.module.main.R;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.main.VeneficaMapActivity;
import com.venefica.module.map.ListingOverlayItem;
import com.venefica.module.map.OnSingleTapListener;
import com.venefica.module.map.TapControlledMapView;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.ImageDownloadManager;
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
public class ListingDetailsActivity extends VeneficaMapActivity implements android.view.View.OnClickListener{
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
    private TextView txtUserName, txtMemberInfo, txtScore;
    /**
     * Buttons
     */
    private ImageButton btnBookmark, btnFlag, btnSendMsg, btnWatch, btnSend;
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
	 * View group to show hide contents
	 */
	private TableLayout layTable;
	private boolean isFullScreenComments = false;

	/**
	 * Send message/comments
	 */
	private ImageButton imgBtnSend;
	private EditText edtMessage;
	private LinearLayout laySend;
	private boolean isSendMsgVisible = false;
	private boolean isSendMessage = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_listing_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //set activity mode
        CURRENT_MODE = getIntent().getExtras().getInt("act_mode");
        selectedListingId = getIntent().getExtras().getLong("ad_id");
        //user details
        txtUserName = (TextView) findViewById(R.id.txtUserViewUserName);
        txtMemberInfo = (TextView) findViewById(R.id.txtUserViewMemberInfo);
        txtScore = (TextView) findViewById(R.id.txtUserViewScore);
        profImgView = (ImageView) findViewById(R.id.imgUserViewProfileImg);
        btnSendMsg = (ImageButton) findViewById(R.id.imgBtnUserViewSendMsg);
        btnSendMsg.setOnClickListener(this);
        //Details
        txtDescription = (TextView) findViewById(R.id.txtActListingDesc);
        //Bookmark
		btnBookmark = (ImageButton) findViewById(R.id.btnActListingDetailsBookmark);
		btnBookmark.setOnClickListener(this);
		//Map
		mapView = (TapControlledMapView) findViewById(R.id.mapviewActListingDetails);
		
		// dismiss balloon upon single tap of MapView 
		mapView.setOnSingleTapListener(new OnSingleTapListener() {		
			@Override
			public boolean onSingleTap(MotionEvent e) {
				overlayItems.hideAllBalloons();
				showFullScreenComments(true);
				return true;
			}
		});
		// satellite or 2d mode
		mapView.setSatellite(false);
		mapView.setTraffic(true);
		
        mapController = mapView.getController();
		mapController.setZoom(8); // Zoom 1 is world view
		overlayItems = new MapItemizedOverlay<ListingOverlayItem>(getResources().getDrawable(R.drawable.icon_location), mapView);
		overlayItems.setShowClose(false);
		overlayItems.setShowDisclosure(false);
		overlayItems.setSnapToCenter(true);
		//Gallery
        gallery = (Gallery) findViewById(R.id.galleryActListingDetailsPhotos);
        /*gallery.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent galleryIntent = new Intent(ListingDetailsActivity.this, GalleryActivity.class);
				startActivity(galleryIntent);
			}
		});*/
        images = new ArrayList<ImageDto>();
        galImageAdapter = new GalleryImageAdapter(this, images, null, false);
        gallery.setAdapter(galImageAdapter);
        /*if (CURRENT_MODE == ACT_MODE_MY_LISTINGS_DETAILS) {
        	btnBookmark.setImageResource(R.drawable.icon_cancel);
		}*/
        //Comments
        listViewComments = (ListView) findViewById(R.id.listListingDetailsComments);
        comments = getEmptyComment();
        adapterComments = new CommentListAdapter(this, comments);
        listViewComments.setAdapter(adapterComments);
        //view groups
        layTable = (TableLayout) findViewById(R.id.tableActListingDetails);
             
        //Send Messages
        
        edtMessage = (EditText) findViewById(R.id.edtActListingDetailsMessage);
        imgBtnSend = (ImageButton) findViewById(R.id.imgBtnActListingDetailsSend);
        imgBtnSend.setOnClickListener(this);
        laySend = (LinearLayout) findViewById(R.id.layActListingDetailsSend);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
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
							|| ERROR_CODE == Constants.RESULT_DELETE_LISTING_SUCCESS) {
						finish();
					}else if (ERROR_CODE == Constants.RESULT_RELIST_LISTING_SUCCESS) {
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
    	if(id == D_ERROR) {
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_MY_LISTINGS){
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
			}else if(ERROR_CODE == Constants.ERROR_CONFIRM_REMOVE_BOOKMARKS){
				message = (String) getResources().getText(R.string.msg_bookmark_confirm_delete)
						/*+" " +selectedListing.getTitle() +" ?"*/;
			}else if(ERROR_CODE == Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS){
				message = (String) getResources().getText(R.string.msg_bookmark_delete_success);
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
			}  		
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
    /**
     * Set details on screen
     * @param listing
     */
	private void setDetails(AdDto listing) {
		//Show on map
		updateMap(listing.getLatitude(), listing.getLongitude()
				, listing.getTitle(), listing.getDescription(), listing.getId()
				, listing.getImage()!= null? listing.getImage().getUrl(): "");
		//set user info
		ImageDownloadManager.getImageDownloadManagerInstance()
			.loadDrawable(Constants.PHOTO_URL_PREFIX + listing.getCreator().getAvatar().getUrl(), profImgView
				, getResources().getDrawable(R.drawable.ic_launcher));
		images.clear();
		images.add(listing.getImage());
		galImageAdapter.notifyDataSetChanged();
		txtUserName.setText(listing.getCreator().getFirstName()+" "+(listing.getCreator().getLastName()));
		txtMemberInfo.setText(getResources().getText(R.string.label_detail_listing_member_since).toString()/*listing.getCreator()*/);
		txtScore.setText(getResources().getText(R.string.label_detail_listing_score).toString()/*listing.getCreator()*/);
		
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
			mapController.zoomToSpan(overlayItems.getLatSpanE6(), overlayItems.getLonSpanE6());
			mapView.invalidate();
		}	
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onStop() {
//		ImageDownloadManager.getImageDownloadManagerInstance().reset();
		super.onStop();
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
			/*if (isFullScreenMap) {
				showFullScreenMap(false);
			} else*/ if (isFullScreenComments) {
				showFullScreenComments(false);
			}  else if (isSendMsgVisible) {
				setMessageLayoutVisiblity(false);
				isSendMessage = false;
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
    /**
     * Method to show hide full screen map
     * @param show
     */
    /*private void showFullScreenMap(boolean show){
    	isFullScreenMap = show;
    	if (show) {
    		Utility.collapse(layTable);   		
		} else {
			Utility.expand(layTable);
		}
    }*/
    @Override
    public void onBackPressed() {
    	/*if (isFullScreenMap) {
			showFullScreenMap(false);
		} else*/ if (isFullScreenComments) {
			showFullScreenComments(false);
		} else if (isSendMsgVisible) {
			setMessageLayoutVisiblity(false);
			isSendMessage = false;
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
		messageDto.setToAvatarUrl(listing.getCreator().getAvatar().getUrl());
		return messageDto;
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {			
			showFullScreenComments(true);
		}else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			showFullScreenComments(false);
		}
		if (isFullScreenComments) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}		
	}*/
	/**
	 * Method to show hide 
	 * @param show
	 */
	private void showFullScreenComments(boolean show){
		isFullScreenComments = show;
		if (show) {
			Utility.collapse(layTable);
//    		Utility.expand(listViewComments);
		} else {
			Utility.expand(layTable);
//    		Utility.expand(mapView);
		}
	}    

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnActListingDetailsBookmark) {
			if (listing != null && !listing.isOwner() && !listing.isInBookmars()) {
				new ListingDetailsTask().execute(ACT_MODE_BOOKMARK_LISTINGS);
			} else if(!listing.isOwner()) {
				ERROR_CODE = Constants.ERROR_CONFIRM_REMOVE_BOOKMARKS;
				showDialog(D_CONFIRM);
			}
		} else if (v.getId() == R.id.imgBtnUserViewSendMsg && listing != null && !listing.isOwner()) {
			setMessageLayoutVisiblity(true);
			isSendMessage = true;
		} else if(v.getId() == R.id.imgBtnActListingDetailsSend && listing != null){
			if (isSendMessage) {
				new ListingDetailsTask().execute(ACT_MODE_SEND_MESSAGE);
			} else {
				new ListingDetailsTask().execute(ACT_MODE_ADD_COMMENT);
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
	
	private ArrayList<CommentDto> getEmptyComment(){
		ArrayList<CommentDto> comments = new ArrayList<CommentDto>();
		CommentDto comment = new CommentDto();
		comment.setPublisherAvatarUrl("");
		comment.setOwner(true);
		comment.setText("");
		comments.add(comment);
		return comments;
	}
}
