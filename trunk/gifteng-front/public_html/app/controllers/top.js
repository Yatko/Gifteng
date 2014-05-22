define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('TopController', ["$scope", "Top", function($scope, Top) {
		var top = Top.query({},function() {
			$scope.users = top;
		});
	}])

});