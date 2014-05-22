define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ConfirmController', ["$scope", "$routeParams", "UserEx", function($scope, $routeParams, UserEx) {
		var code = $routeParams.confirm;
		UserEx.verifyUser.query({'code':code});
	}])

});