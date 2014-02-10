<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::get('/', function()
{
	return View::make('index');
});

Route::get('/biz', function()
{
	return View::make('business');
});


Route::group(array('prefix' => 'api'), function() {
	Route::resource('auth','AuthController', array('only'=>array('store','index','destroy')));
	Route::post('biz/register', 'BizController@register');
	
	Route::post('user/forgotPasswordEmail','UserController@forgotPasswordEmail');
	Route::post('invite/requestInvitation',array('uses' => 'InviteController@requestInvitation'));
	Route::post('invite/verifyInvitation','InviteController@isInvitationValid');
	Route::post('invite/register','InviteController@registerUser');
	Route::post('user/resetPassword','AuthController@changeForgottenPassword');
	
	Route::group(array('before'=>'auth'), function() {
		
		/* ads */
		Route::resource('ad','AdController');
		Route::resource('image','ImageController', array('only'=>array('show','store')));
		//Route::resource('image/storeFile','ImageController', array('only'=>array('showFile','storeFile')));
		
		Route::post('image/profile','ImageController@profile');
		Route::get('image/wipscaled/{id}','ImageController@wipscaled');
		Route::post('image/storeInWIP','ImageController@storeInWIP');
		Route::put('image/profile/cropnsave/{id}','ImageController@cropnsave');
	//	Route::post('image/storeFile','ImageController@storeFile');
		
		Route::put('ad/{id}','AdController@update');
		Route::get('ad/more/{last}', 'AdController@loadMore');
		Route::get('ad/user/{id}', 'AdController@byUser');
		Route::get('ad/requested/{id}', 'AdController@requestedByUser');
		Route::get('ad/bookmarked/{id}', 'AdController@bookmarkedByUser');
		Route::post('ad/bookmark/{id}', 'AdController@bookmark');
		Route::post('ad/unbookmark/{id}', 'AdController@unbookmark');
		Route::post('ad/comment/{id}', 'AdController@comment');
		Route::post('ad/relist/{id}', 'AdController@relist');
		Route::post('ad/request/{id}', 'AdController@request');
		Route::post('ad/request/cancel/{id}', 'AdController@request_cancel');
		Route::post('ad/request/select/{id}', 'AdController@request_select');
		Route::post('ad/request/send/{id}', 'AdController@request_send');
		Route::post('ad/request/receive/{id}', 'AdController@request_receive');
		Route::post('ad/request/hide/{id}', 'AdController@request_hide');
		Route::post('ad/request/issue/{id}', 'AdController@request_issue');
		Route::post('ad/rate/{id}', 'AdController@rateAd');
		Route::get('ad/request/{id}', 'AdController@request_view');
		Route::get('ad/shippingBox', 'AdController@getShippingBoxes');
		
		/* connections */
		Route::get('user/following/{id}', array('uses' => 'UserController@getFollowing'));
		Route::get('user/followers/{id}', array('uses' => 'UserController@getFollowers'));
		Route::get('user/ratings/{id}', array('uses' => 'UserController@getRatings'));
		Route::post('user/follow/{id}', array('uses' => 'UserController@setFollow'));
		Route::post('user/unfollow/{id}', array('uses' => 'UserController@setUnfollow'));
		
		
		/* notifications */
		Route::get('user/notifications', array('uses' => 'UserController@getNotifications'));
		Route::post('user/notifications', array('uses' => 'UserController@setNotifications'));
		
		/* messages */
		Route::get('user/message', array('uses' => 'UserController@getMessages'));
		Route::get('user/message/{id}', array('uses' => 'UserController@getMessage'));
		Route::get('user/hidemessage/{id}', array('uses' => 'UserController@hideMessage'));
		Route::post('user/message', array('uses' => 'UserController@sendMessage'));
		Route::post('user/verifyUser', array('uses' => 'UserController@verifyUser'));
		
		/* user */
		Route::get('user/reinit', array('uses' => 'UserController@reinit'));
		Route::get('top', array('uses' => 'UserController@top'));
		Route::get('user/{id}', array('uses' => 'UserController@getProfile'));
		Route::post('user', array('uses' => 'UserController@updateProfile'));
		
		Route::post('user/resendVerification','UserController@resendVerification'); 
		Route::post('user/changePassword', 'UserController@changePassword');		
		
		/* Admin  */
		
		Route::get('admin/unApproved', 'AdminController@getUnapprovedAds');
		Route::post('admin/approve', 'AdminController@approveAd');
		Route::post('admin/unapprove', 'AdminController@unapproveAd');
		Route::post('admin/online', 'AdminController@onlineAd');
		Route::get('admin/shippingBox', 'AdminController@getShippingBoxes');
		Route::get('admin/getShippings', 'AdminController@getShippings');
		Route::post('admin/sendEmail', 'AdminController@sendEmail');
		Route::post('admin/deleteShipping', 'AdminController@deleteShipping');
		Route::post('admin/updateShipping', array('uses' => 'AdminController@updateShipping'));
		
		
		Route::post('biz/update', 'BizController@updateProfile');
		Route::resource('biz/store', 'StoreController');
		Route::resource('geo','GeoController', array('only'=>array('show')));
		Route::resource('mail','MailController');
		Route::post('mail/send', array('uses' => 'MailController@sendMail'));
		Route::post('report', array('uses' => 'MailController@report'));
	});
});