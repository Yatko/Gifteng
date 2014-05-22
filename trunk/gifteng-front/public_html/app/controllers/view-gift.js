define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ViewGiftController', ["$scope", "$modal", "$routeParams", "$location", "$timeout", "Ad", function($scope, $modal, $routeParams, $location, $timeout, Ad) {
			
		var showMap = function() {
			if($scope.ad.type=="MEMBER") {
				try {
					var loc = [$scope.ad.address.latitude, $scope.ad.address.longitude];
	        		var locationIcon = new L.icon({
		                iconUrl: 'assets/img/ge-location-pin-teal.png',
		                iconSize: [64, 64]
		            });
		            
		            var locationMarker = new L.marker(loc, {
		                icon: locationIcon
		            });
		            
		            var tileLayer = new L.tileLayer.provider('Esri.WorldStreetMap');
		
		            var map = new L.map('view_map').setView([$scope.ad.address.latitude, $scope.ad.address.longitude], 16);
		            tileLayer.addTo(map);
		            locationMarker.addTo(map);
				}
				catch ( ex ) {
		        }
	       	}
		}
		
		$scope.close = function() {
			$location.path('/browse');
		}
		$scope.viewGift = function() {
			if($scope.inModal) {
				var adid = $scope.ad_id;
			}
			else {
				var adid = $routeParams.id;
			}
			$scope.ad = Ad.get({id:adid}, function() {
				if(typeof($scope.ad.comments)!=='undefined' && typeof($scope.ad.comments.comment)!=='undefined') {
					if(typeof($scope.ad.comments.comment.id)!=='undefined')
						$scope.comments = $scope.ad.comments;
					else
						$scope.comments = $scope.ad.comments.comment;
				}
				else {
					$scope.comments=[];
				}
					
				$scope.ad = $scope.ad.ad;
				$timeout(showMap, 100);
			});
		};
		
		$scope.requestGift = function(id, callback) {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/request_create.html',
				controller: function($scope, UserEx, User, $location, $modalInstance) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.submit = function() {
						UserEx.requestAd.query({id:id, text:$('#request_message').val()}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
							$modalInstance.close();
							callback();
							$location.path('/profile/gifts/receiving');
						})
					}
				}
			});
		}
		$scope.requestCancel = function(id) {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/request_cancel.html',
				controller: function($scope, UserEx, $location, $modalInstance) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.submit = function() {
						UserEx.requestCancel.query({id:id}, function() {
							$modalInstance.close();
							$location.path('/profile/gifts/receiving');
						})
					}
				}
			});
		}
		$scope.requestReceive = function(id) {
			var ad = $scope.ad;
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/request_receive.html',
				controller: function($scope, UserEx, $location, $modalInstance) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.submit = function() {
						UserEx.requestReceive.query({id:id}, function() {
							$modalInstance.close();
							$location.path('/profile/gifts/receiving');
						})
					}
					$scope.accepted = (ad.reqStat=='ACCEPTED');
					$scope.reportAd = function() {
						$scope.close();
						var submit = $scope.submit;
						var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/request_issue.html',
							controller: function($scope, UserEx, $modalInstance) {
								$scope.confirm=true;
								$scope.submit = function() {
									UserEx.requestCancel.query({id:id}, function() {
										$modalInstance.close();
									});
								}
								
								$scope.showcancel = (ad.pickUp && (!ad.shippingBox || typeof(ad.shippingBox)==='undefined'));
								
								$scope.firstname = requestFirstname;
								$scope.description = "Hi Gifteng, I'm contacting you regarding this gift ("+ad.id+"), ....type your message here";
								$scope.send = function() {
									UserEx.requestIssue.query({id:id, text:$scope.description}, function() {
										$scope.close();
										var modalInstance = $modal.open({
											templateUrl: 'app/partials/directives/modal/request_issue_response.html',
											controller: function($scope, $modalInstance) {
												$scope.close = function () {
													$modalInstance.close();
												};
											}
										});
									});
								}
								$scope.close = function () {
									$modalInstance.close();
								};
							}
						});
					}
				}
			});
		}
		$scope.editGift = function(id, callback) {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/edit_gift.html',
				controller: function($scope, $modalInstance, $location) {
					$scope.close = function () {
						$modalInstance.close();
						
					};
					$scope.edit = function(){
						$modalInstance.close();
						callback();
						$location.path('/edit/gift/'+id+'/true');
					}
				}
			});
		}
		$scope.deleteGift = function(id, callback) {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/delete_gift.html',
				controller: function($scope, Ad, $location, $modalInstance) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.submit = function() {
						Ad.remove({id:id}, function() {
							$modalInstance.close();
							callback();
							$location.path('/profile/gifts/giving');
						});
					}
				}
			});
		}
		$scope.relistGift = function(id, callback) {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/relist_gift.html',
				controller: function($scope, $modalInstance, $location) {
					$scope.close = function () {
						$modalInstance.close();
					};
					$scope.submit = function() {
						UserEx.relist.query({id:id}, function() {
							$modalInstance.close();
							callback();
							$location.path('/profile/gifts/giving');
						})
					}
				}
			});
		}
		
		$scope.viewGift();
	}]);

});