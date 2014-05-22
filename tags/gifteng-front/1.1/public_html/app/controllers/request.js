define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('RequestController', ["$scope", "$http", "$routeParams", "UserEx","InviteService","TestEmailService","IpService", function($scope, $http, $routeParams, UserEx,InviteService,TestEmailService,IpService) {
		//TODO: Remove jQuery
		$scope.error = "";
		$scope.step = "step1";
		var email = '';
		$scope.showOther = false;
		$scope.selectedItem="";
		$scope.country="";
		var source = ['Google','Facebook','Twitter','From a Friend','Other'];
		$scope.source = source;
		var ip = '';
		var country;
		var otherSource = '';
		
		$http.get('app/countries.json').success(function(data) {
			$scope.countries = data;
		});


		$scope.country = IpService.get();
		
		if($scope.country==null) {
			IpService.call().then(function (data) {
				IpService.set(data);
				ip = data.host;
				country = data.countryName;
			});
		} else {
			ip = $scope.country.ip;
			country = $scope.country.countryName;
		}
		
		$scope.changeValue = function(item){
			if(item == 'Other'){
				$scope.showOther = true;
			}else{
				$scope.showOther = false;
			}
		}
		
		$scope.step1 = function() {	
			//var email = $scope.email;	
				 email = $('#email').val();
			
			if(typeof email == 'undefined' || email == ''){					
				$scope.error = 'Please enter your email address';
				return false;
			}else {
				if (!TestEmailService.isValid(email)){						
					$scope.error = 'Please enter a valid email address ';
					return false;
				}else{
						$scope.error = "";
						$scope.step = "step2";
					  }
			}			
			
		}
		
		$scope.confirmRequest = function(){
			$scope.error = '';
			var source = $('#source').val();
			var country=$('#country').val();
			
			if(source == 'Other'){
				otherSource = $('#otherSource').val();
				if(otherSource == ''){
					$scope.error = 'Please enter the other source';
					return false;
				}
			}
			
			if(country == "" || country==null){
				$scope.error = 'Please select your country';
				return false;
			}
			if(source == "" || source==null){
				$scope.error = 'How did you hear about us?';
				return false;
			}
		
			
			if(country != '' && country!=null && source != '' && source!=null){
				var zip=0;
				if(country=="US") {
					zip=10001;
				}
				
				$scope.error =''; 
				InviteService.requestInvitation.query({
					'invite_email' : email,
					'ipAddress' : ip,
					'country' : country,
					'zipCode' : zip,
					'source' : source,
					'otherSource' : otherSource
				},function(data){				
						if(data.status == 'invalid'){
							$scope.error = 'Can not send Invitation now! ';
						}else{
							$scope.error = '';
							$scope.step="step3";
							
						}
				});
			}
		
		}
				
		
	}])

});