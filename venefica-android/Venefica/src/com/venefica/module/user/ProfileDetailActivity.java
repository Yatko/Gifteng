package com.venefica.module.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.BadgeView;
import com.venefica.module.utils.Utility;
import com.venefica.services.RatingDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class ProfileDetailActivity extends VeneficaActivity implements OnClickListener {

	private int ACT_MODE;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;
	/**
	 * web service task progress 
	 */
	private boolean isWorking = false;
	/**
	 * modes
	 */
	public static final int ACT_MODE_VIEW_PROFILE = 4001;
	public static final int ACT_MODE_GET_USER = 4004;
	public static final int ACT_MODE_CHANGE_PASSWORD = 4005;
	public static final int ACT_MODE_GET_FOLLOWINGS = 4006;
	public static final int ACT_MODE_GET_FOLLOWERS = 4007;
	public static final int ACT_MODE_GET_REVIEWS = 4008;
	public static final int ACT_MODE_FOLLOW_USER = 4009;
	public static final int ACT_MODE_UNFOLLOW_USER = 4010;
	
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * Calendar for current date
	 */
	private Calendar calendar = Calendar.getInstance();
	/**
	 * user to view profile
	 */
	private UserDto user;
	private String userName;
	/**
	 * view to show user details
	 */
	private TextView txtUserName, txtMemberInfo, txtAddress;
	private ImageView profImgView;
	private ImageButton imgButtonEdit;
	private Button btnFollow;
//	private BadgeView badgeView;
	private FrameLayout layUserDetails;

	private ExpandableListView userExpandableListView;
	private ProfileExpandableListAdapter userExpandableListAdapter;
	
	private ExpandableListView reviewExpandableListView;
	private ArrayList<RatingDto> userReviewList;
	private ReviewsExpandableListAdapter reviewsExpandableListAdapter;
	
	private ArrayList<ProfileGroup> profileGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// Show the Up button in the action bar.
//		setupActionBar();
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_profile_detail);
		ACT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_VIEW_PROFILE);
		userName = getIntent().getStringExtra("user_name");
		
		userReviewList = new ArrayList<RatingDto>();
		profileGroups = new ArrayList<ProfileGroup>();
		userExpandableListAdapter = new ProfileExpandableListAdapter(this, profileGroups);
		
		reviewsExpandableListAdapter = new ReviewsExpandableListAdapter(this, userReviewList);
		
		//Reviews
		reviewExpandableListView = (ExpandableListView) findViewById(R.id.expListViewActUserProfieReviews);
		reviewExpandableListView.setAdapter(reviewsExpandableListAdapter);
		//expandable list view follower followings
		userExpandableListView = (ExpandableListView) findViewById(R.id.expListViewActUserProfie);
		userExpandableListView.setAdapter(userExpandableListAdapter);		
		
		// user details
		layUserDetails = (FrameLayout) findViewById(R.id.layActUserProfieUserDetails);
		layUserDetails.setOnClickListener(this);
		txtUserName = (TextView) findViewById(R.id.txtUserViewUserName);
		txtMemberInfo = (TextView) findViewById(R.id.txtUserViewMemberInfo);
		txtAddress = (TextView) findViewById(R.id.txtUserViewAddress);
		profImgView = (ImageView) findViewById(R.id.imgUserViewProfileImg);
		
		imgButtonEdit = (ImageButton) findViewById(R.id.imgBtnUserViewSendMsg);
		imgButtonEdit.setOnClickListener(this);
		btnFollow = (Button) findViewById(R.id.imgBtnUserViewFollow);
		btnFollow.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new UserProfileTask().execute(ACT_MODE_GET_USER);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		onBackPressed();
    	}
    	return true;
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(ProfileDetailActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDetailActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
//							RegisterUserActivity.this.finish();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	if(id == D_DATE){
    		DatePickerDialog dateDg = new DatePickerDialog(ProfileDetailActivity.this, new OnDateSetListener() {
				
				public void onDateSet(DatePicker arg0, int year, int month, int date) {
//					editProfileFragment.setSelectedDate((month>9? month: "0"+month)+"/"+(date>9? date: "0"+date)+"/"+year);					
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
			} else if (ERROR_CODE == Constants.ERROR_RESULT_GET_FOLLOWERS
					|| ERROR_CODE == Constants.ERROR_RESULT_GET_FOLLOWINGS
					|| ERROR_CODE == Constants.ERROR_RESULT_GET_REVIEWS
					|| ERROR_CODE == Constants.ERROR_RESULT_FOLLOW_USER 
					|| ERROR_CODE == Constants.ERROR_RESULT_UNFOLLOW_USER) {
				message = (String) getResources().getText(R.string.g_error);
			} else if(ERROR_CODE == Constants.RESULT_FOLLOW_USER_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_follow_success);
				user.setInFollowings(true);
				setUserDto(user);
			} else if(ERROR_CODE == Constants.RESULT_UNFOLLOW_USER_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_unfollow_success);
				user.setInFollowings(false);
				setUserDto(user);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/**
	 * @author avinash
	 * Class to perform network task.
	 */
	class UserProfileTask extends AsyncTask<Integer, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isWorking = true;
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(Integer... params) {
			UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_GET_USER)) {
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken(), userName);
				}else if (params[0].equals(ACT_MODE_GET_FOLLOWINGS)) {
					wrapper = wsAction.getFollowings(((VeneficaApplication)getApplication()).getAuthToken()
							, user.getId());
				} else if (params[0].equals(ACT_MODE_GET_FOLLOWERS)) {
					wrapper = wsAction.getFollowers(((VeneficaApplication)getApplication()).getAuthToken()
							, user.getId());							
				} else if (params[0].equals(ACT_MODE_GET_REVIEWS)) {
					wrapper = wsAction.getReceivedRatings(((VeneficaApplication)getApplication()).getAuthToken()
							, user.getId());
				} else if (params[0].equals(ACT_MODE_FOLLOW_USER)) {
					ListingDetailsResultWrapper res = wsAction.followUser(((VeneficaApplication)getApplication()).getAuthToken()
							, user.getId());
					wrapper.result = res.result;
				} else if (params[0].equals(ACT_MODE_UNFOLLOW_USER)) {
					ListingDetailsResultWrapper res = wsAction.unfollowUser(((VeneficaApplication)getApplication()).getAuthToken()
							, user.getId());
					wrapper.result = res.result;
				}/*else if (params[0].equals(ACT_MODE_SEND_MESSAGE)) {
					ListingDetailsResultWrapper res = wsAction.sendMessageTo(((VeneficaApplication)getApplication()).getAuthToken()
							, getMessage());
					wrapper.result = res.result;
				}*/
			}catch (IOException e) {
				Log.e("UpdateUserTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("UpdateUserTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(UserRegistrationResultWrapper result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			isWorking = false;
			if(result.userDto == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			} else if (result.userDto != null) {
				user = result.userDto;
				setUserDto(result.userDto);
				getDataForUser();				
			} else if (result.followings != null && result.result == Constants.RESULT_GET_FOLLOWINGS_SUCCESS) {
				setFollowings(result.followings);
			} else if (result.followers != null && result.result == Constants.RESULT_GET_FOLLOWERS_SUCCESS) {
				setFollowers(result.followers);
			} else if (result.reviews != null && result.result == Constants.RESULT_GET_REVIEWS_SUCCESS) {
				setReviews(result.reviews);
			} else if (result.result != -1 && result.result != Constants.ERROR_NO_DATA) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}
	
	private void clearAll(){
		profileGroups.clear();
		userExpandableListAdapter.notifyDataSetChanged();
	}
	/**
	 * Method to update followings list
	 * @param followingsList
	 */
	public void setFollowings(List<UserDto> followingsList) {				
		ProfileGroup profileGroup1 = new ProfileGroup(); 
		profileGroup1.setGroupName(getResources().getString(R.string.g_label_following));
		profileGroup1.setUsers(followingsList);
		profileGroups.add(profileGroup1);
		userExpandableListAdapter.notifyDataSetChanged();		
	}

	public void getDataForUser() {
		clearAll();
		new UserProfileTask().execute(ACT_MODE_GET_FOLLOWINGS);
		new UserProfileTask().execute(ACT_MODE_GET_FOLLOWERS);
		new UserProfileTask().execute(ACT_MODE_GET_REVIEWS);
	}

	/**
	 * Method to update followers
	 * @param followersList
	 */
	public void setFollowers(List<UserDto> followersList) {
		ProfileGroup profileGroup = new ProfileGroup(); 
		profileGroup.setGroupName(getResources().getString(R.string.g_label_followers));
		profileGroup.setUsers(followersList);
		profileGroups.add(profileGroup);
		userExpandableListAdapter.notifyDataSetChanged();
	}
	/**
	 * Method to update reviews
	 * @param reviewsList
	 */
	public void setReviews(List<RatingDto> reviewsList) {
		reviewsList.clear();
		reviewsList.addAll(reviewsList);
		reviewsExpandableListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Set user info
	 * 
	 * @param userDto
	 */
	public void setUserDto(UserDto userDto) {
		this.user = userDto;
		if (userDto != null ) {
			txtUserName.setText(userDto.getFirstName() + " "
					+ (userDto.getLastName()));
			txtMemberInfo.setText(getResources().getText(
					R.string.label_detail_listing_member_since).toString());
			txtMemberInfo.append(" ");
			if (userDto.getJoinedAt() != null) {
				txtMemberInfo.append(Utility.convertShortDateToString(userDto
						.getJoinedAt()));
			}
			if (userDto.getAddress() != null) {
				txtAddress.setText(userDto.getAddress().getCity() + ", " + userDto.getAddress().getCounty());
			}
			if (userDto.getId() == ((VeneficaApplication) getApplication()).getUser().getId()) {
				imgButtonEdit.setVisibility(View.VISIBLE);
				imgButtonEdit.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit));
				btnFollow.setVisibility(View.INVISIBLE);
			} else {
				if (userDto.isInFollowings()) {
					btnFollow.setText(getResources().getText(R.string.g_label_unfollow));
				} else {
					btnFollow.setText(getResources().getText(R.string.label_follow));
				}
				btnFollow.setVisibility(View.VISIBLE);
				imgButtonEdit.setVisibility(View.GONE);
			}
			if (userDto.getAvatar() != null
					&& userDto.getAvatar().getUrl() != null) {
				((VeneficaApplication) getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userDto.getAvatar().getUrl(),
								profImgView,
								getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (!isWorking) {
			switch (id) {
			case R.id.imgBtnUserViewFollow:
				if (user.isInFollowings()) {
					new UserProfileTask().execute(ACT_MODE_UNFOLLOW_USER);
				} else {
					new UserProfileTask().execute(ACT_MODE_FOLLOW_USER);
				}
				break;

			case R.id.imgBtnUserViewSendMsg:
				Intent accountIntent = new Intent(this,
						UserProfileActivity.class);
				accountIntent.putExtra("act_mode",
						UserProfileActivity.ACT_MODE_EDIT_PROFILE);
				startActivity(accountIntent);
				break;
			}
		}
	}
}
