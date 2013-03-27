package com.venefica.module.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.user.PasswordResetEmailLookupFragment.OnPasswordResetEmailLookupListener;
import com.venefica.module.user.PasswordResetFragment.OnPasswordReset;

public class PasswordResetActivity extends SherlockFragmentActivity implements OnPasswordResetEmailLookupListener, OnPasswordReset{

	/**
	 * modes
	 */
	public static int ACT_MODE_VERIFY_EMAIL = 4001;
	public static int ACT_MODE_RESET_PASSWORD = 4002;
	private int ACT_MODE;
	private PasswordResetEmailLookupFragment passwordResetEmailLookupFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_password_reset);
		
		ACT_MODE = getIntent().getExtras().getInt("act_mode", ACT_MODE_VERIFY_EMAIL);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();		
		passwordResetEmailLookupFragment = new PasswordResetEmailLookupFragment();
		fragmentTransaction.add(R.id.layActPwdResetRoot, passwordResetEmailLookupFragment);
		fragmentTransaction.commit();
	}
	@Override
	public void onContinueButtonClick(String email) {
		getSupportFragmentManager().beginTransaction().replace(R.id.layActPwdResetRoot, new PasswordResetFragment()).commit();
	}
	@Override
	public void onResetPasswordButtonClick(String password) {
		setResult(Activity.RESULT_OK);
		finish();
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_password_reset, menu);
		return true;
	}*/

}
