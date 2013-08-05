<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

//define('SERVER_URL',                'http://veneficalabs.com:8080/venefica');
define('SERVER_URL',                'http://veneficalabs.com:8080/gifteng');
//define('SERVER_URL',                'http://gifteng.jelastic.servint.net/gifteng');

define('AUTH_SERVICE_WSDL',         SERVER_URL.'/services/AuthService?wsdl');
define('AD_SERVICE_WSDL',           SERVER_URL.'/services/AdService?wsdl');
define('INVITATION_SERVICE_WSDL',   SERVER_URL.'/services/InvitationService?wsdl');
define('MESSAGE_SERVICE_WSDL',      SERVER_URL.'/services/MessageService?wsdl');
define('USER_SERVICE_WSDL',         SERVER_URL.'/services/UserManagementService?wsdl');
define('UTILITY_SERVICE_WSDL',      SERVER_URL.'/services/UtilityService?wsdl');

define('CONNECT_TO_FACEBOOK_URL',   SERVER_URL.'/connect/facebook?scope=email,publish_stream,user_status&display=popup');
define('SIGN_IN_FACEBOOK_URL',      SERVER_URL.'/signin/facebook?scope=email,publish_stream,user_status&display=popup');
define('CONNECT_TO_TWITTER_URL',   SERVER_URL.'/connect/twitter?display=popup');
define('SIGN_IN_TWITTER_URL',      SERVER_URL.'/signin/twitter?display=popup');

define('DEBUG', 'debug');
define('INFO',  'info');
define('ERROR', 'error');

define('AJAX_STATUS_RESULT', 'result');
define('AJAX_STATUS_ERROR', 'error');

define('AD_BOOKMARKS_NUM', 'ad_bookmarks_num');
define('USER_BOOKMARKS_NUM', 'user_bookmarks_num');

//define('DATE_FORMAT', 'd-m-Y'); //dd-mm-yyyy
define('DESCRIPTION_MAX_LENGTH', 100);
define('COMMENT_MAX_LENGTH', 100);
define('MESSAGE_MAX_LENGTH', 100);
define('UPLOAD_FILE_MAX_SIZE', 2 * 1024 * 1024); //in bytes
define('UPLOAD_IMAGE_MIN_WIDTH', 400); //in pixels
define('UPLOAD_IMAGE_MIN_HEIGHT', 400); //in pixels
define('UPLOAD_FILE_PREFIX', 'gifteng_');
define('TEMPLATES', 4);
define('TEMP_FOLDER', sys_get_temp_dir());


/*
|--------------------------------------------------------------------------
| File and Directory Modes
|--------------------------------------------------------------------------
|
| These prefs are used when checking and setting modes when working
| with the file system.  The defaults are fine on servers with proper
| security, but you may wish (or even need) to change the values in
| certain environments (Apache running a separate process for each
| user, PHP under CGI with Apache suEXEC, etc.).  Octal values should
| always be used to set the mode correctly.
|
*/
define('FILE_READ_MODE', 0644);
define('FILE_WRITE_MODE', 0666);
define('DIR_READ_MODE', 0755);
define('DIR_WRITE_MODE', 0777);

/*
|--------------------------------------------------------------------------
| File Stream Modes
|--------------------------------------------------------------------------
|
| These modes are used when working with fopen()/popen()
|
*/

define('FOPEN_READ',							'rb');
define('FOPEN_READ_WRITE',						'r+b');
define('FOPEN_WRITE_CREATE_DESTRUCTIVE',		'wb'); // truncates existing file data, use with care
define('FOPEN_READ_WRITE_CREATE_DESTRUCTIVE',	'w+b'); // truncates existing file data, use with care
define('FOPEN_WRITE_CREATE',					'ab');
define('FOPEN_READ_WRITE_CREATE',				'a+b');
define('FOPEN_WRITE_CREATE_STRICT',				'xb');
define('FOPEN_READ_WRITE_CREATE_STRICT',		'x+b');


/* End of file constants.php */
/* Location: ./application/config/constants.php */