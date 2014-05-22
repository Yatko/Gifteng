define(['controllers','lang'], function(controllers, lang) {
	'use strict';
	
	controllers.controller('NotificationsController', ["$scope", "UserEx", function($scope, UserEx) {
		$scope.changed=false;
		$scope.allSelected=true;
		
		var values = UserEx.notifications.query({}, function() {
			var texts = lang.notifications;
			$scope.notifications = {};
			angular.forEach(texts, function(value, key) {
				if(typeof(values.notifiableTypes)!=='undefined' && values.notifiableTypes.indexOf(key)>-1) {
					$scope.notifications[key] = {
						'text':value,
						'checked':true
					}
				}
				else {
					$scope.allSelected=false;
					$scope.notifications[key] = {
						'text':value,
						'checked':false
					}
				}
			});
		});
		
		$scope.toggleAll = function() {
			angular.forEach($scope.notifications, function(value, key) {
				value.checked=!$scope.allSelected;
			});
			$scope.allSelected=!$scope.allSelected;
		}
		
		$scope.submit = function() {
			var notifications = [];
			angular.forEach($scope.notifications, function(value, key) {
				if(value.checked) {
					notifications.push(key);
				}
			});
			values.notifiableTypes = notifications;
			$scope.changed=false;
			values.$save();
		}
	}])

});