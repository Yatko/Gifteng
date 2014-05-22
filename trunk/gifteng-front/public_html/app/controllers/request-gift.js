define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('RequestGiftController', ["$scope", "$location", "$modal", "User", "UserEx", "AdDialog", function($scope, $location, $modal, User, UserEx, AdDialog) {
		$scope.user=User.getUser();
		$scope.doPost = function() {
			$scope.close();
    		AdDialog.close();
    		$location.search('view',null);
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/post.html',
				controller: function($scope, UserEx, $modalInstance) {
					$scope.isModal=true;
					$scope.close = function () {
						$modalInstance.close();
					};
				}
			});
		}
	}])

});