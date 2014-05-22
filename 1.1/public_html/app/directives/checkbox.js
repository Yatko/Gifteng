define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('checkbox', function(){
		return {
			scope: {
				ngModel:'=',
				name:'@',
				value:'@'
			},
			restrict: 'E',
			replace: true,
			transclude: true,
			template: '<div class="checkbox"><a class="icons" ng-click="change()" ng-class="{checked: ngModel}"><span class="first-icon glyphicon glyphicon-ge-checkbox-unchecked" ng-show="!ngModel"></span><span class="second-icon glyphicon glyphicon-ge-checkbox-checked" ng-show="ngModel"></span></a><input type="checkbox" name="{{name}}" value="{{value}}" ng-show="false" class="notifiable" ng-model="value.checked" ng-checked="value.checked"></div>',
			controller: function ($scope) {
				$scope.change = function() {
					$scope.ngModel=!$scope.ngModel;
					$scope.$parent.$parent.changed=true;
				}
			},
			compile: function compile() {
			}
		}
	})
});