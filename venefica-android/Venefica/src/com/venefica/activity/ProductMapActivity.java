package com.venefica.activity;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetAdByIdResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.skining.*;
import com.venefica.utils.Constants;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.GoogleMapManager;
import com.venefica.utils.VeneficaApplication;

public class ProductMapActivity extends MapActivityEx implements ICallback
{
	private ProductMapSkinDef T;

	private OfferPinPoint OfferLocation;
	private OfferPinPoint OfferMyLocation;
	private GoogleMapManager MapManager;
	private Product item;
	long productId;

	public class OfferPinPoint extends ItemizedOverlay<OverlayItem>
	{
		private ArrayList<OverlayItem> mOverlaysItems = new ArrayList<OverlayItem>();

		public OfferPinPoint(Drawable defaultMarker)
		{
			super(boundCenterBottom(defaultMarker));
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

		public void Clear()
		{
			mOverlaysItems.clear();
			populate();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new ProductMapSkinDef(this);

		MapManager = new GoogleMapManager(T.MyMapView);

		if (OfferLocation == null)
		{
			OfferLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.marker));
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			productId = extras.getLong(Constants.PRODUCT_ID_PARAM_NAME);

			if (productId == 0)
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

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();

		UpdateMyLocation();

		OfferMyLocation = new OfferPinPoint(getResources().getDrawable(R.drawable.iam));
		OfferMyLocation.addOverlay(new OverlayItem(GeoLocation.LocToGeo(VeneficaApplication.myLocation), "My", "Snipet"));
		MapManager.AddOverlay(OfferMyLocation);

		ShowLoadingDialog();
		MarketEx.getInstance().GetProductById(productId, this);
	}

	private void UpdateUi()
	{
		MapManager.GoToLocation(item.Location, 18);

		OfferLocation.Clear();
		OfferLocation.addOverlay(new OverlayItem(item.Location, "Title", "Snipet"));

		MapManager.AddOverlay(OfferLocation);
	}

	public CallbackReturn Callback(IResult<?> result)
	{
		if (result instanceof GetAdByIdResult)
		{
			HideLoadingDialog();

			GetAdByIdResult ret = (GetAdByIdResult)result;

			switch (ret.SoapResult)
			{
				case Ok:
				{
					item = ret.Return;
					UpdateUi();
					break;
				}

				case Fault:
					ShowInfoDialog(GetStringResource(R.string.get_ad_error), FinishRunnable);
					break;

				case SoapProblem:
					ShowInfoDialog(GetStringResource(R.string.soap_problem), FinishRunnable);
					break;
			}
		}

		return CallbackReturn.Ok;
	}
}
