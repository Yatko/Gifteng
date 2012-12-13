package com.venefica.module.alert;

import java.util.ArrayList;

import com.venefica.module.main.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * @author avinash
 * Alert list activity
 */
public class AlertListActivity extends Activity {
	/**
	 * List to show alerts
	 */
	private ListView listViewAlerts;
	/**
	 * List adapter
	 */
	private AlertListAdapter alertListAdapter;
	/**
	 * Dash board menu item list
	 */
	private ArrayList<AlertData> alerts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_list);
		
		listViewAlerts = (ListView) findViewById(R.id.listActAlertList);
		alerts = getDemoAlerts();
        
		alertListAdapter = new AlertListAdapter(this, alerts);
		listViewAlerts.setAdapter(alertListAdapter);
		listViewAlerts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
	}

	/**
	 * Method to get demo alerts.
	 * @return
	 */
	private ArrayList<AlertData> getDemoAlerts() {
		ArrayList<AlertData> alerts = new ArrayList<AlertData>();
		AlertData alert = new AlertData();
		alert.setAlertId(1);
		alert.setAlertTitle("Demo Alert Demo Alert Demo Alert Demo Alert Demo Alert Demo Alert ");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(2);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(3);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(4);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(5);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(6);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(7);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(8);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alerts.add(alert);
		alert = new AlertData();
		alert.setAlertId(9);
		alert.setAlertTitle("Demo Alert");
		alert.setAlertTime("18:20");
		alert.setAlertDesc("Testing data");
		alert.setUnread(false);
		alerts.add(alert);
		return alerts;
	}
}
