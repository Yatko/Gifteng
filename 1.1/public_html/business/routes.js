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
			when('/welcome',
				{
					templateUrl: 'business/partials/welcome.html',
				}
			).
			otherwise({redirectTo: '/'});
            var numLoadings = 0,
                loadingScreen = $("#loader");
            $httpProvider.interceptors.push(function () {
                return {
                    request: function (config) {
                        return numLoadings++, loadingScreen.show(), config || $q.when(config)
                    },
                    response: function (response) {
                        return --numLoadings === 0 && loadingScreen.hide(), response || $q.when(response)
                    },
                    responseError: function (response) {
                        return --numLoadings || loadingScreen.hide(), $q.reject(response)
                    }
                }
            })
	}]);
});
