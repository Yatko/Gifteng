package com.venefica.module.alert;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.venefica.module.main.R;

/**
 * @author avinash
 * Adapter class for alert list
 */
public class AlertListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<AlertData> alerts;
	/**
	 * Constructor
	 */
	public AlertListAdapter(Context context, ArrayList<AlertData> alerts) {
		this.context = context;
		this.alerts = alerts;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return alerts.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_alert_list_item, parent, false);			
		}
		AlertData alert = alerts.get(position);
		TextView textTitle = (TextView) convertView.findViewById(R.id.txtAlertLItemTitle);
		textTitle.setText(alert.getAlertTitle());
		if(!alert.isUnread()){
			textTitle.setTypeface(null,Typeface.ITALIC);
		}
		TextView textTime = (TextView) convertView.findViewById(R.id.txtAlertLItemTime);
		textTime.setText(alert.getAlertTime());
		TextView  textDesc = (TextView) convertView.findViewById(R.id.txtAlertLItemDesc);
		textDesc.setText(alert.getAlertDesc());
		return convertView;
	}

}
