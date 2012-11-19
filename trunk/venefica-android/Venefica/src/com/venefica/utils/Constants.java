package com.venefica.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Constants
{
	// Error constants
	public static final int ERROR_NETWORK_UNAVAILABLE = 1001;
	public static final int ERROR_NETWORK_CONNECT = 1002;
	public static final int ERROR_NO_DATA = 1003;
	public static final int RESULT_USER_AUTHORISED = 1004;
	public static final int ERROR_USER_UNAUTHORISED =1005;
	public static final int ERROR_RESULT_USER_AUTH = 1006;
	public static final int ERROR_RESULT_REGISTER_USER = 1007;
	public static final int ERROR_RESULT_UPDTAE_USER = 1008;
	public static final int RESULT_REGISTER_USER_DUP_EMAIL = 1009;
	public static final int RESULT_REGISTER_USER_DUP_PHONE = 1010;
	public static final int RESULT_REGISTER_USER_DUP_LOGIN = 1011;
	public static final int RESULT_REGISTER_USER_SUCCESS= 1012;
	public static final int RESULT_UPDATE_USER_SUCCESS= 1013;
	public static final int RESULT_IS_USER_EXISTS_SUCCESS= 1014;
	public static final int ERROR_RESULT_POST_LISTING = 1015;
	public static final int ERROR_RESULT_GET_LOCATION = 1016;
	public static final int RESULT_POST_LISTING_SUCCESS = 1017;
	public static final int ERROR_RESULT_GET_CATEGORIES = 1018;
	public static final int RESULT_GET_CATEGORIES_SUCCESS = 1019;
	
	//date
	public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static final DateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public static final String MAIN_FOLDER = "Venefica";
	public static final String TEMP_FOLDER = "Temp";
	public static final String CAMERA_FOLDER = "CameraPicture";
	public static final String CACHE_FOLDER = "Cache";
	public static final boolean USE_PNG_COMPRESS = false;
	public static final int IMAGE_MAX_SIZE = 320;
	public static final int IMAGE_THUMBNAILS_MAX_SIZE = 64;
	public static final long GEOLOCATION_UPDATE_TIME_MS = 30 * 60 * 1000;

	//settings to connect to the server
	public static final boolean USE_SSL_SERVICES_TRANSPORT = false;
	public static final String SERVER_PORT = USE_SSL_SERVICES_TRANSPORT ? "8843" : "8080";
//	public static final String SERVER_URL = "h2.yatko.com";
//	public static final String SERVER_URL = "192.168.0.100";
	public static final String SERVER_URL = "s1.veneficalabs.com";
	public static final String SERVICES_PROTOCOL = USE_SSL_SERVICES_TRANSPORT ? "https://" : "http://";
	public static final String SERVICES_BASE_URL = SERVICES_PROTOCOL + SERVER_URL + ":" + SERVER_PORT;

	//URLs on the service methods
	/*public static final String SERVICES_USER_URL = SERVICES_BASE_URL + "/venefica-webapp/services/UserManagementService?wsdl";
	public static final String SERVICES_AUTH_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AuthService?wsdl";
	public static final String SERVICES_AD_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AdService?wsdl";
	public static final String SERVICES_MESSAGE_URL = SERVICES_BASE_URL + "/venefica-webapp/services/MessageService?wsdl";*/
	public static final String SERVICES_USER_URL = SERVICES_BASE_URL + "/venefica/services/UserManagementService?wsdl";
	public static final String SERVICES_AUTH_URL = SERVICES_BASE_URL + "/venefica/services/AuthService?wsdl";
	public static final String SERVICES_AD_URL = SERVICES_BASE_URL + "/venefica/services/AdService?wsdl";
	public static final String SERVICES_MESSAGE_URL = SERVICES_BASE_URL + "/venefica/services/MessageService?wsdl";
	
	public static final String SERVICES_NAMESPACE = "http://service.venefica.com";

	//URLs to access through social. net
	public static final String SIGN_IN_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica/signin/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String SIGN_IN_TWITTER_URL = SERVICES_BASE_URL + "/venefica/signin/twitter";
	public static final String SIGN_IN_VK_URL = SERVICES_BASE_URL + "/venefica/signin/vkontakte?display=touch";

	/*public static final String SIGN_IN_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String SIGN_IN_TWITTER_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/twitter";
	public static final String SIGN_IN_VK_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/vkontakte?display=touch";*/

	//URLs to connect social networks
	public static final String CONNECT_TO_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica/connect/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String CONNECT_TO_TWITER_URL = SERVICES_BASE_URL + "/venefica/connect/twitter";
	public static final String CONNECT_TO_VK_URL = SERVICES_BASE_URL + "/venefica/connect/vkontakte?display=touch";

	/*public static final String CONNECT_TO_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String CONNECT_TO_TWITER_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/twitter";
	public static final String CONNECT_TO_VK_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/vkontakte?display=touch";
*/
	/*public static final String PHOTO_URL_PREFIX = SERVICES_BASE_URL + "/venefica-webapp";*/
	public static final String PHOTO_URL_PREFIX = SERVICES_BASE_URL + "/venefica";
	public static final int PRODUCT_LIST_CACHE_SIZE = 10;
	public static final int MESSAGE_LIST_CACHE_SIZE = 10;
	public static final int JPEG_COMPRESS_QUALITY = 85;

	public static final String PRODUCT_ID_PARAM_NAME = "productId";
	public static final String USER_NAME_PARAM_NAME = "userName";

	//Shared prefs keys
	public static final String VENEFICA_PREFERENCES = "venefica_pref";
	public static final String PREFERENCES_AUTH_TOKEN = "auth_token";
	public static final String PREFERENCES_SESSION_IN_TIME = "auth_session_time_out";
	public static final String PREF_KEY_LOGIN = "login_id";
	public static final String PREF_KEY_PASSWORD = "password";
	public static final String PREF_KEY_LOGIN_TYPE = "login_type";
	
	//Shared prefs values
	public static final String PREF_VAL_LOGIN_VENEFICA = "login_venefica";
	public static final String PREF_VAL_LOGIN_FACEBOOK = "login_facebook";
	public static final String PREF_VAL_LOGIN_TWITTER = "login_twitter";
	public static final String PREF_VAL_LOGIN_VK = "login_vk";
	

	public static final long SESSION_TIME_OUT = 2 * 7 * 24 * 60 * 60 * 1000;
		
}
