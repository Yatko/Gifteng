define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('InviteController', ["$scope", "Facebook", "WindowService", "TestEmailService", "User", function($scope, Facebook, WindowService, TestEmailService, User) {
			
		$scope.searchInvites = '';
		$scope.mailTo ='';
		$scope.user = User.getUser();
		var invitationCode='2550'; //Let's define it here
		$scope.$watch(function() {
			return $scope.user;
		}, function(user) {
			if(typeof(user.data)!=='undefined') {
				$scope.mailTxt ='Have you heard about Gifteng?\nIt\'s a new community where you can discover amazing gifts and give away things you no longer need. Get your first free $15 gift card by simply joining me on Gifteng. http://gifteng.com/#/?ref='+$scope.user.data.id;
			}
		},
		true)
		$scope.sendMsg = function(){
			FB.ui({
				method: 'send',
				link: 'http://gifteng.com',
			});
		}
		$scope.sendTwitterMessage = function(){
			var width = 575;
			var height = 450;
			WindowService.open_window_clear('https://twitter.com/share?text=' + 'Check out @gifteng, a unique social community for free stuff and giveaways.', '', width, height);
		}
		$scope.sendMail = function(){
			$scope.error = '';
			$scope.success = '';
			var recepiantsAddress = $scope.mailTo.split(",");
			var body = $scope.mailTxt;
			var recepiants = [];
			// TODO validation email addresses
						
			angular.forEach(recepiantsAddress, function(recepiant){
				if(!TestEmailService.isValid(recepiant)){
					$scope.error += recepiant+' ';
					return true;
				}
				this.push(recepiant);
			}, recepiants);
			if($scope.error){
				$scope.success = '';
				$scope.error = "Please enter a valid email address and if you enter multiple emails, separate them with comma (no spaces).";
				return true;
			}
			TestEmailService.sendMail.send({recepiants:recepiants,body:body},function(data){
				if(data.status == 'valid'){					
					$scope.error = '';
					$scope.success = "Invitation sent successfully!";
				}else{
					$scope.success = '';
					$scope.error = "Please enter a valid email address and if you enter multiple emails, separate them with comma (no spaces).";
				}
				
			});
			
		}
		$scope.friends=[];
		$scope.$watch(function() {
			if(typeof(Facebook.getAuthResponse)==='function') {
				return Facebook.getAuthResponse();
			}
			else {
				return null;
			}
		},function(response){
			if(typeof(response)!=='undefined' && response!==null) {
				if(response.accessToken) {
					Facebook.api('/me/friends', function(r) {
						$scope.friends = r.data;
					});
				}
				else {
					Facebook.login();
				}
			}
		},
		true);
	}])

});