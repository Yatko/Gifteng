define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('adDetailView',function() {
		return {
			restrict: 'E',
			templateUrl: 'app/partials/view/ad.html',
			replace:true,
			transclude:true,
			scope: {
				id:'@'
			},
			controller: function($scope, $location, Ad, AdDialog, Amazon) {
				
				$scope.amazonUrl = Amazon;
				
				$scope.callback = function() {
					$scope.viewGift();
					$scope.$parent.callback();
				};
				
				if(typeof($scope.$parent.filterAds)!=='undefined') {
					var ads = $scope.$parent.filterAds;
					var current = ads.indexOf(parseInt($scope.id));
					
					if(current>0)
						$scope.prev=ads[current-1];
					else
						$scope.prev=ads.length-1;
						
					if(current<ads.length-1)
						$scope.next=ads[current+1];
					else
						$scope.next=0;
				}
				
				$scope.move=function(id) {
					AdDialog.close();
					$location.search('view',id);
				}
				
				$scope.loaded = false;
				
				$scope.close = function() {
					$location.search('view',null);
					AdDialog.close();
				}
				$scope.viewGift = function() {
					var ad = Ad.get({id:$scope.id}, function() {
						$scope.ad = ad.ad;
						$scope.comments = ad.comments;
						$scope.loaded=true;
					});
				}
				
				$scope.viewGift();
				
			}
		}
	})
});