package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.ToggleButton;
import android.app.Activity;

import com.venefica.activity.R;

public class AccauntSettingsSkinDef extends AccauntSettingsTemplate
{
	public AccauntSettingsSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.accaunt_settings_layout);

			toggleUseMiles = (ToggleButton)mActivity.findViewById(R.id.toggleUseMiles);
			btnFacebook = (Button)mActivity.findViewById(R.id.btnFacebook);
			btnTwitter = (Button)mActivity.findViewById(R.id.btnTwitter);
			btnVk = (Button)mActivity.findViewById(R.id.btnVk);
		}
		catch (Exception e)
		{
			Log.d("AccauntSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
