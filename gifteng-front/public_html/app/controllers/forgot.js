define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ForgotController', ["$scope", "$routeParams", "UserEx", "IpService", "TestEmailService", function($scope, $routeParams, UserEx, IpService, TestEmailService) {
		var ip = '';

		$scope.country = IpService.get();
		
		if($scope.country==null) {
			IpService.call().then(function (data) {
				IpService.set(data);				
				ip = data.ip;
			});
		} else {
			ip = $scope.country.ip;
		}

		$scope.step="step1";
		$scope.error = "";
			
		$scope.forgot = function() {	
			//var email = $scope.email;
			var email = $('#email').val();
			
			if(typeof email == 'undefined' || email == ''){					
				$scope.error = 'Please enter your email address';
				return false;
			}else {
				if (!TestEmailService.isValid(email)){						
					$scope.error = 'Please enter a valid email address ';
					return false;
				}else{
					  $scope.error = "";
						UserEx.forgotPasswordEmail.query({
							'email_address' : email,
							   'ip_address' : ip
						},function(data){									
								if(data.status == 'valid'){
									$scope.error = '';
									$scope.step="step2";
								}else{
									$scope.error = ' Sorry we don\'t recognize your email address! ';
								}
						});

				}
			}			
			
			
		}
	}])

});