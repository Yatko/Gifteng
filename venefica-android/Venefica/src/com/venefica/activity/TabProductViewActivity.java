package com.venefica.activity;

import java.util.Stack;

import com.venefica.activity.R;
import com.venefica.market.Product;
import com.venefica.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabProductViewActivity extends TabActivityEx
{
	/** COPY edited product */
	public Product Item;
	public long productId;

	public static TabProductViewActivity sTab;
	private static TabWidget staticTabWidget;
	private TabHost tabHost;
	private Stack<Tabs> VisitedTabsStack;
	boolean IsInit = false;

	public enum Tabs
	{
		Product, Map, Comments;

		private Class<? extends Activity> Activity()
		{
			Class<? extends Activity> result = null;
			switch (this)
			{
				case Product:
					result = ProductViewActivity.class;
					break;

				case Map:
					result = ProductMapActivity.class;
					break;
					
				case Comments:
					result = ProductMessagesActivity.class;
					break;

				default:
					result = ProductViewActivity.class;
					break;
			}

			return result;
		}

		@SuppressWarnings ("unused")
		private int Icon()
		{
			switch (this)
			{
				case Product:
					return R.drawable.browse_tab_icon;
					
				case Map:
					return R.drawable.browse_tab_icon;

				case Comments:
					return R.drawable.messages_tab_icon;

				default:
					return R.drawable.post_tab_icon;
			}
		}

		int Id()
		{
			switch (this)
			{
				case Product:
					return 0;

				case Map:
					return 1;
					
				case Comments:
					return 2;

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
		View view = inflater.inflate(R.layout.tab_product_view, (ViewGroup)parent.findViewById(android.R.id.tabs), false);

		((TextView)view.findViewById(android.R.id.title)).setText(GetString(tab));

		//((ImageView)view.findViewById(android.R.id.icon)).setImageResource(tab.Icon());

		((LinearLayout.LayoutParams)view.getLayoutParams()).weight = 1.0F;
		TabHost.TabSpec result = tabHost.newTabSpec(GetString(tab));
		Intent intent = new Intent().setClass(context, tab.Activity());
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		result.setIndicator(view);
		result.setContent(intent);
		return result;
	}

	private String GetString(Tabs tab)
	{
		switch (tab)
		{
			case Product:
				return GetStringResource(R.string.product);

			case Map:
				return GetStringResource(R.string.map);
				
			case Comments:
				return GetStringResource(R.string.comments);

			default:
				return GetStringResource(R.string.temp_string);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_product_view_layout);
		sTab = this;
		Item = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			productId = extras.getLong(Constants.PRODUCT_ID_PARAM_NAME);

			if (productId != 0)
			{
				Init();
			}
			else
			{
				ShowInfoDialog("Error productId == 0", FinishRunnable);
				return;
			}
		}
		else
		{
			ShowInfoDialog("Error extras == null", FinishRunnable);
			return;
		}		
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

	public void GoToPrevious()
	{
		if ((staticTabWidget != null) && (VisitedTabsStack.size() >= 2))
		{
			VisitedTabsStack.pop();
			tabHost.setCurrentTabByTag(GetString(VisitedTabsStack.peek()));
		}
		else
		{
			finish();
		}
	}

	public void Init()
	{
		if (IsInit)
			return;

		tabHost = getTabHost();

		staticTabWidget = tabHost.getTabWidget();

		View view = getWindow().getDecorView();

		Tabs[] tabs = Tabs.values();
		for (Tabs tab : tabs)
		{
			tabHost.addTab(GetTabSpec(tab, this, getLayoutInflater(), view, tabHost));
		}

		VisitedTabsStack = new Stack<Tabs>();
		VisitedTabsStack.push(Tabs.Product);

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			public void onTabChanged(String tabId)
			{
				if (!(VisitedTabsStack.peek()).toString().equals(tabId))
					VisitedTabsStack.push(Tabs.valueOf(tabId));
			}
		});

		IsInit = true;
	}
}
