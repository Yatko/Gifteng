package com.venefica.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.util.Log;

//import com.venefica.activity.PostStepLogic.PostData;

import com.venefica.module.user.UserDto;
import com.venefica.utils.Constants;
import com.venefica.utils.Utils;

public class ServicesManager
{
	private static final String GET_USER_METHOD = "GetUser";
	private static final String UPDATE_USER_METHOD = "UpdateUser";
	private static final String IS_USER_COMPLETE_METHOD = "IsUserComplete";
	private static final String REGISTE_USER_METHOD = "RegisterUser";
	private static final String AUTHENTICATE_METHOD = "Authenticate";
	private static final String PLACE_AD_METHOD = "PlaceAd";
	private static final String GET_ADS_METHOD = "GetAdsEx";
	private static final String ADD_IMAGE_TO_AD_METHOD = "AddImageToAd";
	private static final String GET_ALL_CATEGORIES_METHOD = "GetAllCategories";
	private static final String UPDATE_AD_METHOD = "UpdateAd";
	private static final String GET_AD_BY_ID_METHOD = "GetAdById";
	private static final String CHANGES_PASSWORD_METHOD = "ChangePassword";
	private static final String BOOKMARK_AD_METHOD = "BookmarkAd";
	private static final String REMOVE_BOOKMARK_METHOD = "RemoveBookmark";
	private static final String GET_BOOKMARKED_ADS_METHOD = "GetBookmarkedAds";
	private static final String GET_CONNECTED_SOCIAL_NETWORKS_METHOD = "GetConnectedSocialNetworks";
	private static final String GET_DISCONNECT_FROM_NETWORKS_METHOD = "DisconnectFromNetwork";
	private static final String SHARE_ON_SOCIAL_NETWORKS_METHOD = "ShareOnSocialNetworks";
	private static final String MARK_AS_SPAM_METHOD = "MarkAsSpam";
	private static final String UNMARK_AS_SPAM_METHOD = "UnmarkAsSpam";
	private static final String GET_USER_BY_NAME_METHOD = "GetUserByName";
	private static final String END_AD_METHOD = "EndAd";
	private static final String DELETE_AD_METHOD = "DeleteAd";
	private static final String RELIST_AD_METHOD = "RelistAd";
	private static final String GET_MY_ADS_METHOD = "GetMyAds";
	private static final String RATE_AD_METHOD = "RateAd";
	private static final String DELETE_MESSAGE_METHOD = "DeleteMessage";
	private static final String UPDATE_COMMENT_METHOD = "UpdateComment";
	private static final String GET_COMMENTS_BY_AD_METHOD = "GetCommentsByAd";
	private static final String SEND_MESSAGE_METHOD = "SendMessage";
	private static final String ADD_COMMENT_TO_AD_METHOD = "AddCommentToAd";
	private static final String HIDE_MESSAGE_METHOD = "HideMessage";
	private static final String GET_ALL_MESSAGE_METHOD = "GetAllMessages";
	private static final String UPDATE_MESSAGE_METHOD = "UpdateMessage";
	private static final String DELETE_IMAGE_FROM_AD_METHOD = "DeleteImageFromAd";

	public static final int BAD_AUTH_TOKEN = -1;
	public static final long BAD_AD_ID = Long.MIN_VALUE;
	public static final long BAD_IMAGE_ID = Long.MIN_VALUE;


	public static String GetStringFromProperty(Object property, final String DefaultValue)
	{
		String result = DefaultValue;
		try
		{
			if (property.getClass() == SoapPrimitive.class)
				result = property.toString();
		}
		catch (Exception e)
		{
			Log.d("GetStringFromProperty Exception:", "");
		}

		return result;
	}

	public enum SoapRequestResult
	{
		Ok, Fault, SoapProblem
	}

	public enum RegisterUserReturn
	{
		Ok, DupeEmail, DupePhone, DupeLogin, Error
	}

	public enum UpdateUserReturn
	{
		Ok, DupeEmail, DupePhone, DupeLogin, Error
	}

	public enum eSocialNetworks
	{
		facebook, twitter, vkontakte
	}

	//- - - - - RESULT - - - - -//
	public static abstract class IResult<ReturnType>
	{
		public SoapRequestResult SoapResult;
		public ReturnType Return;

