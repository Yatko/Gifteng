define(['angular','app','services'], function(angular,app) {
	'use strict';
	
	return app.config(['$routeProvider', '$httpProvider', function($routeProvider,$httpProvider) {
		$routeProvider.
			when('/',
				{
					controller: 'RegisterController',
					templateUrl: 'business/partials/register.html',
				}
			).
			when('/profile',
				{
					controller: 'ProfileController',
					templateUrl: 'business/partials/profile.html',
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
