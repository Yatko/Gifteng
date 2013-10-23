define(['angular','app','services'], function(angular,app) {
	'use strict';
	
	return app.run(function($rootScope) {
		$rootScope.$on("$routeChangeStart", function (event) {
			$('#loading').fadeIn();
		});
		$rootScope.$on("$routeChangeSuccess", function(event) {
			$('#loading').delay(500).fadeOut();
		});
	});
});