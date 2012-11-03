package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.venefica.activity.R;
import com.venefica.market.Category;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.market.MarketEx.UpdateProductsCallback;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.*;
import com.venefica.utils.Constants;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.GoogleMapManager;
import com.venefica.utils.VeneficaApplication;

public class BrowseMapActivity extends MapActivityEx
{
	private BrowseMapTemplate T;
	private OfferPinPoint OfferLocation;
	private OfferPinPoint OfferMyLocation;
	private GoogleMapManager MapMng;
	PopupDialog Popup;
	int height;
	FilterPanel filterPanel;
	final Runnable filterCloseCallback = new Runnable()
	{
		public void run()
		{
			if (filterPanel.isChange())
			{
				MarketEx.getInstance().setFilter(filterPanel.getFilter());
				UpdateProductList();
				HideKeyboard();
			}
		}
	};

	public class OfferPinPoint extends ItemizedOverlay<OverlayItem>
	{
		private ArrayList<OverlayItem> mOverlaysItems = new ArrayList<OverlayItem>();

		public OfferPinPoint(Drawable defaultMarker)
		{
			super(boundCenterBottom(defaultMarker));
			height = defaultMarker.getMinimumHeight();
		}

		public void addOverlay(OverlayItem item)
		{
			mOverlaysItems.add(item);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i)
		{
			return mOverlaysItems.get(i);
		}

		@Override
		public int size()
		{
			return mOverlaysItems.size();
		}

		@Override
		protected boolean onTap(int index)
		{
			List<Product> items = MarketEx.getInstance().GetProducts();

			if (index < 0 || index > items.size() - 1)
				return true;

			Product item = items.get(index);
			if (item != null && Popup!=null)
			{
				GeoPoint ItemGeoPoint = mOverlaysItems.get(index).getPoint();
				Point ScreenPoint = new Point();
				T.Map.getProjection().toPixels(ItemGeoPoint, ScreenPoint);
				ScreenPoint.offset(-50, 0);

				GeoPoint OffsetGeoPoint = T.Map.getProjection().fromPixels(ScreenPoint.x, ScreenPoint.y);
				MapMng.GoToLocation(OffsetGeoPoint, 17);
				View view = Popup.getView();

				if (item.imageThumbnail != null && item.imageThumbnail.url != null)
				{
					ImageView imgProduct = (ImageView)view.findViewById(R.id.imgProduct);
//					VeneficaApplication.ImgLoader.displayImage(item.imageThumbnail.url, imgProduct, VeneficaApplication.ImgLoaderOptions);
				}

				((RelativeLayout)view.findViewById(R.id.relativelayout)).setBackgroundResource(item.wanted ? R.drawable.popup_wanted_bg : R.drawable.popup_bg);
				((TextView)view.findViewById(R.id.lblTitle)).setText(item.Title);
				((TextView)view.findViewById(R.id.lblCategory)).setText(Category.GetCategoryById(item.Category).Desc);
				((TextView)view.findViewById(R.id.lblPrice)).setText(item.Price);

				Popup.show(ItemGeoPoint, height, item.Id);
			}

			return true;
		}

		public void Clear()
		{
			mOverlaysItems.clear();
			populate();
		}

		public void SetPopup(PopupDialog dialog)
		{
			Popup = dialog;
		}

		@Override
		public boolean onTouchEvent(MotionEvent paramMotionEvent, MapView paramMapView)
		{
			Popup.hide();
			return super.onTouchEvent(paramMotionEvent, paramMapView);
		}
	}

	public class PopupDialog
	{
		boolean isVisible = false;
		View popup;
		long productId;

