package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ProductMessagesTemplate extends ActivityTemplate
{
	public ImageView imgProduct;
	public TextView lblTitle;
	public TextView lblCategory;
	public TextView lblPrice;
	public TextView lblMiles;
	public TextView lblUnitDistance;
	public ListView listMessages;
	public EditText editMessage;
	public Button btnAddComment;
	public Button btnCloseEditMessage;

	public ProductMessagesTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (imgProduct == null)
			imgProduct = new ImageView(mActivity);

		if (lblTitle == null)
			lblTitle = new TextView(mActivity);

		if (lblCategory == null)
			lblCategory = new TextView(mActivity);

		if (lblPrice == null)
			lblPrice = new TextView(mActivity);

		if (lblMiles == null)
			lblMiles = new TextView(mActivity);
		
		if (lblUnitDistance == null)
			lblUnitDistance = new TextView(mActivity);

		if (listMessages == null)
			listMessages = new ListView(mActivity);

		if (editMessage == null)
			editMessage = new EditText(mActivity);

		if (btnAddComment == null)
			btnAddComment = new Button(mActivity);

		if (btnCloseEditMessage == null)
			btnCloseEditMessage = new Button(mActivity);
	}
}
