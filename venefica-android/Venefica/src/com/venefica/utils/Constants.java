package com.venefica.utils;

public class Constants
{
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
	public static final String SERVER_PORT = USE_SSL_SERVICES_TRANSPORT ? "8843" : "8081";
	public static final String SERVER_URL = "192.168.1.1";
	public static final String SERVICES_PROTOCOL = USE_SSL_SERVICES_TRANSPORT ? "https://" : "http://";
	public static final String SERVICES_BASE_URL = SERVICES_PROTOCOL + SERVER_URL + ":" + SERVER_PORT;

	//URLs on the service methods
	public static final String SERVICES_USER_URL = SERVICES_BASE_URL + "/venefica-webapp/services/UserManagementService?wsdl";
	public static final String SERVICES_AUTH_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AuthService?wsdl";
	public static final String SERVICES_AD_URL = SERVICES_BASE_URL + "/venefica-webapp/services/AdService?wsdl";
	public static final String SERVICES_MESSAGE_URL = SERVICES_BASE_URL + "/venefica-webapp/services/MessageService?wsdl";
	public static final String SERVICES_NAMESPACE = "http://service.venefica.com";

	//URLs to access through social. net
	public static final String SIGN_IN_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String SIGN_IN_TWITER_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/twitter";
	public static final String SIGN_IN_VK_URL = SERVICES_BASE_URL + "/venefica-webapp/signin/vkontakte?display=touch";

	//URLs to connect social networks
	public static final String CONNECT_TO_FACEBOOK_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/facebook?scope=email,publish_stream,user_status&display=touch";
	public static final String CONNECT_TO_TWITER_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/twitter";
	public static final String CONNECT_TO_VK_URL = SERVICES_BASE_URL + "/venefica-webapp/connect/vkontakte?display=touch";

	public static final String PHOTO_URL_PREFIX = SERVICES_BASE_URL + "/venefica-webapp";

	public static final int PRODUCT_LIST_CACHE_SIZE = 10;
	public static final int MESSAGE_LIST_CACHE_SIZE = 10;
	public static final int JPEG_COMPRESS_QUALITY = 85;

	public static final String PRODUCT_ID_PARAM_NAME = "productId";
	public static final String USER_NAME_PARAM_NAME = "userName";

	public static final String PREFERENCES_STORAGE_NAME = "VeneficaPreferencesStorage";
	public static final String PREFERENCES_TOKEN_NAME = "AuthToken";
	public static final String PREFERENCES_TOKEN_SAVE_TIME_NAME = "AuthTokenSaveTime";
	public static final String PREFERENCES_TOKEN_VALID_TIME_NAME = "AuthTokenValidTime";

	public static final long TOKEN_VALID_TIME = 2 * 7 * 24 * 60 * 60 * 1000;
}
