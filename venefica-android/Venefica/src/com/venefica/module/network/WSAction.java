package com.venefica.module.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.venefica.module.invitation.InvitationResultWrapper;
import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.browse.BrowseCatResultWrapper;
import com.venefica.module.listings.browse.SearchListingResultWrapper;
import com.venefica.module.listings.post.PostListingResultWrapper;
import com.venefica.module.messages.MessageResultWrapper;
import com.venefica.module.user.UserDto;
import com.venefica.module.user.UserRegistrationResultWrapper;
import com.venefica.module.utils.Utility;
import com.venefica.services.AdDto;
import com.venefica.services.AddressDto;
import com.venefica.services.CategoryDto;
import com.venefica.services.CommentDto;
import com.venefica.services.FilterDto;
import com.venefica.services.ImageDto;
import com.venefica.services.InvitationDto;
import com.venefica.services.MessageDto;
import com.venefica.services.RatingDto;
import com.venefica.utils.Constants;

/**
 * @author avinash Class to perform webservice operations.
 */

public class WSAction {
	/**
	 * Web method name constants
	 */
	private final String WS_METHOD_AUTHENTICATE = "Authenticate";
	private final String WS_METHOD_IS_USER_REGISTERED = "IsUserComplete";
	private final String WS_METHOD_GET_USER = "GetUser";
	private final String WS_METHOD_GET_USER_BY_NAME = "GetUserByName";
	private final String WS_METHOD_UPDATE_USER = "UpdateUser";
	private final String WS_METHOD_GET_ALL_CATEGORIES = "GetAllCategories";
	private final String WS_METHOD_REGISTER_USER = "RegisterUser";
	private final String WS_METHOD_PLACE_AD = "PlaceAd";
	private final String WS_METHOD_GET_MY_ADS = "GetMyAds";
	private final String WS_METHOD_GET_AD_BY_ID = "GetAdById";
	private final String WS_METHOD_END_AD = "EndAd";
	private final String WS_METHOD_RELIST_AD = "RelistAd";
	private final String WS_METHOD_DELETE_AD = "DeleteAd";
	private final String WS_METHOD_GET_BOOKMARKED_ADS = "GetBookmarkedAds";
	private final String WS_METHOD_REMOVE_BOOKMARK = "RemoveBookmark";
	private final String WS_METHOD_GET_ADS = "GetAdsExDetail";
	private final String WS_METHOD_UPDATE_AD = "UpdateAd";
	private final String WS_METHOD_BOOKMARK = "BookmarkAd";
	private final String WS_METHOD_GET_COMMENTS_BY_AD = "GetCommentsByAd";
	private final String WS_METHOD_SEND_MESSAGE = "SendMessage";
	private final String WS_METHOD_ADD_COMMENT_TO_LISTING = "AddCommentToAd";
	private final String WS_METHOD_ADD_IMAGE_TO_AD = "AddImageToAd";
	private final String WS_METHOD_GET_ALL_MESSAGE = "GetAllMessages";
	private final String WS_METHOD_HIDE_MESSAGE = "HideMessage";
	private final String WS_METHOD_RATE_AD = "RateAd";
	private final String WS_METHOD_REQUEST_INVITATION = "RequestInvitation";
	private final String WS_METHOD_VERIFY_INVITATION = "IsInvitationValid";
	private final String WS_METHOD_CHANGE_PASSWORD = "ChangePassword";
	private final String WS_METHOD_FOLLOW_USER = "Follow";
	private final String WS_METHOD_UNFOLLOW_USER = "Unfollow";
	private final String WS_METHOD_GET_FOLLOWING = "GetFollowings";
	private final String WS_METHOD_GET_FOLLOWERS = "GetFollowers";
	private final String WS_METHOD_GET_RECEIVED_REVIEWS = "GetReceivedRatings";
	private final String WS_METHOD_MARK_AS_SPAM = "MarkAsSpam";
	private final String WS_METHOD_UNMARK_AS_SPAM = "UnmarkAsSpam";
	private final String WS_METHOD_REQUEST_AD = "RequestAd";
	private final String WS_METHOD_CANCEL_REQUEST = "CancelRequest";
	private final String WS_METHOD_DELETE_IMAGES = "DeleteImagesFromAd";
	public static final int BAD_AUTH_TOKEN = -1;
	public static final long BAD_AD_ID = Long.MIN_VALUE;
	public static final long BAD_IMAGE_ID = Long.MIN_VALUE;
	
			
	
