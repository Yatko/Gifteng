/**
 * 
 */
package com.venefica.module.listings.post;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.DatePicker;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.post.PostImagesFragment.OnPostImagesListener;
import com.venefica.module.main.R;
import com.venefica.utils.Constants;

/**
 * @author avinash
 *
 */
public class PostListingActivity extends SherlockFragmentActivity implements OnPostImagesListener{

	private PostImagesFragment imgFragment;
	
	
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Activity MODE
	 */	
	public static final int ACT_MODE_POST_LISTING = 3001;
	public static final int ACT_MODE_UPDATE_LISTING = 3002;
	public static final int ACT_MODE_GET_LISTING = 3003;
	public static final int ACT_MODE_PROCESS_BITMAP = 3004;
	public static final int  ACT_MODE_UPLOAD_IMAGES = 3005;
	private static int CURRENT_MODE = ACT_MODE_POST_LISTING;
	
	private static int REQ_GET_LISTING_DETAILS_TO_POST = 5001;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
		setProgressBarIndeterminateVisibility(false);
		
		setContentView(R.layout.activity_post_listing);
		//set mode
		CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_POST_LISTING);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		imgFragment = new PostImagesFragment();
		fragmentTransaction.add(R.id.layPostlistingMain, imgFragment);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return true;
	}
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(PostListingActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(PostListingActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS 
							|| ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS
							|| ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE){
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
    	if(id == D_ERROR) {
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_POST_LISTING){
				message = (String) getResources().getText(R.string.error_postlisting);
			}else if(ERROR_CODE == Constants.RESULT_POST_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_success);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UPDATE_LISTING){
				message = (String) getResources().getText(R.string.error_update_listing);
			}else if(ERROR_CODE == Constants.RESULT_UPDATE_LISTING_SUCCESS){
				message = (String) getResources().getText(R.string.msg_postlisting_update_success);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	@Override
	public void onPickFromGallery() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraPick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetCoverImage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraError(int errorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNextButtonClick() {
		Intent intent = new Intent(this, GetListingDetails.class);
		startActivityForResult(intent, REQ_GET_LISTING_DETAILS_TO_POST);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		
	}
}
