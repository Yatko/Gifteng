package com.venefica.module.user;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.SigninFragment.OnSigninListener;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Class for startup activity 
 */
public class LoginActivity extends SherlockFragmentActivity implements OnClickListener, OnSigninListener {
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CUSTOM = 3;
	/**
	 * Constants to identify auth request
	 */
	private final int AUTH_FACEBOOK = 11, AUTH_TWITTER = 12, AUTH_VK = 13
			, AUTH_VENEFICA = 14, CHECK_REGRISTRATION = 15, MODE_GET_USER = 16;
	/**
	 * activity request codes
	 */
	private int REQ_RESET_PASSWORD = 4001;
	/**
	 * Auth type
	 */
	private static int AUTH_TYPE = -1;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Shared prefs
	 */
	private SharedPreferences prefs;
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * userId & password
	 */
	private String userId, password;
	/**
	 * flag processing (working) indicates post listing process is running
	 */
	private boolean uploadingData = false;
	/**
	 * login fragment
	 */
	private SigninFragment signinFragment;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_login);
		
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		signinFragment = new SigninFragment();
		fragmentTransaction.add(R.id.layActLoginRoot, signinFragment);
		fragmentTransaction.commit();
		
		if(!WSAction.isNetworkConnected(this)){
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		int id = v.getId();
		

	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if(id == D_ERROR) {
			//Display error message as per the error code
			if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_network_01));
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_network_02));
			}else if(ERROR_CODE == Constants.ERROR_RESULT_USER_AUTH){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_social_auth));
			}else if(ERROR_CODE == Constants.ERROR_USER_UNAUTHORISED){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.msg_invalid_user_password));
			}
		}    	
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		//Create progress dialog
		if(id == D_PROGRESS){
			ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
		//Create error dialog
		if(id == D_ERROR){
			AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}    	 	
		return null;
	}


	/**
	 * Method to start social network login activity
	 * @param url to authenticate
	 */
	private void loginWithSocialNetwork(String url, int requestCode){
		Intent intent = new Intent(this, WebAuthorizationActivity.class);
		intent.putExtra(WebAuthorizationActivity.AUTH_URL, url);
		startActivityForResult(intent, requestCode);
	}

	


	/**
	 * Start home screen of application
	 */
	private void startHomeScreen(){
		Intent intent = new Intent(this, SearchListingsActivity.class);
		intent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY);
		startActivity(intent);
		finish();
	}
	/**
	 * Method to store authToken
	 */
	private void saveAuthToken(String authToken){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PREFERENCES_AUTH_TOKEN, authToken);
		editor.commit();
	}
	
	/**
	 * Method to store user password when remember me is checked
	 */
	private void rememberUser(boolean rememberUser, String userId, String password){
		if(prefs == null){
			prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
		}
		SharedPreferences.Editor editor = prefs.edit();
		if (rememberUser) {
			editor.putString(Constants.PREF_KEY_LOGIN_TYPE, Constants.PREF_VAL_LOGIN_VENEFICA);
			editor.putString(Constants.PREF_KEY_LOGIN, userId);
			editor.putString(Constants.PREF_KEY_PASSWORD, password);
		} else {
			editor.putString(Constants.PREF_KEY_LOGIN, "");
			editor.putString(Constants.PREF_KEY_PASSWORD, "");
		}        
		editor.commit();
	}
	
	@Override
	public void onSigninButtonClick(boolean rememberUser, String userId, String password) {
		if (!uploadingData) {
			if (WSAction.isNetworkConnected(this)) {
				this.userId = userId;
				this.password = password;
				//Call web service to authenticate user
				new AuthenticationTask().execute(AUTH_VENEFICA + "");
				//remember user pass
				rememberUser(rememberUser, userId, password);
			} else {
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		}
	}

	@Override
	public void onForgotPasswordButtonClick() {
		if (!uploadingData) {
			Intent forgotPassIntent = new Intent(this,
					PasswordResetActivity.class);
			forgotPassIntent.putExtra("act_mode",
					PasswordResetActivity.ACT_MODE_VERIFY_EMAIL);
			startActivityForResult(forgotPassIntent, REQ_RESET_PASSWORD);
		}		
	}
	/**
	 * @author avinash
	 *	Class to perform login task
	 */
	class AuthenticationTask extends AsyncTask<String, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadingData = true;
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(String... params) {
			UserRegistrationResultWrapper wrapper = null;
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equalsIgnoreCase(AUTH_VENEFICA+"")) {
					wrapper = wsAction.authenticateUser(userId, password);					
				}else if (params[0].equalsIgnoreCase(CHECK_REGRISTRATION+"")) {
					wrapper = wsAction.checkUserRegistration(params[1]);
				}else if (params[0].equalsIgnoreCase(MODE_GET_USER+"")) {
					wrapper = new UserRegistrationResultWrapper();
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken());
					if (wrapper.userDto == null) {//change after modifying Dto
						wrapper = null;
					}
				}
			} catch (IOException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
			} catch (XmlPullParserException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(UserRegistrationResultWrapper result) {
			super.onPostExecute(result);
			uploadingData = false;
			setSupportProgressBarIndeterminateVisibility(false);
			if(result != null){
				if (result != null && result.result == Constants.RESULT_USER_AUTHORISED) {
					saveAuthToken(result.data);
					((VeneficaApplication)getApplication()).setAuthToken(result.data);
					//Call web service to authenticate user
					new AuthenticationTask().execute(MODE_GET_USER+"", result.data);
				}else if (result != null && result.result == Constants.ERROR_USER_UNAUTHORISED) {
					ERROR_CODE = result.result;
					showDialog(D_ERROR);
				}else if (result != null &&   result.result == Constants.RESULT_IS_USER_EXISTS_SUCCESS) {
					if(result.data.contains("false")){
						Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
						intent.putExtra("activity_mode",RegisterUserActivity.MODE_UPDATE_PROF);
						if(AUTH_TYPE == AUTH_FACEBOOK){
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_FACEBOOK);
						}else if (AUTH_TYPE == AUTH_TWITTER) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_TWITTER);
						}else if (AUTH_TYPE == AUTH_VK) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_VK);
						}else if (AUTH_TYPE == AUTH_VENEFICA) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_VENEFICA);
						}
						startActivityForResult(intent, 0);
					}else{
						startHomeScreen();
					}
				}else if (result != null && result.userDto != null) {
					((VeneficaApplication)getApplication()).setUser(result.userDto);
					startHomeScreen();
				}else{
					ERROR_CODE  = Constants.ERROR_NETWORK_CONNECT;
					showDialog(D_ERROR);
				}				
			}else{
				ERROR_CODE  = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}
		}
	}	
}