define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ResetController', ["$scope", "$routeParams", "$location", "$timeout", "UserEx", function($scope, $routeParams, $location, $timeout, UserEx) {
		//TODO: Remove jQuery 
		$scope.error = '';
		$scope.completed = false;
		$scope.resetPassword = function(){
			var code = $routeParams.reset;
			var password = $('#password').val();
			var newPassword = $('#new_password').val();
					if(typeof code == 'undefined' || code == ''){
						$scope.error = 'Your session is expired.'
						return false;
					}
					if(typeof password == 'undefined' || password == ''){
						$scope.error = 'You must enter the new password twice in order to confirm it.';
						return false;
					}else if(password.length <6){
						$scope.error = 'Password is too short.';
						return false;
					}
					if(typeof newPassword == 'undefined' || newPassword == ''){
						$scope.error = 'You must enter the new password twice in order to confirm it.';
						return false;
					}else if(newPassword.length <6){
						$scope.error = 'Password is too short.';
						return false;
					}
					
					
					
					if(password == newPassword){
						$scope.error = "";
						UserEx.resetPassword.query({
							'code' : code,
							'password' : password
						},function(data){									
								if(data.status == 'true'){
									$scope.completed = true;
									$timeout(function() {
										$location.search('reset',null);
										$scope.oLogin();
									}, 5000);
								}else{
									$scope.error = 'Password reset does not succeeded!';
								}
						});
						
					}else{
						$scope.error = 'You must enter the same password twice in order to confirm it.';
						return false;
					}


		}
		
	
	}])

});