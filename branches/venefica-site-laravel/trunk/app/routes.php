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


Route::group(array('prefix' => 'api'), function() {
	Route::resource('auth','AuthController', array('only'=>array('store','index','destroy')));
	Route::group(array('before'=>'auth'), function() {
		/* ads */
		Route::resource('ad','AdController');
		Route::resource('image','ImageController', array('only'=>array('show','store')));
		Route::get('ad/more/{last}', 'AdController@loadMore');
		Route::get('ad/user/{id}', 'AdController@byUser');
		Route::get('ad/requested/{id}', 'AdController@requestedByUser');
		Route::get('ad/bookmarked/{id}', 'AdController@bookmarkedByUser');
		
		/* connections */
		Route::get('user/following/{id}', array('uses' => 'UserController@getFollowing'));
		Route::get('user/followers/{id}', array('uses' => 'UserController@getFollowers'));
		Route::post('user/follow/{id}', array('uses' => 'UserController@setFollow'));
		Route::post('user/unfollow/{id}', array('uses' => 'UserController@setUnfollow'));
		
		
		/* notifications */
		Route::get('user/notifications', array('uses' => 'UserController@getNotifications'));
		Route::post('user/notifications', array('uses' => 'UserController@setNotifications'));
		
		/* messages */
		Route::get('user/message', array('uses' => 'UserController@getMessages'));
		Route::get('user/message/{id}', array('uses' => 'UserController@getMessage'));
		Route::post('user/message', array('uses' => 'UserController@sendMessage'));
		
		/* user */
		Route::get('user/{id}', array('uses' => 'UserController@getProfile'));
		Route::resource('geo','GeoController', array('only'=>array('show')));
	});
});