define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('backButton', function(){
	    return {
	      restrict: 'A',
	
	      link: function(scope, element, attrs) {
	        element.bind('click', goBack);
	
	        function goBack() {
	          history.back();
	          scope.$apply();
	        }
	      }
	    }
	})
});