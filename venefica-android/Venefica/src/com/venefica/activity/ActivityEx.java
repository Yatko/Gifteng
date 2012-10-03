package com.venefica.activity;

import com.venefica.utils.Constants;
import com.venefica.utils.CustomLoadingDialog;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.MyApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class ActivityEx extends Activity
{
	/** Hendler current main stream */
	protected Handler MainHandler = null;

	/** global application */
	protected MyApp App;

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

		App = (MyApp)getApplication();

		LoadingDialog = new CustomLoadingDialog(this);

		if (MyApp.MyLocation == null || System.currentTimeMillis() - MyApp.MyLocation.getTime() > Constants.GEOLOCATION_UPDATE_TIME_MS)
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
				MyApp.MyLocation = location;
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

	/** Hides the soft keyboard, unless the user explicitly enable it caused */
	public void HideImplicidKeyboard()
	{
		getIMM().toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public void HideKeyboard()
	{
		//getIMM().toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		getIMM().hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
	}

	public void ShowQuestionDialog(final String question, final Runnable okEvent)
	{
		ShowQuestionDialog(question, okEvent, null, GetStringResource(android.R.string.yes), GetStringResource(android.R.string.no));
	}

	public void ShowQuestionDialog(final String question, final Runnable okEvent, final Runnable notEvent, final String positiveButtonText, final String negativeButtonText)
	{
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle(question).setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (okEvent != null)
					okEvent.run();
			}
		}).setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (notEvent != null)
					notEvent.run();
			}
		}).create();

		dialog.show();
	}

	public void ShowQuestionDialog(final String question, final Runnable firstEvent, final Runnable secondEvent, final Runnable thirdEvent, final String firstButtonText, final String secondButtonText, final String thirdButtonText)
	{
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle(question).setPositiveButton(firstButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (firstEvent != null)
					firstEvent.run();
			}
		}).setNegativeButton(secondButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (secondEvent != null)
					secondEvent.run();
			}
		}).setNeutralButton(thirdButtonText, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (thirdEvent != null)
					thirdEvent.run();
			}
		}).create();

		dialog.show();
	}
}
