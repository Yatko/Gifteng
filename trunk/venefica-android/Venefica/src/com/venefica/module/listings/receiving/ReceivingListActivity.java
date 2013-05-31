package com.venefica.module.listings.receiving;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.services.RequestDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class ReceivingListActivity extends VeneficaActivity {

	public static final int ACT_MODE_GET_REQUESTED = 4001;
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
        listViewrequests.setAdapter(receivingListAdapter);
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
			}
    	}
    	((AlertDialog) dialog).setMessage(message);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		finish();
    	}
    	return true;
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
			}else if(result.result == Constants.RESULT_GET_REQUESTS_SUCCESS && result.requests != null){
				requestDtos.clear();
				requestDtos.addAll(result.requests) ;
				receivingListAdapter.notifyDataSetChanged();				
			}else {
				if (result.requests.size() == 0) {
					ERROR_CODE = Constants.ERROR_NO_DATA;
				} else {
					ERROR_CODE = result.result;
				}				
				showDialog(D_ERROR);
			}
    	}
    }
}