		public IResult()
		{
			SoapResult = SoapRequestResult.Ok;
			Return = null;
		}

		public IResult(SoapRequestResult SoapResult, ReturnType Return)
		{
			Set(SoapResult, Return);
		}

		public void Set(SoapRequestResult SoapResult, ReturnType Return)
		{
			this.SoapResult = SoapResult;
			this.Return = Return;
		}
	}

	public static class GetUserResult extends IResult<User>
	{
		public GetUserResult(SoapRequestResult SoapResult, User Return)
		{
			super(SoapResult, Return);
		}

		public GetUserResult()
		{

		}
	}

	public static class UpdateUserResult extends IResult<UpdateUserReturn>
	{
		public UpdateUserResult(SoapRequestResult SoapResult, UpdateUserReturn Return)
		{
			super(SoapResult, Return);
		}

		public UpdateUserResult()
		{

		}
	}

	public static class IsUserCompleteResult extends IResult<Boolean>
	{
		public IsUserCompleteResult(SoapRequestResult SoapResult, Boolean Return)
		{
			super(SoapResult, Return);
		}

		public IsUserCompleteResult()
		{

		}
	}

	public static class RegisterUserResult extends IResult<RegisterUserReturn>
	{
		public RegisterUserResult(SoapRequestResult SoapResult, RegisterUserReturn Return)
		{
			super(SoapResult, Return);
		}

		public RegisterUserResult()
		{

		}

	}

	public static class AuthenticateResult extends IResult<String>
	{
		public AuthenticateResult(SoapRequestResult SoapResult, String Return)
		{
			super(SoapResult, Return);
		}

		public AuthenticateResult()
		{

		}

	}

	public static class PlaceAdResult extends IResult<Long>
	{
		public PlaceAdResult(SoapRequestResult SoapResult, Long Return)
		{
			super(SoapResult, Return);
		}

		public PlaceAdResult()
		{

		}

	}

	/*public static class GetAdsResult extends IResult<List<Product>>
	{
		public GetAdsResult(SoapRequestResult SoapResult, List<Product> Return)
		{
			super(SoapResult, Return);
		}

		public GetAdsResult()
		{

		}

	}*/

	public static class AddImageToAdResult extends IResult<Long>
	{
		public AddImageToAdResult(SoapRequestResult SoapResult, Long Return)
		{
			super(SoapResult, Return);
		}

		public AddImageToAdResult()
		{

		}

	}

	public static class GetCategoriesResult extends IResult<Boolean>
	{
		public GetCategoriesResult(SoapRequestResult SoapResult, Boolean Return)
		{
			super(SoapResult, Return);
		}

		public GetCategoriesResult()
		{

		}

	}

	public static class UpdateAdResult extends IResult<Void>
	{
		public UpdateAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public UpdateAdResult()
		{

		}
	}

/*	public static class GetAdByIdResult extends IResult<Product>
	{
		public GetAdByIdResult(SoapRequestResult SoapResult, Product Return)
		{
			super(SoapResult, Return);
		}

		public GetAdByIdResult()
		{

		}
	}*/

	public static class ChangePasswordResult extends IResult<Void>
	{
		public ChangePasswordResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public ChangePasswordResult()
		{

		}
	}

	public static class BookmarkAdResult extends IResult<Long>
	{
		public BookmarkAdResult(SoapRequestResult SoapResult, Long Return)
		{
			super(SoapResult, Return);
		}

		public BookmarkAdResult()
		{

		}
	}

	public static class RemoveBookmarkResult extends IResult<Void>
	{
		public RemoveBookmarkResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public RemoveBookmarkResult()
		{

		}
	}

/*	public static class GetBookmarkedAdsResult extends IResult<List<Product>>
	{
		public GetBookmarkedAdsResult(SoapRequestResult SoapResult, List<Product> Return)
		{
			super(SoapResult, Return);
		}

		public GetBookmarkedAdsResult()
		{

		}

	}*/

	public static class GetConnectedSocialNetworksResult extends IResult<List<eSocialNetworks>>
	{
		public GetConnectedSocialNetworksResult(SoapRequestResult SoapResult, List<eSocialNetworks> Return)
		{
			super(SoapResult, Return);
		}

		public GetConnectedSocialNetworksResult()
		{

		}
	}