		public PopupDialog()
		{
			ViewGroup localViewGroup = (ViewGroup)T.Map.getParent();
			popup = BrowseMapActivity.this.getLayoutInflater().inflate(R.layout.popup, localViewGroup, false);
			popup.findViewById(R.id.relativelayout).setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					hide();
					GoToPreview(productId);
				}
			});
		}

		private void hide()
		{
			if (isVisible)
			{
				isVisible = false;
				((ViewGroup)popup.getParent()).removeView(this.popup);
				Log.d("PopupDialog", "hide");
			}
		}

		public View getView()
		{
			return popup;
		}

		void show(GeoPoint paramGeoPoint, int paramInt1, long productId)
		{
			this.productId = productId;
			Projection localProjection = T.Map.getProjection();
			Point localPoint = new Point();
			Log.i("**********Height", "" + paramInt1);
			localProjection.toPixels(paramGeoPoint, localPoint);
			localPoint.offset((int)(-78.0F * BrowseMapActivity.this.getResources().getDisplayMetrics().density), -(paramInt1 - 5));
			GeoPoint localGeoPoint = localProjection.fromPixels(localPoint.x, localPoint.y);

			BrowseMapActivity.this.MapMng.GoToLocation(localGeoPoint, 17);

			MapView.LayoutParams localLayoutParams = new MapView.LayoutParams(-2, -2, localGeoPoint, 81);
			hide();
			T.Map.addView(this.popup, localLayoutParams);
			Animation localAnimation = AnimationUtils.loadAnimation(BrowseMapActivity.this.getParent(), R.anim.pop_up_animation);
			popup.startAnimation(localAnimation);
			isVisible = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new BrowseMapSkinDef(this);
		TabMainActivity.ShowTabs();

		T.HeaderTap.SetOnClickToList(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				GoToList();
			}
		});
		T.HeaderTap.SetOnClickToPhoto(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				GoToPhoto();
			}
		});

		MapMng = new GoogleMapManager(T.Map);
		MapMng.GoToLocation(VeneficaApplication.myLocation, 17);

		Popup = new PopupDialog();

		filterPanel = new FilterPanel(getParent(), null, T.filterLayout, filterCloseCallback);
		filterPanel.setFilter(MarketEx.getInstance().getFilter());
		T.HeaderTap.SetOnClickToFilter(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				filterPanel.show();
				HideKeyboard();
			}
		});

		T.HeaderTap.SetOnClickToRefresh(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String str = T.HeaderTap.GetSearch();
				MarketEx.getInstance().setFilterSearchString(str.length() > 0 ? str : null);
				UpdateProductList();
				HideKeyboard();
			}
		});
	}

	public void onResume()
	{
		super.onResume();
		TabMainActivity.ShowTabs();
		T.HeaderTap.SetSearch(MarketEx.getInstance().getFilter().searchString);

		MarketEx.getInstance().getFilter().latitude = VeneficaApplication.myLocation.getLatitude();
		MarketEx.getInstance().getFilter().longitude = VeneficaApplication.myLocation.getLongitude();
		
		if (MarketEx.getInstance().getFilter().wanted)
			OfferLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.marker_wanted));
		else
			OfferLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.marker));
		
		UpdateMyLocation();

		OfferMyLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.iam));
		OfferMyLocation.addOverlay(new OverlayItem(GeoLocation.LocToGeo(VeneficaApplication.myLocation), "My", "Snipet"));
		MapMng.AddOverlay(OfferMyLocation);

		if (MarketEx.getInstance().GetNumProduct() == 0)
		{
			UpdateProductList();
		}
		else
		{
			GenerateMap();
		}

	}

	private void UpdateProductList()
	{
		OfferLocation.Clear();
		
		if (MarketEx.getInstance().getFilter().wanted)
			OfferLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.marker_wanted));
		else
			OfferLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.marker));
		
		MarketEx.getInstance().UpdateProductsList(new UpdateProductsCallback()
		{
			public void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products)
			{
				GenerateMap();
			}
		});
	}

	/** Fill the card from the list of products */
	private void GenerateMap()
	{
		OfferLocation.Clear();
		for (Product item : MarketEx.getInstance().GetProducts())
		{
			OfferLocation.addOverlay(new OverlayItem(item.Location, "Title", "Snipet"));
			OfferLocation.SetPopup(Popup);
		}

		MapMng.AddOverlay(OfferLocation);
	}

	void GoToList()
	{
		final Intent intent = new Intent(this, BrowseListActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	void GoToPhoto()
	{
		final Intent intent = new Intent(this, BrowsePhotoActivity.class);
		((BrowseGroupActivity)getParent()).startActivityReplaceView(intent);
	}

	public void GoToPreview(long productId)
	{
		final Intent intent = new Intent(this, TabProductViewActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		startActivity(intent);
	}

	@Override
	public void onBackPressed()
	{
		if (getParent() instanceof BrowseGroupActivity)
		{
			BrowseGroupActivity parent = (BrowseGroupActivity)getParent();
			parent.onBackPressed();
		}
		return;
	}
}
