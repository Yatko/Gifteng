package com.venefica.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.venefica.activity.R;
import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.market.MarketEx;
import com.venefica.services.ServicesManager;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.PlaceAdContext;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.PlaceAdResult;
import com.venefica.skining.*;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.VeneficaApplication;

public class PostPreviewActivity extends ActivityEx
{
	PostPreviewTemplate T;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new PostPreviewSkinDef(this);

		PostData Post = App.post;
		if (Post != null)
		{
			T.btnPost.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View paramView)
				{
					Posted();
				}
			});

			if (Post.ImageShow != null)
				T.imgPostPreview.setImageBitmap(Post.ImageShow);

			T.lblTitle.setText(Post.Title);
			T.lblPrice.setText(Post.Price);

			//�������� �����
			float dist = GeoLocation.GeoToLoc(Post.GeoLocation).distanceTo(VeneficaApplication.myLocation);
			String unit;
			if (VeneficaApplication.user.isUseMiles())
			{
				dist = dist * 0.000621371192f; //����
				unit = GetStringResource(R.string.miles);
			}
			else
			{
				dist = dist / 1000.0f; //��
				unit = GetStringResource(R.string.km);
			}

			T.lblMiles.setText(String.format("%.2f", dist));
			T.lblStaticMiles.setText(unit);

			T.lblDesc.setText(Post.Desc);

			T.imgMapPreview.setImageBitmap(Post.MapPreview);
		}
		else
		{
			GoToBrowse();
		}
	}

	public void Posted()
	{
		ShowLoadingDialog();

		VeneficaApplication.asyncServices.PlaceAd(new PlaceAdContext(App.post, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result instanceof PlaceAdResult)
				{
					PlaceAdResult res = (PlaceAdResult)result;

					if (res.Return != ServicesManager.BAD_AD_ID)
					{
						//If all ok bunches then scrape post
						App.post = null;
						//and along with the list of products to re progruzitsya
						MarketEx.getInstance().ClearProductsList();

						ShowInfoDialog(GetStringResource(R.string.place_ad_successful), new Runnable()
						{
							public void run()
							{
								GoToBrowse();
							}
						});
					}
					else
					{
						switch (res.SoapResult)
						{
							case SoapProblem:
								ShowInfoDialog(GetStringResource(R.string.soap_problem));
								break;
							case Fault:
								ShowInfoDialog(GetStringResource(R.string.place_ad_error));
								break;
							case Ok:
								ShowInfoDialog(GetStringResource(R.string.place_ad_error));
								Log.d("Posted Info!", "BAD_AD_ID && SoapResult.Ok");
								break;
						}
					}
					return CallbackReturn.Ok;
				}
				else
				{
					return CallbackReturn.Error;
				}
			}
		}));
	}

	public void GoToBrowse()
	{
		//Intent intent = new Intent(this, TabMainActivity.class);
		//startActivity(intent);
		TabMainActivity.sTab.GoToPrevious();
		finish();
	}
}
