package com.venefica.module.user;

import java.io.IOException;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.ChangePasswordDialogFragment.OnChangePasswordDialogListener;
import com.venefica.module.user.EditProfileFragment.OnEditProfileListener;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Activity to show/edit user profile
 */
public class UserProfileActivity extends VeneficaActivity implements OnEditProfileListener, OnChangePasswordDialogListener{

	/**
	 * modes
	 */
	public static int ACT_MODE_VIEW_PROFILE = 4001;
	public static int ACT_MODE_EDIT_PROFILE = 4002;
	public static int ACT_MODE_UPDATE_PROF = 4003;
	public static int ACT_MODE_GET_USER = 4004;
	public static int ACT_MODE_CHANGE_PASSWORD = 4005;
	public static int ACT_MODE_GET_FOLLOWINGS = 4006;
	public static int ACT_MODE_GET_FOLLOWERS = 4007;
	public static int ACT_MODE_GET_REVIEWS = 4008;
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
	 * Calendar for current date
	 */
	private Calendar calendar = Calendar.getInstance();
	/**
	 * web service task progress 
	 */
	private boolean isWorking = false;
	/**
	 * true if edit fragment displayed
	 */
	private boolean isEditFragDisplayed = false;
	/**
	 * fragments
	 */
	private ViewProfileDetailsFragment viewProfileDetailsFragment;
	private EditProfileFragment editProfileFragment;
	private ChangePasswordDialogFragment chPasswordDialogFragment;
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * for change password
	 */
	public String oldPassword = null;
	public String newPassword = null;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_user_profile);
		ACT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_EDIT_PROFILE);
			
		editProfileFragment = new EditProfileFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.layActUserProfieRoot, editProfileFragment).commit();
		isEditFragDisplayed = true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_user_profile, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (ACT_MODE == ACT_MODE_VIEW_PROFILE) {
			menu.findItem(R.id.menu_save_profile).setEnabled(false);
		} else if (ACT_MODE == ACT_MODE_EDIT_PROFILE){
			menu.findItem(R.id.menu_save_profile).setEnabled(true);
		}		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		onBackPressed();
    	} else if (itemId == R.id.menu_save_profile) {
    		//update profile
    		if (!isWorking) {
    			if(WSAction.isNetworkConnected(this)){
    				if (editProfileFragment.validateFields()) {
						new UserProfileTask().execute(ACT_MODE_UPDATE_PROF);
					}
    			} else {
    		    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
    		    	showDialog(D_ERROR);	 
    		    }
			}			
		}
		return true;
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UPDTAE_USER){
				message = (String) getResources().getText(R.string.error_update_user);
			}else if(ERROR_CODE == Constants.RESULT_UPDATE_USER_SUCCESS){
				message = (String) getResources().getText(R.string.msg_update_user_success);
			}else if(ERROR_CODE == Constants.RESULT_CHANGE_PASSWORD_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_change_pwd_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_CHANGE_PASSWORD){
				message = (String) getResources().getText(R.string.g_error_change_pwd);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_INVALID_OLD_PASSWORD){
				message = (String) getResources().getText(R.string.g_error_invalid_old_pwd);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(UserProfileActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
    		DatePickerDialog dateDg = new DatePickerDialog(UserProfileActivity.this, new OnDateSetListener() {
				
				public void onDateSet(DatePicker arg0, int year, int month, int date) {
					editProfileFragment.setSelectedDate((month>9? month: "0"+month)+"/"+(date>9? date: "0"+date)+"/"+year);					
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			return dateDg;
    	}
    	return null;
    }

	@Override
	public void onDateEdit() {
		showDialog(D_DATE);
	}
	
	/**
	 * @author avinash
	 * Class to perform network task.
	 */
	class UserProfileTask extends AsyncTask<Integer, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(Integer... params) {
			UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_UPDATE_PROF)) {
					wrapper.result = wsAction.updateUser(((VeneficaApplication)getApplication()).getAuthToken()
							, editProfileFragment.getUserDataToUpdate());
				} else if (params[0].equals(ACT_MODE_GET_USER)) {
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken(),"");
				} else if (params[0].equals(ACT_MODE_CHANGE_PASSWORD)) {
					wrapper = wsAction.changePassword(((VeneficaApplication)getApplication()).getAuthToken(), oldPassword, newPassword);
				} else if (params[0].equals(ACT_MODE_GET_FOLLOWINGS)) {
					wrapper = wsAction.getFollowings(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());
				} else if (params[0].equals(ACT_MODE_GET_FOLLOWERS)) {
					wrapper = wsAction.getFollowers(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());							
				} else if (params[0].equals(ACT_MODE_GET_REVIEWS)) {
					wrapper = wsAction.getReceivedRatings(((VeneficaApplication)getApplication()).getAuthToken()
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
			} else if (result.followings != null && result.result == Constants.RESULT_GET_FOLLOWINGS_SUCCESS) {
				viewProfileDetailsFragment.setFollowings(result.followings);
			} else if (result.followers != null && result.result == Constants.RESULT_GET_FOLLOWERS_SUCCESS) {
				viewProfileDetailsFragment.setFollowers(result.followers);
			} else if (result.reviews != null && result.result == Constants.RESULT_GET_REVIEWS_SUCCESS) {
				viewProfileDetailsFragment.setFollowers(result.followers);
			} else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
				if(ERROR_CODE == Constants.RESULT_UPDATE_USER_SUCCESS){					
					onBackPressed();
				} else if (ERROR_CODE == Constants.RESULT_CHANGE_PASSWORD_SUCCESS) {
					chPasswordDialogFragment.dismiss();
				}
			}
		}
	}
	/**
	 * show change password dialog
	 */
	private void showEditDialog() {
        chPasswordDialogFragment = new ChangePasswordDialogFragment();
        chPasswordDialogFragment.show(getSupportFragmentManager(), "fragment_change_Password");
    }
	@Override
	public void onBackPressed() {
		/*if (isEditFragDisplayed) {
			isEditFragDisplayed = false;
			ACT_MODE = ACT_MODE_VIEW_PROFILE;
			new UserProfileTask().execute(ACT_MODE_GET_USER); 
		}*/
		super.onBackPressed();
	}

	@Override
	public void onSavePassword(String oldPassword, String newPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		new UserProfileTask().execute(ACT_MODE_CHANGE_PASSWORD);
	}

	@Override
	public void onChangePassword() {
		showEditDialog();
	}

	@Override
	public void onSaveButtonClick() {
		//update profile
		if (!isWorking) {
			if(WSAction.isNetworkConnected(this)){
				if (editProfileFragment.validateFields()) {
					new UserProfileTask().execute(ACT_MODE_UPDATE_PROF);
				}
			} else {
		    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
		    	showDialog(D_ERROR);	 
		    }
		}	
	}
}
