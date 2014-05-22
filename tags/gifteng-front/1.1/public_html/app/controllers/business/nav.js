define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('NavController',function($scope, $location, $modal, User, Auth) {
		$scope.$watch(
			function() {
				return User.getUser();
			},
			function(user) {
				if(typeof(user) !== 'undefined') {
					$scope.user = user;
					if($scope.user.logged==false
						&& $location.$$path!='/') {
						$location.path('/');
					} else if($scope.user.logged==true && $location.$$path=='/') {
						$location.path('/profile');
					}
					if(typeof(user.data)!=='undefined') {
						if(typeof(user.data.avatar) !== 'undefined')
							$scope.user.data.avatar_url = "https://s3.amazonaws.com/ge-dev/user/"+user.data.avatar.id+"_112";
						else
							$scope.user.data.avatar_url = "http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-no-profile-picture.png";
					}
				}
			},
			true
		);
		$scope.login = function() {
			var modalInstance = $modal.open({
				templateUrl: 'business/partials/modal/login.html',
				controller: function($scope, $modalInstance, Auth, User) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.doLogin = function() {
						var auth = new Auth;
						auth.email = $('#email').val();
						auth.password = $('#password').val();
						if(auth.password && auth.email) {
							auth.$save(function(response) {
								var user = new Auth.get();
								User.setUser(user);
								$location.path('/profile');
								$modalInstance.close();
							});
						}
					}
				}
			});
		};
		
		$scope.logout = function() {
			var user = new Auth.remove({id:0});
			User.setUser({});
			$location.path('/');
		}
	})
	

});