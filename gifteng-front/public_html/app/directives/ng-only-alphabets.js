define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('ngOnlyAlphabets', function () {
		return {
			restrict: 'A',
			require: '?ngModel',
			link: function (scope, element, attrs, ngModel) {
				if (!ngModel) return;
				ngModel.$parsers.unshift(function (inputValue) {
					var digits = inputValue.split('').filter(function (s) { return (!s.match(/[^a-zA-Z\s]/gi)); }).join('');
					ngModel.$viewValue = digits;
					ngModel.$render();
					return digits;
				});
			}
		};
	})
});