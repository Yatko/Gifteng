package com.venefica.module.main;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.venefica.module.user.LoginActivity;

@SuppressWarnings("deprecation")
public class StartUpActivity extends TabActivity implements OnClickListener {

	/**
	 * Tabs
	 */
	private TabHost tabHost;
	private TabHost.TabSpec spec;
	private static String TAB_INVITATION =  "invitation", TAB_LOGIN = "log_in";
	
	/**
	 * action bar buttons
	 */
	private ImageButton btnBack, btnNext;
	/**
	 * 
	 * steps 
	 *
	 */
	private enum Steps {
		START_PAGE, REQ_INVITE, REQ_INVITE_CONFIRM, REQ_INVITE_SUCCESS, VERIFY_INVITE
	}
	private Steps currentStep;
	/**
	 * layouts
	 */
	private ViewGroup layStartPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_up);
		Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);
		
		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			public void onTabChanged(String tabId) {
				InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMgr.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
			}
		});
		//set Invitation tab
		spec = tabHost.newTabSpec(TAB_INVITATION);
		spec.setIndicator(
				getResources().getString(R.string.g_label_invitation),
				icon);
		spec.setContent(new Intent(this, InvitationActivity.class));
		tabHost.addTab(spec);
		//set login tab
		spec = tabHost.newTabSpec(TAB_LOGIN);
		spec.setIndicator(
				getResources().getString(R.string.g_label_log_in),
				icon);
		spec.setContent(new Intent(this, LoginActivity.class));
		tabHost.addTab(spec);		

		tabHost.setCurrentTab(0);
		tabHost.setClickable(true);
		//set current step
		currentStep = Steps.START_PAGE;
		//action bar buttons
		btnBack = (ImageButton) findViewById(R.id.btnActStartBack);
		btnBack.setOnClickListener(this);
		btnNext = (ImageButton) findViewById(R.id.btnActStartNext);
		btnNext.setOnClickListener(this);
		
		//start page
		layStartPage = (ViewGroup) findViewById(R.id.layStartPage);
		//tabs
		
		
		setActionBarButtonVisibility();		
	}

	/**
	 * method to set actionbar next/back button visibility
	 */
	private void setActionBarButtonVisibility() {
		if (currentStep == Steps.START_PAGE) {
			btnBack.setVisibility(View.INVISIBLE);
			btnNext.setVisibility(View.VISIBLE);
		} else if (currentStep == Steps.REQ_INVITE) {
			btnBack.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.INVISIBLE);
		} else {

		}
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start_up, menu);
		return true;
	}*/

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActStartNext) {
			if (currentStep == Steps.START_PAGE) {
				//got request invite
				layStartPage.setVisibility(ViewGroup.GONE);
				currentStep = Steps.REQ_INVITE;
				tabHost.setVisibility(TabHost.VISIBLE);
			}
			setActionBarButtonVisibility();
		} else if (id == R.id.btnActStartBack){
			if (currentStep == Steps.REQ_INVITE) {
				//go back to start page
				layStartPage.setVisibility(ViewGroup.VISIBLE);
				currentStep = Steps.START_PAGE;
				tabHost.setVisibility(TabHost.INVISIBLE);
			}
			setActionBarButtonVisibility();
		}
		
	}

}
