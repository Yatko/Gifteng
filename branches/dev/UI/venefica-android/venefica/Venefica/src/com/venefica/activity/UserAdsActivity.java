package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.venefica.activity.R;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetMyAdsContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetMyAdsResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.UserAdsSkinDef;
import com.venefica.skining.UserAdsTemplate;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;
import com.venefica.utils.ProductListAdapter;

public class UserAdsActivity extends ActivityEx
{
	private UserAdsTemplate T;
	private ProductListAdapter activeAdAdapter;
	private ProductListAdapter expiredAdAdapter;
	private boolean showExpired;

	private final ICallback getMyAdsCallback = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			HideLoadingDialog();

			GetMyAdsResult ret = (GetMyAdsResult)result;

			if (ret.SoapResult == SoapRequestResult.Ok)
			{
				final List<Product> productsActive = new ArrayList<Product>();
				final List<Product> productsExpired = new ArrayList<Product>();

				for (Product it : ret.Return)
				{
					if (it.expired)
						productsExpired.add(it);
					else
						productsActive.add(it);
				}

				activeAdAdapter = new ProductListAdapter(UserAdsActivity.this, R.layout.product_row, productsActive);
				expiredAdAdapter = new ProductListAdapter(UserAdsActivity.this, R.layout.product_row, productsExpired);

				updateUi();
			}
			else
			{
				ShowInfoDialog(GetStringResource(R.string.get_my_ads_error), FinishRunnable);
			}

			return CallbackReturn.Ok;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new UserAdsSkinDef(this);
		T.btnActive.setSelected(true);
		T.btnExpired.setSelected(false);
		showExpired = false;

		T.btnActive.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				T.btnActive.setSelected(true);
				T.btnExpired.setSelected(false);
				showExpired = false;
				updateUi();
			}
		});

		T.btnExpired.setOnClickListener(new OnClickListener()
		{
			public void onClick(View paramView)
			{
				T.btnActive.setSelected(false);
				T.btnExpired.setSelected(true);
				showExpired = true;
				updateUi();
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();

		ShowLoadingDialog();

		VeneficaApplication.asyncServices.GetMyAds(new GetMyAdsContext(getMyAdsCallback));
	}

	private void updateUi()
	{
		if (showExpired)
			T.listItem.setAdapter(expiredAdAdapter);
		else
			T.listItem.setAdapter(activeAdAdapter);
		
		T.listItem.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				Product item = (Product)arg0.getItemAtPosition(position);
				if (item != null)
				{
					goToPreview(item.Id);
				}
			}
		});
		
		expiredAdAdapter.notifyDataSetChanged();
		activeAdAdapter.notifyDataSetChanged();
	}

	public void goToPreview(long productId)
	{
		final Intent intent = new Intent(this, TabProductViewActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		startActivity(intent);
	}

}