	/**
	 * TO hold soap action
	 */
	private String soapAction;
	

	/**
	 * Method to check network connection's availability
	 * 
	 * @param context
	 * @return connection state
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	private static String getStringFromProperty(Object responseObj,
			final String defaultValue) {
		String result = defaultValue;
		try {
			if (responseObj.getClass() == SoapPrimitive.class)
				result = responseObj.toString();
		} catch (Exception ex) {
			Log.d("WSAction::getStringFromProperty :", ex.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * Method to authenticate user
	 * 
	 * @param user
	 * @param password
	 * @return result
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public UserRegistrationResultWrapper authenticateUser(String user,
			String password) throws IOException, XmlPullParserException {
		UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
		try {
			soapAction = Constants.SERVICES_NAMESPACE + WS_METHOD_AUTHENTICATE;

			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_AUTHENTICATE);

			request.addProperty("name", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utility
					.getServicesTransport(Constants.SERVICES_AUTH_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(soapAction, envelope, headerList);

			Object obj = envelope.getResponse();
			if (obj.toString().indexOf("Fault") > -1) {
				throw (new SoapFault());
			} else {
				// Parse response
				wrapper.data = getStringFromProperty(obj, null);
				wrapper.result = Constants.RESULT_USER_AUTHORISED;
			}
		} catch (SoapFault e) {
			String message = e.getMessage();
			if (message.contains("Wrong user")) {
				wrapper.result = Constants.ERROR_USER_UNAUTHORISED;
			}
		}
		return wrapper;
	}

	/**
	 * Method to check if user is already registered.
	 * 
	 * @param data
	 *            validation data from social network.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public UserRegistrationResultWrapper checkUserRegistration(String token)
			throws IOException, XmlPullParserException {
		UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
		Object obj;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE
				+ WS_METHOD_IS_USER_REGISTERED;
		SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
				WS_METHOD_IS_USER_REGISTERED);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = Utility
				.getServicesTransport(Constants.SERVICES_USER_URL);

		List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
		headerList.add(new HeaderProperty("authToken", token));

		androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
		obj = envelope.getResponse();
		wrapper.data = getStringFromProperty(obj, "false");
		wrapper.result = Constants.RESULT_IS_USER_EXISTS_SUCCESS;
		return wrapper;
	}

	/**
	 * Method to update user profile details
	 * 
	 * @param data
	 * @param user
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public int updateUser(String token, UserDto user) throws IOException,
			XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE
				+ WS_METHOD_UPDATE_USER;
		int result = -1;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_UPDATE_USER);

			request.addProperty("user", user);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);
			new ImageDto().register(envelope);
			new AddressDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			boolean res = Boolean.parseBoolean(getStringFromProperty(obj,
					"false"));
			if (res) {
				result = Constants.RESULT_UPDATE_USER_SUCCESS;
			} else {
				result = Constants.ERROR_RESULT_UPDTAE_USER;
			}
		} catch (SoapFault e) {
			String message = e.getMessage();
			if (message.contains("User with the same email already exists!")) {
				result = Constants.RESULT_REGISTER_USER_DUP_EMAIL;
			} else if (message.equalsIgnoreCase("PHONE")) {
				result = Constants.RESULT_REGISTER_USER_DUP_PHONE;
			} else {
				result = Constants.ERROR_RESULT_UPDTAE_USER;
			}
		}
		return result;
	}

	/**
	 * Method to register new user
	 * 
	 * @param password
	 * @param user
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */

