package com.venefica.activity;

import com.venefica.utils.Constants;
import com.venefica.utils.CustomLoadingDialog;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.VeneficaApplication;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class TabActivityEx extends TabActivity
{
	/** Hendler current main stream */
	protected Handler MainHandler = null;

	/** global application */
	protected VeneficaApplication App;

	/** download dialog */
	protected CustomLoadingDialog LoadingDialog;

	/** Manager Input Method */
	private static InputMethodManager cachedImm;

	protected final Runnable FinishRunnable = new Runnable()
	{
		public void run()
		{
			finish();
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Only landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		MainHandler = new Handler();

		App = (VeneficaApplication)getApplication();

		LoadingDialog = new CustomLoadingDialog(this);

		if (VeneficaApplication.myLocation == null || System.currentTimeMillis() - VeneficaApplication.myLocation.getTime() > Constants.GEOLOCATION_UPDATE_TIME_MS)
		{
			UpdateMyLocation();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		HideKeyboard();
	}
	
	public String GetStringResource(final int ResId)
	{
		String result;

		try
		{
			result = getResources().getString(ResId);
		}
		catch (Exception e)
		{
			Log.d("GetStringResource Exception:", "Resource(" + ResId + ") not found, message:" + e.getLocalizedMessage());
			result = "";
		}

		return result;
	}

	public boolean GetBooleanResource(final int ResId)
	{
		boolean result;

		try
		{
			result = getResources().getBoolean(ResId);
		}
		catch (Exception e)
		{
			Log.d("GetBooleanResource Exception:", "Resource(" + ResId + ") not found, message:" + e.getLocalizedMessage());
			result = false;
		}

		return result;
	}

	public void ShowInfoDialog(final String message)
	{
		ShowInfoDialog(message, null);
	}

	public void ShowInfoDialog(final String message, final Runnable OkEvent)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				if (OkEvent != null)
				{
					OkEvent.run();
				}

				dialog.cancel();
			}
		});
		builder.create().show();
	}

	public void UpdateMyLocation()
	{
		new GeoLocation(this, new GeoLocation.GeoLocationResult()
		{
			@Override
			public void ChangeLocation(Location location)
			{
				VeneficaApplication.myLocation = location;
			}
		});
	}

	public void ShowLoadingDialog()
	{
		if (LoadingDialog != null && LoadingDialog.isShowing() == false)
		{
			LoadingDialog.show();
		}
	}

	public void HideLoadingDialog()
	{
		if (LoadingDialog != null && LoadingDialog.isShowing())
		{
			LoadingDialog.dismiss();
		}
	}

	@Override
	public void onDetachedFromWindow()
	{
		try
		{
			super.onDetachedFromWindow();
		}
		catch (IllegalArgumentException e)
		{

		}
	}

	private InputMethodManager getIMM()
	{
		if (cachedImm == null)
			cachedImm = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
		return cachedImm;
	}

	/**Hides the soft keyboard, unless the user explicitly enable it caused */
	public void HideImplicidKeyboard()
	{
		getIMM().toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public void HideKeyboard()
	{
		//getIMM().toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		getIMM().hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}
}
