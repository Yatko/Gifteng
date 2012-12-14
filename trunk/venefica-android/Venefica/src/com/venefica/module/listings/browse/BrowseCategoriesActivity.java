package com.venefica.module.listings.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.venefica.module.listings.bookmarks.BookmarkListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.services.CategoryDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * 
 * @author avinash
 * Activity to search by Categories
 */
public class BrowseCategoriesActivity extends VeneficaActivity {

	/**
	 * List to show Categories
	 */
	private ListView listViewCategories;
	/**
	 * List adapter
	 */
	private CategoryListAdapter categoriesListAdapter;
	/**
	 * Categories list
	 */
	private List<CategoryDto> categories;
		/**
	 * Activity modes
	 */
	public static final int ACT_MODE_GET_CATEGORY = 1001;
	public static final int ACT_MODE_DOWNLOAD_CATEGORY = 1003;
	
	private int CURRENT_MODE = ACT_MODE_GET_CATEGORY;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	private WSAction wsAction;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                
        listViewCategories = (ListView) findViewById(R.id.listActBrowseCatCategories);
		listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				
				CategoryDto cat = categories.get(index);  
				if(cat.getSubcategories() != null && cat.getSubcategories().size() > 0){
					categories.clear();
					categories.addAll(cat.getSubcategories());
					categoriesListAdapter.notifyDataSetChanged();
				}else{
					if (CURRENT_MODE == ACT_MODE_GET_CATEGORY) {
						Intent resIntent = new Intent();
						resIntent.putExtra("category_name", categories.get(index).getName());
						resIntent.putExtra("cat_id", categories.get(index).getId());
						setResult(Activity.RESULT_OK, resIntent);
						finish();
					}else {
						Intent intent = new Intent(BrowseCategoriesActivity.this, SearchListingsActivity.class);
						intent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY);
						intent.putExtra("category_name", categories.get(index).getName());
						intent.putExtra("category_id", categories.get(index).getId());
				    	startActivity(intent);					
					}	
				}
							
			}
		});
//        categories = getCategories();// Demo data
		categories = new ArrayList<CategoryDto>();
        categoriesListAdapter = new CategoryListAdapter(this, categories);
        listViewCategories.setAdapter(categoriesListAdapter);
        CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_GET_CATEGORY);
        //Download categories list
        if (WSAction.isNetworkConnected(this)) {
        	new BrowseTask().execute(ACT_MODE_DOWNLOAD_CATEGORY);
		} else {
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
        
    }

    
    @Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(BrowseCategoriesActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(BrowseCategoriesActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE){
						finish();
					}else {
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
			}else if(ERROR_CODE == Constants.ERROR_RESULT_GET_CATEGORIES){
				message = (String) getResources().getText(R.string.error_get_categories);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
	}

	/**
	 * 
	 * @author avinash
	 * Class to perform download operations
	 */
	class BrowseTask extends AsyncTask<Integer, Integer, BrowseCatResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(D_PROGRESS);
		}
		@Override
		protected BrowseCatResultWrapper doInBackground(Integer... params) {
			BrowseCatResultWrapper wrapper = new BrowseCatResultWrapper();
			try{
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equals(ACT_MODE_DOWNLOAD_CATEGORY)) {
					wrapper = wsAction.getCategories(((VeneficaApplication)getApplication()).getAuthToken());
				}
			}catch (IOException e) {
				Log.e("BrowseTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("BrowseTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		
		protected void onPostExecute(BrowseCatResultWrapper result) {
			dismissDialog(D_PROGRESS);
			if(result.categories == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_CATEGORIES_SUCCESS && result.categories != null
					&& result.categories.size() > 0) {
				/*ERROR_CODE = result.result;
				showDialog(D_ERROR);*/
				categories.clear();
				categories.addAll(result.categories);
				categoriesListAdapter.notifyDataSetChanged();
			}
		}
	}
	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browse_categories, menu);
        return true;
    }*/
}
