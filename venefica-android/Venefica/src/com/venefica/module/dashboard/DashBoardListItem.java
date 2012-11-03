/**
 * 
 */
package com.venefica.module.dashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author avinash
 *
 */
public class DashBoardListItem extends LinearLayout {
	private ImageView imgIcon;
	private TextView txtTitle;
	/**
	 * @param context
	 */
	public DashBoardListItem(Context context) {
		super(context);
		
	}

	/**
	 * Constructor to use view in xml
	 * @param context
	 * @param attrs
	 */
	public DashBoardListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

}
