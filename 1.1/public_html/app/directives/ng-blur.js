define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('ngBlur', ['$parse', function($parse) {
		return function(scope, element, attr) {
			var fn = $parse(attr['ngBlur']);
			element.bind('blur', function(event) {
				scope.$apply(function() {
					fn(scope, {$event:event});
				});
			});
		}
	}])
});