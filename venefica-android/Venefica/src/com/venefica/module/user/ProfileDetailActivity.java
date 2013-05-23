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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.BadgeView;
import com.venefica.module.utils.Utility;
import com.venefica.services.RatingDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class ProfileDetailActivity extends VeneficaActivity {

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
	public static int ACT_MODE_VIEW_PROFILE = 4001;
	public static int ACT_MODE_GET_USER = 4004;
	public static int ACT_MODE_CHANGE_PASSWORD = 4005;
	public static int ACT_MODE_GET_FOLLOWINGS = 4006;
	public static int ACT_MODE_GET_FOLLOWERS = 4007;
	public static int ACT_MODE_GET_REVIEWS = 4008;
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * Calendar for current date
	 */
	private Calendar calendar = Calendar.getInstance();
	private UserDto user;
	/**
	 * view to show user details
	 */
	private TextView txtUserName, txtMemberInfo, txtAddress;
	private ImageView profImgView;
	private ImageButton imgButtonMsg;
	private BadgeView badgeView;

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
		
		findViewById(R.id.imgBtnUserViewFollow).setVisibility(View.GONE);
		// user details
		txtUserName = (TextView) findViewById(R.id.txtUserViewUserName);
		txtMemberInfo = (TextView) findViewById(R.id.txtUserViewMemberInfo);
		txtAddress = (TextView) findViewById(R.id.txtUserViewAddress);
		profImgView = (ImageView) findViewById(R.id.imgUserViewProfileImg);
		
		imgButtonMsg = (ImageButton) findViewById(R.id.imgBtnUserViewSendMsg);
		badgeView = new BadgeView(this, imgButtonMsg);
		badgeView.setText("2");
		badgeView.setTextColor(Color.BLUE);
		badgeView.setBadgeBackgroundColor(Color.YELLOW);
		// badgeView.setTextSize(12);
		badgeView.toggle();
		new UserProfileTask().execute(ACT_MODE_GET_FOLLOWINGS);
		new UserProfileTask().execute(ACT_MODE_GET_FOLLOWERS);
		new UserProfileTask().execute(ACT_MODE_GET_REVIEWS);
		
		setUserDto(((VeneficaApplication) getApplication()).getUser());

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_user_profile, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		if (ACT_MODE == ACT_MODE_VIEW_PROFILE) {
			menu.findItem(R.id.menu_edit_profile).setEnabled(true);
			menu.findItem(R.id.menu_save_profile).setEnabled(false);
		} /*else if (ACT_MODE == ACT_MODE_EDIT_PROFILE){
			menu.findItem(R.id.menu_edit_profile).setEnabled(false);
			menu.findItem(R.id.menu_save_profile).setEnabled(true);
		}*/		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
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
					&& ERROR_CODE == Constants.ERROR_RESULT_GET_FOLLOWINGS
					&& ERROR_CODE == Constants.ERROR_RESULT_GET_REVIEWS) {
				message = (String) getResources().getText(R.string.g_error);
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
//				showDialog(D_PROGRESS);
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
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken());
				}else if (params[0].equals(ACT_MODE_GET_FOLLOWINGS)) {
					wrapper = wsAction.getFollowings(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());
				} else if (params[0].equals(ACT_MODE_GET_FOLLOWERS)) {
					wrapper = wsAction.getFollowers(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());							
				} else if (params[0].equals(ACT_MODE_GET_REVIEWS)) {
					wrapper = wsAction.getReviews(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());
				}
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
			if(result.userDto == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			} else if (result.userDto != null) {
				((VeneficaApplication)getApplication()).setUser(result.userDto);
				user = result.userDto;
			} else if (result.followings != null && result.result == Constants.RESULT_GET_FOLLOWINGS_SUCCESS) {
				setFollowings(result.followings);
			} else if (result.followers != null && result.result == Constants.RESULT_GET_FOLLOWERS_SUCCESS) {
				setFollowers(result.followers);
			} else if (result.reviews != null && result.result == Constants.RESULT_GET_REVIEWS_SUCCESS) {
				setReviews(result.reviews);
			} else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
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
}
