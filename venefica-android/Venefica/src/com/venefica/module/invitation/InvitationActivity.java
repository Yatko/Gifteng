package com.venefica.module.invitation;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.venefica.module.invitation.JoinGiftingFragment.OnJoinGiftingFragmentListener;
import com.venefica.module.invitation.RequestInvitationConfirmFragment.OnRequestInvitationConfirmClickListener;
import com.venefica.module.invitation.VerifyInvitationFragment.OnVerifyInvitationClickListener;
import com.venefica.module.invitation.VerifyInvitationSuccessFragment.OnVerifyInvitationSuccessClickListener;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.UserDto;
import com.venefica.module.user.UserRegistrationResultWrapper;
import com.venefica.module.utils.Utility;
import com.venefica.services.InvitationDto;
import com.venefica.services.InvitationDto.UserType;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Activity to handle invitation functionality
 */
public class InvitationActivity extends VeneficaActivity 
	implements OnRequestInvitationConfirmClickListener, OnVerifyInvitationClickListener,
		OnJoinGiftingFragmentListener, OnVerifyInvitationSuccessClickListener{
	
	/**
	 * child Fragments
	 */
	private RequestInvitationConfirmFragment reqInvConfirmFragment;
	private VerifyInvitationFragment verifyInvitationFragment;
	/**
	 * modes
	 */
	public static int ACT_MODE_CONF_INVITATION_REQ = 4001;
	public static int ACT_MODE_INVITATION_REQ_SUCCESS = 4002;
	public static int ACT_MODE_VERIFY_INVTATION = 4003;
	public static int ACT_MODE_VERIFY_INVTATION_SUCCESS = 4004;
	public static int ACT_MODE_JOIN_GIFTENG = 4005;
	private int ACT_MODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	private WSAction wsAction;
	/**
	 * to request invitation
	 */
	private InvitationDto invitationDto;
	/**
	 * email to request invitation
	 */
	private String email = "";
	/**
	 * true if working in background
	 */
	private boolean uploadingData = false;
	/**
	 * Invitation code to verify
	 */
	private String invitationCode = ""; 
	/**
	 * password
	 */
	private String password = "";
	/**
	 * User to register
	 */
	private UserDto userDto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_invitation);
		
		ACT_MODE = getIntent().getExtras().getInt("act_mode", ACT_MODE_CONF_INVITATION_REQ);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		if (ACT_MODE == ACT_MODE_CONF_INVITATION_REQ) {
			email = getIntent().getStringExtra("email");
			reqInvConfirmFragment = new RequestInvitationConfirmFragment();
			fragmentTransaction.add(R.id.layActInvitationRoot, reqInvConfirmFragment);
		} else if (ACT_MODE == ACT_MODE_VERIFY_INVTATION){
			verifyInvitationFragment = new VerifyInvitationFragment();
			fragmentTransaction.add(R.id.layActInvitationRoot, verifyInvitationFragment);
		}
		
		fragmentTransaction.commit();
	}
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(InvitationActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(InvitationActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
						InvitationActivity.this.finish();
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
    	if(id == D_ERROR) {
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_REQ_INVITATION){
				message = (String) getResources().getText(R.string.g_error_req_invtation_code);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_VERIFY_INVITATION){
				message = (String) getResources().getText(R.string.g_error_invtation_code);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_REGISTER_USER){
				message = (String) getResources().getText(R.string.error_register_user);
			} else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_DUP_EMAIL){
				message = (String) getResources().getText(R.string.msg_reg_user_duplicate_email);
			} else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_DUP_LOGIN){
				message = (String) getResources().getText(R.string.msg_reg_user_duplicate_login);
			} else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
				message = (String) getResources().getText(R.string.msg_reg_user_registration_success);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/* (non-Javadoc)
	 * @see com.venefica.module.invitation.RequestInvitationConfirmFragment.OnRequestInvitationConfirmClickListener#onConfirmRequest(java.lang.String, java.lang.String, com.venefica.services.UserType)
	 */
	@Override
	public void onConfirmRequest(String zipCode, String source, UserType useType) {
		if (WSAction.isNetworkConnected(this)) {
			if (!uploadingData) {
				if (invitationDto == null) {
					invitationDto = new InvitationDto();
				}
				invitationDto.setEmail(email);
				invitationDto.setSource(source);
				invitationDto.setZipCode(zipCode);
				invitationDto.setUserType(useType);
				new InvitationTask().execute(ACT_MODE_CONF_INVITATION_REQ);
			} else {
				Utility.showLongToast(
						this,
						getResources().getString(
								R.string.msg_working_in_background));
			}
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.invitation.VerifyInvitationFragment.OnVerifyInvitationClickListener#OnVerifyInvitation(java.lang.String)
	 */
	@Override
	public void OnVerifyInvitation(String invitationCode) {
		this.invitationCode = invitationCode;
		new InvitationTask().execute(ACT_MODE_VERIFY_INVTATION);
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.invitation.JoinGiftingFragment.OnJoinGiftingFragmentListener#onJoinButtonClick()
	 */
	@Override
	public void onJoinButtonClick(String password, UserDto userDto) {
		if (WSAction.isNetworkConnected(this)) {
			if (!uploadingData) {
				this.password = password;
				this.userDto = userDto;
			new RegisterUserTask().execute(ACT_MODE_JOIN_GIFTENG);
			} else {
				Utility.showLongToast(
						this,
						getResources().getString(
								R.string.msg_working_in_background));
			}
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}
	/* (non-Javadoc)
	 * @see com.venefica.module.invitation.VerifyInvitationSuccessFragment.OnVerifyInvitationSuccessClickListener#OnGotoJoinClick()
	 */
	@Override
	public void OnGotoJoinClick() {
		JoinGiftingFragment joinGiftingFragment = new JoinGiftingFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layActInvitationRoot, joinGiftingFragment).commit();
	}
	
	/**
	 * @author avinash
	 * Class to perform network task.
	 */
	class InvitationTask extends AsyncTask<Integer, Integer, InvitationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadingData = true;
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected InvitationResultWrapper doInBackground(Integer... params) {
			InvitationResultWrapper wrapper = new InvitationResultWrapper();
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_CONF_INVITATION_REQ)) {
					wrapper = wsAction.requestInvitation(((VeneficaApplication)getApplication()).getAuthToken(), invitationDto);
				} else if (params[0].equals(ACT_MODE_VERIFY_INVTATION)) {
					wrapper = wsAction.verifyInvitation(((VeneficaApplication)getApplication()).getAuthToken(), invitationCode);
				}
			}catch (IOException e) {
				Log.e("InvitationTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("InvitationTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(InvitationResultWrapper result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			uploadingData = false;
			if(result.data == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			} else if (result.result == Constants.ERROR_RESULT_REQ_INVITATION ) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			} else if (result.result == Constants.ERROR_RESULT_VERIFY_INVITATION) {
				if (verifyInvitationFragment != null) {
					verifyInvitationFragment.showInvalidCodeError(true);
				}
			} else if (result.result == Constants.RESULT_REQ_INVITATION_SUCCESS) {
				//Show success message
				RequestInvitationSuccessFragment requestInvitationSuccessFragment = new RequestInvitationSuccessFragment();
				getSupportFragmentManager().beginTransaction()
					.replace(R.id.layActInvitationRoot, requestInvitationSuccessFragment).commit();
			} else if (result.result == Constants.RESULT_VIFY_INVITATION_SUCCESS) {
				if (verifyInvitationFragment != null) {					
					verifyInvitationFragment.showInvalidCodeError(false);
				}
				//Show success message
				VerifyInvitationSuccessFragment invitationSuccessFragment = new VerifyInvitationSuccessFragment();
				getSupportFragmentManager().beginTransaction()
					.replace(R.id.layActInvitationRoot, invitationSuccessFragment).commit();
			}
		}
	}
	
	/**
	 * @author avinash
	 * Class to perform network task.
	 */
	class RegisterUserTask extends AsyncTask<Integer, Integer, UserRegistrationResultWrapper>{
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
				if (params[0].equals(ACT_MODE_JOIN_GIFTENG)) {					
					wrapper.result = wsAction.registerUser(password, userDto, invitationCode);					
				}
			}catch (IOException e) {
				Log.e("RegisterUserTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("RegisterUserTask::doInBackground :", e.toString());
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
			}else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);				
			}
		}
	}
}
