package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

import com.venefica.activity.R;

public class ProductMessagesSkinDef extends ProductMessagesTemplate
{
	public ProductMessagesSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.product_messages);
			
			imgProduct = (ImageView)mActivity.findViewById(R.id.imgProduct);
			lblTitle = (TextView)mActivity.findViewById(R.id.lblTitle);
			lblCategory = (TextView)mActivity.findViewById(R.id.lblCategory);
			lblPrice = (TextView)mActivity.findViewById(R.id.lblPrice);
			lblMiles = (TextView)mActivity.findViewById(R.id.lblMiles);
			lblUnitDistance = (TextView)mActivity.findViewById(R.id.lblUnitDistance);
			listMessages = (ListView)mActivity.findViewById(R.id.listMessages);
			editMessage= (EditText)mActivity.findViewById(R.id.editMessage);
			btnAddComment = (Button)mActivity.findViewById(R.id.btnAddComment);
			btnCloseEditMessage = (Button)mActivity.findViewById(R.id.btnCloseEditMessage);
		}
		catch (Exception e)
		{
			Log.d("EditAdDetailsSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
