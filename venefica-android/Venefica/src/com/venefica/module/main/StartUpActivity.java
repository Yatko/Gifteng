package com.venefica.module.main;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.invitation.InvitationActivity;
import com.venefica.module.invitation.RequestInvitationFragment;
import com.venefica.module.invitation.RequestInvitationFragment.OnRequestInvitationListener;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.LoginActivity;
import com.venefica.utils.Constants;

public class StartUpActivity extends SherlockFragmentActivity implements OnClickListener, OnRequestInvitationListener {
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CUSTOM = 3;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * flag processing (working) indicates post listing process is running
	 */
	private boolean uploadingData = false;
	/**
	 * invitation fragment
	 */
	private RequestInvitationFragment invitationFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_start_up);
		
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		invitationFragment = new RequestInvitationFragment();
		fragmentTransaction.add(R.id.layActStartupRoot, invitationFragment);
		fragmentTransaction.commit();
	}

		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_start_up, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_login) {
			startLoginActivity();
		}
		return true;
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
			ProgressDialog pDialog = new ProgressDialog(StartUpActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
		//Create error dialog
		if(id == D_ERROR){
			AlertDialog.Builder builder = new AlertDialog.Builder(StartUpActivity.this);
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

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (!uploadingData) {
			
		}
	}

		
	/**
	 * Start home screen of application
	 */
	private void startLoginActivity(){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}


	@Override
	public void onRequestInvitationClick(String email) {
		//start invitation activity to request invitation code
		Intent invitationIntent = new Intent(this, InvitationActivity.class);
		invitationIntent.putExtra("email", email);
		invitationIntent.putExtra("act_mode", InvitationActivity.ACT_MODE_CONF_INVITATION_REQ);
		startActivity(invitationIntent);
	}


	@Override
	public void onHaveInvitationClick() {
		//start invitation activity to verify invitation code
		Intent invitationIntent = new Intent(this, InvitationActivity.class);
		invitationIntent.putExtra("act_mode", InvitationActivity.ACT_MODE_VERIFY_INVTATION);
		startActivity(invitationIntent);
	}	
	
}
