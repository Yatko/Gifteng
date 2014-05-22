define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('UserAdsController', ["$scope", "$modal", "$location", "$routeParams", "AdDialog", "User", "UserEx", "AdUser", "Filters", "$window", function($scope, $modal, $location, $routeParams, AdDialog, User, UserEx, AdUser, Filters, $window) {
			
		Filters.setMyGifts("all");
		
		$scope.$watch(
			function() {
				return Filters.getMyGifts();
			},
			function(mygifts) {
				$scope.mygifts = "filter-"+mygifts;
			},
			true
		);
		
		$scope.$on('$locationChangeSuccess', function(event) {
			if(typeof($routeParams.view)!=='undefined') {
				AdDialog.open($routeParams.view, $scope);
			}
		});
		var ads;
		$scope.cols=[];
		
		var toCols = function() {			
			var c=0;
			for(var i=0;i<$scope.numberColumns;i++) {
				$scope.cols[i]=[];
			}
			for(var i=0;i<ads.length;i++) {
				$scope.cols[c].push(ads[i]);
				c++;
				if(c==$scope.numberColumns) {
					c=0;
				}
			}
		}
		
		
		$scope.columnCheck = function() {
			if(window.innerWidth<480) {
				return 1;
			} else if(window.innerWidth<768) {
				return 2;
			} else if(window.innerWidth<992) {
				return 3;
			} else {
				return 4;
			}
		}
		$scope.distributeColumns = function() {
			$scope.numberColumns=$scope.columnCheck();
			if($scope.colsClass!="col-xs-"+(12/$scope.numberColumns)) {
				$scope.colsClass = "col-xs-"+(12/$scope.numberColumns);
			}
		}

		angular.element($window).bind('resize',function(){
        	$scope.$apply(function() {
				$scope.distributeColumns();
            });
		});
		
		$scope.loadAds = function() {
			var user = UserEx.reinit.query({}, function() {
				User.setUser(user);
			});
			
			$scope.distributeColumns();
			
			if(typeof($routeParams.id)!=='undefined') {
				$scope.selfview=false;
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
							$scope.selfview = true;
						}
					},
					true
				);
				ads = AdUser.query({id:$routeParams.id}, function() {
					$scope.loaded=true;
					$scope.hasAds = ads.length>0;
					toCols();
				});
			}
			else {
				$scope.selfview=true;
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data) !== 'undefined') {
							ads = AdUser.query({id:user.data.id}, function() {
								$scope.loaded=true;
								$scope.hasAds = ads.length>0;
								toCols();
							});
						}
					},
					true
				);
			}
		};
		$scope.loadAds();
		$scope.callback = $scope.loadAds;
		
		
		$scope.doPost = function() {
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
		
		if(typeof($routeParams.req)!=='undefined') {
			var id=$routeParams.req;
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/request_view.html',
				controller: function($scope, UserEx,AdImage, $modalInstance) {
					$scope.loaded=false;
					var data = UserEx.requestView.query({id:id}, function() {
						$scope.data = data;
						var ad = data.ads;
						AdImage.setAd(ad);
						AdImage.setRequest(data.request);
						if(ad.status ==  'SELECTED'){
							$scope.accepted = true;
						} else {
							$scope.accepted = false;
						}
						$scope.loaded=true;
					});
					$scope.send = function() {
						UserEx.requestSend.query({id:id}, function() {
							$scope.close();
							callback();
						})
					}
					$scope.accept = function() {
						
						UserEx.requestSelect.query({id:id}, function() {
													
							var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/show-shipping-and-pickup.html',
									controller: function($scope, UserEx,AdImage, $modalInstance) {
										$scope.showContactLater = false;
										$scope.showOk = false;
										$scope.description = '';
										$scope.requestFirstname = AdImage.getRequest().user.firstName;												
										var ad = AdImage.getAd();										
																			
										if(ad.freeShipping && ad.pickUp){
											$scope.description = "You can contact "+ $scope.requestFirstname +" to coordinate the exchange. If "+ $scope.requestFirstname +" pays for shipping through our system we'll email you a prepaid label. Simple as that :)";
											$scope.showOk = false;
										} else if(!ad.freeShipping && ad.pickUp) {
											$scope.description = "Contact "+ $scope.requestFirstname +" now to coordinate the exchange.";
											$scope.showOk = false;
										} else {
											$scope.description = "As soon as "+ $scope.requestFirstname +" pays for shipping, through our system, we will email you a prepaid label. Simple as that! :)";
											$scope.showOk = true;
										}
										$scope.later = function() {											
												$modalInstance.close();
												later();
										}
										
										$scope.close = function () {
											$modalInstance.close();
										};
										$scope.showMessage = function(){											
											var ad = AdImage.getAd();
											var creatorId = ad.creator.id;
											var msgid = AdImage.getRequest().id;												

											var modalInstance = $modal.open({
												templateUrl: 'app/partials/directives/modal/show_gift_messages.html',
												controller: function($scope, UserEx, $location, $modalInstance) {
													$scope.creatorId = creatorId;
													
													function loadMsg() {
														var messages = UserEx.message.query({id: msgid}, function() {
															if(typeof messages[0] != 'undefined' && messages[0] != null){
																if(messages[0].owner) {
																	var fromId=messages[0].toId;
																}
																else {
																	var fromId=messages[0].fromId;
																}
																var from = UserEx.profile.query({id:fromId}, function() {
																	$scope.messages = messages;
																	$scope.from = from;
																});
															}
														});
													}
													
													loadMsg();

													$scope.close = function () {
														$modalInstance.close();
													};

													
													$scope.sendMsg = function() {
														var message = new UserEx.message();
														message.text = $('#text_msg').val();
														message.requestId = $scope.messages[0].requestId;
														if($scope.messages[0].owner)
														message.toId = $scope.messages[0].toId;
														else
														message.toId = $scope.messages[0].fromId;
														
														message.$save(function() {
															$('#text_msg').val('');
															loadMsg();
														});
													};
												}
											});
										}
									}
								});
								var later = function(){
									$scope.close();
								};
							
						});
					}
					$scope.decline = function() {
						UserEx.requestCancel.query({id:id}, function() {
							$scope.close();
							callback();
						})
					}
					
			    	$scope.sendMsg = function() {
			    		var message = new UserEx.message();
			            message.text = $('#text_msg').val();
			            message.requestId = id;
			            if($scope.data.messages[0].owner)
			            message.toId = $scope.data.messages[0].toId;
			            else
			            message.toId = $scope.data.messages[0].fromId;
			            
			            message.$save(function() {
							var data = UserEx.requestView.query({id:id}, function() {
								$('#text_msg').val('');
								$scope.data = data;
							});
			            });
			    	};
					$scope.close = function () {
						$location.search('req',null);
						$modalInstance.close();
					};
				}
			})
		}
	}])

});