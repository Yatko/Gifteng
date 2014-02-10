package com.venefica.module.listings;

import java.io.IOException;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.UserDto;
import com.venefica.module.utils.Utility;
import com.venefica.services.RatingDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class RatingActivity extends VeneficaActivity {
	 /** 
	  * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2;
	private WSAction wsAction;
	/**
	 * modes
	 */
	private int ACT_MODE_POST_RATING = 4001;
	/**
	 * flag to indicate network call progress
	 */
	private boolean isWorking;
	/**
	 * listing to rate
	 */
	private long adId = 0;
	private long toUserId = 0;
	/**
	 * rating value
	 */
	private int ratingValue = -1;
	/**
	 * UI components
	 */
//	private RatingBar ratingBar;
	private TextView txtUser, txtRatingLevel;
	private EditText edtTitle, edtComment;
	private ImageView imgProfile;
	private Button btnPost;
	private RadioGroup valueRadioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
		setContentView(R.layout.activity_rating);
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); 
		setProgressBarIndeterminateVisibility(false);
		adId = getIntent().getExtras().getLong("ad_id");
		toUserId = getIntent().getExtras().getLong("to_user_id");
		
		imgProfile = (ImageView) findViewById(R.id.imgActRatingProfile);
		if (((VeneficaApplication) getApplication()).getUser().getAvatar() != null
				&& ((VeneficaApplication) getApplication()).getUser().getAvatar().getUrl() != null) {
			((VeneficaApplication) getApplication()).getImgManager().loadImage(
					Constants.PHOTO_URL_PREFIX
							+ ((VeneficaApplication) getApplication())
									.getUser().getAvatar().getUrl(),
					imgProfile,
					getResources().getDrawable(R.drawable.icon_user));
		}
		/*ratingBar = (RatingBar) findViewById(R.id.ratBarActRating);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ratingValue = (int) rating;
			}
		});*/
		
		valueRadioGroup = (RadioGroup) findViewById(R.id.radioGroupActRatingValue);
		txtUser = (TextView) findViewById(R.id.txtActRatingUserName);
		txtUser.setText(((VeneficaApplication)getApplication()).getUser().getFirstName()+" "
				+ ((VeneficaApplication)getApplication()).getUser().getLastName());
		txtRatingLevel = (TextView) findViewById(R.id.txtActRatingLevel);
		
		edtTitle = (EditText) findViewById(R.id.edtActRatingTitle);
		edtComment = (EditText) findViewById(R.id.edtActRatingComments);
		
		btnPost = (Button) findViewById(R.id.btnActRatingPost);
		btnPost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utility.showLongToast(RatingActivity.this, "Pending for discussion.");
				/*if (ratingValue > -1) {
					new RatingTask().execute(ACT_MODE_POST_RATING);
				} else {
					Utility.showShortToast(RatingActivity.this, getResources().getString(R.string.msg_rating_validation));
				}	*/			
			}
		});
	}

	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	if(id == D_ERROR) {
    		StringBuffer message = new StringBuffer();
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message.append((String) getResources().getText(R.string.error_network_01));
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message.append((String) getResources().getText(R.string.error_network_02));
			}else if(ERROR_CODE == Constants.ERROR_RESULT_POST_RATING){
				message.append((String) getResources().getText(R.string.error_post_rating));
			}else if(ERROR_CODE == Constants.RESULT_POST_RATING_SUCCESS){				
				message.append((String) getResources().getText(R.string.msg_post_rating_success));
			}
    		((AlertDialog) dialog).setMessage(message.toString());
		}    	
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		finish();
    	}
		return true;
	}

	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(RatingActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(RatingActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == Constants.RESULT_POST_RATING_SUCCESS) {
						finish();					
					}					
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	return null;
    }
	/**
	 * @author avinash
	 * Class to to handle ratings activity
	 */
	class RatingTask extends AsyncTask<Integer, Integer, ListingDetailsResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
			isWorking  = true;
		}
		@Override
		protected ListingDetailsResultWrapper doInBackground(Integer... params) {
			ListingDetailsResultWrapper wrapper = new ListingDetailsResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_POST_RATING)){
					wrapper = wsAction.rateAd(((VeneficaApplication)getApplication()).getAuthToken(), getRating());
				}
			} catch (IOException e) {
				Log.e("RatingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("RatingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (Exception e) {
				Log.e("RatingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			}
			return wrapper;
		}
		
		@Override
		protected void onPostExecute(ListingDetailsResultWrapper result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
			} else {
				ERROR_CODE = result.result;				
			}
			showDialog(D_ERROR);
			isWorking  = false;
		}
	}
	
	/**
	 * Get rating to update
	 * @return rating RatingDto
	 */
	private RatingDto getRating(){
		RatingDto ratingDto = new RatingDto();
		ratingDto.setAdId(adId);
		ratingDto.setFromUser(((VeneficaApplication)getApplication()).getUser());
		ratingDto.setRatedAt(new Date());
		ratingDto.setText(edtComment.getText().toString());
		ratingDto.setToUser(new UserDto());
		ratingDto.setToUserId(toUserId);
		switch (valueRadioGroup.getCheckedRadioButtonId()) {
		  case R.id.radioActProfileLike :
			  ratingDto.setValue(1);
	              break;
		  case R.id.radioActProfileDislike :
			  ratingDto.setValue(-1);
			  break;
		}
		return ratingDto;
	}
}