	public int registerUser(String password, UserDto user, String invitationCode) throws IOException,
			XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE
				+ WS_METHOD_REGISTER_USER;
		int result = -1;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_REGISTER_USER);

			request.addProperty("user", user);
			request.addProperty("password", password);
			request.addProperty("invitationCode", invitationCode);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utility
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object obj = envelope.getResponse();

			if (obj != null) {
				result = Constants.RESULT_REGISTER_USER_SUCCESS;
			}
		} catch (SoapFault e) {
			String message = e.getMessage().toString();
			if (message.contains("User with the specified email already exists!")) {
				result = Constants.RESULT_REGISTER_USER_DUP_EMAIL;
			} else if(message.contains("User with the same name already exists!")){
				result = Constants.RESULT_REGISTER_USER_DUP_LOGIN;
			}			
		}
		return result;
	}

	/**
	 * Method to get user data for specified data
	 * 
	 * @param data
	 * @return User
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public UserDto getUser(String token, String name) throws IOException,
			XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE ;
				if (name.equalsIgnoreCase("")) {
					SOAP_ACTION.concat(WS_METHOD_GET_USER);
				} else {
					SOAP_ACTION.concat(WS_METHOD_GET_USER_BY_NAME);
				}
		UserDto user = null;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					name.equalsIgnoreCase("")? WS_METHOD_GET_USER :WS_METHOD_GET_USER_BY_NAME);
			if (!name.equalsIgnoreCase("")) {
				request.addProperty("name", name);
			}
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);
			new AddressDto().registerRead(envelope);
			
			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			if (envelope.getResponse() instanceof UserDto) {
				user = (UserDto) envelope.getResponse();
			} else {
				Log.e("WSAction getUser: ", "UserDto not found." + envelope.getResponse().getClass());
				user = null;
			}
		} catch (SoapFault e) {
			Log.e("WSAction getUser: ", e.getMessage());
			user = null;
		}
		return user;
	}

	/**
	 * Method to post new listing
	 * 
	 * @param token
	 * @param listing
	 * @return PostListingResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public PostListingResultWrapper postListing(String token, AdDto listing)
			throws IOException, XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_PLACE_AD;
		PostListingResultWrapper result = new PostListingResultWrapper();

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_PLACE_AD);

			// AdDto ad = new AdDto(Post);
			request.addProperty("ad", listing);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			listing.register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility
					.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			result.data = getStringFromProperty(obj, "" + BAD_AD_ID);

			if (result.data.equalsIgnoreCase(BAD_AD_ID + "")) {
				result.result = Constants.ERROR_RESULT_POST_LISTING;
				Log.d("PlaceAd Info!", "Bad AD id");
			} else {
				result.result = Constants.RESULT_POST_LISTING_SUCCESS;
			}
		} catch (SoapFault e) {
			result.result = Constants.ERROR_RESULT_POST_LISTING;
		}
		return result;
	}

	/**
	 * Method to get all categories
	 * @param token String
	 * @return result BrowseCatResultWrapper
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public BrowseCatResultWrapper getCategories(String token) throws IOException, XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE
				+ WS_METHOD_GET_ALL_CATEGORIES;
		BrowseCatResultWrapper result = new BrowseCatResultWrapper();
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,WS_METHOD_GET_ALL_CATEGORIES);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new CategoryDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			List<CategoryDto> categories = (List<CategoryDto>) envelope.getResponse();

			result.categories = categories;
			result.result = Constants.RESULT_GET_CATEGORIES_SUCCESS;
		} catch (SoapFault e) {
			result.result = Constants.ERROR_RESULT_GET_CATEGORIES;
		}
		return result;
	}
	/**
	 * Method to get 
	 * @param token
	 * @return result MyListingsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	public SearchListingResultWrapper getMyListings(String token) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_MY_ADS;
		SearchListingResultWrapper result = new SearchListingResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_MY_ADS);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.ERROR_NO_DATA;
			} else if (response instanceof AdDto){
				result.listings = new ArrayList<AdDto>();
				result.listings.add((AdDto)response);
				result.result = Constants.RESULT_GET_LISTINGS_SUCCESS;
			}else{
				result.listings = (List<AdDto>)response;
				result.result = Constants.RESULT_GET_LISTINGS_SUCCESS;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_MY_LISTINGS;
		}
		return result;
	}
	/**
	 * Method to get ad details
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper getListingById(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_GET_AD_BY_ID;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
		try{
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
			if (response instanceof AdDto){
				result.listing = (AdDto)response;
				result.result = Constants.RESULT_GET_LISTING_DETAILS_SUCCESS;
			}else{
				result.result = Constants.ERROR_RESULT_GET_LISTING_DETAILS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_LISTING_DETAILS;
		}
		return result;
	}
	
	/**
	 * Method to end listing
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper endListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_END_AD;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if(response == null)
				result.result = Constants.RESULT_END_LISTING_SUCCESS;
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_END_LISTING;
		}
		return result;
	}
	
	/**
	 * Method to relist listing
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper relistListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_RELIST_AD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if(response == null)
				result.result = Constants.RESULT_RELIST_LISTING_SUCCESS;
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_RELIST_LISTING;
		}
		return result;
	}
	
	/**
	 * Method to delete listing
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper deleteListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_DELETE_AD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if(response == null)
				result.result = Constants.RESULT_DELETE_LISTING_SUCCESS;
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_DELETE_LISTING;
		}
		return result;
	}
	/**
	 * Method to get bookmaked listings
	 * @param token
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public SearchListingResultWrapper getBookmarkedListings(String token) throws IOException, XmlPullParserException{
		String SOAP_METHOD = WS_METHOD_GET_BOOKMARKED_ADS;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		SearchListingResultWrapper result = new SearchListingResultWrapper();
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);
			new FilterDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			if (response instanceof AdDto) {
				result.listings = new ArrayList<AdDto>();
				result.listings.add((AdDto)response);
			} else {
				result.listings = (List<AdDto>)response;
			}
			if (result.listings == null || result.listings.size() == 0) {
				result.result = Constants.ERROR_NO_BOOKMARKS;
			} else {
				result.result = Constants.RESULT_GET_LISTINGS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_BOOKMARKS;
		}
		return result;
	}
	
	/**
	 * Method to remove bookmarked listing
	 * @param token
	 * @param adId
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper removeBookmarkedListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_REMOVE_BOOKMARK;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();

			if (response == null) {
				result.result = Constants.RESULT_REMOVE_BOOKMARKS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_REMOVE_BOOKMARKS;
		}
		return result;
	}
	
	/**
	 * Method to get listings by filter criteria
	 * @param token
	 * @param lastAdId
	 * @param numberAds
	 * @param filter
	 * @return result SearchListingResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	public SearchListingResultWrapper searchListings(String token, long lastAdId, long numberAds, FilterDto filter) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_ADS;
		SearchListingResultWrapper result = new SearchListingResultWrapper();
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_ADS);

			request.addProperty("lastAdId", lastAdId);
			request.addProperty("numberAds", numberAds);
			request.addProperty("filter", filter);
			request.addProperty("includeImages", false);
			request.addProperty("includeCreator", true);
			request.addProperty("includeCommentsNumber", 0);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new AdDto().registerRead(envelope);
			new FilterDto().register(envelope);
			new AddressDto().register(envelope);
			
			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			if (response instanceof AdDto) {
				result.listings = new ArrayList<AdDto>();
				result.listings.add((AdDto)response);
			} else {
				result.listings = (List<AdDto>)response;
			}
			
			if (result.listings == null || result.listings.size() == 0) {
				result.result = Constants.ERROR_NO_DATA;
			} else {
				result.result = Constants.RESULT_GET_LISTINGS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_LISTINGS;
		}
		return result;
	}
	/**
	 * Method to update listing
	 * @param token
	 * @param Post
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public PostListingResultWrapper updateListing(String token, AdDto listing) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_UPDATE_AD;
		PostListingResultWrapper result = new PostListingResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_UPDATE_AD);
			
			request.addProperty("ad", listing);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			listing.register(envelope);
			new AddressDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();

			if (response == null) {
				result.result = Constants.RESULT_UPDATE_LISTING_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_UPDATE_LISTING;
		}
		return result;
	}
	
	/**
	 * Method to bookmark listing
	 * @param token
	 * @param adId
	 * @return ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper bookmarkListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_BOOKMARK;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			long id = Long.parseLong(envelope.getResponse().toString());
			if (id > 0) {
				result.result = Constants.RESULT_BOOKMARKS_LISTING_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_BOOKMARKS_LISTING;
			}
			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_BOOKMARKS_LISTING;
		}
		return result;
	}

	/**
	 * Method to get comments by listing
	 * @param token
	 * @param adId
	 * @param lastCommentId
	 * @param numComments
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	public ListingDetailsResultWrapper getCommentsByListing(String token, long adId, long lastCommentId, int numComments) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_COMMENTS_BY_AD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_COMMENTS_BY_AD);

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
			
			Object response = envelope.getResponse();
			if (response instanceof CommentDto){
				result.comments = new ArrayList<CommentDto>();
				result.comments.add((CommentDto)response);
				result.result = Constants.RESULT_GET_COMMENTS_SUCCESS;
			}else{
				result.comments = (List<CommentDto>)response;
				result.result = Constants.RESULT_GET_COMMENTS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_COMMENTS;			
		}
		return result;
	}
	
	/**
	 * Method to send message to user
	 * @param token
	 * @param message
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper sendMessageTo(String token, MessageDto message) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_SEND_MESSAGE ;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try{
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
			long id = Long.parseLong(envelope.getResponse().toString());
			if (id > 0) {
				result.result = Constants.RESULT_SEND_MESSAGE_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_SEND_MESSAGE;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_SEND_MESSAGE;
		}
		return result;
	}
	
	/**
	 * Method to add comments to listing
	 * @param token
	 * @param adId
	 * @param comment
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper addCommentToListing(String token, long adId, CommentDto comment) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_ADD_COMMENT_TO_LISTING;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try{
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
			long id = Long.parseLong(envelope.getResponse().toString());
			if (id > 0) {
				result.result = Constants.RESULT_ADD_COMMENT_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_ADD_COMMENT;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_ADD_COMMENT;
		}
		return result;
	}

	/**
	 * Method to add image to listing
	 * @param token
	 * @param adId
	 * @param image
	 * @return PostListingResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public PostListingResultWrapper addImageToAd(String token, long adId, Bitmap image) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_ADD_IMAGE_TO_AD;
		PostListingResultWrapper result = new PostListingResultWrapper();
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_ADD_IMAGE_TO_AD);

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

			result.data = getStringFromProperty(obj, "" + BAD_AD_ID);

			if (result.data.equalsIgnoreCase(BAD_AD_ID + "")) {
				result.result = Constants.ERROR_RESULT_ADD_IMAGE_TO_AD;
			} else {
				result.result = Constants.RESULT_ADD_IMAGE_TO_AD_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_ADD_IMAGE_TO_AD;
		}
		return result;
	}
	
	/**
	 * Method to get all messages
	 * @param token String
	 * @return messages MessageResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	public MessageResultWrapper getAllMessages(String token) throws IOException, XmlPullParserException {
		final String SOAP_METHOD = WS_METHOD_GET_ALL_MESSAGE;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		MessageResultWrapper result = new MessageResultWrapper();
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					SOAP_METHOD);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			new MessageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			
			Object response = envelope.getResponse();
			if (response instanceof MessageDto) {
				result.messages = new ArrayList<MessageDto>();
				result.messages.add((MessageDto) response);
				result.result = Constants.RESULT_GET_ALL_MESSAGES_SUCCESS;
			} else {
				result.messages = (List<MessageDto>) response;
				result.result = Constants.RESULT_GET_ALL_MESSAGES_SUCCESS;
			}
		} catch (SoapFault e) {
			result.result = Constants.ERROR_RESULT_GET_ALL_MESSAGES;
		}
		return result;
	}
	
	/**
	 * Method to delete message
	 * @param token
	 * @param messageId
	 * @return result MessageResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public MessageResultWrapper hideMessage(String token, long messageId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_HIDE_MESSAGE;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		MessageResultWrapper result = new MessageResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_MESSAGE_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("messageId", messageId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.RESULT_DELETE_MESSAGE_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_DELETE_MESSAGE;
		}
		return result;
	}
	
	/**
	 * Method to post ratings for ad
	 * @param token
	 * @param adId
	 * @param ratingValue
	 * @return result MessageResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper rateAd(String token, long adId, int ratingValue) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_RATE_AD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
		try{
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
			float id = Float.valueOf(getStringFromProperty(obj, "0.0f"));
			if (id != 0.0f) {
				result.result = Constants.RESULT_POST_RATING_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_POST_RATING;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_POST_RATING;
		}
		return result;
	}
	
	/**
	 * Method to request invitation
	 * @param token
	 * @param invitationDto
	 * @return InvitationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public InvitationResultWrapper requestInvitation(String token, InvitationDto invitationDto) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_REQUEST_INVITATION;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		InvitationResultWrapper result = new InvitationResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_INVITATION_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("invitation", invitationDto);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();
			long id = Long.parseLong(obj.toString());
			if (id > 0) {
				result.result = Constants.RESULT_REQ_INVITATION_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_REQ_INVITATION;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_REQ_INVITATION;
		}
		return result;
	}
	
	/**
	 * Method to verify invitation
	 * @param token
	 * @param invitationCode
	 * @return InvitationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public InvitationResultWrapper verifyInvitation(String token, String invitationCode) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_VERIFY_INVITATION;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		InvitationResultWrapper result = new InvitationResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_INVITATION_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("code", invitationCode);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();
			boolean isValid = Boolean.valueOf(obj.toString());
			if (isValid) {
				result.result = Constants.RESULT_VIFY_INVITATION_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_VERIFY_INVITATION;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_VERIFY_INVITATION;
		}
		return result;
	}
	
	/**
	 * Method to change password
	 * @param token
	 * @param oldPassword
	 * @param newPassword
	 * @return result UserRegistrationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public UserRegistrationResultWrapper changePassword(String token, String oldPassword, String newPassword) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_CHANGE_PASSWORD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		UserRegistrationResultWrapper result = new UserRegistrationResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("oldPassword", oldPassword);
			request.addProperty("newPassword", newPassword);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AUTH_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			if(envelope.getResponse() == null){
				result.result = Constants.RESULT_CHANGE_PASSWORD_SUCCESS;
			}		
		}catch (SoapFault e){
			String message = e.getMessage().toString();
			if (message.contains("Old passwod is wrong!")) {
				result.result = Constants.ERROR_RESULT_INVALID_OLD_PASSWORD;
			} else {
				result.result = Constants.ERROR_RESULT_CHANGE_PASSWORD;
			}
		}
		return result;
	}
	
	/**
	 * Method to follow user
	 * @param token
	 * @param userId
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper followUser(String token, Long userId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_FOLLOW_USER;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_USER_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("userId", userId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();
			if (obj == null) {
				result.result = Constants.RESULT_FOLLOW_USER_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_FOLLOW_USER;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_FOLLOW_USER;
		}
		return result;
	}
	
	/**
	 * Method to unfollow user
	 * @param token
	 * @param userId
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper unfollowUser(String token, Long userId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_UNFOLLOW_USER;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_USER_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("userId", userId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();
			if (obj == null) {
				result.result = Constants.RESULT_UNFOLLOW_USER_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_UNFOLLOW_USER;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_UNFOLLOW_USER;
		}
		return result;
	}
	
	
	/**
	 * Method to get followers
	 * @param token
	 * @param userId
	 * @return result UserRegistrationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public UserRegistrationResultWrapper getFollowers(String token, Long userId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_GET_FOLLOWERS;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		UserRegistrationResultWrapper result = new UserRegistrationResultWrapper();
		
		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_USER_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("userId", userId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.dotNet = true;
			new UserDto().registerRead(envelope);
			new AddressDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();
			if (obj == null) {
				result.result = Constants.ERROR_NO_DATA;
			}else if (obj instanceof UserDto){
				result.followers = new ArrayList<UserDto>();
				result.followers.add((UserDto)obj);
				result.result = Constants.RESULT_GET_FOLLOWERS_SUCCESS;
			} else {
				result.followers = (List<UserDto>) obj;
				result.result = Constants.RESULT_GET_FOLLOWERS_SUCCESS;
			}
			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_FOLLOWERS;
		}
		return result;
	}
	/**
	 * Method to get followings
	 * @param token
	 * @param userId
	 * @return result UserRegistrationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public UserRegistrationResultWrapper getFollowings(String token, long userId) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_FOLLOWING;
		UserRegistrationResultWrapper result = new UserRegistrationResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_USER_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_FOLLOWING);

			request.addProperty("userId", userId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new AddressDto().register(envelope);
			
			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.ERROR_NO_DATA;
			} else if (response instanceof UserDto){
				result.followings = new ArrayList<UserDto>();
				result.followings.add((UserDto)response);
				result.result = Constants.RESULT_GET_FOLLOWINGS_SUCCESS;
			}else{
				result.followings = (List<UserDto>)response;
				result.result = Constants.RESULT_GET_FOLLOWINGS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_FOLLOWINGS;			
		}
		return result;
	}
	/**
	 * Method to get reviews
	 * @param token
	 * @param userId
	 * @return result UserRegistrationResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public UserRegistrationResultWrapper getReceivedRatings(String token, long userId) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_RECEIVED_REVIEWS;
		UserRegistrationResultWrapper result = new UserRegistrationResultWrapper();

		HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_RECEIVED_REVIEWS);

			request.addProperty("userId", userId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			new RatingDto().registerRead(envelope);
			
			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.ERROR_NO_DATA;
			} else if (response instanceof RatingDto){
				result.reviews = new ArrayList<RatingDto>();
				result.reviews.add((RatingDto)response);
				result.result = Constants.RESULT_GET_REVIEWS_SUCCESS;
			}else{
				result.reviews = (List<RatingDto>)response;
				result.result = Constants.RESULT_GET_REVIEWS_SUCCESS;
			}
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_GET_REVIEWS;			
		}
		return result;
	}
	
	/**
	 * Method to mark ad as spam
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper markListingAsSpam(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_MARK_AS_SPAM;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.RESULT_MARK_AS_SPAM_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_MARK_AS_SPAM;
			}
			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_MARK_AS_SPAM;
		}
		return result;
	}
	
	/**
	 * Method to unmark ad as spam
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper unmarkListingAsSpam(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_UNMARK_AS_SPAM;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.RESULT_UNMARK_AS_SPAM_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_UNMARK_AS_SPAM;
			}
			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_UNMARK_AS_SPAM;
		}
		return result;
	}
	
	/**
	 * Method to place request for listing
	 * @param token
	 * @param adId
	 * @return request ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper requestListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_REQUEST_AD;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			long id = Long.parseLong(envelope.getResponse().toString());
			if (id > 0) {
				result.result = Constants.RESULT_REQUEST_AD_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_REQUEST_AD;
			}
			
		}catch (SoapFault e){
			if (e.toString().contains("No more available.") || e.toString().contains("Max request limit reached.")) {
				result.result = Constants.ERROR_AD_NO_MORE_AVAILABLE;
			} else {
				result.result = Constants.ERROR_RESULT_REQUEST_AD;
			}			
		}
		return result;
	}
	
	/**Method to place cancel request for listing by requester
	 * @param token
	 * @param adId
	 * @return result ListingDetailsResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ListingDetailsResultWrapper cancelRequestForListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_CANCEL_REQUEST;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		ListingDetailsResultWrapper result = new ListingDetailsResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.RESULT_CANCEL_REQUEST_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_CANCEL_REQUEST;
			}
			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_CANCEL_REQUEST;
		}
		return result;
	}
	
	/**
	 * Method to delete images from Ad
	 * @param token
	 * @param adId
	 * @param imagesToDelete
	 * @return result PostListingResultWrapper
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public PostListingResultWrapper deleteImagesFromListing(String token, long adId, ArrayList<Long> imagesToDelete) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_DELETE_IMAGES;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		PostListingResultWrapper result = new PostListingResultWrapper();

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, SOAP_METHOD);

			request.addProperty("adId", adId);
			for (int i = 0; i < imagesToDelete.size(); i++) {
				request.addProperty("imageIds", imagesToDelete.get(i));
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utility.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object response = envelope.getResponse();
			if (response == null) {
				result.result = Constants.RESULT_DELETE_IMAGES_SUCCESS;
			} else {
				result.result = Constants.ERROR_RESULT_DELETE_IMAGES;
			}			
		}catch (SoapFault e){
			result.result = Constants.ERROR_RESULT_DELETE_IMAGES;
		}
		return result;
	}
}
