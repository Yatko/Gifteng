define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('RequestedAdsController', ["$scope", "$routeParams", "User", "UserEx", "AdRequested", "AdDialog", "GeModel", "Filters", "$window", function($scope, $routeParams, User, UserEx, AdRequested, AdDialog, GeModel, Filters, $window) {
		
		Filters.setMyGifts("all");
		
		$scope.$watch(
			function() {
				return Filters.getMyGifts();
			},
			function(mygifts) {
				$scope.mygifts = "filter-"+mygifts;
			},
			true
		);
		
		$scope.$on('$locationChangeSuccess', function(event) {
			if(typeof($routeParams.view)!=='undefined') {
				AdDialog.open($routeParams.view, $scope);
			}
		});
		
		var ads;
		$scope.cols=[];
		
		var toCols = function() {		
			var c=0;
			for(var i=0;i<$scope.numberColumns;i++) {
				$scope.cols[i]=[];
			}
			for(var i=ads.ads.length-1;i>=0;i--) {
				$scope.cols[c].push(ads.ads[i]);
				c++;
				if(c==$scope.numberColumns) {
					c=0;
				}
			}
		}
		
		
		$scope.columnCheck = function() {
			if(window.innerWidth<480) {
				return 1;
			} else if(window.innerWidth<768) {
				return 2;
			} else if(window.innerWidth<992) {
				return 3;
			} else {
				return 4;
			}
		}
		$scope.distributeColumns = function() {
			$scope.numberColumns=$scope.columnCheck();
			if($scope.colsClass!="col-xs-"+(12/$scope.numberColumns)) {
				$scope.colsClass = "col-xs-"+(12/$scope.numberColumns);
			}
		}

		angular.element($window).bind('resize',function(){
        	$scope.$apply(function() {
				$scope.distributeColumns();
            });
		});
		
		$scope.loadAds = function() {
			$scope.loaded=false;
			var user = UserEx.reinit.query({}, function() {
				User.setUser(user);
			});
			
			$scope.distributeColumns();
			
			if(typeof($routeParams.id)!=='undefined') {
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
				ads = AdRequested.query({id:$routeParams.id}, function() {
					$scope.ads = ads.ads;
					$scope.creators = ads.creators;
					GeModel.creators = ads.creators;
					$scope.loaded=true;
					$scope.hasAds = $scope.ads.length>0;
					toCols();
					
				});
			}
			else {
				$scope.selfview=true;
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data) !== 'undefined') {
							ads = AdRequested.query({id:user.data.id}, function() {
								$scope.ads = ads.ads;
								$scope.creators = ads.creators;
								GeModel.creators = ads.creators;
								$scope.loaded=true;
								$scope.hasAds = $scope.ads.length>0;
								toCols();
							});
						}
					},
					true
				);
			}
		};
		$scope.loadAds();
		$scope.callback = $scope.loadAds;
	}])

});