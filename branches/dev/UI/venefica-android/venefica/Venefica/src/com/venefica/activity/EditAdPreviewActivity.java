package com.venefica.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.venefica.activity.R;
import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.AddImagesToAdContext;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.DeleteImagesFromAdContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.UpdateAdContext;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.UpdateAdResult;
import com.venefica.skining.EditAdPreviewSkinDef;
import com.venefica.skining.EditAdPreviewTemplate;
import com.venefica.utils.BitmapUtil;
import com.venefica.utils.Constants;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.ImageAd;
import com.venefica.utils.ImageAdapter;
import com.venefica.utils.VeneficaApplication;

public class EditAdPreviewActivity extends ActivityEx
{
	EditAdPreviewTemplate T;
	private ImageAdapter adapter;
	private Product item;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new EditAdPreviewSkinDef(this);

		item = TabAdEditActivity.Item;
		adapter = new ImageAdapter(this, TabAdEditActivity.images);
		T.galleryImage.setAdapter(adapter);

		T.btnPost.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				Posted();
			}
		});
	}

	private final ICallback addImagesCallback = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			VeneficaApplication.asyncServices.DeleteImagesFromAd(new DeleteImagesFromAdContext(item.Id, TabAdEditActivity.imagesForDelete, deleteImagesCallback));
			return CallbackReturn.Ok;
		}
	};

	private final ICallback deleteImagesCallback = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			HideLoadingDialog();

			ShowInfoDialog(GetStringResource(R.string.update_ad_successful), TabAdEditActivity.sTab.FinishResultRunnable);
			return CallbackReturn.Ok;
		}
	};

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();

		adapter.notifyDataSetChanged();

		Product Post = TabAdEditActivity.Item;
		if (Post != null)
		{
			T.lblTitle.setText(Post.Title);
			T.lblPrice.setText(Post.Price);

			float dist = GeoLocation.GeoToLoc(Post.Location).distanceTo(VeneficaApplication.myLocation);
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

			T.imgMapPreview.setImageBitmap(TabAdEditActivity.MapPreview);
		}
	}

	public void Posted()
	{
		ShowLoadingDialog();

		final PostData Post = new PostData(TabAdEditActivity.Item);

		if (TabAdEditActivity.images.size() > 0)
		{
			ImageAd img = TabAdEditActivity.images.get(0);

			if (img.bitmap != null)
			{
				Post.ImageShow = img.bitmap;
			}
			else
			{
				File f = ImageLoader.getInstance().getDiscCache().get(img.url);

				try
				{
					if (f != null)
						Post.ImageShow = BitmapUtil.DecodeImagef(BitmapFactory.decodeStream(new FileInputStream(f)), Constants.IMAGE_MAX_SIZE);
				}
				catch (FileNotFoundException e)
				{
					Log.e("EditAdPreviewActivity FileNotFoundException", e.getLocalizedMessage());
				}
				catch (Exception e)
				{
					Log.e("EditAdPreviewActivity Exception", e.getLocalizedMessage());
				}
				
				TabAdEditActivity.imagesForDelete.add(Long.valueOf(img.id));
			}

			TabAdEditActivity.images.remove(0);
		}

		VeneficaApplication.asyncServices.UpdateAd(new UpdateAdContext(Post, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				if (result instanceof UpdateAdResult)
				{
					UpdateAdResult res = (UpdateAdResult)result;

					switch (res.SoapResult)
					{
						case Ok:
							TabAdEditActivity.IsUpdate = true;
							MarketEx.getInstance().ClearProductsList();

							VeneficaApplication.asyncServices.AddImagesToAd(new AddImagesToAdContext(item.Id, TabAdEditActivity.images, addImagesCallback));
							break;
						case SoapProblem:
							ShowInfoDialog(GetStringResource(R.string.soap_problem));
							break;
						case Fault:
							ShowInfoDialog(GetStringResource(R.string.update_ad_error));
							break;
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
}
