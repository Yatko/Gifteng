package com.venefica.utils;

import java.text.DateFormat;

import android.graphics.Bitmap.CompressFormat;

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
	public static final int ERROR_RESULT_GET_MY_LISTINGS = 1020;
	public static final int RESULT_GET_MY_LISTINGS_SUCCESS = 1021;
	public static final int RESULT_GET_LISTING_DETAILS_SUCCESS = 1022;
	public static final int ERROR_RESULT_GET_LISTING_DETAILS = 1023;
	public static final int RESULT_END_LISTING_SUCCESS = 1024;
	public static final int ERROR_RESULT_END_LISTING = 1025;
	public static final int RESULT_RELIST_LISTING_SUCCESS = 1026;
	public static final int ERROR_RESULT_RELIST_LISTING = 1027;
	public static final int RESULT_DELETE_LISTING_SUCCESS = 1028;
	public static final int ERROR_RESULT_DELETE_LISTING = 1029;
	public static final int RESULT_GET_BOOKMARKS_SUCCESS = 1030;
	public static final int ERROR_RESULT_GET_BOOKMARKS = 1031;
	public static final int RESULT_REMOVE_BOOKMARKS_SUCCESS = 1032;
	public static final int ERROR_RESULT_REMOVE_BOOKMARKS = 1033;
	public static final int RESULT_GET_LISTINGS_SUCCESS = 1034;
	public static final int ERROR_RESULT_GET_LISTINGS = 1035;
	public static final int RESULT_UPDATE_LISTING_SUCCESS = 1036;
	public static final int ERROR_RESULT_UPDATE_LISTING = 1037;	
	public static final int ERROR_ENABLE_LOCATION_PROVIDER = 1038;
	public static final int RESULT_BOOKMARKS_LISTING_SUCCESS = 1039;
	public static final int ERROR_RESULT_BOOKMARKS_LISTING = 1040;
	public static final int ERROR_NO_BOOKMARKS = 1041;
	public static final int ERROR_CONFIRM_REMOVE_BOOKMARKS = 1042;
	public static final int ERROR_RESULT_GET_COMMENTS = 1043;
	public static final int RESULT_GET_COMMENTS_SUCCESS = 1044;
	public static final int RESULT_SEND_MESSAGE_SUCCESS = 1045;
	public static final int ERROR_RESULT_SEND_MESSAGE = 1046;
	public static final int ERROR_RESULT_ADD_COMMENT = 1047;
	public static final int RESULT_ADD_COMMENT_SUCCESS = 1048;
	public static final int ERROR_LOW_RESOLUTION_CROP = 1049;
	public static final int ERROR_SIGN_OUT_APPLICATION = 1050;
	public static final int RESULT_ADD_IMAGE_TO_AD_SUCCESS = 1051;
	public static final int ERROR_RESULT_ADD_IMAGE_TO_AD = 1052;
	public static final int ERROR_START_CAMERA = 1053;
	public static final int ERROR_RESULT_GET_ALL_MESSAGES = 1054;
	public static final int RESULT_GET_ALL_MESSAGES_SUCCESS = 1055;
	public static final int RESULT_DELETE_MESSAGE_SUCCESS = 1056;
	public static final int ERROR_RESULT_DELETE_MESSAGE = 1057;
	public static final int ERROR_RESULT_POST_RATING = 1058;
	public static final int RESULT_POST_RATING_SUCCESS = 1059;
	public static final int ERROR_RESULT_REQ_INVITATION = 1060;
	public static final int RESULT_REQ_INVITATION_SUCCESS = 1061;
	public static final int RESULT_VIFY_INVITATION_SUCCESS = 1062;
	public static final int ERROR_RESULT_VERIFY_INVITATION = 1063;
	public static final int RESULT_CHANGE_PASSWORD_SUCCESS = 1064;
	public static final int ERROR_RESULT_INVALID_OLD_PASSWORD = 1065;
	public static final int ERROR_RESULT_CHANGE_PASSWORD = 1066;
	public static final int RESULT_FOLLOW_USER_SUCCESS = 1067;
	public static final int ERROR_RESULT_FOLLOW_USER = 1068;
	public static final int RESULT_UNFOLLOW_USER_SUCCESS = 1069;
	public static final int ERROR_RESULT_UNFOLLOW_USER = 1070;	
	public static final int RESULT_GET_FOLLOWINGS_SUCCESS = 1071;
	public static final int ERROR_RESULT_GET_FOLLOWINGS = 1072;	
	public static final int RESULT_GET_FOLLOWERS_SUCCESS = 1073;
	public static final int ERROR_RESULT_GET_FOLLOWERS = 1074;
	public static final int RESULT_GET_REVIEWS_SUCCESS = 1075;
	public static final int ERROR_RESULT_GET_REVIEWS = 1076;
	public static final int RESULT_MARK_AS_SPAM_SUCCESS = 1077;
	public static final int ERROR_RESULT_MARK_AS_SPAM = 1078;
	public static final int RESULT_UNMARK_AS_SPAM_SUCCESS = 1079;
	public static final int ERROR_RESULT_UNMARK_AS_SPAM = 1080;
	public static final int RESULT_REQUEST_AD_SUCCESS = 1081;
	public static final int ERROR_RESULT_REQUEST_AD = 1082;
	public static final int RESULT_CANCEL_REQUEST_SUCCESS = 1083;
	public static final int ERROR_RESULT_CANCEL_REQUEST = 1084;
	public static final int RESULT_DELETE_IMAGES_SUCCESS = 1085;
	public static final int ERROR_RESULT_DELETE_IMAGES = 1086;
	public static final int ERROR_AD_NO_MORE_AVAILABLE = 1087;	
	//date
	public static final DateFormat dateFormat = DateFormat.getDateInstance();
	public static final DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();
	public static final DateFormat timeFormat = DateFormat.getTimeInstance();
	public static final DateFormat dateMMDDYYYYFormat = DateFormat.getDateInstance(DateFormat.SHORT);
	
	//Location settings
	public static final long LOCATION_UPDATE_PERIOD = 2* 60 * 1000; 
	public static final float LOCATION_UPDATE_MIN_DISTANCE = 1000;//meters
	
	public static final String MAIN_FOLDER = "Venefica";
	public static final String TEMP_FOLDER = "Temp";
	public static final String CAMERA_FOLDER = "CameraPicture";
	public static final String CACHE_FOLDER = "Cache";
	public static final boolean USE_PNG_COMPRESS = false;
	public static final int IMAGE_MAX_SIZE_X= 480;
	public static final int IMAGE_MAX_SIZE_Y= 360;
	public static final int IMAGE_CROP_MAX_SIZE_X= 1280;
	public static final int IMAGE_CROP_MAX_SIZE_Y= 960;
	public static final int IMAGE_ASPECT_X= 4;//3;
	public static final int IMAGE_ASPECT_Y= 3;//2;
	public static final String TEMP_PHOTO_FILE = "temp.jpeg";
	public static final int IMAGE_THUMBNAILS_MAX_SIZE = 64;
	public static final int IMAGE_THUMBNAILS_MIN_SIZE = 30;//in Kb
	public static final long GEOLOCATION_UPDATE_TIME_MS = 30 * 60 * 1000;
	public static final int PRODUCT_LIST_CACHE_SIZE = 10;
	public static final int MESSAGE_LIST_CACHE_SIZE = 10;
	public static final int JPEG_COMPRESS_QUALITY = 80;
	public static final int IO_BUFFER_SIZE = 8 * 1024;//in Kb
	public static final int EXTERNAL_IMAGE_CACHE_LIMIT = 20 * 1024  * 1024;// in MB
	public static final String EXTERNAL_IMAGE_CACHE_NAME = "venefica-cache";
	public static final CompressFormat IMAGE_COMPRESS_FORMAT = CompressFormat.JPEG;
	public static final int IMAGE_THUMBNAILS_WIDTH = 200;
	public static final int IMAGE_THUMBNAILS_HEIGHT = 200;

	//settings to connect to the server
	public static final boolean USE_SSL_SERVICES_TRANSPORT = false;
	public static final String SERVER_PORT = USE_SSL_SERVICES_TRANSPORT ? "8843" : "8080";
