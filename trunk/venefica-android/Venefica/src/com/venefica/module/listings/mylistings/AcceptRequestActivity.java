package com.venefica.module.listings.mylistings;

import android.os.Bundle;

import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;

public class AcceptRequestActivity extends VeneficaActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		setContentView(R.layout.activity_accept_request);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}

}
