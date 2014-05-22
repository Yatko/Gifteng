define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('adItemBox', function($compile) {
		return {
			restrict:'E',
			scope: {
				img:'@',
				imgSize:'@',
				status:'@',
				type:'@',
				creator:'=',
				id:'@',
				ad:'=',
				callback:'&',
				selfview:'=',
				distance:'@',
				inBookmarks:'@',
				nested:'@'
			},
			templateUrl: 'app/partials/directives/ad-item-box.html',
			replace:true,
			transclude:true,
			controller: function($scope, $location, UserEx, $modal, $routeParams, User, SharedService, AdImage, Amazon, Ad) {
				
				$scope.amazonUrl = Amazon;
				var ad = $scope.ad;
				if(ad.status=='SELECTED' || ad.status=="SOLD" || ad.status=="SENT"){
					if(angular.isArray(ad.requests.item)){
						var items = ad.requests.item;
						items.forEach(function(item){
							if((item.status == 'ACCEPTED' || item.status=="SENT" || item.status=="SOLD" || item.status=="RECEIVED") && item.accepted){
								if(typeof(item.user.avatar)!=='undefined')
									$scope.requestAvatar = item.user.avatar.id;
								
								$scope.requestFirstname = item.user.firstName;
								$scope.ad.request=item;
								$scope.itemId = item.id;
							}
						});
					}
					else{
						if(ad.requests.item.user.avatar)
							$scope.requestAvatar = ad.requests.item.user.avatar.id;
						$scope.itemId = ad.requests.item.id;
					}
				}
				
				$scope.viewInModal = function(id) {
					$location.search('view',id);
				}
				
				var reviewConfirm = function(id, toUser) {
					var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/review_confirm.html',
						controller: function($scope, $modalInstance) {
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.confirm = function() {
								$modalInstance.close();
								review(id,toUser);
							}
						}
					});
				}
				var review = function(id,toUser) {
					var callback=$scope.callback;
					var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/review.html',
						controller: function($scope, UserEx, $modalInstance) {
							var user = UserEx.profile.query({id:toUser}, function() {
								$scope.user = user;
							});
							$scope.submit = function() {
								UserEx.rateAd.query({id:id,value:$('#reviewValue').val(),text:$('#reviewText').val(),toUserId:toUser}, function() {
									$modalInstance.close();
									callback();
								});
							}
							$scope.close = function () {
								$modalInstance.close();
							};
						}
					});
				}
				
				$scope.review=review;
				
				/* giving modals */
				$scope.requestView = function(id, toUser) {
					var callback=$scope.callback;
					$scope.accepted = false;
					AdImage.setAd($scope.ad);
					AdImage.setCreator($scope.creatorId);
					var requestFirstname = $scope.requestFirstname;
					var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/request_view.html',
						controller: function($scope, UserEx, $modal, $modalInstance) {
						
							$scope.loaded=false;
						
							var ad = AdImage.getAd();
							if(ad.status ==  'SELECTED'){
								$scope.accepted = true;
							}
							var data = UserEx.requestView.query({id:id}, function() {
								$scope.data = data;
								AdImage.setRequest(data.request);
								$scope.loaded=true;
							});
							$scope.reviewConfirm = function() {
								reviewConfirm(id, toUser);
							};
							$scope.send = function() {
								UserEx.requestSend.query({id:id}, function() {
									$scope.close();
									callback();
								})
							}
							$scope.reportAd = function() {
								$scope.close();
								var decline = $scope.decline;
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/request_issue.html',
									controller: function($scope, UserEx, $modalInstance) {
										$scope.decline = function() {
											$modalInstance.close();
											decline();
										}
										
										//$scope.showcancel = (ad.pickUp && (!ad.shippingBox || typeof(ad.shippingBox)==='undefined'));
										
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
							$scope.accept = function() {
								UserEx.requestSelect.query({id:id}, function() {
									callback();
								
									var modalInstance = $modal.open({
										templateUrl: 'app/partials/directives/modal/show-shipping-and-pickup.html',
										controller: function($scope,$rootScope, UserEx, $modalInstance) {
											 $scope.showContactLater = false;
														$scope.showOk = false;
													$scope.description = '';
											  $scope.requestFirstname = AdImage.getRequest().user.firstName;												
																var ad = AdImage.getAd();
										
																				
											if(ad.freeShipping && ad.pickUp){
												$scope.description = "You can contact "+ $scope.requestFirstname +" to coordinate the exchange. If "+ $scope.requestFirstname +" pays for shipping through our system we'll email you a prepaid label. Simple as that :)";
												$scope.showContactLater = true;
												$scope.showOk = false;
											}else if(!ad.freeShipping && ad.pickUp){
												$scope.description = "Contact "+ $scope.requestFirstname +" now to coordinate the exchange.";
												$scope.showContactLater = true;
												$scope.showOk = false;
											}else{
												$scope.description = "As soon as "+ $scope.requestFirstname +" pays for shipping through our system we'll email you a prepaid label. Simple as that :)";
												$scope.showContactLater = false;
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
												var creatorId = AdImage.getCreator();
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
											};
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
				
				
				$scope.showRequest = function(id) {
					$location.search('req',id);
					$scope.requestView(id);
				}
				
				$scope.redeem = function(id) {
					var ad = $scope.ad;
					var creator = $scope.creator;
					var callback=$scope.callback;
					var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/redeem.html',
						controller: function($scope, $modalInstance) {
							$scope.close = function() {
								$modalInstance.close();
							};
							$scope.submit = function() {
								$modalInstance.close();
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/redeem_confirm.html',
									controller: function($scope, $modalInstance) {
										$scope.ad = ad;
										$scope.ad.creator = creator;
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

				$scope.viewReason = function(id) {
					var ad = $scope.ad;
					var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/view_reason.html',
						controller: function($scope, $modalInstance) {
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.ad=ad;
							
							
						}
					})
				}
				$scope.deleteGift = function(id) {
					var callback=$scope.callback;
				    var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/delete_gift.html',
						controller: function($scope, Ad, $modalInstance) {
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.submit = function() {
								Ad.remove({id:id}, function() {
									$modalInstance.close();
									callback();
								});
							}
						}
					});
				}
				$scope.relistGift = function(id) {
					var callback=$scope.callback;
				    var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/relist_gift.html',
						controller: function($scope, $modalInstance, $location) {
							$scope.ad = ad;
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.edit = function(){
								$modalInstance.close();
								$location.path('/edit/gift/'+id);
							};
							$scope.submit = function() {
								UserEx.relist.query({id:id}, function() {
									$modalInstance.close();
									callback();
								})
							}
						}
					});
				}
				
				/* receiving modals */
				$scope.requestHide = function(id) {
					UserEx.requestHide.query({id:id}, function() {
						$scope.callback();
					})
				}
				
				
				$scope.requestCancel = function(id) {
					var callback=$scope.callback;
				    var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/request_cancel.html',
						controller: function($scope, UserEx, $location, $modalInstance) {
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.submit = function() {
								UserEx.requestCancel.query({id:id}, function() {
									$modalInstance.close();
									callback();
								})
							}
						}
					});
				}
				$scope.requestReceive = function(id,toUser) {
					var ad = $scope.ad;
					var callback=$scope.callback;
					var requestFirstname = $scope.creatorFirstname;
				    var modalInstance = $modal.open({
						templateUrl: 'app/partials/directives/modal/request_receive.html',
						controller: function($scope, UserEx, $location, $modalInstance) {
							$scope.ad=ad;
							$scope.close = function () {
								$modalInstance.close();
							};
							$scope.submit = function() {
								UserEx.requestReceive.query({id:id}, function() {
									$modalInstance.close();
									callback();
									reviewConfirm(id, toUser);
								});
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
										
										//$scope.showcancel = (ad.pickUp && (!ad.shippingBox || typeof(ad.shippingBox)==='undefined'));
										
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
				
				/* favorites modals */
				$scope.requestGift = function(id) {
					var callback=$scope.callback;
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
									UserEx.requestAd.query({id:id, text:$('#request_message').val()}, function() {
										var user = UserEx.reinit.query({}, function() {
											User.setUser(user);
										});
										$modalInstance.close();
										callback();
									})
								}
							}
						});
					} else {
						UserEx.requestAd.query({id:id, text:''}, function() {
							var ad = Ad.get({id:$scope.id}, function() {
								$scope.redeem(ad.user_request.id);
							});
						});
					}
				}
				
				
				$scope.unbookmark = function(id) {
					UserEx.unbookmark.query({id:id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
						$scope.callback();
					});
				}
				
				var path = $location.$$protocol+"://"+$location.$$host;
				$scope.share_on_facebook_modal = function () {
					 SharedService.shareOnFacebook($scope.ad.title, path+'/#/view/gift/'+$scope.id, $scope.img);
										
				}
				$scope.share_on_twitter_modal = function () {
					SharedService.shareOnTwitter($scope.ad.title, path+'/#/view/gift/'+$scope.id, $scope.img);
					
				}
				$scope.share_on_pinterest_modal = function () {
					SharedService.shareOnPinterest($scope.ad.title, path+'/#/view/gift/'+$scope.id, $scope.img);
					
				}
				
				$scope.bookmark = function(id) {
					UserEx.bookmark.query({id:id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
							$scope.callback();
						});
					});
					$scope.numBookmarks++;
					$scope.inBookmarks=true;
				}
				var addcomment = function(comment) {
					if(typeof($scope.ad.comments.item)==='undefined') {
						$scope.ad.comments.item = [];
					}
					
					$scope.ad.comments.item.unshift(comment);
				}
				$scope.doComment = function (id) {
					var callback=$scope.callback;
				    var modalInstance = $modal.open({
  						templateUrl: 'app/partials/directives/modal/comment.html',
  						controller: function($scope, $modalInstance, UserEx, User) {
  							
							$scope.close = function(){
								$modalInstance.close();
							};
  							
  							$scope.add = function () {
  								var txt = $('#comment_text').val();
  								if(txt!='') {
	  								UserEx.comment.query({id:id,text:txt},function() {
	  									var user = User.getUser();
	  									var created = new Date().getTime();
	  									var comment = {
											'createdAt': created,
											'owner':true,
											'publisherAvatarUrl': user.data.avatar.url,
											'publisherFullName': user.data.firstName+" "+user.data.lastName,
											'text': txt

										};
										addcomment(comment);
									});
									$modalInstance.close();
								}
							};
  						}
  					});
				  }
				  $scope.doShare = function (id) {
					var adImage = {"title":$scope.ad.title,"id":$scope.ad.id,"img":$scope.img};
					AdImage.setAdImage(adImage);
					var callback=$scope.callback; 
					
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

                $scope.showMessage = function(){
                    var creatorId = $scope.creatorId;
                    var msgid = $scope.ad.request.id;
                    $scope.ad.request.numUnreadMessages = 0;

                    var modalInstance = $modal.open({
                        templateUrl: 'app/partials/directives/modal/show_gift_messages.html',
                        controller: function($scope, UserEx, $location, $modalInstance) {
                            $scope.creatorId = creatorId;
                            
                            function loadMsg() {
                            	var messages = UserEx.message.query({id: msgid}, function() {

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
			},
			compile: function compile() {
				return {
					pre: function(scope, iElement, iAttrs) {
			        	iAttrs.$observe('img', function() {
			        		if(!scope.imgSize) {
			        			scope.imgSize=320;
			        		}
							if(scope.img) {
								scope.img = scope.amazonUrl+'ad/'+scope.img+'_'+scope.imgSize;
							}
							else {
								scope.img = 'assets/img/ge-gift.png';
							}
						});
					},
					post: function(scope, iElement, iAttrs) {
						if(iAttrs.nested) {
							$('.well',iElement).removeClass('well');
						}
					}
			      }
			}
		}
	})
});