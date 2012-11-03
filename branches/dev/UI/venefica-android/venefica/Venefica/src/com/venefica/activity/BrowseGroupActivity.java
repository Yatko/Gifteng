package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import com.venefica.activity.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class BrowseGroupActivity extends ActivityGroup
{
	private List<View> history;
	private boolean dirtyExit = false;

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		history = new ArrayList<View>();
		startActivityReplaceView(new Intent(this, BrowseListActivity.class));
	}

	public void back()
	{
		View view;
		if (history.size() > 1)
		{
			history.remove(history.size() - 1);
			view = history.get(history.size() - 1);
			//i = ((Integer)view.getTag()).intValue();
			setContentView(view);
		}
		else
		{
			final Object parent = getParent();
			if (parent == null)
			{
				finish();
			}
			else if(parent instanceof TabMainActivity)
			{
				if (dirtyExit)
				{
					((TabMainActivity)parent).GoToPrevious();
				}
				else
				{
					dirtyExit = true;
					Toast.makeText(this, R.string.exit_toast_message, Toast.LENGTH_LONG).show();
				}				
			}
			else
			{
				finish();
			}
		}
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK so that
	 * all systems call onBackPressed().
	 */
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event)
	{
		boolean result;
		
		/*if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			onBackPressed();
			result =  true;
		}*/
		result= super.onKeyDown(keyCode, event);
		
		return result;
	}

	/**
	 * If a Child Activity handles KeyEvent.KEYCODE_BACK. Simply override and
	 * add this method.
	 */
	@Override
	public void onBackPressed()
	{
		back();
		Log.d(getClass().getName(), "onBackPressed");
	}

	public void startActivityReplaceView(final Intent intent)
	{
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final String str = intent.getComponent().getClassName();
		//view.setTag(Integer.valueOf(paramInt));
		replaceView(getLocalActivityManager().startActivity(str, intent).getDecorView());
	}

	public void replaceView(final View view)
	{
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		history.add(view);
		setContentView(view);
		dirtyExit = false;
	}
}
