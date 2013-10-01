define(['angular','services'], function(angular) {
	'use strict';
	
	return angular.module('gifteng.controllers', ['gifteng.services'])
		
		/**
		 * Index Controller
		 */
		.controller('IndexController', function() {
			
		})
		
		/**
		 * Browse Controller
		 */
		.controller('BrowseController', function($scope, Ad) {
			var ads = Ad.query({}, function() {
				$scope.col1 = [];
				$scope.col2 = [];
				$scope.col3 = [];
				
				for(var i=0;i<ads.length;i++) {
					if((i%3)==0) {
						$scope.col3.push(ads[i]);
					}
					else if((i%2)==0) {
						$scope.col2.push(ads[i]);
					}
					else {
						$scope.col1.push(ads[i]);
					}
				}
			});
		})
		
		/**
		 * Nav Controller
		 */
		.controller('NavController',function($scope, $location, Auth, User) {
		
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					$scope.user = user;
				},
				true
			);
			
			$scope.logout = function() {
				$('#open-left').click();
				var user = new Auth.remove({id:0});
				User.setUser({});
				$location.path('/');
			}
		})
		
		/**
		 * Login Controller
		 */
		.controller('LoginController', function($scope, $location, Auth, User) {
			$scope.login = function() {
				var auth = new Auth;
				auth.email = $('#email').val();
				auth.password = $('#password').val();
				auth.$save(function(response) {
					var user = new Auth.get();
					User.setUser(user);
					$location.path('/browse');
				});
			}
		})
		
		/**
		 * Profile Controller
		 */
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
		
		/**
		 * View -> Profile Controller
		 */
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
