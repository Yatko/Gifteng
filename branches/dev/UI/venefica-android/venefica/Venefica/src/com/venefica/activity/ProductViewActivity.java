package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.venefica.activity.R;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.BookmarkAdContext;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.DeleteAdContext;
import com.venefica.services.AsyncServices.EndAdContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.AsyncServices.MarkAsSpamContext;
import com.venefica.services.AsyncServices.RateAdContext;
import com.venefica.services.AsyncServices.RelistAdContext;
import com.venefica.services.AsyncServices.RemoveBookmarkContext;
import com.venefica.services.AsyncServices.ShareOnSocialNetworksContext;
import com.venefica.services.AsyncServices.UnmarkAsSpamContext;
import com.venefica.services.ServicesManager.BookmarkAdResult;
import com.venefica.services.ServicesManager.GetAdByIdResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.RateAdResult;
import com.venefica.services.ServicesManager.RemoveBookmarkResult;
import com.venefica.services.ServicesManager.ShareOnSocialNetworksResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.skining.*;
import com.venefica.utils.Constants;
import com.venefica.utils.ImageAd;
import com.venefica.utils.ImageAdapter;
import com.venefica.utils.VeneficaApplication;

public class ProductViewActivity extends ActivityEx implements ICallback
{
	private static final int SHARE_CHOOSE_DIALOG_ID = 0;

	ProductViewTemplate T;

	Product item;
	long productId;
	private final List<ImageAd> images = new ArrayList<ImageAd>();
	private ImageAdapter adapter;

	private final View.OnClickListener clickAddBookmark = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			ShowLoadingDialog();

