package com.venefica.module.user;

import java.io.IOException;
import java.util.regex.Pattern;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Class for startup activity 
 */
public class LoginActivity extends VeneficaActivity implements View.OnClickListener{
	/**
	 * SignIn and SignUp buttons
	 */
	private Button btnSignUp, btnSignIn, btnWelComeLogin, btnForgotPass, btnRequestPass;
	/**
	 * Input fields for login id and password
	 */
	private EditText edtLogin, edtPassword;
	/**
	 * Facebook, Twitter and VK buttons
	 */
	private Button btnFacebook, btnTwitter, btnVk;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CUSTOM = 3;
	/**
	 * Constants to identify auth request
	 */
	private final int AUTH_FACEBOOK = 11, AUTH_TWITTER = 12, AUTH_VK = 13, AUTH_VENEFICA = 14, CHECK_REGRISTRATION = 15;
	/**
	 * Auth type
	 */
	private static int AUTH_TYPE = -1;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Field validator
	 */
	private InputFieldValidator validator;
	/**
	 * Checkbox remember me
	 */
	private CheckBox chkRemember;
	/**
	 * Shared prefs
	 */
	private SharedPreferences prefs;
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * view groups 
	 */
	private RelativeLayout layWelcome, layForgetPass;
	private ScrollView layLogin;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS | Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		setSupportProgressBarIndeterminateVisibility(false);
		//view groups
		layWelcome = (RelativeLayout) findViewById(R.id.layLoginActWelcome);
		layLogin = (ScrollView) findViewById(R.id.layActLoginSignIn);
		layForgetPass = (RelativeLayout) findViewById(R.id.layLoginActForgetPassword);
		//Welcome 
		//Login
		btnWelComeLogin = (Button) findViewById(R.id.btnActLoginWelcomeSignIn);
		btnWelComeLogin.setOnClickListener(this);
		//Sign up button
		btnSignUp = (Button) findViewById(R.id.btnActLoginSignUp);
		btnSignUp.setOnClickListener(this);
		//Sign in button		
		btnSignIn = (Button) findViewById(R.id.btnActLoginSignIn);
		btnSignIn.setOnClickListener(this);
		//Facebook 
		btnFacebook = (Button) findViewById(R.id.btnActLoginFacebook);
		btnFacebook.setOnClickListener(this);
		//Twitter
		btnTwitter = (Button) findViewById(R.id.btnActLoginTwitter);
		btnTwitter.setOnClickListener(this);
		//VK
		btnVk = (Button) findViewById(R.id.btnActLoginVK);
		btnVk.setOnClickListener(this);
		//Forget pass
		btnForgotPass = (Button) findViewById(R.id.btnActLoginForgotPass);
		btnForgotPass.setOnClickListener(this);
		//Request pass
		btnRequestPass = (Button) findViewById(R.id.btnActLoginRequestPass);
		btnRequestPass.setOnClickListener(this);
		
		edtLogin = (EditText) findViewById(R.id.edtActLoginUserId);
		edtPassword = (EditText) findViewById(R.id.edtActLoginPassword);

