package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetAdByIdResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.utils.Constants;
import com.venefica.utils.ImageAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabAdEditActivity extends TabActivityEx
{
	/** COPY edited product */
	public static Product Item;
	public static List<ImageAd> images;
	public static List<Long> imagesForDelete;
	public static Bitmap MapPreview;
	public static boolean IsUpdate = false;

	public static TabAdEditActivity sTab;
	private static TabWidget staticTabWidget;
	private TabHost tabHost;
	private Stack<Tabs> VisitedTabsStack;

	public final Runnable FinishResultRunnable = new Runnable()
	{
		public void run()
		{
			if (IsUpdate)
			{
				Intent intent = new Intent();
				intent.putExtra("id", Item.Id);
				setResult(RESULT_OK, intent);
			}

			finish();
		}
	};

	public enum Tabs
	{
		Details, Show, GeoLoc, Preview;

		private Class<? extends Activity> Activity()
		{
			Class<? extends Activity> result = null;
			switch (this)
			{
				case Details:
					result = EditAdDetailsActivity.class;
					break;

				case Show:
					result = EditAdShowActivity.class;
					break;

				case GeoLoc:
					result = EditAdLocationActivity.class;
					break;

				case Preview:
					result = EditAdPreviewActivity.class;
					break;

				default:
					result = EditAdDetailsActivity.class;
					break;
			}

			return result;
		}

		private int Icon()
		{
			switch (this)
			{
				case Details:
					return R.drawable.post_tab_icon;

				case Show:
					return R.drawable.post_tab_icon;

				case GeoLoc:
					return R.drawable.post_tab_icon;

				case Preview:
					return R.drawable.post_tab_icon;

				default:
					return R.drawable.post_tab_icon;
			}
		}

		int Id()
		{
			switch (this)
			{
				case Details:
					return 0;

				case Show:
					return 1;

				case GeoLoc:
					return 2;

				case Preview:
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
		View view = inflater.inflate(R.layout.tab_edit_ad, (ViewGroup)parent.findViewById(android.R.id.tabs), false);

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
			case Details:
				return GetStringResource(R.string.details);

			case Show:
				return GetStringResource(R.string.show);

			case GeoLoc:
				return GetStringResource(R.string.geo_loc);

			case Preview:
				return GetStringResource(R.string.preview);

			default:
				return GetStringResource(R.string.temp_string);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_edit_ad_layout);
		sTab = this;

		Item = null;
		images = new ArrayList<ImageAd>();
		imagesForDelete = new ArrayList<Long>();
		MapPreview = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			long id = extras.getLong(Constants.PRODUCT_ID_PARAM_NAME);

			if (id != 0)
			{
				ShowLoadingDialog();
				MarketEx.getInstance().GetProductById(id, new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();
						
						GetAdByIdResult res = (GetAdByIdResult)result;

						switch (res.SoapResult)
						{
							case Ok:
								Item = res.Return;
								
								if(Item.image != null && Item.image.url !=null)
									images.add(Item.image);
									
								images.addAll(Item.images);
									
								InitTab();
								break;
							case SoapProblem:
								ShowInfoDialog(GetStringResource(R.string.soap_problem));
								break;
							case Fault:
								ShowInfoDialog("Error: Fault", FinishRunnable);
								break;
						}

						return CallbackReturn.Ok;
					}
				});
			}
			else
			{
				ShowInfoDialog("Error: id == 0", FinishRunnable);
				return;
			}
		}
		else
		{
			ShowInfoDialog("Error: extras == null", FinishRunnable);
			return;
		}
	}

	protected void InitTab()
	{
		tabHost = getTabHost();

		staticTabWidget = tabHost.getTabWidget();

		View view = getWindow().getDecorView();

		Tabs[] tabs = Tabs.values();
		for (Tabs tab : tabs)
		{
			tabHost.addTab(GetTabSpec(tab, this, getLayoutInflater(), view, tabHost));
		}

		VisitedTabsStack = new Stack<Tabs>();
		VisitedTabsStack.push(Tabs.Details);

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{
			public void onTabChanged(String tabId)
			{
				if (!(VisitedTabsStack.peek()).toString().equals(tabId))
					VisitedTabsStack.push(Tabs.valueOf(tabId));
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
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

}