	public static class DisconnectFromNetworkResult extends IResult<Void>
	{
		public DisconnectFromNetworkResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public DisconnectFromNetworkResult()
		{

		}
	}

	public static class ShareOnSocialNetworksResult extends IResult<Void>
	{
		public ShareOnSocialNetworksResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public ShareOnSocialNetworksResult()
		{

		}
	}

	public static class MarkAsSpamResult extends IResult<Void>
	{
		public MarkAsSpamResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public MarkAsSpamResult()
		{

		}
	}

	public static class UnmarkAsSpamResult extends IResult<Void>
	{
		public UnmarkAsSpamResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public UnmarkAsSpamResult()
		{

		}
	}

	public static class GetUserByNameResult extends IResult<User>
	{
		public GetUserByNameResult(SoapRequestResult SoapResult, User Return)
		{
			super(SoapResult, Return);
		}

		public GetUserByNameResult()
		{

		}
	}

	public static class EndAdResult extends IResult<Void>
	{
		public EndAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public EndAdResult()
		{

		}
	}

	public static class DeleteAdResult extends IResult<Void>
	{
		public DeleteAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public DeleteAdResult()
		{

		}
	}

	public static class RelistAdResult extends IResult<Void>
	{
		public RelistAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public RelistAdResult()
		{

		}
	}

/*	public static class GetMyAdsResult extends IResult<List<Product>>
	{
		public GetMyAdsResult(SoapRequestResult SoapResult, List<Product> Return)
		{
			super(SoapResult, Return);
		}

		public GetMyAdsResult()
		{

		}
	}*/

	public static class RateAdResult extends IResult<Float>
	{
		public RateAdResult(SoapRequestResult SoapResult, Float Return)
		{
			super(SoapResult, Return);
		}

		public RateAdResult()
		{

		}
	}

	public static class DeleteMessageResult extends IResult<Void>
	{
		public DeleteMessageResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public DeleteMessageResult()
		{

		}
	}
	
	public static class UpdateCommentResult extends IResult<Void>
	{
		public UpdateCommentResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public UpdateCommentResult()
		{

		}
	}
	
	public static class SendMessageToResult extends IResult<Void>
	{
		public SendMessageToResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public SendMessageToResult()
		{

		}
	}
	
	public static class AddCommentToAdResult extends IResult<Void>
	{
		public AddCommentToAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public AddCommentToAdResult()
		{

		}
	}
	
	public static class HideMessageResult extends IResult<Void>
	{
		public HideMessageResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public HideMessageResult()
		{

		}
	}
	
	public static class GetCommentsByAdResult extends IResult<List<CommentDto>>
	{
		public GetCommentsByAdResult(SoapRequestResult SoapResult, List<CommentDto> Return)
		{
			super(SoapResult, Return);
		}

		public GetCommentsByAdResult()
		{

		}
	}
	
	public static class GetAllMessagesResult extends IResult<List<MessageDto>>
	{
		public GetAllMessagesResult(SoapRequestResult SoapResult, List<MessageDto> Return)
		{
			super(SoapResult, Return);
		}

		public GetAllMessagesResult()
		{

		}
	}

	public static class UpdateMessageResult extends IResult<Void>
	{
		public UpdateMessageResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public UpdateMessageResult()
		{

		}
	}
	
	public static class DeleteImageFromAdResult extends IResult<Void>
	{
		public DeleteImageFromAdResult(SoapRequestResult SoapResult, Void Return)
		{
			super(SoapResult, Return);
		}

		public DeleteImageFromAdResult()
		{

		}
	}
	

