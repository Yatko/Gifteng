define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('SettingsController', ["$scope", "$location", "$modal", "Auth", "User", "UserEx", "$window","SocialService","SocialNetwork","JsonObject", function($scope, $location, $modal, Auth, User, UserEx,$window,SocialService,SocialNetwork,JsonObject) {
			
		$scope.changed=false;
		
		$scope.user = User.getUser();
		$scope.showLink = false;
		
		
		$scope.$watch(
			function() {
				return $scope.user;
			},
			function(user) {
				if(typeof(user.data)!=='undefined') {
					$scope.editData = {
						'firstName': user.data.firstName,
						'lastName': user.data.lastName,
						'about': user.data.about,
						'zipCode': user.data.address.zipCode
					}
					SocialService.getNetworks.query({},function(data){
					   var networks = data.userConnection;
						if(!JsonObject.isEmpty(networks)){						   
							$scope.editData.fbVerified = true;
						}else{
							$scope.editData.fbVerified = false;
						}
					});					
				}
			},
			true
		);
		
		$scope.resend_verification = function(){						
			UserEx.resendVerification.query({});
			$scope.showLink = true;
		};
		$scope.cancel = function(){
			window.history.back();
		};
		$scope.verifyFacebookAccount = function(){
		    var auth = Auth.query({}, function() {
				if(typeof auth.token != 'undefined' && auth.token != null){
					$window.location.href = SocialNetwork+"/social-connect/?scope=email,user_status&AuthToken="+auth.token.AuthToken;
				}
			});
		};
		
		$scope.changePassword = function(){
			 var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/change-password.html',
				controller: function($scope, $modalInstance, User, UserEx,$location) {						
					
					$scope.error = '';
					$scope.close = function(){
						$modalInstance.close();
					};
					$scope.forgotPassword = function(){
						$scope.close();
						modalInstance = $modal.open({
							templateUrl: 'app/partials/forgot.html',
							controller: function($scope, $modalInstance) {
								$scope.inner=true;
								$scope.close = function() {
									$modalInstance.close();
								};
							}
						});
					};
					$scope.submit = function() {						
						
						var oldPassword = $('#password').val();
						var new_password_1 = $('#new_password_1').val();
						var new_password_2 = $('#new_password_2').val();
						
							
							if(typeof oldPassword == 'undefined' || oldPassword == ''){
								$scope.error = 'Please enter your current password.';
								return false;
							}
							if(typeof new_password_1 == 'undefined' || new_password_1 == ''){
								$scope.error = 'You must enter the new password twice in order to confirm it.';
								return false;
							}
							if(typeof new_password_2 == 'undefined' || new_password_2 == ''){
								$scope.error = 'You must enter the new password twice in order to confirm it.';
								return false;
							}
							if(new_password_1.length <6){
								$scope.error = 'Password is too short.';
								return false;
							}
							if(new_password_2.length <6){
								$scope.error = 'Password is too short.';
								return false;
							}
							if(new_password_1 == new_password_2){
								$scope.error = "";
								UserEx.changePassword.query({
										'old_password' : oldPassword,
											'password' : new_password_1
								},function(data){									
										if(data.status == 'valid'){
											$scope.error = '';
											$modalInstance.close();
										}else{
											$scope.error = 'Your old password is incorrect. ';
										}
								});
								
							}else{
								$scope.error = 'You must enter the same password twice in order to confirm it.';
								return false;
							}
						
					
					}
				}
			});
		
		};
		
		// TODO: Remove jQuery here !
		$scope.submit  = function(){
			UserEx.updateProfile.query({
				'first_name':$('#firstName').val(),
				'last_name':$('#lastName').val(),
				'about':$('#about').val(),
				'zipCode':$('#zipCode').val()
			}, function (){
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				$scope.changed=false;
			});
		};  
	}])

});