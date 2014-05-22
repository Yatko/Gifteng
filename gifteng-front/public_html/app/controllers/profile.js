define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ProfileController', ["$scope", "$routeParams", "User", function($scope, $routeParams, User) {
		if(typeof($routeParams.id)!=='undefined' && $routeParams.id) {
			$scope.self = false;
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user.data) !== 'undefined') {
						if(user.data.id == $routeParams.id) {
							$scope.self = true;
						}
					}
				},
				true
			);
		}
		else {
			$scope.self = true;
		}
		
		if(!$routeParams.profilePage && !$routeParams.section) {
			$scope.section="";
			$scope.tab_page="";
			if(!$scope.self) {
				$scope.section="gifts";
				$scope.tab_page="giving";
			}
		}
		else if(!$routeParams.profilePage) {
			$scope.isin=true;
			$scope.section=$routeParams.section;
			if($routeParams.section=="gifts") {
				$scope.tab_page="giving";
			} else if($routeParams.section=="connections") {
				$scope.tab_page="following";
			} else if($routeParams.section=="account") {
				$scope.tab_page="notifications";
			}else if($routeParams.section=="settings") {
				$scope.tab_page="settings";
			}
			
		}
		else {
			$scope.isin=true;
			$scope.section=$routeParams.section;
			$scope.tab_page=$routeParams.profilePage;
		}
		
	}])

});