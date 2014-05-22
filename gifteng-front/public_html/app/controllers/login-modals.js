define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('LoginModalsController', ["$scope", "$location", "$routeParams", "$modal", "$window", "IpService", "SocialNetwork", "$modalStack", function($scope, $location, $routeParams, $modal, $window, IpService, SocialNetwork, $modalStack) {
		
		$scope.close = function () {
			$location.search('login',null);
			$location.search('forgot',null);
			$location.search('request',null);
			$location.search('verify',null);
			$location.search('facebookLogin',null);
			if($modalStack.getTop())
				$modalStack.dismiss($modalStack.getTop().key,'close');
		};

		$scope.country = IpService.get();
		
		if($scope.country==null) {
			IpService.call().then(function (data) {
				IpService.set(data);
				$scope.country = data.country.iso_code;
			});
		}
			$scope.$watch(function() {
				return $scope.country;
			}, function(data) {
				if(typeof(data)!=='undefined' && data!==null && typeof(data.country)!=='undefined') {
					$scope.country=data.country.iso_code;
				}

				var country = $scope.country;
				var oLogin = function() {
					$scope.close();
					if($location.$$path!="/give")
						$location.search('login');
						modalInstance = $modal.open({
							templateUrl: 'app/partials/login.html',
							controller: function($scope, $modalInstance, $modal) {
								$scope.country = country;
								$scope.oSign = oS;
								$scope.oForgot = oForgot;
								$scope.oRequest = oRequest;
								
								$scope.close = function() {
									$modalInstance.close();
									$location.search('login',null);
								};
							}
						});
				}
				var oForgot = function() {
					$scope.close();
					$location.search('forgot');
					modalInstance = $modal.open({
						templateUrl: 'app/partials/forgot.html',
						controller: function($scope, $modalInstance) {
							$scope.country = country;
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.oSign = oS;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('forgot',null);
							};
						}
					});
				}
				
				var oReset = function() {
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/reset-password.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin=oLogin;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('reset',null);
							};
						}
					});
				}
				var oConfirm = function() {
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/confirm.html',
						controller: function($scope, $modalInstance) {
							$scope.close = function() {
								$modalInstance.close();
								$location.search('confirm',null);
							};
						}
					});
				}
				
				var oRequest = function() {
					$scope.close();
					$location.search('request');
					modalInstance = $modal.open({
						templateUrl: 'app/partials/request.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oVerify = oVerify;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('request',null);
							};
						}
					});
				}
				
				var oS = function() {
					var ref = $routeParams.ref;
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/verify.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.defstep="step3";
							$scope.ref = ref;
							$scope.unclosable=false;
							$scope.close = function() {
								$modalInstance.close();
							};
						}
					});
				}
				
				var oV = function(code) {
					var ref = $routeParams.ref;
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/verify.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.ref = ref;
							$scope.unclosable=false;
							if(typeof(code)!=='undefined') {
								$scope.invitationCode = code;
							} else {
								$scope.invitationCode = '';
							}
							$scope.close = function() {
								$modalInstance.close();
								$location.search('verify',null);
							};
						}
					});
				}
				
				if($scope.country!='US')
					var oVerify = oV;
				else
					var oVerify = oS;
				
				$scope.oVerify = oVerify;
				$scope.oLogin = oLogin;
				$scope.oRequest = oRequest;
			
				var oFacebookLogin = function(){
					$scope.close();
					$location.search('facebookLogin');
					$window.location.href = SocialNetwork+'/social-signin/?scope=email,user_status,user_about_me,user_activities,user_birthday,user_education_history,user_likes,user_location,friends_about_me,friends_birthday,friends_likes,friends_location';
				};
				$scope.oFacebookLogin = oFacebookLogin;


			},true);



	}])

});