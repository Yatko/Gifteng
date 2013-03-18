package com.venefica.module.messages;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
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
	private final int ACT_MODE_DELETE_MESSAGES = 4002;
	public static final int REQ_SHOW_MESSAAGE_DETAILS = 5001;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;
	/**
	 * ActionMode
	 */
	private ActionMode actionMode;
	/**
	 * action mode visible flag
	 */
	private boolean isActionModeActive = false;
	/**
	 * web service task progress 
	 */
	private boolean isWorking = false;
	/**
	 * list header
	 */
	private TextView txtListHeader;
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
		listViewMessages.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		txtListHeader = new TextView(this);
		txtListHeader.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT
				, AbsListView.LayoutParams.WRAP_CONTENT));
		//set list header
		listViewMessages.addHeaderView(txtListHeader);
		messages = new ArrayList<MessageDto>();

		messageListAdapter = new MessageListAdapter(this, messages);
		listViewMessages.setAdapter(messageListAdapter);
		
		//get all messages
		if(WSAction.isNetworkConnected(this)){
			new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
		} else {
	    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
	    	showDialog(D_ERROR);	 
	    }
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
					} else if (ERROR_CODE == Constants.RESULT_DELETE_MESSAGE_SUCCESS  && actionMode != null) {
						if (messageListAdapter != null) {
							messageListAdapter.clearSelectedPositions();
						}						
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
    		StringBuffer message = new StringBuffer();
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message.append((String) getResources().getText(R.string.error_network_01));
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message.append((String) getResources().getText(R.string.error_network_02));
			} else if(ERROR_CODE == Constants.ERROR_RESULT_GET_ALL_MESSAGES){
				message.append((String) getResources().getText(R.string.error_get_all_messages));
			} else if(ERROR_CODE == Constants.ERROR_RESULT_DELETE_MESSAGE){
				message.append((String) getResources().getText(R.string.error_delete_messages));
			} else if(ERROR_CODE == Constants.RESULT_DELETE_MESSAGE_SUCCESS){
				message.append(messageListAdapter.getSelectedPositions().size());
				message.append(" ");
				message.append((String) getResources().getText(R.string.msg_delete_messages_success));
			}
    		((AlertDialog) dialog).setMessage(message.toString());
		}    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		finish();
    	} else if (itemId == R.id.menu_message_list_refresh) {
    		//refresh messages
    		if (!isWorking) {
    			if(WSAction.isNetworkConnected(this)){
    				new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
    			} else {
    		    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
    		    	showDialog(D_ERROR);	 
    		    }
			}			
		}
    	return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater()
		.inflate(R.menu.activity_message_list, menu);
    	return super.onCreateOptionsMenu(menu);
    }
	/**
	 * @author avinash
	 * Class to handle action modes
	 */
	private final class AnActionModeOfMessageList implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			getSupportMenuInflater()
					.inflate(R.menu.activity_message_list_action_modes, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			isActionModeActive = true;
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.menu_message_list_delete) {
				//delete selected messages
				if(WSAction.isNetworkConnected(MessageListActivity.this)){
					new MessageListTask().execute(ACT_MODE_DELETE_MESSAGES);
				} else {
			    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			    	showDialog(D_ERROR);	 
			    }
				if (actionMode != null) {
					actionMode.finish();
				}				
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			isActionModeActive = false;
		}
	}
    
	/**
	 * Method to delete multiple messages
	 * @param token
	 * @param wsAction
	 * @return result MessageResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private MessageResultWrapper deleteSelectedMessages(String token, WSAction wsAction) throws IOException, XmlPullParserException{
		MessageResultWrapper result = new MessageResultWrapper();
		if (messageListAdapter != null) {
			ArrayList<Long> selMessages = messageListAdapter.getSelectedPositions();
			for (Long id : selMessages) {
				result = wsAction.hideMessage(token, id);
			}
		}
		return result;		
	}
    /**
     * Helper method to show action modes
     * @param show boolean
     */
    public void showActionMode(boolean show){
    	if (show) {
    		if (!isActionModeActive) {
    			actionMode = startActionMode(new AnActionModeOfMessageList());
			}    		
		} else if (actionMode != null) {
			actionMode.finish();
		}    	
    }
    /**
     * Method to set action mode title
     * @param title CharSequence
     */
    public void setActionModeTitle(CharSequence title){
    	if (actionMode != null) {
    		actionMode.setTitle(title);
		}    	
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == Activity.RESULT_OK 
    			&& requestCode == REQ_SHOW_MESSAAGE_DETAILS && data.getBooleanExtra("is_deleted", false)){
    		//refresh list if any message is deleted
    		if(WSAction.isNetworkConnected(this)){
				new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
			} else {
		    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
		    	showDialog(D_ERROR);	 
		    }
    	}
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
			isWorking = true;
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
				} else if (params[0].equals(ACT_MODE_DELETE_MESSAGES)) {
					wrapper = deleteSelectedMessages(((VeneficaApplication)getApplication()).getAuthToken(), wsAction);
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
			} else if (result.result == Constants.RESULT_GET_ALL_MESSAGES_SUCCESS) {
				messages.clear();
				if (result.messages != null) {
					messages.addAll(result.messages);					
				}
				//set message count to list header
				txtListHeader.setText(messages.size()+" "+getResources().getText(R.string.msg_messages_found));				
				messageListAdapter.notifyDataSetChanged();
			} else if (result.result == Constants.RESULT_DELETE_MESSAGE_SUCCESS) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
				//get all messages
				new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
			} else if(result.result == Constants.ERROR_RESULT_GET_ALL_MESSAGES 
					|| result.result == Constants.ERROR_RESULT_DELETE_MESSAGE){
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
			}
			isWorking = false;
		}
	}

}
