package com.venefica.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.venefica.activity.R;
import com.venefica.market.Category;
import com.venefica.market.MarketEx;
import com.venefica.market.Product;
import com.venefica.services.CommentDto;
import com.venefica.services.AsyncServices.AddCommentToAdContext;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetCommentsByAdContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetAdByIdResult;
import com.venefica.services.ServicesManager.GetCommentsByAdResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.skining.ProductMessagesSkinDef;
import com.venefica.skining.ProductMessagesTemplate;
import com.venefica.utils.CommentListAdapter;
import com.venefica.utils.Constants;
import com.venefica.utils.MyApp;

public class ProductMessagesActivity extends ActivityEx implements ICallback
{
	public static final String CONVERSATION_ID = "conversationId";

	ProductMessagesTemplate T;

	Product item;
	CommentListAdapter Adapter;
	boolean ViewMyMessage = false;
	long productId = 0;
	long conversationId = 0;
	long msgEditId;

	//animation
	Animation AnimationShowEdit;
	Animation AnimationHideEdit;
	Animation AnimationShowButton;
	Animation AnimationHideButton;

	private final ICallback getComment = new ICallback()
	{
		public CallbackReturn Callback(IResult<?> result)
		{
			HideLoadingDialog();

			GetCommentsByAdResult ret = (GetCommentsByAdResult)result;

			switch (ret.SoapResult)
			{
				case Ok:
				{
					Adapter = new CommentListAdapter(ProductMessagesActivity.this, ret.Return);
					UpdateUi();
					break;
				}

				case Fault:
					ShowInfoDialog(GetStringResource(R.string.get_comment_error), FinishRunnable);
					break;

				case SoapProblem:
					ShowInfoDialog(GetStringResource(R.string.soap_problem), FinishRunnable);
					break;
			}

			return CallbackReturn.Ok;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new ProductMessagesSkinDef(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			productId = extras.getLong(Constants.PRODUCT_ID_PARAM_NAME);
			conversationId = extras.getLong(CONVERSATION_ID);

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

		T.listMessages.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> adapter, View paramView, int position, long paramLong)
			{
				final CommentDto msg = (CommentDto)adapter.getItemAtPosition(position);
				if (msg.owner)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(ProductMessagesActivity.this);
					builder.setMessage(GetStringResource(R.string.edit_your_message)).setCancelable(false);
					builder.setPositiveButton(GetStringResource(R.string.yes), new OnClickListener()
					{
						public void onClick(DialogInterface paramDialogInterface, int paramInt)
						{
							GoToEditComment(msg.id, msg.text);
						}
					});

					builder.setNegativeButton(GetStringResource(R.string.no), new OnClickListener()
					{
						public void onClick(DialogInterface dialog, int paramInt)
						{
							dialog.dismiss();
						}
					});
					builder.create().show();
				}
				else
				{
					goToUserInfo(msg.publisherName);
				}
			}
		});

		//animation
		AnimationShowEdit = AnimationUtils.loadAnimation(this, R.anim.layout_message_show_animation);
		AnimationHideEdit = AnimationUtils.loadAnimation(this, R.anim.layout_message_hide_animation);
		AnimationShowButton = AnimationUtils.loadAnimation(this, R.anim.cross_button_show_animation);
		AnimationHideButton = AnimationUtils.loadAnimation(this, R.anim.cross_button_hide_animation);

		//button
		T.btnAddComment.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ViewMyMessage)
				{
					String message = T.editMessage.getText().toString();
					if (message.length() > 0)
					{
						CommentDto comment = new CommentDto();
						comment.text = message;

						ShowLoadingDialog();
						MyApp.AsyncServices.AddCommentToAd(new AddCommentToAdContext(item.Id, comment, new ICallback()
						{
							public CallbackReturn Callback(IResult<?> result)
							{
								HideLoadingDialog();

								switch (result.SoapResult)
								{
									case Ok:
										ChangeAddMessageView(false);
										T.editMessage.setText("");
										ShowLoadingDialog();
										MyApp.AsyncServices.GetCommentsByAd(new GetCommentsByAdContext(item.Id, -1, -1, getComment));
										break;

									case Fault:
									{
										Log.d("AddMessageToAd.Callback Warning", "SoapProblem");
										ShowInfoDialog(GetStringResource(R.string.add_comment_error));
										break;
									}

									case SoapProblem:
									{
										Log.d("AddMessageToAd.Callback Warning", "SoapProblem");
										ShowInfoDialog(GetStringResource(R.string.soap_problem));
										break;
									}
								}

								return CallbackReturn.Ok;
							}
						}));
					}
					else
					{
						ShowInfoDialog(GetStringResource(R.string.short_message));
					}
				}
				else
				{
					ChangeAddMessageView(true);
				}
			}
		});

		T.btnCloseEditMessage.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				ChangeAddMessageView(false);
			}
		});

		ChangeAddMessageView(false);

		AnimationHideEdit.setAnimationListener(new AnimationListener()
		{
			public void onAnimationStart(Animation animation)
			{

			}

			public void onAnimationRepeat(Animation animation)
			{

			}

			public void onAnimationEnd(Animation animation)
			{
				T.editMessage.setVisibility(View.GONE);
			}
		});

		AnimationHideButton.setAnimationListener(new AnimationListener()
		{
			public void onAnimationStart(Animation animation)
			{

			}

			public void onAnimationRepeat(Animation animation)
			{

			}

			public void onAnimationEnd(Animation animation)
			{
				T.btnCloseEditMessage.setVisibility(View.GONE);
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

	protected void UpdateUi()
	{
		if (item == null)
		{
			ShowInfoDialog("Error: item == null", FinishRunnable);
			Log.e("ProductMessagesActivity.onCreate", "item == null");
			return;
		}

		T.listMessages.setAdapter(Adapter);

		//header
		if (item.imageThumbnail != null && item.imageThumbnail.url != null)
		{
			MyApp.ImgLoader.displayImage(item.imageThumbnail.url, T.imgProduct, MyApp.ImgLoaderOptions);
		}

		T.lblTitle.setText(item.Title);
		T.lblCategory.setText(Category.GetCategoryById(item.Category).Desc);
		T.lblPrice.setText(item.Price);

		float dist = item.KMeters;
		String unit;
		if (MyApp.user.useMiles)
		{
			dist = dist * 0.621371192f; //мили
			unit = GetStringResource(R.string.miles);
		}
		else
		{
			unit = GetStringResource(R.string.km);
		}

		T.lblMiles.setText(String.format("%.2f", dist));
		T.lblUnitDistance.setText(unit);
	}

	/** Изменяет видимость текстовой панельки для добавления сообщения */
	protected void ChangeAddMessageView(boolean visible)
	{
		ViewMyMessage = visible;

		if (ViewMyMessage)
		{//visible
			T.editMessage.setVisibility(View.VISIBLE);
			T.btnCloseEditMessage.setVisibility(View.VISIBLE);

			T.editMessage.startAnimation(AnimationShowEdit);
			T.btnCloseEditMessage.startAnimation(AnimationShowButton);
		}
		else
		{//invisible
			T.editMessage.startAnimation(AnimationHideEdit);
			T.btnCloseEditMessage.startAnimation(AnimationHideButton);
			HideKeyboard();
		}
	}

	/** Переход к активити для редактирования сообщения */
	protected void GoToEditComment(long messageId, String Message)
	{
		msgEditId = messageId;

		Intent intent = new Intent(this, EditMessageActivity.class);
		intent.putExtra(EditMessageActivity.MESSAGE_ID, messageId);
		intent.putExtra(EditMessageActivity.MESSAGE_TEXT, Message);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != -1 || data == null)
		{
			return;
		}
		/** Смотрим что вернул активити для редактирования сообщения */
		long id = data.getLongExtra("id", -1);
		if (id == msgEditId)
		{
			UpdateUi();
		}
	}

	public CallbackReturn Callback(IResult<?> result)
	{
		if (result instanceof GetAdByIdResult)
		{
			GetAdByIdResult ret = (GetAdByIdResult)result;

			switch (ret.SoapResult)
			{
				case Ok:
				{
					item = ret.Return;
					MyApp.AsyncServices.GetCommentsByAd(new GetCommentsByAdContext(item.Id, -1, -1, getComment));
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

	private void goToUserInfo(final String name)
	{
		Intent intent = new Intent().setClass(this, UserInfoActivity.class);
		intent.putExtra(Constants.USER_NAME_PARAM_NAME, name);
		startActivity(intent);
	}
}