			VeneficaApplication.asyncServices.BookmarkAd(new BookmarkAdContext(item.Id, new ICallback()
			{
				public CallbackReturn Callback(IResult<?> result)
				{
					HideLoadingDialog();

					BookmarkAdResult res = (BookmarkAdResult)result;

					switch (res.SoapResult)
					{
						case Ok:
							T.btnBookmark.setText(R.string.remove_bookmark);
							T.btnBookmark.setOnClickListener(clickRemoveBookmark);
							MarketEx.getInstance().ClearProductsList();
							ShowInfoDialog(GetStringResource(R.string.add_bookmark_successful));
							break;

						case Fault:
							ShowInfoDialog(GetStringResource(R.string.add_bookmark_error));
							break;

						case SoapProblem:
							ShowInfoDialog(GetStringResource(R.string.soap_problem));
							break;
					}

					return CallbackReturn.Ok;
				}
			}));
		}
	};

	private final View.OnClickListener clickRemoveBookmark = new View.OnClickListener()
	{
		public void onClick(View paramView)
		{
			ShowLoadingDialog();

			VeneficaApplication.asyncServices.RemoveBookmark(new RemoveBookmarkContext(item.Id, new ICallback()
			{
				public CallbackReturn Callback(IResult<?> result)
				{
					HideLoadingDialog();

					RemoveBookmarkResult res = (RemoveBookmarkResult)result;

					switch (res.SoapResult)
					{
						case Ok:
							T.btnBookmark.setText(R.string.add_bookmark);
							T.btnBookmark.setOnClickListener(clickAddBookmark);
							MarketEx.getInstance().ClearProductsList();
							ShowInfoDialog(GetStringResource(R.string.remove_bookmark_successful));
							break;

						case Fault:
							ShowInfoDialog(GetStringResource(R.string.remove_bookmark_error));
							break;

						case SoapProblem:
							ShowInfoDialog(GetStringResource(R.string.soap_problem));
							break;
					}

					return CallbackReturn.Ok;
				}
			}));
		}
	};

	private final View.OnClickListener clickSpam = new OnClickListener()
	{
		public void onClick(View paramView)
		{
			ShowQuestionDialog(GetStringResource(R.string.marked_spam_question), new Runnable()
			{
				public void run()
				{
					markSpam();
				}
			});
		}
	};

	private final View.OnClickListener clickUnSpam = new OnClickListener()
	{
		public void onClick(View paramView)
		{
			ShowQuestionDialog(GetStringResource(R.string.unmarked_spam_question), new Runnable()
			{
				public void run()
				{
					unMarkSpam();
				}
			});
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new ProductViewSkinDef(this);

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

		adapter = new ImageAdapter(this, images);
		T.galleryImage.setAdapter(adapter);

		T.btnEditAd.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				StartEditActivity();
			}
		});

		T.btnShare.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				showDialog(SHARE_CHOOSE_DIALOG_ID);
			}
		});

		T.btnEndAd.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				ShowQuestionDialog(GetStringResource(R.string.end_this_ad), new Runnable()
				{
					public void run()
					{
						endAd();
					}
				});
			}
		});

		T.btnDeleteAd.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				ShowQuestionDialog(GetStringResource(R.string.delete_this_ad), new Runnable()
				{
					public void run()
					{
						deleteAd();
					}
				});
			}
		});

		T.btnRelist.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				ShowQuestionDialog(GetStringResource(R.string.relist_this_ad_question), new Runnable()
				{
					public void run()
					{
						relistAd();
					}
				});
			}
		});

		T.btnRating.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				showRatingDialog();
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();

		ShowLoadingDialog();
		MarketEx.getInstance().GetProductById(productId, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != -1 || data == null)
		{
			return;
		}

		long id = data.getLongExtra("id", -1);
		if (id == item.Id)
		{
			UpdateUi();
		}
	}

	public void StartEditActivity()
	{
		Intent intent = new Intent(this, TabAdEditActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, item.Id);
		startActivityForResult(intent, 1);
	}

	private void UpdateUi()
	{
		if (item == null)
		{
			ShowInfoDialog("Error: item == null", FinishRunnable);
			Log.e("ProductViewActivity.onCreate", "item == null");
			return;
		}

		if (item.canMarkAsSpam)
		{			
			T.btnSpam.setText(GetStringResource(R.string.mark_spam));
			T.btnSpam.setOnClickListener(clickSpam);
		}
		else
		{
			T.btnSpam.setText(GetStringResource(R.string.unmark_spam));
			T.btnSpam.setOnClickListener(clickUnSpam);
		}

		if (item.expired)
		{
			T.lblAdExpired.setVisibility(View.VISIBLE);

			if (item.numAvailProlongations > 0)
				T.btnRelist.setVisibility(View.VISIBLE);
			else
				T.btnRelist.setVisibility(View.GONE);

			T.btnEndAd.setVisibility(View.GONE);
		}
		else
		{
			T.lblAdExpired.setVisibility(View.GONE);
			T.btnRelist.setVisibility(View.GONE);
			T.btnEndAd.setVisibility(View.VISIBLE);
		}

		if (item.inBookmars)
		{
			T.btnBookmark.setText(R.string.remove_bookmark);
			T.btnBookmark.setOnClickListener(clickRemoveBookmark);
		}
		else
		{
			T.btnBookmark.setText(R.string.add_bookmark);
			T.btnBookmark.setOnClickListener(clickAddBookmark);
		}

		if (item.canRate && item.owner == false)
			T.btnRating.setVisibility(View.VISIBLE);
		else
			T.btnRating.setVisibility(View.GONE);

		if (item.owner)
		{
			T.btnEditAd.setVisibility(View.VISIBLE);
			T.layoutTotalViews.setVisibility(View.VISIBLE);
			T.lblTotalViews.setText("" + item.numViews);
			T.btnSpam.setVisibility(View.GONE);
			T.layoutOtherButton.setVisibility(View.VISIBLE);
			T.btnOwner.setVisibility(View.GONE);
		}
		else
		{
			T.btnEditAd.setVisibility(View.GONE);
			T.layoutTotalViews.setVisibility(View.INVISIBLE);
			T.btnSpam.setVisibility(View.VISIBLE);
			T.layoutOtherButton.setVisibility(View.GONE);

			if (item.creator != null)
			{
				T.btnOwner.setVisibility(View.VISIBLE);
//				T.btnOwner.setText(GetStringResource(R.string.owner) + " " + item.creator.firstName + " " + item.creator.lastName);
				T.btnOwner.setText(GetStringResource(R.string.owner) + " " + item.creator.getFirstName() + " " + item.creator.getLastName());
				T.btnOwner.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
//						goToUserInfo(item.creator.name);
						goToUserInfo(item.creator.getName());
					}
				});
			}
			else
			{
				T.btnOwner.setVisibility(View.GONE);
			}
		}

		adapter.notifyDataSetChanged();

		T.lblRating.setText(String.format("%.2f", item.rating));
		T.lblTitle.setText(item.Title);
		T.lblPrice.setText(item.Price);

		float dist = item.KMeters;
		String unit;
		if (VeneficaApplication.user.isUseMiles())
		{
			dist = dist * 0.621371192f;
			unit = GetStringResource(R.string.miles);
		}
		else
		{
			unit = GetStringResource(R.string.km);
		}

		T.lblMiles.setText(String.format("%.2f", dist));
		T.lblStaticMiles.setText(unit);

		T.lblDesc.setText(item.Desc);
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

					images.clear();

					if (item.image != null && item.image.url != null)
						images.add(item.image);

					images.addAll(item.images);

					if (images.size() == 0)
					{
						Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
						images.add(new ImageAd(0, null, img));
					}

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

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case SHARE_CHOOSE_DIALOG_ID:
			{
				CharSequence[] ListItems = new CharSequence[2];
				ListItems[0] = GetStringResource(R.string.social_networks);
				ListItems[1] = GetStringResource(R.string.other);
				//ListItems[1] = GetStringResource(R.string.email);
				//ListItems[2] = GetStringResource(R.string.sms);

				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle(GetStringResource(R.string.share));
				dialog.setItems(ListItems, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								shareToSocialNetworks();
								break;
							case 1:
								//shareToEmail();
								shareToOther();
								break;
							/*case 2:
								shareToSms();
								break;*/
							default:
								break;
						}

						dialog.cancel();
					}
				});
				return dialog.create();
			}
		}
		return null;
	}

	private void shareToSocialNetworks()
	{
		ShowLoadingDialog();

		String message = item.Title + " " + item.Desc + " Price: " + item.Price;

		VeneficaApplication.asyncServices.ShareOnSocialNetworks(new ShareOnSocialNetworksContext(message, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				ShareOnSocialNetworksResult ret = (ShareOnSocialNetworksResult)result;

				switch (ret.SoapResult)
				{
					case Ok:
						ShowInfoDialog(GetStringResource(R.string.share_ad_successful));
						break;
					case Fault:
						ShowInfoDialog(GetStringResource(R.string.share_ad_error));
						break;

					case SoapProblem:
						ShowInfoDialog(GetStringResource(R.string.soap_problem));
						break;
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	@SuppressWarnings ("unused")
	private void shareToEmail()
	{
		String msg = item.Title + " " + item.Desc + " Price: " + item.Price;

		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_SUBJECT, GetStringResource(R.string.e_mail_subject));
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		startActivity(Intent.createChooser(intent, "Send mail..."));
	}

	@SuppressWarnings ("unused")
	private void shareToSms()
	{
		String msg = item.Title + " " + item.Desc + " Price: " + item.Price;

		try
		{
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", msg);
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
		}
		catch (Exception e)
		{

		}
	}

	private void shareToOther()
	{
		String msg = item.Title + " " + item.Desc + " Price: " + item.Price;

		try
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			/*intentt.setData(Uri.parse("sms:"));
			intentt.setType("vnd.android-dir/mms-sms");*/
			intent.putExtra(Intent.EXTRA_TEXT, msg);
			intent.putExtra("sms_body", msg);
			startActivity(Intent.createChooser(intent, ""));
		}
		catch (Exception e)
		{

		}
	}

	private void markSpam()
	{
		ShowLoadingDialog();
		VeneficaApplication.asyncServices.MarkAsSpam(new MarkAsSpamContext(productId, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult == SoapRequestResult.Ok)
				{
					ShowInfoDialog(GetStringResource(R.string.marked_spam_successful));
					T.btnSpam.setVisibility(View.GONE);
				}
				else
					ShowInfoDialog(GetStringResource(R.string.marked_spam_error));

				return CallbackReturn.Ok;
			}
		}));
	}

	private void unMarkSpam()
	{
		ShowLoadingDialog();
		VeneficaApplication.asyncServices.UnmarkAsSpam(new UnmarkAsSpamContext(productId, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult == SoapRequestResult.Ok)
				{
					ShowInfoDialog(GetStringResource(R.string.unmarked_spam_successful));
					T.btnSpam.setVisibility(View.GONE);
				}
				else
					ShowInfoDialog(GetStringResource(R.string.unmarked_spam_error));

				return CallbackReturn.Ok;
			}
		}));
	}

	private void endAd()
	{
		ShowLoadingDialog();

		VeneficaApplication.asyncServices.EndAd(new EndAdContext(productId, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult == SoapRequestResult.Ok)
				{
					MarketEx.getInstance().ClearProductsList();
					ShowInfoDialog(GetStringResource(R.string.end_ad_successful), FinishRunnable);
				}
				else
				{
					ShowInfoDialog(GetStringResource(R.string.end_ad_error));
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	private void deleteAd()
	{
		ShowLoadingDialog();

		VeneficaApplication.asyncServices.DeleteAd(new DeleteAdContext(productId, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult == SoapRequestResult.Ok)
				{
					MarketEx.getInstance().ClearProductsList();
					ShowInfoDialog(GetStringResource(R.string.delete_ad_successful), FinishRunnable);
				}
				else
				{
					ShowInfoDialog(GetStringResource(R.string.delete_ad_error));
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	private void goToUserInfo(final String name)
	{
		Intent intent = new Intent().setClass(this, UserInfoActivity.class);
		intent.putExtra(Constants.USER_NAME_PARAM_NAME, name);
		startActivity(intent);
	}

	private void relistAd()
	{
		ShowLoadingDialog();

		VeneficaApplication.asyncServices.RelistAd(new RelistAdContext(productId, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult == SoapRequestResult.Ok)
				{
					MarketEx.getInstance().ClearProductsList();
					ShowInfoDialog(GetStringResource(R.string.relist_ad_successful), FinishRunnable);
				}
				else
				{
					ShowInfoDialog(GetStringResource(R.string.relist_ad_error));
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	private void showRatingDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.rating_dialog, null);

		final TextView lblRating = (TextView)view.findViewById(R.id.lblRating);
		final Button btnRatingUp = (Button)view.findViewById(R.id.btnRatingUp);
		final Button btnRatingDown = (Button)view.findViewById(R.id.btnRatingDown);

		builder.setView(view);
		builder.setTitle(GetStringResource(R.string.rating_));
		builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dlg, int sumthin)
			{

			}
		});

		final AlertDialog dialog = builder.create();

		lblRating.setText(String.format("%.2f", item.rating));

		btnRatingUp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();

				ShowLoadingDialog();
				VeneficaApplication.asyncServices.RateAd(new RateAdContext(item.Id, 1, new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();

						RateAdResult ret = (RateAdResult)result;

						if (ret.SoapResult == SoapRequestResult.Ok)
						{
							item.rating = ret.Return.floatValue();
							T.btnRating.setVisibility(View.GONE);
							T.lblRating.setText(String.format("%.2f", item.rating));
						}
						else
						{
							ShowInfoDialog(GetStringResource(R.string.rating_change_error));
						}

						return CallbackReturn.Ok;
					}
				}));
			}
		});

		btnRatingDown.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				dialog.dismiss();

				ShowLoadingDialog();
				VeneficaApplication.asyncServices.RateAd(new RateAdContext(item.Id, -1, new ICallback()
				{
					public CallbackReturn Callback(IResult<?> result)
					{
						HideLoadingDialog();

						RateAdResult ret = (RateAdResult)result;

						if (ret.SoapResult == SoapRequestResult.Ok)
						{
							item.rating = ret.Return.floatValue();
							T.btnRating.setVisibility(View.GONE);
							T.lblRating.setText(String.format("%.2f", item.rating));
						}
						else
						{
							ShowInfoDialog(GetStringResource(R.string.rating_change_error));
						}

						return CallbackReturn.Ok;
					}
				}));
			}
		});

		dialog.show();
	}
}
