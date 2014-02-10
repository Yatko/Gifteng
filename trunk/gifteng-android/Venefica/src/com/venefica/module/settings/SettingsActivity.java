package com.venefica.module.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.venefica.module.main.R;
import com.venefica.utils.Constants;

public class SettingsActivity extends SherlockPreferenceActivity{

	public static final int ACT_MODE_FILTER_SETTINGS = 1001;
	public static final int ACT_MODE_APP_SETTINGS = 1002;
	public static int CURRENT_MODE = ACT_MODE_FILTER_SETTINGS;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPreferenceManager().setSharedPreferencesName(Constants.VENEFICA_PREFERENCES);
        getPreferenceManager().setSharedPreferencesMode(Activity.MODE_PRIVATE);
        
        CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_FILTER_SETTINGS);
        if( CURRENT_MODE == ACT_MODE_FILTER_SETTINGS){
        	addPreferencesFromResource(R.xml.activity_search_filter_settings);
        } else {
        	addPreferencesFromResource(R.xml.activity_settings);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d("Settings", data.getStringExtra("category_name"));
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
