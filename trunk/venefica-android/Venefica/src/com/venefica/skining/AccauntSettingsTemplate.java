package com.venefica.skining;

import android.app.Activity;
import android.widget.Button;
import android.widget.ToggleButton;

public abstract class AccauntSettingsTemplate extends ActivityTemplate
{
	public ToggleButton toggleUseMiles;
	public Button btnFacebook;
	public Button btnTwitter;
	public Button btnVk;

	public AccauntSettingsTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (toggleUseMiles == null)
			toggleUseMiles = new ToggleButton(mActivity);

		if (btnFacebook == null)
			btnFacebook = new Button(mActivity);

		if (btnTwitter == null)
			btnTwitter = new Button(mActivity);

		if (btnVk == null)
			btnVk = new Button(mActivity);
	}
}
