package com.venefica.module.main;


import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.invitation.InvitationActivity;
import com.venefica.module.invitation.RequestInvitationFragment;
import com.venefica.module.invitation.RequestInvitationFragment.OnRequestInvitationListener;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.LoginActivity;
import com.venefica.utils.Constants;

public class StartUpActivity extends VeneficaActivity implements OnClickListener, OnRequestInvitationListener {
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
	private ImageButton btnShareTweeter, btnShareFacebook, btnShareGmail, btnShareSms;
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
		
		btnShareTweeter = (ImageButton) findViewById(R.id.imgBtnActLoginShareTweeter);
		btnShareTweeter.setOnClickListener(this);
		btnShareFacebook = (ImageButton) findViewById(R.id.imgBtnActLoginShareFacebook);
		btnShareFacebook.setOnClickListener(this);
		btnShareGmail = (ImageButton) findViewById(R.id.imgBtnActLoginShareMail);
		btnShareGmail.setOnClickListener(this);
		btnShareSms = (ImageButton) findViewById(R.id.imgBtnActLoginShareChat);
		btnShareSms.setOnClickListener(this);
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
			switch (id) {
			case R.id.imgBtnActLoginShareTweeter:
				shareOnTweeter();
				break;
			case R.id.imgBtnActLoginShareFacebook:
				shareOnFacebook();
				break;
			case R.id.imgBtnActLoginShareMail:
				shareWithGmail();
				break;
			case R.id.imgBtnActLoginShareChat:
				shareWithSMS();
				break;
			default:
				break;
			}
		}
	}

		
	/**
	 * Mehod to send sms
	 */
	private void shareWithSMS() {
		String shareText = getResources().getString(R.string.app_name)
				+" "+ getResources().getString(R.string.g_geftang_url);
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", shareText); 
		sendIntent.setType("vnd.android-dir/mms-sms");
		startActivity(sendIntent);
	}


	/**
	 * Method to share on facebook
	 */
	private void shareOnFacebook() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		String shareText = getResources().getString(R.string.app_name)+" "
				+ getResources().getString(R.string.g_label_share)+" "+ getResources().getString(R.string.g_facebook_share);
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
	}


	/**
	 * Method to share on tweeter
	 */
	private void shareOnTweeter() {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		String shareText = getResources().getString(R.string.app_name)+" "
				+ getResources().getString(R.string.g_label_share)+" "+ getResources().getString(R.string.g_twitter_share);
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

	/**
	 * method to share with gmail
	 */
	private void shareWithGmail(){
		String to = "";
		String subject = getResources().getString(R.string.app_name);
		String message = getResources().getString(R.string.app_name) + " "
				+ getResources().getString(R.string.g_label_share) + " "
				+ getResources().getString(R.string.g_geftang_url);

		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(Intent.EXTRA_TEXT, message);

		// need this to prompts email client only
		email.setType("message/rfc822");

		startActivity(Intent.createChooser(email, getResources().getString(R.string.g_label_choose_email)));
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
