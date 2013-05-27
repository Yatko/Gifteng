package com.venefica.module.user;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.PasswordResetEmailLookupFragment.OnPasswordResetEmailLookupListener;
import com.venefica.module.user.PasswordResetFragment.OnPasswordReset;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class PasswordResetActivity extends VeneficaActivity implements OnPasswordResetEmailLookupListener, OnPasswordReset{

	/**
	 * modes
	 */
	public static int ACT_MODE_VERIFY_EMAIL = 4001;
	public static int ACT_MODE_RESET_PASSWORD = 4002;
	private static int ACT_MODE;
	private static int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;
	
	private PasswordResetEmailLookupFragment passwordResetEmailLookupFragment;
	
	private WSAction wsAction;
	private boolean uploadingData = false;
	
	private String emailToVerify, newPassword, code; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_password_reset);
		
		ACT_MODE = getIntent().getExtras().getInt("act_mode", ACT_MODE_VERIFY_EMAIL);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		passwordResetEmailLookupFragment = new PasswordResetEmailLookupFragment();
		fragmentTransaction.add(R.id.layActPwdResetRoot, passwordResetEmailLookupFragment);
		fragmentTransaction.commit();
	}
	@Override
	public void onContinueButtonClick(String email) {
		emailToVerify = email;
		if (!uploadingData) {
			new ResetPasswordTask().execute(ACT_MODE_VERIFY_EMAIL);
			//		getSupportFragmentManager().beginTransaction().replace(R.id.layActPwdResetRoot, new PasswordResetFragment()).commit();
		}
	}
	@Override
	public void onResetPasswordButtonClick(String password) {
		if (!uploadingData) {
			new ResetPasswordTask().execute(ACT_MODE_RESET_PASSWORD);
		}		
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(PasswordResetActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == Constants.RESULT_RESET_PASSWORD_SUCCESS) {
						setResult(Activity.RESULT_OK);
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
		String message = "";
    	if(id == D_ERROR) {    		
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			} else if(ERROR_CODE == Constants.RESULT_RESET_PASSWORD_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_reset_pwd_success);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_RESET_PASSWORD_CODE_NOT_FOUND){
				message = (String) getResources().getText(R.string.g_error_reset_pwd_invalid_code);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_RESET_PASSWORD_CODE_EXPIRED){
				message = (String) getResources().getText(R.string.g_error_reset_pwd_code_expired);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_RESET_PASSWORD_CODE_USED){
				message = (String) getResources().getText(R.string.g_error_reset_pwd_used_code);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_RESET_PASSWORD){
				message = (String) getResources().getText(R.string.g_error);
			}
    	}
    	((AlertDialog) dialog).setMessage(message);
	}
	/**
	 * Class to handle network tasks
	 * @author avinash
	 *
	 */
	private class ResetPasswordTask extends AsyncTask<Integer, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadingData = true;
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(Integer... params) {
			UserRegistrationResultWrapper wrapper = null;
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_VERIFY_EMAIL)) {
					wrapper = wsAction.verifyForgotPasswordEmail(((VeneficaApplication)getApplication()).getAuthToken(), emailToVerify);					
				}else if (params[0].equals(ACT_MODE_RESET_PASSWORD)) {
					wrapper = wsAction.changeForgottenPassword(((VeneficaApplication)getApplication()).getAuthToken()
							, newPassword, code)	;			
				}
			} catch (IOException e) {
				Log.e("ResetPasswordTask::doInBackground :", e.toString());
			} catch (XmlPullParserException e) {
				Log.e("ResetPasswordTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(UserRegistrationResultWrapper result) {
			super.onPostExecute(result);
			uploadingData = false;
			setSupportProgressBarIndeterminateVisibility(false);
			if (result.result == Constants.RESULT_VERIFY_EMAIL_SUCCESS) {
				getSupportFragmentManager().beginTransaction().replace(R.id.layActPwdResetRoot, new PasswordResetFragment()).commit();
			} else if (result.result == Constants.RESULT_RESET_PASSWORD_SUCCESS) {
				ERROR_CODE = Constants.RESULT_RESET_PASSWORD_SUCCESS;
				showDialog(D_ERROR);				
			} else {
				if(result.result == -1){
					ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				}
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}

}
