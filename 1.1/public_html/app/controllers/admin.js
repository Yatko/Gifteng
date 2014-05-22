define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('AdminController', ["$scope", "$location", "Auth", "User", "AdminService", "Amazon", function($scope, $location, Auth, User,AdminService, Amazon) {
		$scope.user = User.getUser();
		$scope.showGiftDashboard = false;
		$scope.showShippingDashboard = false;
		var shippingLabels = new Array();
		
		$scope.giftDashboard = function(){
			$scope.showShippingDashboard = false;
			AdminService.getUnApproved.query(function(data){
				if(data.role != 'user'){
					if(typeof data.ads.ad != 'undefined' && data.ads.ad != null){					
							$scope.ads = data.ads.ad;	
							$scope.showGiftDashboard = true;					
							
					}else{
						$scope.showGiftDashboard = false;
					}
				}else{
					$location.path('/signin');						
				}							
			});
		};			
		
		$scope.approve_ad = function(adId){
			AdminService.approved.query(
			{
				'ad_Id':adId,
			}
			,function(data){			
						
			});
		};
		$scope.unapprove_ad = function(adId){			
			AdminService.unapproved.query(
			{
				'ad_Id':adId,
			  'reason' :$('#reason_'+adId).val()
			},
			function(data){
				if(data.status == 'valid'){
					var ads = $scope.ads;
					 angular.forEach(ads, function(v, index){
						if(v.id == adId){
							ads.splice(ads.indexOf(v), 1);
							$scope.ads = ads;	 
						}
					});
				}
							
			});
		};
		$scope.online_ad = function(adId){
			AdminService.online.query(
			{				
				'ad_Id':adId
			},function(data){
			/*	if(data.status == 'valid'){
					var ads = $scope.ads;
					 angular.forEach(ads, function(v, index){
						if(v.id == adId){
							ads.splice(ads.indexOf(v), 1);
							$scope.ads = ads;	 
						}
					});
				}*/
							
			});
		};
		var updateShippingBoxLabelUrl = function(shipping){				
			if(shipping.length > 1){
				for(var i = 0; i < shipping.length; i++){
					if(typeof shipping[i].barcodeImage != 'undefined'){							
						shipping[i].fileUrl = Amazon+"shipping/"+shipping[i].barcodeImage.id;							
					}
					if(!shipping[i].trackingNumber && !shipping[i].receivedAmount && !shipping[i].barcodeImage){
						shipping[i].showUpdate = true;
					}else{
						shipping[i].showUpdate = false;
					}
				}
			}
			else{
				shipping.shipping.fileUrl = Amazon+"shipping/"+shipping.shipping.barcodeImage.id;					
				if(!shipping.shipping.trackingNumber && !shipping.shipping.receivedAmount && !shipping.shipping.barcodeImage){
						shipping.shipping.showUpdate = true;
					}else{
						shipping.shipping.showUpdate = false;
					}
			}
		};
		$scope.shippingDashboard = function(){				
			$scope.showGiftDashboard = false;				
			AdminService.getShippings.query(function(data){
				if(data.role != 'user'){
					if(typeof data.shippings != 'undefined' && data.shippings != null){					
							if(data.shippings.shipping.length >1){
								$scope.ads = data.shippings.shipping;	
							}else{
								$scope.ads = data.shippings;
							}
							updateShippingBoxLabelUrl($scope.ads);
							$scope.showShippingDashboard = true;					
						
					}else{
						$scope.showShippingDashboard = false;
					}
				}else{
					$location.path('/signin');						
				}							
			});
		};
		
		$scope.sendEmailToSendor = function(shippingId){
			AdminService.sendEmail.query(
				{
					'shippingId':shippingId,
					'to':'creator'
				}
				,function(data){			
				
			});
		};
		$scope.sendEmailToReceiver = function(shippingId){
			AdminService.sendEmail.query(
				{
					'shippingId':shippingId,
					'to':'receiver'
				}
				,function(data){			
				
			});
		};
		$scope.sendAgainEmailToSendor = function(shippingId){
			AdminService.sendEmail.query(
				{
					'shippingId':shippingId,
					'to':'creator'
				}
				,function(data){			
				
			});
		};
		$scope.sendAgainEmailToReceiver = function(shippingId){
			AdminService.sendEmail.query(
				{
					'shippingId':shippingId,
					'to':'receiver'
				}
				,function(data){			
				
			});
		};
		$scope.deleteShipping = function(shippingId){
			AdminService.deleteShipping.query(
			{
				'shippingId':shippingId,
			}
			,function(data){			
				$scope.shippingDashboard();
			});
		};
		
		$scope.updateShipping = function(shipping){	
			var shippingBox = {};
			
			shippingBox.shippingId = shipping.id;
			shippingBox.trackingNumber = shipping.trackingNumber;
			shippingBox.receivedAmount = shipping.receivedAmount
			angular.forEach(shippingLabels, function(value , key) {
				if(value.shippingId == shipping.id){
					shippingBox.barcodeImage = {};
					shippingBox.barcodeImage.url = value.fileUrl;
				}
			});							
			
			AdminService.updateShipping.query(shippingBox
				, function() {
		
				});
		};

		$scope.sendFile = function(el) {
	
			$scope.action="api/image";
			var $form = $(el).parents('form');
			
			if ($(el).val() == '') {
				return false;
			}					
			var idar = $(el).attr('id').split('-');
			var id = idar[1];
			var extension = el.value.replace(/^.*\./, '');

			if (extension == el.value) {
				extension = '';
			} else {						
				extension = extension.toLowerCase();
			}
			if(extension!= 'pdf'){
				return false;
			}
			 
			$form.attr('action', $scope.action);						

			$form.ajaxSubmit({
				type: 'POST',
				uploadProgress: function(event, position, total, percentComplete) {								

				},
				error: function(event, statusText, responseText, form) {									
					$form.removeAttr('action');					
				},
				success: function(responseText, statusText, xhr, form) {                             
					if(typeof(responseText.error)==='undefined') {
						var ar = $(el).val().split('\\'), 
						filename =  ar[ar.length-1];								   
						$form.removeAttr('action');		                    
						
						$scope.$apply(function() {																			
							$scope.image = "api/image/"+filename;									
							angular.forEach($scope.ads, function(value , key) {
								if(value.id == id){
									value.fileName = filename;
									value.fileUrl = "api/image/"+filename;
								}
							});										
							 shippingLabels.push({'shippingId':id,'fileUrl':"api/image/"+filename});
						});
					}									
				}
			});
		};			
		
	}])

});