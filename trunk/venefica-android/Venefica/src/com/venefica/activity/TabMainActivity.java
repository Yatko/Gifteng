package com.venefica.activity;

import java.util.Stack;

import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetCategoriesContext;
import com.venefica.services.AsyncServices.GetUserContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetUserResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;
import com.venefica.utils.PreferencesManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabMainActivity extends TabActivityEx
{
	public static TabMainActivity sTab;
	private static TabWidget staticTabWidget;
	private static TextView lblUnreadMessages;
	private TabHost tabHost;
	private static int NumUnreadMessages;
	private Stack<Tabs> VisitedTabsStack;

	final ICallback GetUserCallback = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			HideLoadingDialog();

			if (result.SoapResult != SoapRequestResult.Ok)
			{
				ShowInfoDialog(GetStringResource(R.string.get_user_error), FinishRunnable);
			}
			else
			{
				GetUserResult ret = (GetUserResult)result;
				MyApp.user = ret.Return;

				Init();
			}

			return CallbackReturn.Ok;
		}
	};

	final ICallback GetCategoryCallback = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			if (result.SoapResult != SoapRequestResult.Ok)
			{
				HideLoadingDialog();
				ShowInfoDialog(GetStringResource(R.string.get_categories_error), FinishRunnable);
			}
			else
			{
				MyApp.AsyncServices.GetUser(new GetUserContext(GetUserCallback));
			}

			return CallbackReturn.Ok;
		}
	};

	public enum Tabs
	{
		Browse, Post, Messages, Account;

		private Class<? extends Activity> Activity()
		{
			Class<? extends Activity> result = null;
			switch (this)
			{
				case Browse:
					result = BrowseGroupActivity.class;
					break;

				case Post:
					result = PostActivity.class;
					break;

				case Messages:
					result = MessagesActivity.class;
					break;

				case Account:
					result = AccauntActivity.class;
					break;

				default:
					result = BrowseGroupActivity.class;
					break;
			}

			return result;
		}

		private int Icon()
		{
			switch (this)
			{
				case Browse:
					return R.drawable.browse_tab_icon;

				case Post:
					return R.drawable.post_tab_icon;

				case Messages:
					return R.drawable.messages_tab_icon;

				case Account:
					return R.drawable.accaunt_tab_icon;

				default:
					return R.drawable.post_tab_icon;
			}
		}

		int Id()
		{
			switch (this)
			{
				case Browse:
					return 0;

				case Post:
					return 1;

				case Messages:
					return 2;

				case Account:
					return 3;

				default:
					return -1;
			}
		}

		public String toString()
		{
			return "" + Id();
		}
	}

	TabHost.TabSpec GetTabSpec(Tabs tab, Context context, LayoutInflater inflater, View parent, TabHost tabHost)
	{
		View view = inflater.inflate(R.layout.tab_main, (ViewGroup)parent.findViewById(android.R.id.tabs), false);

		((TextView)view.findViewById(android.R.id.title)).setText(GetString(tab));
		((ImageView)view.findViewById(android.R.id.icon)).setImageResource(tab.Icon());

		((LinearLayout.LayoutParams)view.getLayoutParams()).weight = 1.0F;
		TabHost.TabSpec result = tabHost.newTabSpec(GetString(tab));
		Intent intent = new Intent().setClass(context, tab.Activity());
		result.setIndicator(view);
		result.setContent(intent);
		return result;
	}

	private String GetString(Tabs tab)
	{
		switch (tab)
		{
			case Browse:
				return GetStringResource(R.string.browse);

			case Post:
				return GetStringResource(R.string.post);

			case Messages:
				return GetStringResource(R.string.messages);

			case Account:
				return GetStringResource(R.string.accaunt);

			default:
				return GetStringResource(R.string.temp_string);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main_layout);
		sTab = this;

		/*Bitmap bb = BitmapUtil.DecodeFile(MyApp.TEMP_FILE, 320);
		ServicesManager sv = new ServicesManager();
		AddImageToAdResult res = sv.AddImageToAd(MyApp.AuthToken, 51, bb);*/
		
		ShowLoadingDialog();
		MyApp.AsyncServices.GetCategories(new GetCategoriesContext(GetCategoryCallback));
	}

	protected void Init()
	{
		tabHost = getTabHost();

		staticTabWidget = tabHost.getTabWidget();

		View view = getWindow().getDecorView();

		Tabs[] tabs = Tabs.values();
		for (Tabs tab : tabs)
		{
			tabHost.addTab(GetTabSpec(tab, this, getLayoutInflater(), view, tabHost));
		}

		view = staticTabWidget.getChildAt(Tabs.Messages.Id());
		if (view != null)
			lblUnreadMessages = (TextView)view.findViewById(R.id.lblNumUnread);
		if (lblUnreadMessages == null)
			lblUnreadMessages = new TextView(this);

		VisitedTabsStack = new Stack<Tabs>();
		VisitedTabsStack.push(Tabs.Browse);

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			public void onTabChanged(String tabId)
			{
				if (!(VisitedTabsStack.peek()).toString().equals(tabId))
				{
					VisitedTabsStack.push(Tabs.valueOf(tabId));
				}

				//if (tabId.equals(GetString(Tabs.Messages)))
				//  TabPanelActivity.this.updateMessagesList("");

				//TODO then message of updates and products (Market.NeedUpdate)
				UpdateUnreadMessagesView();
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		MarketEx.getInstance().ClearProductsList();
		MarketEx.getInstance().ClearCategory();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		UpdateUnreadMessagesView();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Log.d(getClass().getName(), "onBackPressed");
	}

	public static void HideTabs()
	{
		if (staticTabWidget != null)
			staticTabWidget.setVisibility(View.GONE);
	}

	public static void ShowTabs()
	{
		if (staticTabWidget != null)
			staticTabWidget.setVisibility(View.VISIBLE);
	}

	public static void UpdateUnreadMessagesView()
	{
		if (lblUnreadMessages != null)
		{
			lblUnreadMessages.post(new Runnable()
			{
				public void run()
				{
					if (NumUnreadMessages <= 99)
						lblUnreadMessages.setText("" + NumUnreadMessages);
					else
						lblUnreadMessages.setText("99+");
				}
			});
		}
	}

	public static int GetNumberOfUnreadMessages()
	{
		return NumUnreadMessages;
	}

	public static void IncrementNumberOfUnreadMessages()
	{
		SetNumberOfUnreadMessages(NumUnreadMessages + 1);
	}

	public static void DecrementNumberOfUnreadMessages()
	{
		SetNumberOfUnreadMessages(NumUnreadMessages - 1);
	}

	public static void SetNumberOfUnreadMessages(int num)
	{
		NumUnreadMessages = num;
		UpdateUnreadMessagesView();
	}

	public void GoToPrevious()
	{
		if ((staticTabWidget != null) && (VisitedTabsStack.size() >= 2))
		{
			VisitedTabsStack.pop();
			tabHost.setCurrentTabByTag(GetString(VisitedTabsStack.peek()));
		}
		else
		{
			PreferencesManager pref = new PreferencesManager(this, Constants.PREFERENCES_STORAGE_NAME);
			if (MyApp.RememberMe)
			{

				pref.Put(Constants.PREFERENCES_TOKEN_NAME, MyApp.AuthToken);
				pref.Put(Constants.PREFERENCES_TOKEN_SAVE_TIME_NAME, System.currentTimeMillis());
			}
			else
			{
				pref.Put(Constants.PREFERENCES_TOKEN_NAME, "");
				pref.Put(Constants.PREFERENCES_TOKEN_SAVE_TIME_NAME, (long)0);
			}
			pref.Commit();
			
			Intent intent = new Intent();
			intent.putExtra(ActivityLogOut.LOGOUT_PARAMETR_NAME, true);
			setResult(RESULT_OK, intent);

			
			finish();
		}
	}
}
