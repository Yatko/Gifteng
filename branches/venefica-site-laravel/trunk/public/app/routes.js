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
					controller: 'PostController',
					templateUrl: 'app/partials/post.html',
				}
			).
			when('/top-giftengers',
				{
					controller: 'TopController',
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
			when('/view/profile/:id',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			when('/view/profile/:id/:section',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			when('/view/profile/:id/:section/:profilePage',
				{
					controller: 'ProfileController',
					templateUrl: 'app/partials/view/profile.html',
				}
			).
			when('/view/gift/:id',
				{
					controller: 'ViewGiftController',
					templateUrl: 'app/partials/view/ad.html',
				}
			).
			when('/invite',
				{
					controller: 'InviteController',
					templateUrl: 'app/partials/invite.html',
				}
			).
			otherwise({redirectTo: '/'});
			
			
			var numLoadings = 0;
	        var loadingScreen = $('<div style="position:fixed;top:0;left:0;right:0;bottom:0;z-index:10000;background-color:gray;background-color:rgba(70,70,70,0.2);"><div style="padding-top:250px;width:100px;margin:0 auto;"><div class="progress progress-striped active"><div class="progress-bar" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%"><span class="sr-only">Loading</span></div></div></div></div>')
	            .appendTo($('body')).hide();
	        $httpProvider.responseInterceptors.push(function() {
	            return function(promise) {
	                numLoadings++;
	                loadingScreen.show();
	                var hide = function(r) { if (!(--numLoadings)) loadingScreen.hide(); return r; };
	                return promise.then(hide, hide);
	            };
	        });
	}]);
});
