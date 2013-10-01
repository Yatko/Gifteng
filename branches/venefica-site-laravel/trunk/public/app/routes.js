define(['angular','app','services'], function(angular,app) {
	'use strict';
	
	return app.config(['$routeProvider', '$httpProvider', function($routeProvider,$httpProvider) {
		$routeProvider.
			when('/',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/index.html',
				}
			).
			when('/browse',
				{
					controller: 'BrowseController',
					templateUrl: 'app/partials/browse.html',
				}
			).
			when('/login',
				{
					controller: 'LoginController',
					templateUrl: 'app/partials/login.html',
				}
			).
			when('/request',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/request.html',
				}
			).
			when('/forgot',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/forgot.html',
				}
			).
			when('/verify',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/verify.html',
				}
			).
			when('/post',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/post.html',
				}
			).
			when('/post-2',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/post-2.html',
				}
			).
			when('/top-giftengers',
				{
					controller: 'IndexController',
					templateUrl: 'app/partials/top.html',
				}
			).
			when('/profile',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/profile.html',
				}
			).
			when('/profile/:section',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/profile.html',
				}
			).
			when('/profile/:section/:profilePage',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/profile.html',
				}
			).
			when('/view/profile',
				{
					controller: 'ViewProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			when('/view/profile/:section',
				{
					controller: 'ViewProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			when('/view/profile/:section/:profilePage',
				{
					controller: 'ViewProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			otherwise({redirectTo: '/'});
	}]);
});
