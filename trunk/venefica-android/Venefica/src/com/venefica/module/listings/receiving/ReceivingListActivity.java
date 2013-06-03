package com.venefica.module.listings.receiving;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.RatingActivity;
import com.venefica.module.listings.receiving.ReceivingListAdapter.OnButtonClickListener;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.UserDto;
import com.venefica.services.MessageDto;
import com.venefica.services.RequestDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class ReceivingListActivity extends VeneficaActivity implements OnButtonClickListener, android.view.View.OnClickListener {

	public static final int ACT_MODE_GET_REQUESTED = 4001;
	public static final int ACT_MODE_CANCEL_REQUEST_AD = 4002;
	public static final int ACT_MODE_SEND_MESSAGE = 4003;
	public static final int ACT_MODE_MARK_RECEIVED = 4004;
	/**
     * Current mode
     */
    public static int CURRENT_MODE = ACT_MODE_GET_REQUESTED;
    
    /**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	
	/**
	 * requests
	 */
	private ListView listViewrequests;
	private List<RequestDto> requestDtos;
	private ReceivingListAdapter receivingListAdapter;
	private WSAction wsAction;
	private long adId, requestId;
	private UserDto userDto;
	/**
	 * message layout
	 */
	private LinearLayout laySend;
	private boolean isSendMsgVisible = false;
	private boolean isOwner;
	private EditText edtComment;
	private ImageButton imgBtnSendComment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
//		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        setContentView(R.layout.activity_receiving_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        listViewrequests = (ListView) findViewById(R.id.listActRequestList);
        this.requestDtos = new ArrayList<RequestDto>();
        this.receivingListAdapter = new ReceivingListAdapter(this, requestDtos);
        this.receivingListAdapter.setButtonClickListener(this);
        listViewrequests.setAdapter(receivingListAdapter);
        
        //comment 
        laySend = (LinearLayout) findViewById(R.id.layActReceivingSend);
        edtComment = (EditText) findViewById(R.id.edtActReceivingMessage);
        imgBtnSendComment = (ImageButton) findViewById(R.id.imgBtnActReceivingSend);
        imgBtnSendComment.setOnClickListener(this);
        setSupportProgressBarIndeterminateVisibility(false);
        new ReceivingTask().execute(ACT_MODE_GET_REQUESTED);
	}

	
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(ReceivingListActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(ReceivingListActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == Constants.RESULT_CANCEL_REQUEST_SUCCESS) {
						new ReceivingTask().execute(ACT_MODE_GET_REQUESTED);
					} else if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE ||
							ERROR_CODE == Constants.ERROR_NETWORK_CONNECT ||
							ERROR_CODE == Constants.ERROR_NO_DATA) {
						finish();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}if(id == D_CONFIRM){
    		AlertDialog.Builder builder = new AlertDialog.Builder(ReceivingListActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			
			builder.setPositiveButton(R.string.label_btn_yes, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					//Delete when code id confirm to delete
					
					dismissDialog(D_CONFIRM);					
				}
			});
			
			builder.setNegativeButton(R.string.label_btn_no, new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_CONFIRM);
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
			} else if(ERROR_CODE == Constants.ERROR_NO_DATA){
				message = (String) getResources().getText(R.string.g_error_no_requests);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_GET_REQUESTS){
				message = (String) getResources().getText(R.string.g_error);
			} else if(ERROR_CODE == Constants.RESULT_CANCEL_REQUEST_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_cancel_request_ad_success);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_CANCEL_REQUEST){
				message = (String) getResources().getText(R.string.g_error);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_SEND_MESSAGE){
				message = (String) getResources().getText(R.string.error_send_message);
			} else if(ERROR_CODE == Constants.RESULT_SEND_MESSAGE_SUCCESS){
				setMessageLayoutVisiblity(false);
				message = (String) getResources().getText(R.string.msg_send_message_success);
			} else if(ERROR_CODE == Constants.RESULT_MARK_RECEIVED_SUCCESS){
				message = (String) getResources().getText(R.string.g_msg_msrk_received_success);
			} else if(ERROR_CODE == Constants.ERROR_RESULT_MARK_RECEIVED){
				message = (String) getResources().getText(R.string.g_error);
			}
    	}
    	((AlertDialog) dialog).setMessage(message);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		if (isSendMsgVisible) {
				setMessageLayoutVisiblity(false);
			}else{
				finish();
			}
    	}
    	return true;
    }

    @Override
    public void onBackPressed() {
    	if (isSendMsgVisible) {
			setMessageLayoutVisiblity(false);
    	} else {
			super.onBackPressed();
		}    	
    }
    /**
	 * Set send comment layout visibility
	 * @param isVisible
	 */
	private void setMessageLayoutVisiblity(boolean isVisible){
		if (isVisible) {
			laySend.setVisibility(ViewGroup.VISIBLE);
		} else {
			laySend.setVisibility(ViewGroup.GONE);
		}		
		isSendMsgVisible = isVisible;
	}
    class ReceivingTask extends AsyncTask<Integer, Integer, RequestsResultWrapper>{
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		setSupportProgressBarIndeterminateVisibility(true);
    	}
    	@Override
		protected RequestsResultWrapper doInBackground(Integer... params) {
    		RequestsResultWrapper wrapper = new RequestsResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_GET_REQUESTED)) {
					wrapper = wsAction.getRequests(((VeneficaApplication)getApplication()).getAuthToken()
							, ((VeneficaApplication)getApplication()).getUser().getId());
				} else if (params[0].equals(ACT_MODE_CANCEL_REQUEST_AD)) {
					wrapper = wsAction.cancelRequestForListing(((VeneficaApplication)getApplication()).getAuthToken()
					, requestId);
				} else if (params[0].equals(ACT_MODE_CANCEL_REQUEST_AD)) {
					wrapper = wsAction.markListingAsReceived(((VeneficaApplication)getApplication()).getAuthToken()
					, requestId);
				}else if (params[0].equals(ACT_MODE_SEND_MESSAGE)) {
					ListingDetailsResultWrapper res =
					 wsAction.sendMessageTo(((VeneficaApplication)getApplication()).getAuthToken()
							, getMessage());
					wrapper.result = res.result;
				}
			}catch (IOException e) {
				Log.e("ReceivingTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("ReceivingTask::doInBackground :", e.toString());
			} catch (Exception e) {
				Log.e("ReceivingTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
    	@Override
    	protected void onPostExecute(RequestsResultWrapper result) {
    		super.onPostExecute(result);
    		setSupportProgressBarIndeterminateVisibility(false);
    		if(result.requests == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if(result.result == Constants.RESULT_GET_REQUESTS_SUCCESS){
				if (result.requests != null && result.requests.size() > 0) {
					requestDtos.clear();
					requestDtos.addAll(result.requests);
					receivingListAdapter.notifyDataSetChanged();
				} else {
					ERROR_CODE = Constants.ERROR_NO_DATA;
					showDialog(D_ERROR);
				}	
			}else {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
    	}
    }

	@Override
	public void onCancelRequest(long requestId) {
		this.requestId = requestId;
		new ReceivingTask().execute(ACT_MODE_CANCEL_REQUEST_AD);
	}


	@Override
	public void onSendMessage(UserDto toUser) {
		this.userDto = toUser;
		setMessageLayoutVisiblity(true);
	}

	/**
	 * Get message to send
	 * @return messageDto
	 */
	public MessageDto getMessage() {
		MessageDto messageDto = new MessageDto();
		messageDto.setCreatedAt(new Date());
		messageDto.setOwner(true);
		messageDto.setText(edtComment.getText().toString());
		messageDto.setToName(this.userDto.getName());
		messageDto.setToFullName(this.userDto.getFirstName() +" "+this.userDto.getLastName());
		messageDto.setToAvatarUrl(this.userDto.getAvatar() != null && this.userDto.getAvatar().getUrl() != null 
				? this.userDto.getAvatar().getUrl() : "/url_null");
		return messageDto;
	}
	@Override
	public void onMarkRceived(long requestId) {
		this.requestId = requestId;
		new ReceivingTask().execute(ACT_MODE_MARK_RECEIVED);
	}


	@Override
	public void onLeaveReview(long adId, UserDto toUser) {
		this.userDto = toUser;
		//start rating activity
		Intent reviewIntent = new Intent(this, RatingActivity.class);
		reviewIntent.putExtra("ad_id", adId);
		reviewIntent.putExtra("to_user_id", toUser.getId());
		startActivity(reviewIntent);
	}


	public UserDto getUserToPostReview(){
		return userDto;		
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.imgBtnActReceivingSend) {
			new ReceivingTask().execute(ACT_MODE_SEND_MESSAGE);
		}
	}
}
