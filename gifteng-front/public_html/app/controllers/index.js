define(['controllers','wistia'], function(controllers) {
	'use strict';
	
	controllers.controller('IndexController', ["$scope", function($scope) {
		/*
		var wistiaEmbed = Wistia.embed("mqg1xki1su", {
            videoWidth: 580,
            videoHeight: 326,
            volumeControl: true,
            videoFoam: true
        });
*/
		$scope.playvideo = function() {
			$scope.showvideo = true;
		};
        
        $scope.scrollDown = function() {
			$(".snap-content").animate({ scrollTop: $('.ng-view').height() }, 2000);
        }
        $scope.scrollUp = function() {
			$(".snap-content").animate({ scrollTop: 0 }, 2000);
        }
	}])

});