	//- - - - - FUNCTION - - - - -//
	public GetUserResult GetUser(String token)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_USER_METHOD;
		GetUserResult result = new GetUserResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_USER_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			if (envelope.getResponse() instanceof UserDto)
			{
				result.Return = new User((UserDto)envelope.getResponse());
				Log.d("GetUser Info!", "Ok");
			}
			else
			{
				Log.d("GetUser Warning!", "userDto not found");
				result = new GetUserResult(SoapRequestResult.SoapProblem, null);
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetUser Alert!", StatusSoap);
			result = new GetUserResult(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetUser Alert!", StatusSoap);
			result = new GetUserResult(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetUser Alert!", StatusSoap);
			result = new GetUserResult(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetUser Alert!", StatusSoap);
			result = new GetUserResult(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public UpdateUserResult UpdateUser(String token, UserDto user)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + UPDATE_USER_METHOD;
		UpdateUserResult result = new UpdateUserResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, UPDATE_USER_METHOD);

			request.addProperty("user", user);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);
			new ImageDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			boolean res = Boolean.parseBoolean(GetStringFromProperty(obj, "false"));
			if (res)
			{
				Log.d("UpdateUser Info!", "Ok");
				result.Set(SoapRequestResult.Ok, UpdateUserReturn.Ok);
			}
			else
			{
				Log.d("UpdateUser Alert!", "Return false");
				result.Set(SoapRequestResult.Ok, UpdateUserReturn.Error);
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("UpdateUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, UpdateUserReturn.Error);

			String message = e.getMessage();
			if (message.equalsIgnoreCase("EMAIL"))
			{
				result.Return = UpdateUserReturn.DupeEmail;
			}
			else if (message.equalsIgnoreCase("PHONE"))
			{
				result.Return = UpdateUserReturn.DupePhone;
			}
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("UpdateUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, UpdateUserReturn.Error);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("UpdateUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, UpdateUserReturn.Error);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("UpdateUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, UpdateUserReturn.Error);
		}

		return result;
	}
	@Deprecated
	public IsUserCompleteResult IsUserComplete(String token)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + IS_USER_COMPLETE_METHOD;
		IsUserCompleteResult result = new IsUserCompleteResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, IS_USER_COMPLETE_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			result.Return = Boolean.parseBoolean(GetStringFromProperty(obj, "false"));

			Log.d("isUserComplete Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("isUserComplete Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, false);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("isUserComplete Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("isUserComplete Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("isUserComplete Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}

		return result;
	}
	@Deprecated
	public RegisterUserResult RegisterUser(String password, UserDto user)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + REGISTE_USER_METHOD;
		RegisterUserResult result = new RegisterUserResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, REGISTE_USER_METHOD);

			request.addProperty("user", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object obj = envelope.getResponse();

			if (obj != null)
			{
				result.Return = RegisterUserReturn.Ok;
				Log.d("RegisterUser Info!", "Ok");
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("RegisterUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, RegisterUserReturn.Error);

			if (e.detail != null)
			{
				Element UserAlreadyExists = (Element)((Element)e.detail.getChild(0)).getChild(0);
				if (UserAlreadyExists.getName().equalsIgnoreCase("UserAlreadyExists"))
				{
					Element duplicatedField = (Element)UserAlreadyExists.getChild(0);
					if (duplicatedField.getName().equalsIgnoreCase("duplicatedField"))
					{
						String message = (String)duplicatedField.getChild(0);
						if (message.equalsIgnoreCase("EMAIL"))
						{
							result.Return = RegisterUserReturn.DupeEmail;
						}
						else if (message.equalsIgnoreCase("PHONE"))
						{
							result.Return = RegisterUserReturn.DupePhone;
						}
						else if (message.equalsIgnoreCase("NAME"))
						{
							result.Return = RegisterUserReturn.DupeLogin;
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("RegisterUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, RegisterUserReturn.Error);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("RegisterUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, RegisterUserReturn.Error);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("RegisterUser Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, RegisterUserReturn.Error);
		}

		return result;
	}
	@Deprecated
	public AuthenticateResult Authenticate(String user, String password)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + AUTHENTICATE_METHOD;
		AuthenticateResult result = new AuthenticateResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, AUTHENTICATE_METHOD);

			request.addProperty("name", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AUTH_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object obj = envelope.getResponse();
			result.Return = GetStringFromProperty(obj, "");

			if (result.Return.equals(""))
			{
				result.Set(SoapRequestResult.Fault, null);
				Log.d("Authenticate Info!", "Bad authToken");
			}
			else
			{
				Log.d("Authenticate Info!", "Ok authToken - " + result.Return);
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("Authenticate Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("Authenticate Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("Authenticate Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("Authenticate Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	/**
	 * @return BAD_AD_ID - To declare, not a ride
	 */
	/*@Deprecated
	public PlaceAdResult PlaceAd(String token, PostData Post)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + PLACE_AD_METHOD;
		PlaceAdResult result = new PlaceAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, PLACE_AD_METHOD);

			AdDto ad = new AdDto(Post);
			request.addProperty("ad", ad);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			ad.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			result.Return = Long.parseLong(GetStringFromProperty(obj, "" + BAD_AD_ID));

			if (result.Return != BAD_AD_ID)
			{
				Log.d("PlaceAd Info!", "Ok AD id - " + result);
			}
			else
			{
				Log.d("PlaceAd Info!", "Bad AD id");
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("PlaceAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, BAD_AD_ID);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("PlaceAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_AD_ID);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("PlaceAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_AD_ID);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("PlaceAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_AD_ID);
		}

		return result;
	}*/
	@Deprecated
	/*public GetAdsResult GetAds(String token, long lastAdId, long numberAds, FilterDto filter)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_ADS_METHOD;
		GetAdsResult result = new GetAdsResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_ADS_METHOD);

			request.addProperty("lastAdId", lastAdId);
			request.addProperty("numberAds", numberAds);
			request.addProperty("filter", filter);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);
			new FilterDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			//HttpTransportSE androidHttpTransport = Utils.GetServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			result.Return = new ArrayList<Product>();
			Object response = envelope.getResponse();
			if (response instanceof AdDto)
			{
				result.Return.add(new Product((AdDto)response));
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<AdDto> obj = (Vector<AdDto>)response;
				if (obj != null)
				{
					for (AdDto ad : obj)
					{
						result.Return.add(new Product(ad));
					}
				}
			}

			Log.d("GetAds Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}*/

	public AddImageToAdResult AddImageToAd(String token, long adId, Bitmap image)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + ADD_IMAGE_TO_AD_METHOD;
		AddImageToAdResult result = new AddImageToAdResult();
		String StatusSoap = "Ok";
		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, ADD_IMAGE_TO_AD_METHOD);

			request.addProperty("adId", adId);
			request.addProperty("image", new ImageDto(image));

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			envelope.setAddAdornments(false);
			envelope.implicitTypes = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			new ImageDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			result.Return = Long.parseLong(GetStringFromProperty(obj, "" + BAD_IMAGE_ID));

			if (result.Return != BAD_IMAGE_ID)
				Log.d("AddImageToAd Info!", "Ok");
			else
				Log.d("AddImageToAd Error!", "return = " + result);
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("AddImageToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, BAD_IMAGE_ID);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("AddImageToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_IMAGE_ID);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("AddImageToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_IMAGE_ID);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("AddImageToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, BAD_IMAGE_ID);
		}

		return result;
	}

	@Deprecated
	@SuppressWarnings ("unchecked")
	public GetCategoriesResult GetCategories(String token)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_ALL_CATEGORIES_METHOD;
		GetCategoriesResult result = new GetCategoriesResult();
		String StatusSoap = "Ok";
		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_ALL_CATEGORIES_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new CategoryDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Vector<CategoryDto> Arr = (Vector<CategoryDto>)envelope.getResponse();

			/*if (Arr != null)
			{
				for (CategoryDto cat : Arr)
				{
					if (cat != null)
					{
						Category rootCat = Category.AddRootCategory((int)cat.id, cat.name);
						loadSubCategory(rootCat, cat.subcategories);
						for (CategoryDto subCat : cat.subcategories)
						{
							if (subCat != null)
								rootCat.AddSubCategory((int)subCat.id, subCat.name);
						}
					}
				}

				Log.d("GetCategories Info!", "Ok");
			}
			else
			{
				Log.d("GetCategories Warning!", "Arr == null");
			}*/

			result.Return = true;
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetCategories Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, false);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetCategories Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetCategories Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetCategories Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, false);
		}

		return result;
	}

	/*private void loadSubCategory(Category parentCat, Vector<CategoryDto> sub)
	{
		if (sub != null)
		{
			for (CategoryDto it : sub)
			{
				Category cat = parentCat.AddSubCategory((int)it.id, it.name);
				loadSubCategory(cat, it.subcategories);
			}
		}
	}*/
	/*@Deprecated
	public UpdateAdResult UpdateAd(String token, PostData Post)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + UPDATE_AD_METHOD;
		UpdateAdResult result = new UpdateAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, UPDATE_AD_METHOD);

			AdDto ad = new AdDto(Post);
			request.addProperty("ad", ad);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			ad.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("UpdateAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("UpdateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("UpdateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("UpdateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("UpdateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}*/
	@Deprecated
	/*public GetAdByIdResult GetAdById(String token, long adId)
	{
		final String SOAP_METHOD = GET_AD_BY_ID_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		GetAdByIdResult result = new GetAdByIdResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));
			
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			if (response instanceof AdDto)
			{
				result.Return = new Product((AdDto)response);
			}
			else
			{
				Log.d("GetAdById Alert!", "response instanceof AdDto");
				result.Set(SoapRequestResult.Fault, null);
			}

			Log.d("GetAdById Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetAdById Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetAdById Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetAdById Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetAdById Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}*/

	public ChangePasswordResult ChangePassword(String token, String oldPassword, String newPassword)
	{
		final String SOAP_METHOD = CHANGES_PASSWORD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ChangePasswordResult result = new ChangePasswordResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("oldPassword", oldPassword);
			request.addProperty("newPassword", newPassword);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AUTH_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("ChangePassword Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("ChangePassword Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("ChangePassword Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("ChangePassword Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("ChangePassword Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}
	@Deprecated
	public BookmarkAdResult BookmarkAd(String token, long adId)
	{
		final String SOAP_METHOD = BOOKMARK_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		BookmarkAdResult result = new BookmarkAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			result.Return = Long.parseLong(envelope.getResponse().toString());

			Log.d("BookmarkAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("BookmarkAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("BookmarkAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("BookmarkAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("BookmarkAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public RemoveBookmarkResult RemoveBookmark(String token, long adId)
	{
		final String SOAP_METHOD = REMOVE_BOOKMARK_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		RemoveBookmarkResult result = new RemoveBookmarkResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("RemoveBookmark Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("RemoveBookmark Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("RemoveBookmark Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("RemoveBookmark Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("RemoveBookmark Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	/*public GetBookmarkedAdsResult GetBookmarkedAds(String token)
	{
		String SOAP_METHOD = GET_BOOKMARKED_ADS_METHOD;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		GetBookmarkedAdsResult result = new GetBookmarkedAdsResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);
			new FilterDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			result.Return = new ArrayList<Product>();
			Object response = envelope.getResponse();
			if (response instanceof AdDto)
			{
				result.Return.add(new Product((AdDto)response));
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<AdDto> obj = (Vector<AdDto>)response;
				if (obj != null)
				{

					for (AdDto ad : obj)
					{
						result.Return.add(new Product(ad));
					}
				}
			}

			Log.d("GetBookmarkedAds Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetBookmarkedAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetBookmarkedAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetBookmarkedAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetBookmarkedAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}*/

	public GetConnectedSocialNetworksResult GetConnectedSocialNetworks(String token)
	{
		String SOAP_METHOD = GET_CONNECTED_SOCIAL_NETWORKS_METHOD;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		GetConnectedSocialNetworksResult result = new GetConnectedSocialNetworksResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			result.Return = new ArrayList<eSocialNetworks>();
			Object response = envelope.getResponse();
			if (response instanceof SoapPrimitive)
			{
				result.Return.add(eSocialNetworks.valueOf(GetStringFromProperty(response, "")));
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<SoapPrimitive> obj = (Vector<SoapPrimitive>)response;
				if (obj != null)
				{
					for (SoapPrimitive ad : obj)
					{
						result.Return.add(eSocialNetworks.valueOf(GetStringFromProperty(ad, "")));
					}
				}
			}

			Log.d("GetConnectedSocialNetworks Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetConnectedSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetConnectedSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetConnectedSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetConnectedSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public DisconnectFromNetworkResult DisconnectFromNetwork(String token, eSocialNetworks networkName)
	{
		final String SOAP_METHOD = GET_DISCONNECT_FROM_NETWORKS_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		DisconnectFromNetworkResult result = new DisconnectFromNetworkResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			request.addPropertyIfValue("networkName", networkName.toString());

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("DisconnectFromNetwork Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("DisconnectFromNetwork Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("DisconnectFromNetwork Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("DisconnectFromNetwork Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("DisconnectFromNetwork Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public ShareOnSocialNetworksResult ShareOnSocialNetworks(String token, String message)
	{
		final String SOAP_METHOD = SHARE_ON_SOCIAL_NETWORKS_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ShareOnSocialNetworksResult result = new ShareOnSocialNetworksResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("message", message);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("ShareOnSocialNetworks Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("ShareOnSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("ShareOnSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("ShareOnSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("ShareOnSocialNetworks Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public MarkAsSpamResult MarkAsSpam(String token, long adId)
	{
		final String SOAP_METHOD = MARK_AS_SPAM_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		MarkAsSpamResult result = new MarkAsSpamResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("MarkAsSpam Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("MarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("MarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("MarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("MarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public UnmarkAsSpamResult UnmarkAsSpam(String token, long adId)
	{
		final String SOAP_METHOD = UNMARK_AS_SPAM_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		UnmarkAsSpamResult result = new UnmarkAsSpamResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("UnmarkAsSpam Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("UnmarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("UnmarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("UnmarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("UnmarkAsSpam Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public GetUserByNameResult GetUserByName(String token, String name)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_USER_BY_NAME_METHOD;
		GetUserByNameResult result = new GetUserByNameResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_USER_BY_NAME_METHOD);

			request.addProperty("name", name);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			if (envelope.getResponse() instanceof UserDto)
			{
				result.Return = new User((UserDto)envelope.getResponse());
				Log.d("GetUserByName Info!", "Ok");
			}
			else
			{
				Log.d("GetUser Warning!", "userDto not found");
				result = new GetUserByNameResult(SoapRequestResult.SoapProblem, null);
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetUserByName Alert!", StatusSoap);
			result = new GetUserByNameResult(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetUserByName Alert!", StatusSoap);
			result = new GetUserByNameResult(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetUserByName Alert!", StatusSoap);
			result = new GetUserByNameResult(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetUserByName Alert!", StatusSoap);
			result = new GetUserByNameResult(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public EndAdResult EndAd(String token, long adId)
	{
		final String SOAP_METHOD = END_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		EndAdResult result = new EndAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("EndAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("EndAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("EndAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("EndAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("EndAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public DeleteAdResult DeleteAd(String token, long adId)
	{
		final String SOAP_METHOD = DELETE_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		DeleteAdResult result = new DeleteAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("DeleteAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("DeleteAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("DeleteAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("DeleteAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("DeleteAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public RelistAdResult RelistAd(String token, long adId)
	{
		final String SOAP_METHOD = RELIST_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		RelistAdResult result = new RelistAdResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();

			Log.d("RelistAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("RelistAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("RelistAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("RelistAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("RelistAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	/*public GetMyAdsResult GetMyAds(String token)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_MY_ADS_METHOD;
		GetMyAdsResult result = new GetMyAdsResult();
		String StatusSoap = "Ok";

		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_MY_ADS_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			result.Return = new ArrayList<Product>();
			Object response = envelope.getResponse();
			if (response instanceof AdDto)
			{
				result.Return.add(new Product((AdDto)response));
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<AdDto> obj = (Vector<AdDto>)response;
				if (obj != null)
				{

					for (AdDto ad : obj)
					{
						result.Return.add(new Product(ad));
					}
				}
			}

			Log.d("GetMyAds Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetMyAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetMyAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetMyAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetMyAds Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}*/

	public RateAdResult RateAd(String token, long adId, int ratingValue)
	{
		final String SOAP_METHOD = RATE_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		RateAdResult result = new RateAdResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);
			request.addProperty("ratingValue", ratingValue);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			result.Return = Float.valueOf(GetStringFromProperty(obj, "0.0f"));
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("RateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, 0.0f);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("RateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, 0.0f);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("RateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, 0.0f);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("RateAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, 0.0f);
		}

		return result;
	}

	public DeleteMessageResult DeleteMessage(String token, long messageId)
	{
		final String SOAP_METHOD = DELETE_MESSAGE_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		DeleteMessageResult result = new DeleteMessageResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("messageId", messageId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("DeleteMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("DeleteMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("DeleteMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("DeleteMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public UpdateCommentResult UpdateComment(String token, CommentDto comment)
	{
		final String SOAP_METHOD = UPDATE_COMMENT_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		UpdateCommentResult result = new UpdateCommentResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("comment", comment);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			comment.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
			
			Log.d("UpdateComment Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("UpdateComment Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("UpdateComment Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("UpdateComment Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("UpdateComment Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public GetCommentsByAdResult GetCommentsByAd(String token, long adId, long lastCommentId, int numComments)
	{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + GET_COMMENTS_BY_AD_METHOD;
		GetCommentsByAdResult result = new GetCommentsByAdResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, GET_COMMENTS_BY_AD_METHOD);

			request.addProperty("adId", adId);
			request.addProperty("lastCommentId", lastCommentId);
			request.addProperty("numComments", numComments);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new CommentDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			result.Return = new ArrayList<CommentDto>();
			Object response = envelope.getResponse();
			if (response instanceof CommentDto)
			{
				result.Return.add((CommentDto)response);
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<CommentDto> obj = (Vector<CommentDto>)response;
				if (obj != null)
				{

					for (CommentDto ad : obj)
					{
						result.Return.add(ad);
					}
				}
			}

			Log.d("GetCommentsByAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetCommentsByAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetCommentsByAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetCommentsByAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetCommentsByAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public SendMessageToResult SendMessageTo(String token, MessageDto message)
	{
		final String SOAP_METHOD = SEND_MESSAGE_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		SendMessageToResult result = new SendMessageToResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("message", message);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			message.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("SendMessageTo Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("SendMessageTo Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("SendMessageTo Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("SendMessageTo Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public AddCommentToAdResult AddCommentToAd(String token, long adId, CommentDto comment)
	{
		final String SOAP_METHOD = ADD_COMMENT_TO_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		AddCommentToAdResult result = new AddCommentToAdResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);
			request.addProperty("comment", comment);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			comment.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("AddCommentToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("AddCommentToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("AddCommentToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("AddCommentToAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public HideMessageResult HideMessage(String token, long messageId)
	{
		final String SOAP_METHOD = HIDE_MESSAGE_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		HideMessageResult result = new HideMessageResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("messageId", messageId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("HideMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("HideMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("HideMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("HideMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public GetAllMessagesResult GetAllMessages(String token)
	{
		final String SOAP_METHOD = GET_ALL_MESSAGE_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		GetAllMessagesResult result = new GetAllMessagesResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			new MessageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			
			result.Return = new ArrayList<MessageDto>();
			Object response = envelope.getResponse();
			if (response instanceof MessageDto)
			{
				result.Return.add((MessageDto)response);
			}
			else
			{
				@SuppressWarnings ("unchecked")
				Vector<MessageDto> obj = (Vector<MessageDto>)response;
				if (obj != null)
				{
					for (MessageDto ad : obj)
					{
						result.Return.add(ad);
					}
				}
			}
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("GetAllMessages Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("GetAllMessages Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("GetAllMessages Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("GetAllMessages Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}

	public UpdateMessageResult UpdateMessage(String token, MessageDto message)
	{
		final String SOAP_METHOD = UPDATE_MESSAGE_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		UpdateMessageResult result = new UpdateMessageResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("message", message);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			message.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
			
			Log.d("UpdateMessage Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("UpdateMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("UpdateMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("UpdateMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("UpdateMessage Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}
	
	public DeleteImageFromAdResult DeleteImageFromAd(String token, long adId, long imageId)
	{
		final String SOAP_METHOD = DELETE_IMAGE_FROM_AD_METHOD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		DeleteImageFromAdResult result = new DeleteImageFromAdResult();
		String StatusSoap = "Ok";

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
		try
		{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);
			request.addProperty("imageId", imageId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			envelope.getResponse();
			
			Log.d("DeleteImageFromAd Info!", "Ok");
		}
		catch (SoapFault e)
		{
			StatusSoap = "SoapFault Error! " + e.getMessage();
			Log.d("DeleteImageFromAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.Fault, null);
		}
		catch (IOException e)
		{
			StatusSoap = "IOException Error! " + e.getMessage();
			Log.d("DeleteImageFromAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (XmlPullParserException e)
		{
			StatusSoap = "XmlPullParserException Error! " + e.getMessage();
			Log.d("DeleteImageFromAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}
		catch (Exception e)
		{
			StatusSoap = "Exception Error! " + e.getMessage();
			Log.d("DeleteImageFromAd Alert!", StatusSoap);
			result.Set(SoapRequestResult.SoapProblem, null);
		}

		return result;
	}
}
