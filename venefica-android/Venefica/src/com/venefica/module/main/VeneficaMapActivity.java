package com.venefica.module.main;

import android.content.Intent;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.venefica.activity.R;
import com.venefica.module.dashboard.ISlideMenuCallback;
import com.venefica.module.dashboard.SlideMenuView;
import com.venefica.module.listings.browse.BrowseCategoriesActivity;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.listings.mylistings.MyListingsActivity;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.settings.SettingsActivity;
import com.venefica.module.user.RegisterUserActivity;

/**
 * @author avinash Base class for all map based activities in application
 */
public abstract class VeneficaMapActivity extends SherlockMapActivity implements
		ISlideMenuCallback {
	/**
	 * Slide menu
	 */
	protected SlideMenuView slideMenuView;
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
		switch (itemId) {
		case R.id.slideMenuBrowse: 
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
		case R.id.slideMenuOfferings:
    		break;
		case R.id.slideMenuClaimed:
    		break;
		case R.id.slideMenuBookmarks:
			if(!(getApplicationContext() instanceof SearchListingsActivity)){
				Intent browseIntent = new Intent(getApplicationContext(), SearchListingsActivity.class);
				browseIntent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_DOWNLOAD_BOOKMARKS);
				browseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(browseIntent);
			}
    		break;
		case R.id.slideMenuMyListings:
			if(!(getApplicationContext() instanceof SearchListingsActivity)){
				Intent browseIntent = new Intent(getApplicationContext(), SearchListingsActivity.class);
				browseIntent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_DOWNLOAD_MY_LISTINGS);
				browseIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(browseIntent);
			}
			/*if(!(getApplicationContext() instanceof MyListingsActivity)){
				Intent myListingIntent = new Intent(getApplicationContext(), MyListingsActivity.class);
				myListingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	startActivity(myListingIntent);
			}*/
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
		    	startActivity(settingsIntent);
			}
	    	break;
    	case R.id.slideMenuFeedback:
    		break;
		}
		if((getApplicationContext() instanceof SearchListingsActivity)){
			finish();
		}
	}

}
