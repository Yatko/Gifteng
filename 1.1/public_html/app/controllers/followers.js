define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('FollowersController', ["$scope", "$routeParams", "User", "UserEx", function($scope, $routeParams, User, UserEx) {
		if(typeof($routeParams.id)!=='undefined') {
			var data = UserEx.followers.query({id:$routeParams.id}, function() {
				var user = User.getUser().data;
				$scope.data = [];
				angular.forEach(data.follower, function(v,k) {
					if(user.id == v.id) {
						v.self = true;
					} else {
						v.self = false;
					}
					$scope.data.push(v);
				});
			});
		}
		else {
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user.data) !== 'undefined')
						var data = UserEx.followers.query({id:user.data.id}, function() {
							console.log(data);
							$scope.data = data.follower;
						});
				},
				true
			);
		}
	}])

});