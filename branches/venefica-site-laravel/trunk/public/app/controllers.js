define(['angular','services'], function(angular) {
	'use strict';
	
	return angular.module('gifteng.controllers', ['gifteng.services'])
	
		.controller('IndexController', function($scope, $routeParams, $route, $location, Contact) {
			
		})
		.controller('ProfileController', function($scope, $routeParams) {
			if(!$routeParams.profilePage && !$routeParams.section) {
				$scope.section="gifts";
				$scope.tab_page="giving";
			}
			else if(!$routeParams.profilePage) {
				$scope.section=$routeParams.section;
				if($routeParams.section=="gifts") {
					$scope.tab_page="giving";
				} else if($routeParams.section=="connections") {
					$scope.tab_page="following";
				} else if($routeParams.section=="account") {
					$scope.tab_page="notifications";
				}
			}
			else {
				$scope.section=$routeParams.section;
				$scope.tab_page=$routeParams.profilePage;
			}
		})
		.controller('ViewProfileController', function($scope, $routeParams) {
			
			$scope.profile = {
				img: "http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-no-profile-picture.png",
				name: "Krasimir Stavrev",
				points: "100",
				location: "Sofia, Bulgaria",
				since: "August 2013"
			};
			
			if(!$routeParams.profilePage && !$routeParams.section) {
				$scope.section="gifts";
				$scope.tab_page="giving";
			}
			else if(!$routeParams.profilePage) {
				$scope.section=$routeParams.section;
				if($routeParams.section=="gifts") {
					$scope.tab_page="giving";
				} else if($routeParams.section=="connections") {
					$scope.tab_page="following";
				}
			}
			else {
				$scope.section=$routeParams.section;
				$scope.tab_page=$routeParams.profilePage;
			}
		});
});
