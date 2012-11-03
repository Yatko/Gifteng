package com.venefica.activity;

import android.content.Intent;
@Deprecated
public class ActivityLogOut extends ActivityEx
{
	public static final String LOGOUT_PARAMETR_NAME = "LogOut";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != -1 || data == null)
		{
			return;
		}
		
		if (data.getBooleanExtra(LOGOUT_PARAMETR_NAME, false))
		{
			logOutAction();
			finish();
		}
	}
	
	public void logOutAction()
	{
		Intent intent = new Intent();
		intent.putExtra(LOGOUT_PARAMETR_NAME, true);
		setResult(RESULT_OK, intent);
	}
}
