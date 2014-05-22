define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ReviewsController', ["$scope", "$routeParams", "User", "UserEx", function($scope, $routeParams, User, UserEx) {
			
		$scope.type="all";
		$scope.percent = {};
		
		if(typeof($routeParams.id)!=='undefined' && $routeParams.id) {
			$scope.selfview=false;
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
						$scope.selfview = true;
					}
				},
				true
			);
		}
		else {
			$scope.selfview=true;
		}
		var id;
		if($scope.selfview) {
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					$scope.user = user;
					$scope.percent.positive = Math.round(($scope.user.data.statistics.numPositiveReceivedRatings / $scope.user.data.statistics.numReceivedRatings)*100);
					$scope.percent.negative = Math.round(($scope.user.data.statistics.numNegativeReceivedRatings / $scope.user.data.statistics.numReceivedRatings)*100);
					id=user.data.id;
		
					var data = UserEx.ratings.query({id:id}, function() {
						$scope.ratings = data.ratings;
						$scope.stats = data.stats;
						$scope.percent.neutral = 100 - ($scope.percent.positive + $scope.percent.negative);
					});
				},
				true
			);
		} else {
			var profile = UserEx.profile.query({id:$routeParams.id}, function() {
				$scope.percent.positive = Math.round((profile.statistics.numPositiveReceivedRatings / profile.statistics.numReceivedRatings)*100);
				$scope.percent.negative = Math.round((profile.statistics.numNegativeReceivedRatings / profile.statistics.numReceivedRatings)*100);
				id=$routeParams.id;
		
				var data = UserEx.ratings.query({id:id}, function() {
					$scope.ratings = data.ratings;
					$scope.stats = data.stats;
					$scope.percent.neutral = 100 - ($scope.percent.positive + $scope.percent.negative);
				});
			});
		}
	}])

});