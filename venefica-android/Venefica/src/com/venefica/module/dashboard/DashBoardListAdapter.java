/**
 * 
 */
package com.venefica.module.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.main.R;

/**
 * @author avinash
 * Adapter class for Dashboard List
 */
public class DashBoardListAdapter extends BaseAdapter {
	private Context context;
	private String[] titles;
	private int[] icons;
	/**
	 * Constructor
	 */
	public DashBoardListAdapter(Context context, String[] titles, int[] icons) {
		this.context = context;
		this.titles = titles;
		this.icons  = icons;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return titles.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_dashboard_list_item, parent, false);
		}
	    TextView textView = (TextView) convertView.findViewById(R.id.txtDashBListItemTitle);
	    ImageView imageView = (ImageView) convertView.findViewById(R.id.imgDashBListItemIcon);
	    textView.setText(titles[position]);
	    imageView.setImageResource(icons[position]);
		return convertView;
	}

}