//	public static final String SERVER_URL = "h2.yatko.com";
//	public static final String SERVER_URL = "192.168.0.100";
//	public static final String SERVER_URL = "s1.veneficalabs.com/venefica/services";
	public static final String SERVER_URL = "gifteng.jelastic.servint.net"; //dev server
	public static final String SERVICES_PROTOCOL = USE_SSL_SERVICES_TRANSPORT ? "https://" : "http://";
	public static final String SERVICES_BASE_URL = SERVICES_PROTOCOL + SERVER_URL /*+ ":" + SERVER_PORT*/;//commented for new dev server

	//URLs on the service methods
	/*public static final String SERVICES_USER_URL = SERVICES_BASE_URL + "/venefica-webapp/services/UserManagementService?wsdl";
	public static final String SERVICES_AUTH_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AuthService?wsdl";
	public static final String SERVICES_AD_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AdService?wsdl";
	public static final String SERVICES_MESSAGE_URL = SERVICES_BASE_URL + "/venefica-webapp/services/MessageService?wsdl";*/
	public static final String SERVICES_USER_URL = SERVICES_BASE_URL + "/gifteng/services/UserManagementService?wsdl";
	public static final String SERVICES_AUTH_URL = SERVICES_BASE_URL + "/gifteng/services/AuthService?wsdl";
	public static final String SERVICES_AD_URL = SERVICES_BASE_URL + "/gifteng/services/AdService?wsdl";
	public static final String SERVICES_MESSAGE_URL = SERVICES_BASE_URL + "/gifteng/services/MessageService?wsdl";
	public static final String SERVICES_INVITATION_URL = SERVICES_BASE_URL + "/gifteng/services/InvitationService?wsdl";
	
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
//	public static final String PHOTO_URL_PREFIX = SERVICES_BASE_URL + "/venefica";//old server
	public static final String PHOTO_URL_PREFIX = SERVICES_BASE_URL + "/gifteng";
	

	public static final String PRODUCT_ID_PARAM_NAME = "productId";
	public static final String USER_NAME_PARAM_NAME = "userName";

	//Shared prefs keys
	public static final String VENEFICA_PREFERENCES = "venefica_pref";
	public static final String PREFERENCES_AUTH_TOKEN = "auth_token";
	public static final String PREFERENCES_SESSION_IN_TIME = "auth_session_time_out";
	public static final String PREF_KEY_LOGIN = "login_id";
	public static final String PREF_KEY_PASSWORD = "password";
	public static final String PREF_KEY_LOGIN_TYPE = "login_type";
	public static final String PREF_KEY_CATEGORY_ID = "cat_id";
	public static final String PREF_KEY_CATEGORY = "category";
	
	//Shared prefs values
	public static final String PREF_VAL_LOGIN_VENEFICA = "login_venefica";
	public static final String PREF_VAL_LOGIN_FACEBOOK = "login_facebook";
	public static final String PREF_VAL_LOGIN_TWITTER = "login_twitter";
	public static final String PREF_VAL_LOGIN_VK = "login_vk";
	

	public static final long SESSION_TIME_OUT = 2 * 7 * 24 * 60 * 60 * 1000;
	
	//Filter values
	public static final int PREF_DEF_VAL_MILES = 50;
	public static final String PREF_DEF_VAL_MAX_PRICE = "5000000";
	public static final String PREF_DEF_VAL_MIN_PRICE = "0";
	public static final int PREF_DEF_VAL_CATEGORY = -1;	
	
	public static final int EXPIRE_AD_IN_DAYS = 60;
			
	public static final String FONT_PTSANS_REGULAR = "fonts/pt-sans/PTS55F.ttf";
	public static final String FONT_PTSANS_ITALIC = "fonts/pt-sans/PTS56F.ttf";
	public static final String FONT_PTSANS_BOLD = "fonts/pt-sans/PTS75F.ttf";
	public static final String FONT_PTSANS_BOLD_ITALIC = "fonts/pt-sans/PTS76F.ttf";
	public static final String FONT_PTSANS_CAPTION_REGULAR = "fonts/pt-sans/PTC55F.ttf";
	public static final String FONT_PTSANS_CAPTION_BOLD = "fonts/pt-sans/PTC75F.ttf";
	public static final String FONT_PTSANS_NARROW_REGULAR = "fonts/pt-sans/PTN57F.ttf";
	public static final String FONT_PTSANS_NARROW_BOLD = "fonts/pt-sans/PTN77F.ttf";	
			
}
