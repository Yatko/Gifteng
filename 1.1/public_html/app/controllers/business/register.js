define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('RegisterController', function($scope, $location, Biz, Auth, User) {
			
		$scope.userData = {};
		$scope.register = function() {
			if(
				!$scope.userData.businessName
			) {
				$scope.error="Please enter your business name";
			}
			else if (!$scope.userData.contactName) {
				$scope.error="Please enter your contact name";
			}
			else if(!$scope.userData.phoneNumber) {
				$scope.error="Please enter your phone number";
			}
			else if(!$scope.userData.email) {
				$scope.error="Please enter your email address";
			}
			else if($scope.userData.email!=$scope.userData.email2) {
				$scope.error="Please confirm your email address";
			}
			else if(!$scope.userData.website) {
				$scope.error="Please enter your website address";
			}
			else {
				Biz.register.query($scope.userData, function() {
					$location.path('/welcome');
				});
			}
		}
		
	})

});