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

// admin area

$route['admin/(:any)']  = 'admin/$1';
$route['admin']         = 'admin/dashboard';

// ajax

$route['ajax/(:any)']   = 'ajax/$1';
$route['ajax']          = 'ajax/invalid';

// ads browse

$route['browse/ajax/(:any)']    = 'browse/$1';
$route['browse']                = 'browse/view';

// view and edit profile

$route['profile/ajax/(:any)']   = 'profile/$1';
$route['profile/(:any)']        = 'profile/view/$1';
$route['profile']               = 'profile/view';

$route['edit_profile/ajax/business']    = 'edit_profile_business/ajax';
$route['edit_profile/ajax/member']      = 'edit_profile_member/ajax';

$route['edit_profile/business']         = 'edit_profile_business/view';
$route['edit_profile/member']           = 'edit_profile_member/view';

$route['edit_profile']                  = 'edit_profile/edit_profile_redirect';

// posting (new) and edit ad (business and member)

$route['edit_post/business/(:num)'] = 'post_business/edit/$1';
$route['edit_post/member/(:num)']   = 'post_member/edit/$1';

$route['post/business/ajax/(:any)'] = 'post_business/$1';
$route['post/business']             = 'post_business/create';

$route['post/member/ajax/(:any)']   = 'post_member/$1';
$route['post/member/(:any)']        = 'post_member/$1';
$route['post/member']               = 'post_member/create';

$route['post/edit/(:num)']  = 'post/edit_redirect/$1';
$route['post']              = 'post/post_redirect';

// view ads

$route['view/(:num)']   = 'view/show/$1';
$route['view']          = 'view/show/';

// request ads

$route['request/(:num)']   = 'request/view/$1';
$route['request']          = 'request/view/';

// login

$route['authentication/(:any)'] = 'authentication/$1';
$route['authentication']        = 'authentication/view';
$route['logout']                = 'authentication/logout';

// invitation

$route['invitation/(:any)'] = 'invitation/$1';
$route['invitation']        = 'invitation/view';

// registration (member and business)

$route['registration/(:any)']   = 'registration/$1';
$route['registration']          = 'registration/business';
$route['business']              = 'registration/business';
$route['verify/(:any)']         = 'registration/verify/$1';

// helpers

$route['get_photo/(:any)/(:any)/(:num)']    = 'generator/get_photo/$1///$2/$3'; //custom sized image with folder specified
$route['get_photo/(:any)/(:num)/(:num)']    = 'generator/get_photo/$1/$2/$3//';
$route['get_photo/(:any)/(:num)']           = 'generator/get_photo/$1/$2/$2//'; //width and heoght will be the same size
$route['get_photo/(:any)/(:any)']           = 'generator/get_photo/$1///$2/'; //full sized image with folder specified
$route['get_photo/(:any)']                  = 'generator/get_photo/$1////'; //original sized image
$route['get_photo']                         = 'generator/get_photo/////'; //empty image

// statistics

$route['stat'] = 'stat/view';

// contact static page

$route['contact'] = 'contact/view';

// promotion landing pages

$route['totegiveaway']  = 'landing/totegiveaway';
$route['jobs']          = 'landing/jobs';
$route['help']          = 'landing/help';


$route['(:any)'] = 'index/view';


/* End of file routes.php */
/* Location: ./application/config/routes.php */