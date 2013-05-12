<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

//define('SERVER_URL',                'http://veneficalabs.com:8080/venefica');
define('SERVER_URL',                'http://gifteng.jelastic.servint.net/gifteng');
define('AUTH_SERVICE_WSDL',         SERVER_URL.'/services/AuthService?wsdl');
define('AD_SERVICE_WSDL',           SERVER_URL.'/services/AdService?wsdl');
define('INVITATION_SERVICE_WSDL',   SERVER_URL.'/services/InvitationService?wsdl');
define('MESSAGE_SERVICE_WSDL',      SERVER_URL.'/services/MessageService?wsdl');
define('USER_SERVICE_WSDL',         SERVER_URL.'/services/UserManagementService?wsdl');

define('DEBUG', 'debug');
define('INFO',  'info');
define('ERROR', 'error');

define('DATE_FORMAT', 'd-m-Y'); //dd-mm-yyyy
define('TEMPLATES', 2);


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