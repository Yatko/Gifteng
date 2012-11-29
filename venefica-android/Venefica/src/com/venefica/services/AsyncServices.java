package com.venefica.services;

//import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.module.user.UserDto;
import com.venefica.services.ServicesManager.*;
import com.venefica.utils.ImageAd;
import com.venefica.utils.VeneficaApplication;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.*;
import java.util.List;
@Deprecated
public class AsyncServices
{
	//- - - - - CALLBACK- - - - -//
	public enum CallbackReturn
	{
		/** Callback �� */
		Ok,
		/** Callback made a mistake (while not in use) */
		Error,
		/** Callback Requests the restart operation with the same parameters */
		RetryRequest
	}

	/** ����� ��������� Callback �������� */
	public static interface ICallback
	{
		/**
		 * Called when the asynchronous operation
		 * 
		 * @param result
		 *            - Contains the response from SOAP manager <i> (in its pure
		 *            form Ispolzketsya, should lead to the desired type with
		 *            Check for the possibility of casting)</i>
		 */
		public abstract CallbackReturn Callback(IResult<?> result);
	}

	//- - - - - CONTEXT - - - - -//
	/** Context with the asynchronous request */
	public static abstract class IContext
	{
		ICallback Callback;
	}

	public static class GetUserContext extends IContext
	{
		public GetUserContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class UpdateUserContext extends IContext
	{
		public UserDto user;

		public UpdateUserContext(UserDto user, ICallback Callback)
		{
			this.Callback = Callback;
			this.user = user;
		}
	}

	public static class IsUserCompleteContext extends IContext
	{
		public IsUserCompleteContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class RegisterUserContext extends IContext
	{
		UserDto user;
		String pass;

		public RegisterUserContext(UserDto user, String pass, ICallback Callback)
		{
			this.Callback = Callback;
			this.user = user;
			this.pass = pass;
		}
	}

	public static class AuthenticateContext extends IContext
	{
		String user;
		String pass;

		public AuthenticateContext(String user, String pass, ICallback Callback)
		{
			this.Callback = Callback;
			this.pass = pass;
			this.user = user;
		}
	}

	public static class PlaceAdContext extends IContext
	{
		/*PostData post;

		public PlaceAdContext(PostData post, ICallback Callback)
		{
			this.Callback = Callback;
			this.post = post;
		}*/
	}

	public static class GetAdsContext extends IContext
	{
		long lastAdId;
		long numberAds;
		FilterDto filter;

		public GetAdsContext(long lastAdId, long numberAds, FilterDto filter, ICallback Callback)
		{
			this.Callback = Callback;
			this.lastAdId = lastAdId;
			this.numberAds = numberAds;
			this.filter = filter;
		}
	}

	public static class AddImageToAdContext extends IContext
	{
		long adId;
		Bitmap image;

		public AddImageToAdContext(long adId, Bitmap image, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.image = image;
		}
	}

	public static class AddImagesToAdContext extends IContext
	{
		long adId;
		List<ImageAd> images;

		/**
		 * @param images
		 *            - present auto filter will be added only at the URLs that
		 *            are not already there, and bitmaps
		 */
		public AddImagesToAdContext(long adId, List<ImageAd> images, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.images = images;
		}
	}

	public static class GetCategoriesContext extends IContext
	{
		public GetCategoriesContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class UpdateAdContext extends IContext
	{
		/*PostData post;

		public UpdateAdContext(PostData post, ICallback Callback)
		{
			this.Callback = Callback;
			this.post = post;
		}*/
	}

	public static class GetAdByIdContext extends IContext
	{
		long adId;

		public GetAdByIdContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class ChangePasswordContext extends IContext
	{
		String oldPassword;
		String newPassword;

		public ChangePasswordContext(String oldPassword, String newPassword, ICallback Callback)
		{
			this.Callback = Callback;
			this.oldPassword = oldPassword;
			this.newPassword = newPassword;
		}
	}

