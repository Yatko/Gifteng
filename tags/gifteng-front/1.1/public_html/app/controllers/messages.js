define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('MessagesController', ["$scope", "$routeParams", "$location", "UserEx", "User", function($scope, $routeParams, $location, UserEx, User) {
		$scope.user = User.getUser();
		$scope.loaded = false;
		$scope.messages = UserEx.message.query({}, function() {
			if($scope.messages.length) {
				$scope.hasMsg = true;
			} else {
				$scope.hasMsg = false;
			}
			$scope.loaded=true;
		});
		$scope.showMessage = false;
		
		
		var openmsg = function(id) {
			$scope.msg = id;
			$scope.showMessage = true;
			var message = UserEx.message.query({id:id}, function () {
				if(message[0].owner) {
					var fromId=message[0].toId;
				}
				else {
					var fromId=message[0].fromId;
				}
				var from = UserEx.profile.query({id:fromId});
				$scope.current_message = {
					messages: message,
					from: from
				};
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				var msgs = UserEx.message.query({}, function() {
					$scope.messages = msgs;
					if($scope.messages.length) {
						$scope.hasMsg = true;
					} else {
						$scope.hasMsg = false;
					}
					$scope.loaded=true;
				});
			});
		}
		$scope.openmsg = function(id) {
			$location.search('msg',id);
			openmsg(id);
		}
		$scope.closemsg = function() {
			$location.search('msg',null);
			$scope.msg=null;
			$scope.showMessage=false;
		}
		if(typeof($routeParams.msg)!=='undefined') {
			$scope.openmsg($routeParams.msg);
		}
		$scope.hidemsg = function(id) {
			UserEx.messageHide.query({id:id}, function() {
				var msgs = UserEx.message.query({}, function() {
					$scope.messages = msgs;
					if($scope.messages.length) {
						$scope.hasMsg = true;
					} else {
						$scope.hasMsg = false;
					}
					$scope.loaded=true;
				});
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
			});
		}
	}])

});