define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('loader', function() {
		return {
			restrict:'E',
			replace: true,
			templateUrl:'app/partials/directives/loader.html'
		}
	})
});