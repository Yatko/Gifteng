define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('ad', function() {
		return {
			restrict:'E',
			scope: {
				ad:'=',
				cmn:'=',
				self:'=',
				callback:'&',
				
				user:'@',
				action:'@',
				image:'@',
				title:'@',
				details:'@',
				map:'@',
				comments:'@',
				addcomment:'@'
			},
			templateUrl: 'app/partials/directives/ad.html',
			replace:true,
			transclude:true,
			controller: function($scope, $modal, $location, $timeout, UserEx, User, AdDialog, AdImage, Amazon, Ad) {
				
				$scope.amazonUrl = Amazon;
				var callback=$scope.callback;
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
				
				if($scope.map) {
					$timeout(showMap, 100);
				}
				if($scope.action) {
					var updateButton = function() {
						$scope.ad.user_requested=true;
					};
					
					$scope.requestGift = function(id) {
						var ad = $scope.ad;
						
						if(!ad.free) {
						    var modalInstance = $modal.open({
								templateUrl: 'app/partials/directives/modal/request_create.html',
								controller: function($scope, UserEx, User, $location, $modalInstance) {
									$scope.close = function () {
										$modalInstance.close();
									};
									$scope.ad = ad;
									$scope.submit = function() {
										//TODO: Remove jQuery
										UserEx.requestAd.query({id:id, text:$('#request_message').val()}, function() {
											var user = UserEx.reinit.query({}, function() {
												User.setUser(user);
											});
											$modalInstance.close();
											$('.ge-ad#'+id+' .ge-item-image img').addClass('inactive');
											updateButton();
											callback();
											if(ad.promoCodeProvider!=null) {
												var modalInstance = $modal.open({
													templateUrl: 'app/partials/directives/modal/request_promo_response.html',
													controller: function($scope, User, $modalInstance) {
														$scope.close = function () {
															$modalInstance.close();
														};
														$scope.user = User.getUser();
													}
												})
											}
										})
									}
								}
							});
						} else {
							UserEx.requestAd.query({id:id, text:''}, function() {
								var ad = Ad.get({id:$scope.ad.id}, function() {
									if(typeof(ad.ad)!=='undefined') {
										$scope.ad = ad.ad;
										$scope.redeem(ad.ad.user_request.id);
									}
								});
							});
						}
					};

					$scope.redeem = function(id) {
						var ad = $scope.ad;
					var callback=$scope.callback;
						var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/redeem.html',
							controller: function($scope, $modalInstance) {
								$scope.ad = ad;
								$scope.close = function() {
									$modalInstance.close();
								};
								$scope.submit = function() {
									$modalInstance.close();
									var modalInstance = $modal.open({
										templateUrl: 'app/partials/directives/modal/redeem_confirm.html',
										controller: function($scope, $modalInstance) {
											$scope.ad = ad;
											$scope.close = function() {
												$modalInstance.close();
											};
											$scope.submit = function() {
												UserEx.redeem.query({id:id}, function() {
													$modalInstance.close();
													callback();
												});
											}
										}
									});
								}
							}
						})
					};

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
        								AdDialog.close();
										callback();
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
        								AdDialog.close();
										callback();
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
													callback();
												});
											}
											
											$scope.showcancel = (ad.pickUp && (!ad.shippingBox || typeof(ad.shippingBox)==='undefined'));
											
											$scope.firstname = requestFirstname;
											$scope.report={};
											$scope.report.description = "Hi Gifteng, I'm contacting you regarding this gift ("+ad.id+"), ....type your message here";
											$scope.send = function() {
												UserEx.requestIssue.query({id:id, text:$scope.report.description}, function() {
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
					$scope.editGift = function(id) {
					    var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/edit_gift.html',
							controller: function($scope, $modalInstance, $location) {
								$scope.close = function () {
									$modalInstance.close();
									
								};
								$scope.edit = function(){
									$modalInstance.close();
        							AdDialog.close();
									$location.path('/edit/gift/'+id+'/true');
								}
							}
						});
					}
					$scope.deleteGift = function(id) {
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
        								AdDialog.close();
										$location.path('/profile/gifts/giving');
									})
								}
							}
						});
					}
					$scope.relistGift = function(id) {
						var ad = $scope.ad;
					    var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/relist_gift.html',
							controller: function($scope, $modalInstance, $location) {
								$scope.ad = ad;
								$scope.close = function () {
									$modalInstance.close();
								};
								$scope.edit = function(){
									$modalInstance.close();
        							AdDialog.close();
									$location.path('/edit/gift/'+id);
								};
								$scope.submit = function() {
									UserEx.relist.query({id:id}, function() {
										$modalInstance.close();
										callback();
        								AdDialog.close();
										$location.path('/profile/gifts/giving');
									})
								}
							}
						});
					}
				}
				$scope.viewInModal = function() {
				}
				if($scope.comments) {
					$scope.addComment = function () {
						UserEx.comment.query({id:$scope.ad.id,text:$('#comment_text').val()},function() {
							$('#comment_text').val('');
							callback();
						});
					};
				}
				if($scope.image) {
					  $scope.doShare = function (id) {
						var adImage = {"title":$scope.title,"id":$scope.id,"img":$scope.img};
										
						AdImage.setAdImage(adImage);
						
					   var modalInstance = $modal.open({
      						templateUrl: 'app/partials/directives/modal/share.html',
      						controller: function($scope,$rootScope,$location,AdImage, $modalInstance,SharedService) {													
							
								$scope.close = function(){
									$modalInstance.close();
								};
								
								var path = $location.$$protocol+"://"+$location.$$host;
								
      							$scope.share_on_facebook_modal = function () {
      								 $modalInstance.close();
									 var adImage = AdImage.getAdImage();
									 
									 SharedService.shareOnFacebook(adImage.title, path+'/#/view/gift/'+adImage.id, adImage.img);
														
								};
								$scope.share_on_twitter_modal = function () {
      								var adImage = AdImage.getAdImage();									
									SharedService.shareOnTwitter(adImage.title, path+'/#/view/gift/'+adImage.id, adImage.img);
									$modalInstance.close();
								};
								$scope.share_on_pinterest_modal = function () {
									var adImage = AdImage.getAdImage();
      								SharedService.shareOnPinterest(adImage.title, path+'/#/view/gift/'+adImage.id, adImage.img);
									$modalInstance.close();
								};
      						}
      					});
					  };
					$scope.bookmark = function(id) {
						UserEx.bookmark.query({id:id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
							callback();
						});
						$scope.numBookmarks++;
						$scope.inBookmarks=true;
					}
				}
				  
			}
		}
	})
});