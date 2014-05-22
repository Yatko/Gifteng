define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('datePicker', function() {
	    return {
	        restrict: 'E',
	        require: ['ngModel'],
	        scope: {
	            ngModel: '='
	        },
	        replace: true,
	        template:
	            '<div class="input-group">'     +
	                    '<input type="text"  class="form-control input-lg" ng-model="ngModel" required>' +
	                    '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>' +
	            '</div>' ,
	        link: function(scope, element, attrs) {
	            var input = element.find('input');
	            var nowTemp = new Date();
	            var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(),0,0,0,0);
	
	            input.datepicker({
	               format: "yyyy-mm-dd",
	                onRender: function(date) {
	                    return date.valueOf() < now.valueOf() ? 'disabled' : '';
	                }
	            }).on('changeDate', function() {
	            	scope.ngModel = input.val();
				});
	        }
	    }
	});
});