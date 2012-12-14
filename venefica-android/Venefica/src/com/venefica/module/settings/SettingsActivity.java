package com.venefica.module.settings;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.venefica.module.main.R;
import com.venefica.utils.Constants;

public class SettingsActivity extends SherlockPreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Constants.VENEFICA_PREFERENCES);
        addPreferencesFromResource(R.xml.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return true;
    }
    
    
}
