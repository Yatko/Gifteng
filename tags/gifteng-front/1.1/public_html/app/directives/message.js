define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('message', function() {
		return {
			restrict:'E',
			templateUrl: 'app/partials/directives/message.html',
			scope: {
  				ngModel: '=',
  				callback: '&'
			},
			replace:true,
			require: 'ngModel',
		    controller: function($scope, Amazon, UserEx) {
		    	$scope.amazonUrl = Amazon;
		    	$scope.sendMsg = function() {
		    		var message = new UserEx.message();
		            message.text = $('#text_msg').val();
		            message.requestId = $scope.ngModel.messages[0].requestId;
		            if($scope.ngModel.messages[0].owner)
		            message.toId = $scope.ngModel.messages[0].toId;
		            else
		            message.toId = $scope.ngModel.messages[0].fromId;
		            
		            message.$save(function() {
		            	$('#text_msg').val('');
		            	$scope.callback({reqid:$scope.ngModel.messages[0].requestId});
		            });
		    	};
		    }
		}
	})
});