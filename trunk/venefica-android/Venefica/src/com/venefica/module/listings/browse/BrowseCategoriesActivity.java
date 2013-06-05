package com.venefica.module.listings.browse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.Utility;
import com.venefica.services.CategoryDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * 
 * @author avinash
 * Activity to search by Categories
 */
public class BrowseCategoriesActivity extends VeneficaActivity implements OnClickListener {

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
	public static final int ACT_MODE_SAVE_CATEGORY_PREF = 1002;
	
	private int CURRENT_MODE = ACT_MODE_SAVE_CATEGORY_PREF;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	private WSAction wsAction;
	
	private CheckBox chkMember, chkBusiness;
	private Button btnSave;
	private boolean isWorking;
	/**
	 * Shared Preferences
	 */
	SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_browse_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //get preferences
        prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
        listViewCategories = (ListView) findViewById(R.id.listActBrowseCatCategories);
        TextView textViewFooter = new TextView(this);
        textViewFooter.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, Utility.convertDpToPixel(this, 60)));
        listViewCategories.addFooterView(textViewFooter);
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
						//Save selected value in shared prefs to use in filter settings on SearchListings activity
						SharedPreferences prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString(Constants.PREF_KEY_CATEGORY, categories.get(index).getName());
						editor.putLong(Constants.PREF_KEY_CATEGORY_ID, categories.get(index).getId());
						editor.commit();
						finish();
					}	
				}
							
			}
		});
//        categories = getCategories();// Demo data
		categories = new ArrayList<CategoryDto>();
        categoriesListAdapter = new CategoryListAdapter(this, categories);
        categoriesListAdapter.setForFilterScreen(true);
        listViewCategories.setAdapter(categoriesListAdapter);
        
        //save button
        btnSave = (Button) findViewById(R.id.btnActBrowseCatCategoriesSave);
        btnSave.setOnClickListener(this);
        //AdType
        chkMember = (CheckBox) findViewById(R.id.chkActBrowseCatMember); 
        chkBusiness = (CheckBox) findViewById(R.id.chkActBrowseCatBusiness);
        chkMember.setChecked(prefs.getBoolean(Constants.PREF_KEY_MEMBER, false));
        chkBusiness.setChecked(prefs.getBoolean(Constants.PREF_KEY_BUSINESS, false));
        
        CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_SAVE_CATEGORY_PREF);
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
//			showDialog(D_PROGRESS);
			isWorking = true;
			setSupportProgressBarIndeterminateVisibility(true);
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
//			dismissDialog(D_PROGRESS);
			isWorking = false;
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.categories == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.result == Constants.RESULT_GET_CATEGORIES_SUCCESS && result.categories != null
					&& result.categories.size() > 0) {
				categories.clear();
				categories.addAll(result.categories);
				categoriesListAdapter.setSelectedPositions(getSelectedCategories());
				categoriesListAdapter.notifyDataSetChanged();
			}
		}
	}

	/** Get saved categories
	 * @return
	 */
	private ArrayList<Long> getSelectedCategories(){
		String catString = prefs.getString(Constants.PREF_KEY_CATEGORY_ID, Constants.PREF_DEF_VAL_CATEGORY);
		if (catString.equals(Constants.PREF_DEF_VAL_CATEGORY)) {
			return new ArrayList<Long>();
		} else {
			return Utility.getListFromString(catString);
		}		
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActBrowseCatCategoriesSave) {
			if (isWorking) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_working_in_background));
			} else {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean(Constants.PREF_KEY_MEMBER, chkMember.isChecked());
				editor.putBoolean(Constants.PREF_KEY_BUSINESS, chkBusiness.isChecked());
				editor.putString(Constants.PREF_KEY_CATEGORY_ID, Utility.getStringFromList(categoriesListAdapter.getSelectedPositions()));
				editor.commit();
				finish();
			}
		}
	}
}
