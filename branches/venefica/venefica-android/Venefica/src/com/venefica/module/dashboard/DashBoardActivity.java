/**
 * 
 */
package com.venefica.module.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.venefica.module.alert.AlertListActivity;
import com.venefica.module.listings.browse.BrowseCategoriesActivity;
import com.venefica.module.listings.mylistings.MyListingsActivity;
import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.main.R;
import com.venefica.module.messages.MessageListActivity;
import com.venefica.module.settings.SettingsActivity;
import com.venefica.module.user.RegisterUserActivity;

/**
 * @author avinash
 * Dashboard activity
 */
public class DashBoardActivity extends Activity {
	/**
	 * List to show dashboard items
	 */
	private ListView listViewDashBoard;
	/**
	 * List adapter
	 */
	private DashBoardListAdapter dashBoardAdapter;
	/**
	 * Dash board menu item list
	 */
	private String[] listItems;
	/**
	 * Dash board menu item icons
	 */
	private int[] icons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		
		listViewDashBoard = (ListView) findViewById(R.id.listActDashBoard);
		listItems = getResources().getStringArray(R.array.array_dashboard_menu);
        icons = new int[]{R.drawable.alerts, R.drawable.messages, R.drawable.browse, R.drawable.post, 
        		R.drawable.claimed, R.drawable.offerings, R.drawable.my_listings, R.drawable.account,
        		R.drawable.settings, R.drawable.feedback};
        dashBoardAdapter = new DashBoardListAdapter(this, listItems, icons);
        listViewDashBoard.setAdapter(dashBoardAdapter);
        listViewDashBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch( position ){
					case 0:  Intent alertIntent = new Intent(DashBoardActivity.this, AlertListActivity.class);     
						startActivity(alertIntent);
			            break;
					case 1:  Intent messagesIntent = new Intent(DashBoardActivity.this, MessageListActivity.class);     
	                	startActivity(messagesIntent);
	                	break;
					case 2:  Intent browseIntent = new Intent(DashBoardActivity.this, BrowseCategoriesActivity.class);     
	                	startActivity(browseIntent);
	                	break;
					case 3:  Intent postIntent = new Intent(DashBoardActivity.this, PostListingActivity.class);     
	                	startActivity(postIntent);
	                	break;
					case 6:  Intent myListingIntent = new Intent(DashBoardActivity.this, MyListingsActivity.class);     
	                	startActivity(myListingIntent);
	                	break;
					case 7:  Intent accountIntent = new Intent(DashBoardActivity.this, RegisterUserActivity.class);
						accountIntent.putExtra("activity_mode",RegisterUserActivity.MODE_UPDATE_PROF);
	                	startActivity(accountIntent);
	                	break;
					case 8:  Intent settingsIntent = new Intent(DashBoardActivity.this, SettingsActivity.class);     
	                	startActivity(settingsIntent);
	                	break;
			       default:
			    }
			}
		});
	}

}
