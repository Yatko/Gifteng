package com.venefica.module.messages;

import java.io.IOException;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.MessageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Activity for message details
 */
public class MessageDetailActivity extends VeneficaActivity implements View.OnClickListener{
	/**
	 * text view to display message details
	 */
	private TextView txtSender, txtTime, txtMessageText;
	/**
	 * reply edit box and buttons
	 */
	private EditText edtReply;
	private ImageButton imgBtnSend;
	/**
	 * reply control visibility flag
	 */
	private boolean isReplyControlVisible = false;
	/**
	 * Message to send
	 */
	private MessageDto message;
	/**
	 * processing flag
	 */
	public boolean isWorking = false;
	public WSAction wsAction;
	/**
	 * Modes
	 */
	private final int ACT_MODE_SEND_MESSAGE = 4001;
	private final int ACT_MODE_DELETE_MESSAGE = 4002;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;
	/**
	 * Message sender details
	 */
	private String senderName, senderAvatarUrl;
	private boolean idOwner;
	private long messageId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  
        setProgressBarIndeterminateVisibility(false);
        setContentView(R.layout.activity_message_detail);
        
        txtSender = (TextView) findViewById(R.id.txtActMsgDetailSender);
        txtSender.setText(getIntent().getExtras().getString("sender_full_name"));
        
        txtTime = (TextView) findViewById(R.id.txtActMsgDetailTime);
        txtTime.setText(getIntent().getExtras().getString("time"));
        
        txtMessageText = (TextView) findViewById(R.id.txtActMsgDetailMsgText);
        txtMessageText.setText(getIntent().getExtras().getString("message_text"));
        
        senderName = getIntent().getExtras().getString("sender_name");
        senderAvatarUrl = getIntent().getExtras().getString("avatar_url");
        idOwner = getIntent().getExtras().getBoolean("is_owner");
        messageId = getIntent().getExtras().getLong("message_id");
        
        edtReply = (EditText) findViewById(R.id.edtActMsgDetailReplyText);
        imgBtnSend = (ImageButton)findViewById(R.id.imgBtnActMsgDetailSend);
        imgBtnSend.setOnClickListener(this);
        
        //hide reply view by default
        setReplyViewVisibility(View.GONE);       
    }
    
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(MessageDetailActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetailActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == Constants.RESULT_DELETE_MESSAGE_SUCCESS) {
						Intent resIntent = new Intent();
						resIntent.putExtra("is_deleted", true);
						setResult(Activity.RESULT_OK, resIntent);
						finish();					
					} else if (ERROR_CODE == Constants.RESULT_SEND_MESSAGE_SUCCESS) {
						edtReply.setText("");
						setReplyViewVisibility(View.GONE);
					}
					
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}if(id == D_CONFIRM){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MessageDetailActivity.this);
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
    	if(id == D_ERROR) {
    		StringBuffer message = new StringBuffer();
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message.append((String) getResources().getText(R.string.error_network_01));
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message.append((String) getResources().getText(R.string.error_network_02));
			}else if(ERROR_CODE == Constants.ERROR_RESULT_DELETE_MESSAGE){
				message.append((String) getResources().getText(R.string.error_delete_messages));
			}else if(ERROR_CODE == Constants.RESULT_DELETE_MESSAGE_SUCCESS){				
				message.append((String) getResources().getText(R.string.msg_delete_messages_success));
			}else if(ERROR_CODE == Constants.ERROR_RESULT_SEND_MESSAGE){				
				message.append((String) getResources().getText(R.string.error_send_message));
			}else if(ERROR_CODE == Constants.RESULT_SEND_MESSAGE_SUCCESS){				
				message.append((String) getResources().getText(R.string.msg_send_message_success));
			}
    		((AlertDialog) dialog).setMessage(message.toString());
		}    	
    }
    /**
     * Set reply edit box and send button visibility
     * @param visibility
     */
    private void setReplyViewVisibility(int visibility){
    	if (visibility == View.VISIBLE) {
    		isReplyControlVisible = true;
		}else {
			isReplyControlVisible = false;
		}
    	edtReply.setVisibility(visibility);
    	imgBtnSend.setVisibility(visibility);
    }
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnActMsgDetailSend && !edtReply.getText().toString().trim().equalsIgnoreCase("")){
			//send message
			if (isWorking) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_working_in_background));
			}else {
				// hide virtual keyboard
				InputMethodManager imm = (InputMethodManager) this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtReply.getWindowToken(), 0);
				new MessageDetailsTask().execute(ACT_MODE_SEND_MESSAGE);
			}			
		}
	}
	
	/**
	 * Get message to send
	 * @return message MessageDto
	 */
	private MessageDto getMessageToSend(){
		if (message == null) {
			message = new MessageDto(); 
		}
		message.setCreatedAt(new Date());
		message.setOwner(true);
		message.setText(edtReply.getText().toString());
		message.setToName(senderName);
		message.setToFullName(txtSender.getText().toString());
		message.setToAvatarUrl(senderAvatarUrl);
		return message;		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater()
		.inflate(R.menu.activity_message_detail, menu);
    	return super.onCreateOptionsMenu(menu);
    }
	
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	//hide reply menu if current user is sender/owner
       	menu.findItem(R.id.menu_reply_message).setVisible(!idOwner);		
    	return super.onPrepareOptionsMenu(menu);
    }
    
	public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		finish();
    	} else if (itemId == R.id.menu_reply_message) {
    		//show reply controls
			setReplyViewVisibility(View.VISIBLE);
		} else if (itemId == R.id.menu_message_list_delete) {
			//delete current message
    		new MessageDetailsTask().execute(ACT_MODE_DELETE_MESSAGE);
		}
    	return true;
	}
	@Override
	public void onBackPressed() {
		if (isReplyControlVisible) {
			setReplyViewVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}		
	}
	
	/**
	 * @author avinash
	 * Class to handle send message and delete function
	 */
	class MessageDetailsTask extends AsyncTask<Integer, Integer, MessageResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
			isWorking  = true;
		}
		@Override
		protected MessageResultWrapper doInBackground(Integer... params) {
			MessageResultWrapper wrapper = new MessageResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_SEND_MESSAGE)){
					ListingDetailsResultWrapper detWrap 
						= wsAction.sendMessageTo(((VeneficaApplication)getApplication()).getAuthToken(), getMessageToSend());
					wrapper.result = detWrap.result;
				} else if (params[0].equals(ACT_MODE_DELETE_MESSAGE)) {
					wrapper = wsAction.hideMessage(((VeneficaApplication)getApplication()).getAuthToken(), messageId);
				}
			} catch (IOException e) {
				Log.e("MessageDetailsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("MessageDetailsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (Exception e) {
				Log.e("MessageDetailsTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			}
			return wrapper;
		}
		
		protected void onPostExecute(MessageResultWrapper result) {
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
}
