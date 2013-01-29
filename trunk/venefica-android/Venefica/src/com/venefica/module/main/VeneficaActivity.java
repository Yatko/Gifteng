package com.venefica.module.main;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.venefica.module.dashboard.ISlideMenuCallback;
import com.venefica.module.dashboard.SlideMenuView;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.settings.SettingsActivity;
import com.venefica.module.user.RegisterUserActivity;
import com.venefica.module.utils.Utility;

/**
 * @author avinash
 * Base class for all activities in application
 */
public abstract class VeneficaActivity extends SherlockActivity implements
ISlideMenuCallback {
	/**
	 * Slide menu
	 */
	protected SlideMenuView slideMenuView;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slideMenuView.toggleMenu();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onSideNavigationItemClick(int itemId) {
		switch (itemId-1) {
		case R.id.slideMenuBrowse: 
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			if(!(getApplicationContext() instanceof SearchListingsActivity)){
				Intent browseIntent = new Intent(getApplicationContext(), SearchListingsActivity.class);
				browseIntent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY);
				browseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(browseIntent);
			}
	    	break;
		case R.id.slideMenuPost:
			if(!(getApplicationContext() instanceof PostListingActivity)){
				Intent postIntent = new Intent(getApplicationContext(), PostListingActivity.class);
				postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(postIntent);
			}
	    	break;
		case R.id.slideMenuSelling:
			if(!(getApplicationContext() instanceof SearchListingsActivity)){
				Intent browseIntent = new Intent(getApplicationContext(), SearchListingsActivity.class);
				browseIntent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_DOWNLOAD_MY_LISTINGS);
				browseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(browseIntent);
			}
	    	break;
		case R.id.slideMenuBuying:
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
    		break;
		case R.id.slideMenuWatching:
			if(!(getApplicationContext() instanceof SearchListingsActivity)){
				Intent browseIntent = new Intent(getApplicationContext(), SearchListingsActivity.class);
				browseIntent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_DOWNLOAD_BOOKMARKS);
				browseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(browseIntent);
			}
    		break;
		case R.id.slideMenuMessages:
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
    		break;
		case R.id.slideMenuReviews:
    		Utility.showLongToast(this, getResources().getString(R.string.msg_not_impl));
    		break;
		case R.id.slideMenuInviteFriends:
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
    		break;
		case R.id.slideMenuAccount:  
			if(!(getApplicationContext() instanceof RegisterUserActivity)){
				Intent accountIntent = new Intent(getApplicationContext(), RegisterUserActivity.class);
				accountIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				accountIntent.putExtra("activity_mode",RegisterUserActivity.MODE_UPDATE_PROF);
		    	startActivity(accountIntent);
			}
	    	break;
		case R.id.slideMenuSettings:  
			if(!(getApplicationContext() instanceof SettingsActivity)){
				Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
				settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				settingsIntent.putExtra("act_mode",SettingsActivity.ACT_MODE_APP_SETTINGS);
		    	startActivity(settingsIntent);
			}
	    	break;
		case R.id.slideMenuSignOut:
			Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
    		break;
		}
		if((getApplicationContext() instanceof SearchListingsActivity)){
			finish();
		}
	}
	/**
	 * @return the slideMenuView
	 */
	public SlideMenuView getSlideMenuView() {
		return slideMenuView;
	}

	/**
	 * @param slideMenuView the slideMenuView to set
	 */
	public void setSlideMenuView(SlideMenuView slideMenuView) {
		this.slideMenuView = slideMenuView;
	}

	
}
