/**
 * 
 */
package com.venefica.module.listings.receiving;

import com.venefica.module.main.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author avinash
 * compound button view for receiving list item
 */
public class CustomImageTextButton extends LinearLayout{

	/**
	 * 
	 */
	private TextView txtlabel;
	private ImageButton imgButton;
	public CustomImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_image_text_button, null, false);
		txtlabel = (TextView) view.findViewById(R.id.txtReceivingLItemImage);
		imgButton = (ImageButton) view.findViewById(R.id.imgButtonReceivingLItem);
		addView(view);
	}

	public CustomImageTextButton(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_image_text_button, null, false);
		txtlabel = (TextView) view.findViewById(R.id.txtReceivingLItemImage);
		imgButton = (ImageButton) view.findViewById(R.id.imgButtonReceivingLItem);
		addView(view);
	}
	
}
