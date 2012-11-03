package com.venefica.module.settings;

import com.venefica.activity.R;
import com.venefica.activity.R.layout;
import com.venefica.activity.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
}
