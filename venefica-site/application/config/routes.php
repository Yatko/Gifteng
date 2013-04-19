<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');
/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	http://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There area two reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router what URI segments to use if those provided
| in the URL cannot be matched to a valid route.
|
*/

$route['default_controller'] = 'index/view';
$route['404_override'] = '';

//$route['browse/(:any)'] = 'browse/view/$1';
$route['browse/ajax/(:any)'] = 'browse/ajax/$1';
$route['browse'] = 'browse/view';

$route['authentication/(:any)'] = 'authentication/$1';
$route['authentication'] = 'authentication/view';

$route['invitation/(:any)'] = 'invitation/$1';
$route['invitation'] = 'invitation/view';

$route['registration/(:any)'] = 'registration/$1';
$route['registration'] = 'registration/view';

$route['(:any)'] = 'index/view';


/* End of file routes.php */
/* Location: ./application/config/routes.php */