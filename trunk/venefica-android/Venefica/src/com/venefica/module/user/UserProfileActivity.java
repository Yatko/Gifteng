package com.venefica.module.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Activity to show/edit user profile
 */
public class UserProfileActivity extends VeneficaActivity {

	/**
	 * modes
	 */
	public static int ACT_MODE_VIEW_PROFILE = 4001;
	public static int ACT_MODE_EDIT_PROFILE = 4002;
	private int ACT_MODE;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CONFIRM = 3;
	/**
	 * web service task progress 
	 */
	private boolean isWorking = false;
	/**
	 * fragments
	 */
	private ViewProfileDetailsFragment viewProfileDetailsFragment;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_user_profile);
		ACT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_VIEW_PROFILE);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		viewProfileDetailsFragment = new ViewProfileDetailsFragment();
		fragmentTransaction.add(R.id.layActUserProfieRoot, viewProfileDetailsFragment);
		fragmentTransaction.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_user_profile, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (ACT_MODE == ACT_MODE_VIEW_PROFILE) {
			menu.findItem(R.id.menu_edit_profile).setVisible(true);
			menu.findItem(R.id.menu_save_profile).setVisible(false);
		} else if (ACT_MODE == ACT_MODE_EDIT_PROFILE){
			menu.findItem(R.id.menu_edit_profile).setVisible(false);
			menu.findItem(R.id.menu_save_profile).setVisible(true);
		}		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		onBackPressed();
    	} else if (itemId == R.id.menu_save_profile) {
    		//update profile
    		if (!isWorking) {
    			if(WSAction.isNetworkConnected(this)){
//    				new MessageListTask().execute(ACT_MODE_GET_ALL_MESSAGES);
    			} else {
    		    	ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
    		    	showDialog(D_ERROR);	 
    		    }
			}			
		} else if (itemId == R.id.menu_edit_profile) {
			ACT_MODE = ACT_MODE_EDIT_PROFILE;
		}
		return true;
	}
}
