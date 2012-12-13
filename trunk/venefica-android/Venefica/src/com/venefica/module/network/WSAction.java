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
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.venefica.module.listings.ListingDetailsResultWrapper;
import com.venefica.module.listings.browse.BrowseCatResultWrapper;
import com.venefica.module.listings.browse.SearchListingResultWrapper;
import com.venefica.module.listings.post.PostListingResultWrapper;
import com.venefica.module.user.UserDto;
import com.venefica.module.user.UserRegistrationResultWrapper;
import com.venefica.services.AdDto;
import com.venefica.services.CategoryDto;
import com.venefica.services.FilterDto;
import com.venefica.services.ImageDto;
import com.venefica.services.User;
import com.venefica.utils.Constants;
import com.venefica.utils.Utils;

/**
 * @author avinash Class to perform webservice operations.
 */

public class WSAction {
	/**
	 * Web method name constants
	 */
	private static final String WS_METHOD_AUTHENTICATE = "Authenticate";
	private static final String WS_METHOD_IS_USER_REGISTERED = "IsUserComplete";
	private static final String WS_METHOD_GET_USER = "GetUser";
	private static final String WS_METHOD_UPDATE_USER = "UpdateUser";
	private static final String WS_METHOD_GET_ALL_CATEGORIES = "GetAllCategories";
	private static final String WS_METHOD_REGISTER_USER = "RegisterUser";
	private static final String WS_METHOD_PLACE_AD = "PlaceAd";
	private static final String WS_METHOD_GET_MY_ADS = "GetMyAds";
	private static final String WS_METHOD_GET_AD_BY_ID = "GetAdById";
	private static final String WS_METHOD_END_AD = "EndAd";
	private static final String WS_METHOD_RELIST_AD = "RelistAd";
	private static final String WS_METHOD_DELETE_AD = "DeleteAd";
	private static final String WS_METHOD_GET_BOOKMARKED_ADS = "GetBookmarkedAds";
	private static final String WS_METHOD_REMOVE_BOOKMARK = "RemoveBookmark";
	private static final String WS_METHOD_GET_ADS = "GetAdsEx";
	private static final String WS_METHOD_UPDATE_AD = "UpdateAd";
	private static final String WS_METHOD_BOOKMARK = "BookmarkAd";
	
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

			HttpTransportSE androidHttpTransport = Utils
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
			if (message.contains("Wrong user name or password!")) {
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
		HttpTransportSE androidHttpTransport = Utils
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

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils
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
			if (message.equalsIgnoreCase("EMAIL")) {
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

	public int registerUser(String password, UserDto user) throws IOException,
			XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE
				+ WS_METHOD_REGISTER_USER;
		int result = -1;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_REGISTER_USER);

			request.addProperty("user", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utils
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object obj = envelope.getResponse();

			if (obj != null) {
				result = Constants.RESULT_REGISTER_USER_SUCCESS;
			}
		} catch (SoapFault e) {
			if (e.detail != null) {
				Element UserAlreadyExists = (Element) ((Element) e.detail
						.getChild(0)).getChild(0);
				if (UserAlreadyExists.getName().equalsIgnoreCase(
						"UserAlreadyExists")) {
					Element duplicatedField = (Element) UserAlreadyExists
							.getChild(0);
					if (duplicatedField.getName().equalsIgnoreCase(
							"duplicatedField")) {
						String message = (String) duplicatedField.getChild(0);
						if (message.equalsIgnoreCase("EMAIL")) {
							result = Constants.RESULT_REGISTER_USER_DUP_EMAIL;
						} else if (message.equalsIgnoreCase("PHONE")) {
							result = Constants.RESULT_REGISTER_USER_DUP_PHONE;
						} else if (message.equalsIgnoreCase("NAME")) {
							result = Constants.RESULT_REGISTER_USER_DUP_LOGIN;
						}
					}
				}
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
	public User getUser(String token) throws IOException,
			XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_USER;
		User user = null;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE,
					WS_METHOD_GET_USER);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			if (envelope.getResponse() instanceof UserDto) {
				user = new User((UserDto) envelope.getResponse());
			} else {
				Log.e("WSAction getUser: ", "UserDto not found.");
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

			HttpTransportSE androidHttpTransport = Utils
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
		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			result.listings = (List<AdDto>)response;
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
	public SearchListingResultWrapper removeBookmarkedListing(String token, long adId) throws IOException, XmlPullParserException{
		final String SOAP_METHOD = WS_METHOD_REMOVE_BOOKMARK;

		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + SOAP_METHOD;
		SearchListingResultWrapper result = new SearchListingResultWrapper();

		try{
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
	public SearchListingResultWrapper searchListings(String token, long lastAdId, long numberAds, FilterDto filter) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_ADS;
		SearchListingResultWrapper result = new SearchListingResultWrapper();
		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_ADS);

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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object response = envelope.getResponse();
			result.listings = (List<AdDto>)response;
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

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AD_URL);
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

}