		chkRemember = (CheckBox) findViewById(R.id.chkActLoginRemember);
		//Check if session is valid
		prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
		if((System.currentTimeMillis() - prefs.getLong(Constants.PREFERENCES_SESSION_IN_TIME, 0)) < Constants.SESSION_TIME_OUT 
				&& !prefs.getString(Constants.PREFERENCES_AUTH_TOKEN, "").equals("")){	

			((VeneficaApplication)getApplication()).setAuthToken(prefs.getString(Constants.PREFERENCES_AUTH_TOKEN, ""));
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong(Constants.PREFERENCES_SESSION_IN_TIME, System.currentTimeMillis());
			editor.commit();
			//Go to TabHome for valid session
			startHomeScreen();

			finish();
		}else if(prefs.getString(Constants.PREF_KEY_LOGIN_TYPE, "").equals(Constants.PREF_VAL_LOGIN_VENEFICA)){
			edtLogin.setText(prefs.getString(Constants.PREF_KEY_LOGIN, ""));
			edtPassword.setText(prefs.getString(Constants.PREF_KEY_PASSWORD, ""));
		}
		//Show welcome layout on startup
		layWelcome.setVisibility(ViewGroup.VISIBLE);
		layLogin.setVisibility(ViewGroup.GONE);
		layForgetPass.setVisibility(ViewGroup.GONE);
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
		if (id == R.id.btnActLoginSignUp) {
			AUTH_TYPE = AUTH_VENEFICA;
			//Go to user registration
			Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
			intent.putExtra("activity_mode",RegisterUserActivity.MODE_REGISTER_USR);
			startActivityForResult(intent, 0);
		} else if (id == R.id.btnActLoginSignIn) {
			AUTH_TYPE = AUTH_VENEFICA;
			if(validator == null){
				validator = new InputFieldValidator();
			}
			//Empty field check
			if (edtLogin.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
				Utility.showShortToast(LoginActivity.this, getResources().getString(R.string.msg_empty_user_password)
						+" "+getResources().getString(R.string.hint_user_password_pattern));
			}else if(!validateInput()){
				//Validate fields
				Utility.showShortToast(LoginActivity.this, getResources().getString(R.string.hint_user_password_pattern));
			}else{
				if(WSAction.isNetworkConnected(this)){
					//Call web service to authenticate user
					new AuthenticationTask().execute(AUTH_VENEFICA+"");
					//remember user pass
					rememberUser(chkRemember.isChecked());
				}else{
					ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
					showDialog(D_ERROR);
				}
			}
		} else if (id == R.id.btnActLoginFacebook) {
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			if(WSAction.isNetworkConnected(this)){
				loginWithSocialNetwork(Constants.SIGN_IN_FACEBOOK_URL, AUTH_FACEBOOK);
			}else{
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		} else if (id == R.id.btnActLoginTwitter) {
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			if(WSAction.isNetworkConnected(this)){
				loginWithSocialNetwork(Constants.SIGN_IN_TWITTER_URL, AUTH_TWITTER);
			}else{
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		} else if (id == R.id.btnActLoginVK) {
			if(WSAction.isNetworkConnected(this)){
				loginWithSocialNetwork(Constants.SIGN_IN_VK_URL, AUTH_VK);
			}else{
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		} else if(id == R.id.btnActLoginWelcomeSignIn){
			//Show login layout
			layWelcome.setVisibility(ViewGroup.GONE);
			layLogin.setVisibility(ViewGroup.VISIBLE);
			layForgetPass.setVisibility(ViewGroup.GONE);
		} else if (id == R.id.btnActLoginForgotPass) {
			//Show requset password layout
			layWelcome.setVisibility(ViewGroup.GONE);
			layLogin.setVisibility(ViewGroup.GONE);
			layForgetPass.setVisibility(ViewGroup.VISIBLE);			
		} else if (id == R.id.btnActLoginRequestPass) {
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			if(WSAction.isNetworkConnected(this)){
				
			}else{
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		}

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
	 * Method to validate id and password
	 * @return validation result
	 */
	private boolean validateInput(){
		if((validator.validateField(edtLogin, Pattern.compile(InputFieldValidator.phonePatternRegx)) 
				|| (validator.validateField(edtLogin, Pattern.compile(InputFieldValidator.emailPatternRegx)))
				&& validator.validateField(edtPassword, Pattern.compile(InputFieldValidator.charNumPatternRegx)))){
			return true;
		}
		return false;
	}


	/**
	 * Method to store user password when remember me is checked
	 */
	private void rememberUser(boolean rememberUser){
		if(prefs == null){
			prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
		}
		SharedPreferences.Editor editor = prefs.edit();
		if (rememberUser) {
			editor.putString(Constants.PREF_KEY_LOGIN_TYPE, Constants.PREF_VAL_LOGIN_VENEFICA);
			editor.putString(Constants.PREF_KEY_LOGIN, edtLogin.getText().toString());
			editor.putString(Constants.PREF_KEY_PASSWORD, edtPassword.getText().toString());
		} else {
			editor.putString(Constants.PREF_KEY_LOGIN, "");
			editor.putString(Constants.PREF_KEY_PASSWORD, "");
		}        
		editor.commit();
	}

	/**
	 * @author avinash
	 *	Class to perform login task
	 */
	class AuthenticationTask extends AsyncTask<String, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			showDialog(D_PROGRESS);
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
					wrapper = wsAction.authenticateUser(edtLogin.getText().toString(), edtPassword.getText().toString());					
				}else if (params[0].equalsIgnoreCase(CHECK_REGRISTRATION+"")) {
					wrapper = wsAction.checkUserRegistration(params[1]);
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
//			dismissDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result != null){
				if (result != null && result.result == Constants.RESULT_USER_AUTHORISED) {
					saveAuthToken(result.data);
					VeneficaApplication.authToken = result.data;
					startHomeScreen();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String authToken = "";
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Constants.ERROR_RESULT_USER_AUTH || resultCode == Constants.ERROR_NETWORK_CONNECT){
			ERROR_CODE = resultCode;
			showDialog(D_ERROR);
		}else{
			if(data != null && data.getExtras()!= null && data.getExtras().containsKey(Constants.PREFERENCES_AUTH_TOKEN)){
				AUTH_TYPE = requestCode;
				authToken = data.getExtras().getString(Constants.PREFERENCES_AUTH_TOKEN);
				new AuthenticationTask().execute(CHECK_REGRISTRATION+"", authToken);
				saveAuthToken(authToken);
				//Set data at application level
				((VeneficaApplication)getApplication()).setAuthToken(authToken);
			}
		}		
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
	
	@Override
	public void onBackPressed() {
		
		if (layForgetPass.getVisibility() == ViewGroup.VISIBLE) {
			layWelcome.setVisibility(ViewGroup.GONE);
			layLogin.setVisibility(ViewGroup.VISIBLE);
			layForgetPass.setVisibility(ViewGroup.GONE);
		} else if (layWelcome.getVisibility() == ViewGroup.VISIBLE) {
			super.onBackPressed();
		} else if (layLogin.getVisibility() == ViewGroup.VISIBLE) {
			layWelcome.setVisibility(ViewGroup.VISIBLE);
			layLogin.setVisibility(ViewGroup.GONE);
			layForgetPass.setVisibility(ViewGroup.GONE);
		}
	}
}