	public static class BookmarkAdContext extends IContext
	{
		long adId;

		public BookmarkAdContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class RemoveBookmarkContext extends IContext
	{
		long adId;

		public RemoveBookmarkContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class GetBookmarkedAdsContext extends IContext
	{
		public GetBookmarkedAdsContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class GetConnectedSocialNetworksContext extends IContext
	{
		public GetConnectedSocialNetworksContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class DisconnectFromNetworkContext extends IContext
	{
		eSocialNetworks networkName;

		public DisconnectFromNetworkContext(eSocialNetworks networkName, ICallback Callback)
		{
			this.Callback = Callback;
			this.networkName = networkName;
		}
	}

	public static class ShareOnSocialNetworksContext extends IContext
	{
		String message;

		public ShareOnSocialNetworksContext(String message, ICallback Callback)
		{
			this.Callback = Callback;
			this.message = message;
		}
	}

	public static class MarkAsSpamContext extends IContext
	{
		long adId;

		public MarkAsSpamContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class UnmarkAsSpamContext extends IContext
	{
		long adId;

		public UnmarkAsSpamContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class GetUserByNameContext extends IContext
	{
		final String name;

		public GetUserByNameContext(final String name, ICallback Callback)
		{
			this.Callback = Callback;
			this.name = name;
		}
	}

	public static class EndAdContext extends IContext
	{
		long adId;

		public EndAdContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class DeleteAdContext extends IContext
	{
		long adId;

		public DeleteAdContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class RelistAdContext extends IContext
	{
		long adId;

		public RelistAdContext(long adId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
		}
	}

	public static class GetMyAdsContext extends IContext
	{
		public GetMyAdsContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class RateAdContext extends IContext
	{
		long adId;
		int ratingValue;

		public RateAdContext(long adId, int ratingValue, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.ratingValue = ratingValue;
		}
	}

	public static class DeleteMessageContext extends IContext
	{
		long messageId;

		public DeleteMessageContext(long messageId, ICallback Callback)
		{
			this.Callback = Callback;
			this.messageId = messageId;
		}
	}

	public static class UpdateCommentContext extends IContext
	{
		CommentDto comment;

		public UpdateCommentContext(CommentDto comment, ICallback Callback)
		{
			this.Callback = Callback;
			this.comment = comment;
		}
	}

	public static class GetCommentsByAdContext extends IContext
	{
		long adId;
		long lastCommentId;
		int numComments;

		public GetCommentsByAdContext(long adId, long lastCommentId, int numComments, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.lastCommentId = lastCommentId;
			this.numComments = numComments;
		}
	}

	public static class SendMessageToContext extends IContext
	{
		MessageDto message;

		public SendMessageToContext(MessageDto message, ICallback Callback)
		{
			this.Callback = Callback;
			this.message = message;
		}
	}

	public static class AddCommentToAdContext extends IContext
	{
		long adId;
		CommentDto comment;

		public AddCommentToAdContext(long adId, CommentDto comment, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.comment = comment;
		}
	}

	public static class HideMessageContext extends IContext
	{
		long messageId;

		public HideMessageContext(long messageId, ICallback Callback)
		{
			this.Callback = Callback;
			this.messageId = messageId;
		}
	}

	public static class GetAllMessagesContext extends IContext
	{
		public GetAllMessagesContext(ICallback Callback)
		{
			this.Callback = Callback;
		}
	}

	public static class UpdateMessageContext extends IContext
	{
		MessageDto message;

		public UpdateMessageContext(MessageDto message, ICallback Callback)
		{
			this.Callback = Callback;
			this.message = message;
		}
	}

	public static class DeleteImageFromAdContext extends IContext
	{
		long adId;
		long imageId;

		public DeleteImageFromAdContext(long adId, long imageId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.imageId = imageId;
		}
	}

	public static class DeleteImagesFromAdContext extends IContext
	{
		long adId;
		List<Long> imagesId;

		public DeleteImagesFromAdContext(long adId, List<Long> imagesId, ICallback Callback)
		{
			this.Callback = Callback;
			this.adId = adId;
			this.imagesId = imagesId;
		}
	}

	//- - - - - TASK - - - - -//
	protected abstract class ITask extends AsyncTask<Void, Void, IResult<?>>
	{
		protected IContext Context;

		public ITask(IContext Context)
		{
			this.Context = Context;
		}

		protected void onPostExecute(IResult<?> result)
		{
			CallbackReturn ret = CallbackReturn.Ok;
			try
			{
				if (Context.Callback != null)
					ret = Context.Callback.Callback(result);

				if (ret == null)
					return;

				if (ret == CallbackReturn.RetryRequest)
				{
					Log.d("ITask onPostExecute", "Callback return RetryRequest");
					ReLaunchTask();
				}

				if (ret == CallbackReturn.Error)
					Log.d("ITask onPostExecute", "Callback return Error");
			}
			catch (RuntimeException e)
			{
				throw new RuntimeException(e);
			}
			catch (Exception e)
			{
				Log.d("ITask onPostExecute" + " Exception:", e.getLocalizedMessage());
			}
		}

		protected void ReLaunchTask()
		{
			try
			{
				//Class<?> clazz = this.getClass();
				Constructor<?> ctor = this.getClass().getConstructor(AsyncServices.class, Context.getClass());
				ITask task = (ITask)ctor.newInstance(AsyncServices.this, Context);
				task.execute(new Void[0]);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	protected class GetUserTask extends ITask
	{
		public GetUserTask(GetUserContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetUser(VeneficaApplication.authToken);
		}
	}

	public class UpdateUserTask extends ITask
	{
		public UpdateUserTask(UpdateUserContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.UpdateUser(VeneficaApplication.authToken, ((UpdateUserContext)Context).user);
		}
	}

	public class IsUserCompleteTask extends ITask
	{
		public IsUserCompleteTask(IsUserCompleteContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.IsUserComplete(VeneficaApplication.authToken);
		}
	}

	public class RegisterUserTask extends ITask
	{
		public RegisterUserTask(RegisterUserContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.RegisterUser(((RegisterUserContext)Context).pass, ((RegisterUserContext)Context).user);
		}

	}

	public class AuthenticateTask extends ITask
	{
		public AuthenticateTask(AuthenticateContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.Authenticate(((AuthenticateContext)Context).user, ((AuthenticateContext)Context).pass);
		}
	}

	public class PlaceAdTask extends ITask
	{
		public PlaceAdTask(PlaceAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return null;//VeneficaApplication.services.PlaceAd(VeneficaApplication.authToken, ((PlaceAdContext)Context).post);
		}
	}

	public class GetAdsTask extends ITask
	{
		public GetAdsTask(GetAdsContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetAds(VeneficaApplication.authToken, ((GetAdsContext)Context).lastAdId, ((GetAdsContext)Context).numberAds, ((GetAdsContext)Context).filter);
		}
	}

	public class AddImageToAdTask extends ITask
	{
		public AddImageToAdTask(AddImageToAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.AddImageToAd(VeneficaApplication.authToken, ((AddImageToAdContext)Context).adId, ((AddImageToAdContext)Context).image);
		}
	}

	public class AddImagesToAdTask extends ITask
	{
		public AddImagesToAdTask(AddImagesToAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			AddImagesToAdContext con = (AddImagesToAdContext)Context;

			for (ImageAd it : con.images)
			{
				if (it.bitmap != null)
				{
					VeneficaApplication.services.AddImageToAd(VeneficaApplication.authToken, con.adId, it.bitmap);
				}
			}
			return new AddImageToAdResult();
		}
	}

	public class GetCategoriesTask extends ITask
	{
		public GetCategoriesTask(GetCategoriesContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetCategories(VeneficaApplication.authToken);
		}
	}

	public class UpdateAdTask extends ITask
	{
		public UpdateAdTask(UpdateAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return null;//VeneficaApplication.services.UpdateAd(VeneficaApplication.authToken, ((UpdateAdContext)Context).post);
		}
	}

	public class GetAdByIdTask extends ITask
	{
		public GetAdByIdTask(GetAdByIdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetAdById(VeneficaApplication.authToken, ((GetAdByIdContext)Context).adId);
		}
	}

	public class ChangePasswordTask extends ITask
	{
		public ChangePasswordTask(ChangePasswordContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.ChangePassword(VeneficaApplication.authToken, ((ChangePasswordContext)Context).oldPassword, ((ChangePasswordContext)Context).newPassword);
		}
	}

	public class BookmarkAdTask extends ITask
	{
		public BookmarkAdTask(BookmarkAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.BookmarkAd(VeneficaApplication.authToken, ((BookmarkAdContext)Context).adId);
		}
	}

	public class RemoveBookmarkTask extends ITask
	{
		public RemoveBookmarkTask(RemoveBookmarkContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.RemoveBookmark(VeneficaApplication.authToken, ((RemoveBookmarkContext)Context).adId);
		}
	}

	public class GetBookmarkedAdsTask extends ITask
	{
		public GetBookmarkedAdsTask(GetBookmarkedAdsContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetBookmarkedAds(VeneficaApplication.authToken);
		}
	}

	public class GetConnectedSocialNetworksTask extends ITask
	{
		public GetConnectedSocialNetworksTask(GetConnectedSocialNetworksContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetConnectedSocialNetworks(VeneficaApplication.authToken);
		}
	}

	public class DisconnectFromNetworkTask extends ITask
	{
		public DisconnectFromNetworkTask(DisconnectFromNetworkContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.DisconnectFromNetwork(VeneficaApplication.authToken, ((DisconnectFromNetworkContext)Context).networkName);
		}
	}

	public class ShareOnSocialNetworksTask extends ITask
	{
		public ShareOnSocialNetworksTask(ShareOnSocialNetworksContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.ShareOnSocialNetworks(VeneficaApplication.authToken, ((ShareOnSocialNetworksContext)Context).message);
		}
	}

	public class MarkAsSpamTask extends ITask
	{
		public MarkAsSpamTask(MarkAsSpamContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.MarkAsSpam(VeneficaApplication.authToken, ((MarkAsSpamContext)Context).adId);
		}
	}

	public class UnmarkAsSpamTask extends ITask
	{
		public UnmarkAsSpamTask(UnmarkAsSpamContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.UnmarkAsSpam(VeneficaApplication.authToken, ((UnmarkAsSpamContext)Context).adId);
		}
	}

	public class GetUserByNameTask extends ITask
	{
		public GetUserByNameTask(GetUserByNameContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetUserByName(VeneficaApplication.authToken, ((GetUserByNameContext)Context).name);
		}
	}

	public class EndAdTask extends ITask
	{
		public EndAdTask(EndAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.EndAd(VeneficaApplication.authToken, ((EndAdContext)Context).adId);
		}
	}

	public class DeleteAdTask extends ITask
	{
		public DeleteAdTask(DeleteAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.DeleteAd(VeneficaApplication.authToken, ((DeleteAdContext)Context).adId);
		}
	}

	public class RelistAdTask extends ITask
	{
		public RelistAdTask(RelistAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.RelistAd(VeneficaApplication.authToken, ((RelistAdContext)Context).adId);
		}
	}

	public class GetMyAdsTask extends ITask
	{
		public GetMyAdsTask(GetMyAdsContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetMyAds(VeneficaApplication.authToken);
		}
	}

	public class RateAdTask extends ITask
	{
		public RateAdTask(RateAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.RateAd(VeneficaApplication.authToken, ((RateAdContext)Context).adId, ((RateAdContext)Context).ratingValue);
		}
	}

	public class DeleteMessageTask extends ITask
	{
		public DeleteMessageTask(DeleteMessageContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.DeleteMessage(VeneficaApplication.authToken, ((DeleteMessageContext)Context).messageId);
		}
	}

	public class UpdateCommentTask extends ITask
	{
		public UpdateCommentTask(UpdateCommentContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.UpdateComment(VeneficaApplication.authToken, ((UpdateCommentContext)Context).comment);
		}
	}

	public class GetCommentsByAdTask extends ITask
	{
		public GetCommentsByAdTask(GetCommentsByAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetCommentsByAd(VeneficaApplication.authToken, ((GetCommentsByAdContext)Context).adId, ((GetCommentsByAdContext)Context).lastCommentId, ((GetCommentsByAdContext)Context).numComments);
		}
	}

	public class SendMessageToTask extends ITask
	{
		public SendMessageToTask(SendMessageToContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.SendMessageTo(VeneficaApplication.authToken, ((SendMessageToContext)Context).message);
		}
	}

	public class AddCommentToAdTask extends ITask
	{
		public AddCommentToAdTask(AddCommentToAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.AddCommentToAd(VeneficaApplication.authToken, ((AddCommentToAdContext)Context).adId, ((AddCommentToAdContext)Context).comment);
		}
	}

	public class HideMessageTask extends ITask
	{
		public HideMessageTask(HideMessageContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.HideMessage(VeneficaApplication.authToken, ((HideMessageContext)Context).messageId);
		}
	}

	public class GetAllMessagesTask extends ITask
	{
		public GetAllMessagesTask(GetAllMessagesContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.GetAllMessages(VeneficaApplication.authToken);
		}
	}

	public class UpdateMessageTask extends ITask
	{
		public UpdateMessageTask(UpdateMessageContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.UpdateMessage(VeneficaApplication.authToken, ((UpdateMessageContext)Context).message);
		}
	}

	public class DeleteImageFromAdTask extends ITask
	{
		public DeleteImageFromAdTask(DeleteImageFromAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			return VeneficaApplication.services.DeleteImageFromAd(VeneficaApplication.authToken, ((DeleteImageFromAdContext)Context).adId, ((DeleteImageFromAdContext)Context).imageId);
		}
	}

	public class DeleteImagesFromAdTask extends ITask
	{
		public DeleteImagesFromAdTask(DeleteImagesFromAdContext Context)
		{
			super(Context);
		}

		@Override
		protected IResult<?> doInBackground(Void... param)
		{
			DeleteImagesFromAdContext con = (DeleteImagesFromAdContext)Context;

			for (Long it : con.imagesId)
			{
				if (it.longValue() != 0)
					VeneficaApplication.services.DeleteImageFromAd(VeneficaApplication.authToken, con.adId, it.longValue());
			}

			return new DeleteImageFromAdResult();
		}
	}

	//- - - - - FUNCTION - - - - -//
	/** In Callback will type <b>GetUserResult</b> */
	public void GetUser(GetUserContext Context)
	{
		new GetUserTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>UpdateUserResult</b> */
	public void UpdateUser(UpdateUserContext Context)
	{
		new UpdateUserTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>IsUserCompleteResult</b> */
	public void IsUserComplete(IsUserCompleteContext Context)
	{
		new IsUserCompleteTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>RegisterUserResult</b> */
	public void RegisterUser(RegisterUserContext Context)
	{
		new RegisterUserTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>AuthenticateResult</b> */
	public void Authenticate(AuthenticateContext Context)
	{
		new AuthenticateTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>PlaceAdResult</b> */
	public void PlaceAd(PlaceAdContext Context)
	{
		new PlaceAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetAdsResult</b> */
	public void GetAds(GetAdsContext Context)
	{
		new GetAdsTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>AddImageToAdResult</b> */
	public void AddImageToAd(AddImageToAdContext Context)
	{
		new AddImageToAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>AddImageToAdResult</b> */
	public void AddImagesToAd(AddImagesToAdContext Context)
	{
		new AddImagesToAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetCategoriesResult</b> */
	public void GetCategories(GetCategoriesContext Context)
	{
		new GetCategoriesTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>UpdateAdResult</b> */
	public void UpdateAd(UpdateAdContext Context)
	{
		new UpdateAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetAdByIdResult</b> */
	public void GetAdById(GetAdByIdContext Context)
	{
		new GetAdByIdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>ChangePasswordResult</b> */
	public void ChangePassword(ChangePasswordContext Context)
	{
		new ChangePasswordTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>BookmarkAdResult</b> */
	public void BookmarkAd(BookmarkAdContext Context)
	{
		new BookmarkAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>RemoveBookmarkResult</b> */
	public void RemoveBookmark(RemoveBookmarkContext Context)
	{
		new RemoveBookmarkTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetBookmarkedAdsResult</b> */
	public void GetBookmarkedAds(GetBookmarkedAdsContext Context)
	{
		new GetBookmarkedAdsTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetConnectedSocialNetworksResult</b> */
	public void GetConnectedSocialNetworks(GetConnectedSocialNetworksContext Context)
	{
		new GetConnectedSocialNetworksTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetConnectedSocialNetworksResult</b> */
	public void DisconnectFromNetwork(DisconnectFromNetworkContext Context)
	{
		new DisconnectFromNetworkTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>ShareOnSocialNetworksResult</b> */
	public void ShareOnSocialNetworks(ShareOnSocialNetworksContext Context)
	{
		new ShareOnSocialNetworksTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>BookmarkAdResult</b> */
	public void MarkAsSpam(MarkAsSpamContext Context)
	{
		new MarkAsSpamTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>RemoveBookmarkResult</b> */
	public void UnmarkAsSpam(UnmarkAsSpamContext Context)
	{
		new UnmarkAsSpamTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetUserByNameResult</b> */
	public void GetUserByName(GetUserByNameContext Context)
	{
		new GetUserByNameTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>EndAdResult</b> */
	public void EndAd(EndAdContext Context)
	{
		new EndAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>DeleteAdResult</b> */
	public void DeleteAd(DeleteAdContext Context)
	{
		new DeleteAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>RelistAdResult</b> */
	public void RelistAd(RelistAdContext Context)
	{
		new RelistAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetMyAdsResult</b> */
	public void GetMyAds(GetMyAdsContext Context)
	{
		new GetMyAdsTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>RateAdResult</b> */
	public void RateAd(RateAdContext Context)
	{
		new RateAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>DeleteMessageResult</b> */
	public void DeleteMessage(DeleteMessageContext Context)
	{
		new DeleteMessageTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>UpdateCommentResult</b> */
	public void UpdateComment(UpdateCommentContext Context)
	{
		new UpdateCommentTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetCommentsByAdResult</b> */
	public void GetCommentsByAd(GetCommentsByAdContext Context)
	{
		new GetCommentsByAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>SendMessageToResult</b> */
	public void SendMessageTo(SendMessageToContext Context)
	{
		new SendMessageToTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>AddCommentToAdResult</b> */
	public void AddCommentToAd(AddCommentToAdContext Context)
	{
		new AddCommentToAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>HideMessageResult</b> */
	public void HideMessage(HideMessageContext Context)
	{
		new HideMessageTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>GetAllMessagesResult</b> */
	public void GetAllMessages(GetAllMessagesContext Context)
	{
		new GetAllMessagesTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>UpdateMessageResult</b> */
	public void UpdateMessage(UpdateMessageContext Context)
	{
		new UpdateMessageTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>DeleteImageFromAdResult</b> */
	public void DeleteImageFromAd(DeleteImageFromAdContext Context)
	{
		new DeleteImageFromAdTask(Context).execute(new Void[0]);
	}

	/** In Callback will type <b>DeleteImagesFromAdResult</b> */
	public void DeleteImagesFromAd(DeleteImagesFromAdContext Context)
	{
		new DeleteImagesFromAdTask(Context).execute(new Void[0]);
	}
}
