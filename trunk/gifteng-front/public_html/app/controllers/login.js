define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('LoginController', ["$scope", "$location", "$cookies", "$modal", "Auth", "UserEx", "User", "TestEmailService", function($scope, $location, $cookies, $modal, Auth, UserEx, User, TestEmailService) {
		$scope.remember = false;
		var email_cookie = $cookies.giftengEmail;
		if(email_cookie) {
			$scope.email = email_cookie;
			$scope.remember = true;
		}

		$scope.error = '';
		$scope.login = function() {
			$location.search('login',null);
			var email = $scope.email;
			var password = $scope.password;
			var lEmail = $('#email').val();
			if(typeof lEmail == 'undefined' || lEmail == ''){					
				//$scope.error = 'Please enter your email ';
				return false;
			} else if(typeof password != 'undefined' || password != ''){					
				if(!TestEmailService.isValid(lEmail)){						
					$scope.error = 'Please enter a vaild email address';
					return false;
				}
			}
			
			if(typeof password == 'undefined' || password == ''){					
			//	$scope.error = 'Please enter your password ';
				return false;
			}
		
			var auth = new Auth;
			auth.email = $scope.email;
			auth.password = $scope.password;
			auth.$save(function(response) {
				if(response.success == 'true'){
					var user = new Auth.get();
					User.setUser(user);
					if($scope.remember) {
						$cookies.giftengEmail = $scope.email;
					}
					else {
						$cookies.giftengEmail = '';
					}
					$scope.close();
				
					$scope.$watch(function() {
						return user;
					}, function(user) {
						if(typeof(user.data)!=='undefined') {
							var latestRelease = new Date(1392311475000);
							var lastLoginDate = new Date(parseInt(user.data.previousLoginAt));
							
							if(lastLoginDate == null || typeof(lastLoginDate) == 'undefined'){
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/welcome_new.html',
									controller: function($scope, $modalInstance) {
										$scope.close = function () {
											$modalInstance.close();
										};
									
									}
								});
							}
							else if (lastLoginDate < latestRelease) {
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/welcome_old.html',
									controller: function($scope, $modalInstance) {
										$scope.close = function () {
											$modalInstance.close();
										};
									
									}
								});
							}
						}
					}, true);
					
					if($location.$$path!=='/give')
						$location.path('/browse');
				}else{
					$scope.error = 'Oops, the email address or password is not correct. ';
				}
			});
		}
	}])

});