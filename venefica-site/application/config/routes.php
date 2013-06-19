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
$route['browse/ajax/(:any)'] = 'browse/$1';
$route['browse'] = 'browse/view';

$route['profile/ajax/(:any)'] = 'profile/$1';
$route['profile/(:any)'] = 'profile/view/$1';
$route['profile'] = 'profile/view';

$route['edit_profile/ajax/business'] = 'business_edit_profile/ajax';
$route['edit_profile/ajax/member'] = 'member_edit_profile/ajax';
$route['edit_profile/business'] = 'business_edit_profile/view';
$route['edit_profile/member'] = 'member_edit_profile/view';
$route['edit_profile'] = 'edit_profile/edit_profile_redirect';



$route['post/business/ajax/(:any)'] = 'business_post/$1';
$route['post/business'] = 'business_post/view';

$route['post/member/(:any)'] = 'member_post/$1';
$route['post/member'] = 'member_post/view';

$route['post'] = 'post/post_redirect';



$route['view/(:num)'] = 'view/show/$1';
$route['view'] = 'view/invalid';

$route['authentication/(:any)'] = 'authentication/$1';
$route['authentication'] = 'authentication/view';

$route['invitation/(:any)'] = 'invitation/$1';
$route['invitation'] = 'invitation/view';

$route['registration/(:any)'] = 'registration/$1';
$route['registration'] = 'registration/business';
$route['business'] = 'registration/business';

$route['get_photo/(:any)'] = 'generator/get_photo/$1/0/0'; //original sized image
$route['get_photo/(:any)/(:num)'] = 'generator/get_photo/$1/$2/$2'; //width and heoght will be the same size
$route['get_photo/(:any)/(:num)/(:num)'] = 'generator/get_photo/$1/$2/$3';
$route['get_photo'] = 'generator/get_photo//0/0'; //empty image

$route['contact'] = 'contact/view';

//promotion landing pages
$route['totegiveaway'] = 'landing/totegiveaway';

$route['(:any)'] = 'index/view';


/* End of file routes.php */
/* Location: ./application/config/routes.php */