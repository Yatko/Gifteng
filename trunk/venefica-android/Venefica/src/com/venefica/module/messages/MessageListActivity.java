package com.venefica.module.messages;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.services.MessageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash Activity for messages
 */
public class MessageListActivity extends VeneficaActivity {
	/**
	 * List to show messages
	 */
	private ListView listViewMessages;
	/**
	 * List adapter
	 */
	private MessageListAdapter messageListAdapter;
	/**
	 * Message list
	 */
	private ArrayList<MessageDto> messages;
	public WSAction wsAction;
	/**
	 * Modes
	 */
	private final int ACT_MODE_GET_ALL_MESSAGES = 4001;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature( Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   
		setContentView(R.layout.activity_message_list);
		listViewMessages = (ListView) findViewById(R.id.listActMessageList);
		messages = new ArrayList<MessageDto>();

		messageListAdapter = new MessageListAdapter(this, messages);
		listViewMessages.setAdapter(messageListAdapter);
		listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent intent = new Intent(MessageListActivity.this,
						MessageDetailActivity.class);
				startActivity(intent);
			}
		});
		new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
	}
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(MessageListActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MessageListActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if (ERROR_CODE == Constants.ERROR_RESULT_GET_LISTING_DETAILS
							|| ERROR_CODE == Constants.ERROR_NETWORK_CONNECT) {
						finish();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}if(id == D_CONFIRM){
    		AlertDialog.Builder builder = new AlertDialog.Builder(MessageListActivity.this);
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
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_ALL_MESSAGES){
				message = (String) getResources().getText(R.string.error_get_all_messages);
			}  		
    		((AlertDialog) dialog).setMessage(message);
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
	/**
	 * @author avinash
	 * Class to handle background operations  
	 */
	class MessageListTask extends AsyncTask<Integer, Integer, MessageResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected MessageResultWrapper doInBackground(Integer... params) {
			MessageResultWrapper wrapper = new MessageResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_GET_ALL_MESSAGES)){
					wrapper = wsAction.getAllMessages(((VeneficaApplication)getApplication()).getAuthToken());
				}
			} catch (IOException e) {
				Log.e("MessageListTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("MessageListTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (Exception e) {
				Log.e("MessageListTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(MessageResultWrapper result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.message == null && result.messages == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			} else if (result.result == Constants.RESULT_GET_ALL_MESSAGES_SUCCESS && result.messages != null) {
				messages.addAll(result.messages);
				messageListAdapter.notifyDataSetChanged();
			} else if(result.result != Constants.ERROR_RESULT_GET_ALL_MESSAGES){
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
		}
	}

}
