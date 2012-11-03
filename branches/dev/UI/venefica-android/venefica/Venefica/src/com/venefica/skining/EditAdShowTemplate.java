package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.Gallery;

public abstract class EditAdShowTemplate extends ActivityTemplate
{
	public Button btnTakePhoto;
	public Button btnChoosePhoto;
	public Gallery galleryImage;

	public EditAdShowTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnTakePhoto == null)
			btnTakePhoto = new Button(mActivity);

		if (btnChoosePhoto == null)
			btnChoosePhoto = new Button(mActivity);
		
		if (galleryImage == null)
			galleryImage = new Gallery(mActivity);
	}
}
