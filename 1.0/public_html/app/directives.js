define(['angular','services','jquery','jcrop'], function(angular,services,jQuery) {
	'use strict';
	
	return angular.module('gifteng.directives', ['gifteng.services'])
		.directive('loader', function() {
			return {
				restrict:'E',
				replace: true,
				templateUrl:'app/partials/directives/loader.html'
			}
		})
		.directive('checkbox', function(){
			return {
				scope: {
					ngModel:'=',
					name:'@',
					value:'@'
				},
				restrict: 'E',
				replace: true,
				transclude: true,
				template: '<div class="checkbox"><a class="icons" ng-click="change()" ng-class="{checked: ngModel}"><span class="first-icon glyphicon glyphicon-ge-checkbox-unchecked" ng-show="!ngModel"></span><span class="second-icon glyphicon glyphicon-ge-checkbox-checked" ng-show="ngModel"></span></a><input type="checkbox" name="{{name}}" value="{{value}}" ng-show="false" class="notifiable" ng-model="value.checked" ng-checked="value.checked"></div>',
				controller: function ($scope) {
					$scope.change = function() {
						$scope.ngModel=!$scope.ngModel;
						$scope.$parent.$parent.changed=true;
					}
				},
				compile: function compile() {
				}
			}
		})
        .directive('imgJcrop', function() {
            return {
                restrict : 'E',
                replace : true,
                scope : {
                    src : '@',
                    selected : '&',
                    ngModel : "="
                },
                link : function(scope, element, attr) {
                    var myImg;
                    var clear = function() {
                        if (myImg) {
                            myImg.next().remove();
                            myImg.remove();
                            myImg = undefined;
                        }
                    };
                    scope.$watch('src', function(nv) {
                        clear();
                        if (nv) {
                           // element.after("<img  class='img-responsive'/>");
                            myImg = element.next();
                            if(myImg.attr("id") === "imgplaceholder") {
                                myImg.remove();
                            }

                            element.after("<img  class='img-responsive jcimg upimg center-block'  id='imgplaceholder' style='width: none !important;'/>");
                            myImg = element.next();

                            myImg.attr('src', nv);
                            $(myImg).Jcrop({
                                addClass: 'jcrop-dark',
                                minSize:[112,112],
                                setSelect:   [ 250, 250, 112, 112 ],
                                bgColor: 'black', bgOpacity: 0.4, bgFade: true,
                                trackDocument : true,
								
                                onSelect : function(x) {
								$(".jcrop-keymgr").remove();
									scope.ngModel = x;
                                    scope.$apply(function() {
                                        scope.selected({
                                            cords : x
                                        });
                                    });
                                },
								
                                aspectRatio : 1
                            }, function() {
                                // Use the API to get the real image size
                                var bounds = this.getBounds();
                            });
							
							//$('input[type="radio"]').hide();
                        }else{
                            element.after("<img id='imgplaceholder' src='assets/img/ge-upload.png' class='img-responsive upimg center-block'/>");
                            //$scope.placeholderimg = element.next();
                        }
						$('#imgplaceholder').click(function() {
							$('input[type="file"]').click();
						});
                    });
                    scope.$on('$destroy', clear);
                }
            };
        })
	/*	.directive('shipping', function(){
			return {
				restrict: 'E',
				scope: {
					adId:'@',
					creatorFullName:'@',
					acceptedAt:'@',
					receiverFullName:'@',
					ad:'='
				},
				templateUrl:'app/partials/directives/shipping.html',
				replace:true,
				transclude:true,
				controller: function($scope,Auth, User,AdminService){
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
						
						if(typeof $scope.fileUrl != 'undefined'){
							shippingBox.barcodeImage = {};
							shippingBox.barcodeImage.url = $scope.fileUrl;
						}
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
										$scope.download = true;											
										$scope.image = "api/image/"+filename;
										$scope.fileUrl = "api/image/"+filename;
										$scope.fileName = filename;
								
									});
								}									
							}
						});
					}
					
				}
			}
		})   */
		.directive('adItemBox', function($compile) {
			return {
				restrict:'E',
				scope: {
					img:'@',
					imgSize:'@',
					status:'@',
					type:'@',
					creatorId:'@',
					creatorFirstname:'@',
					creatorLastname:'@',
					creatorSince:'@',
					creatorCity:'@',
					creatorAvatar:'@',
					creatorPoints:'@',
					creator:'=',
					id:'@',
					ad:'=',
					callback:'&',
					selfview:'@',
					distance:'@',
					inBookmarks:'@',
					nested:'@'
				},
				templateUrl: 'app/partials/directives/ad-item-box.html',
				replace:true,
				transclude:true,
				controller: function($scope, $location, UserEx, $modal, $routeParams, User, SharedService, AdImage, Amazon) {
					
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
							
								var ad = AdImage.getAd();
								if(ad.status ==  'SELECTED'){
									$scope.accepted = true;
								}
								var data = UserEx.requestView.query({id:id}, function() {
									$scope.data = data;
									AdImage.setRequest(data.request);
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
							controller: function($scope, $modalInstance, $location, Ad) {
							var ad = Ad.get({id:id}, function(){
								$scope.ad = ad.ad;
							});
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
					
					/* favorites modals */
					$scope.requestGift = function(id) {
						var callback=$scope.callback;
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
									})
								}
							}
						});
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
							});
						});
						$scope.numBookmarks++;
						$scope.inBookmarks=true;
					}
					$scope.doComment = function (id) {
						var callback=$scope.callback;
					    var modalInstance = $modal.open({
      						templateUrl: 'app/partials/directives/modal/comment.html',
      						controller: function($scope, $modalInstance) {
      							$scope.add = function () {
      								UserEx.comment.query({id:id,text:$('#comment_text').val()},function() {
										callback();
									});
									$modalInstance.close();
								};
								
								$scope.close = function(){
									$modalInstance.close();
								};
      						}
      					});
					  }
					  $scope.doShare = function (id) {
						var adImage = {"title":$scope.title,"id":$scope.id,"img":$scope.img};
										
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
		.directive('userProfile', function() {
			return {
				restrict:'E',
				scope: {
					id:'@',
					img:'@',
					name:'@',
					location:'@',
					since:'@',
					points:'@',
					nested:'@',
					following:'=',
					self:'=',
					avatar:'@',
					fullscore:'@',
					callback:'&',
					imgSize:'@',
					stats:'='
				},
				templateUrl: 'app/partials/directives/user-profile.html',
				replace:true,
				controller: function( $scope, $element, $attrs, UserEx, User, Amazon ) {
					$scope.follow = function() {
						$scope.data = UserEx.follow.query({id:$scope.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$scope.following=true;
					}
					$scope.unfollow = function() {
						$scope.data = UserEx.unfollow.query({id:$scope.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$scope.following=false;
					}
					$scope.amazonUrl = Amazon;
				},
				compile: function() {
					return {
						pre: function(scope, iElement, iAttrs) {
				        	iAttrs.$observe('img', function() {
				        		if(!scope.imgSize) {
				        			scope.imgSize=60;
				        		}
								if(scope.img) {
									scope.img = scope.amazonUrl+'user/'+scope.img+'_'+scope.imgSize;
								}
								else {
									scope.img = 'assets/img/ge-no-profile-picture.png';
								}
							});
						},
						post: function(scope, iElement, iAttrs) {
							if(iAttrs.nested) {
								$('.panel',iElement).removeClass('panel').removeClass('panel-default');
								$('.panel-body',iElement).removeClass('panel-body');
							}
						}
					}
				}
			}
		})
		.directive('ad', function() {
			return {
				restrict:'E',
				scope: {
					ad:'=',
					cmn:'=',
					self:'=',
					callback:'=',
					
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
				controller: function($scope, $modal, $location, $timeout, UserEx, User, AdDialog, AdImage) {
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
											$('.ge-ad#'+id+' .ge-item-image img').addClass('inactive');
											updateButton();
											callback();
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
							});
							$scope.numBookmarks++;
							$scope.inBookmarks=true;
						}
					}
					  
				}
			}
		})
		.directive('message', function() {
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
		.directive('backButton', function(){
		    return {
		      restrict: 'A',
		
		      link: function(scope, element, attrs) {
		        element.bind('click', goBack);
		
		        function goBack() {
		          history.back();
		          scope.$apply();
		        }
		      }
		    }
		})
		.directive('giftimg', function() {
			return {
				restrict: 'E',
				replace: false,
				templateUrl: 'app/partials/directives/giftimg.html',
				scope: {
					action: '@',
					def: '=',
                    ngModel: '='
				},
				controller: function ($scope,$rootScope) {
					
					var sizeToBytes = function(size){
							var scale = 1;
							
							if (~ size.indexOf('k')){ 
							  scale = 1024; 
							} else if (~ size.indexOf('m')){ 
							  scale = 1024 * 1024; 
							} else if (~ size.indexOf('g')){ 
							  scale = 1024 * 1024 * 1024; 
							}
							return parseInt(size,10) * scale;
					};
					
					$scope.progress = 0;
					
					$scope.$watch(
						function() {
							return $scope.def;
						},
						function(img) {
                   			$scope.img = (img == null || $.trim(img).length == 0) ?
                                            'assets/img/ge-upload.png'
                                        	: img;
						},
						true);
						


					$scope.sendFile = function(el) {						
						$('#error-msg').css({'display':'none'});
						
						if ($(el).val() == '') {							
							return false;
						}
						
						var maxSize = sizeToBytes('5m');
						var fileSize = el.files[0].size;
						var extension = el.value.replace(/^.*\./, '');

						if (extension == el.value) {
							extension = '';
						} else {						
							extension = extension.toLowerCase();
						}
						
							switch(extension){
								case 'jpg':							
								case 'png':
								case 'jpeg':
								case 'JPG':							
								case 'PNG':
								case 'JPEG':
								if (fileSize < maxSize){
									// starting ....
									
										var $form = $(el).parents('form');
					
										if ($(el).val() == '') {
											return false;
										}
								
										$form.attr('action', $scope.action);
										$scope.$apply(function() {
											$scope.progress = 0;
										});				
								
										$form.ajaxSubmit({
										type: 'POST',
										uploadProgress: function(event, position, total, percentComplete) { 
											
											$scope.$apply(function() {
												$scope.progress = percentComplete;
											});
							
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
														$scope.progress = 0;
														$scope.giftimage = "api/image/"+filename;
														$scope.img = "api/image/"+filename;
														$scope.def = "api/image/"+filename;
														$scope.error = "";
														$scope.ngModel = "api/image/"+filename;
													});
												}
												else {
													$scope.$apply(function() {
														$scope.progress = 0;
														$('#gift-error-msg').css({'display':'block'});
														$('#error-msg').css({'display':'none'});
														$scope.error = responseText.error;
													});
												}
								
											}
										});
											
									// Ending ...
									
									}else{
											$(el).value='';
											$scope.$apply(function() {
												$scope.progress = 0;
												$('#gift-error-msg').css({'display':'block'});
												$('#error-msg').css({'display':'none'});
												$scope.error = 'Please limit photo size to 5 MB ';
											});
											return false;
										}
									break;
									
								default:
									$(el).value='';
									$scope.$apply(function() {
										$scope.progress = 0;
										$('#gift-error-msg').css({'display':'block'});
										$('#error-msg').css({'display':'none'});
										$scope.error = 'Invalid file format. Please upload an image';
									});
									return false;
							}
						
					
						}
				
				},
				link: function(scope, elem, attrs, ctrl) {
					scope.$watch("ngModel", function() {
                        scope.giftimage = scope.ngModel;
                    });
					elem.find('.fake-uploader').click(function() {
						elem.find('input[type="file"]').click();
					});
			
				}
			};
		
		})
		.directive('fb', ['Facebook', function(Facebook) {
	        return {
	            restrict: "E",
	            replace: true,
	            template: "<div id='fb-root'></div>",
	            compile: function(tElem, tAttrs) {
	                return {
	                    post: function(scope, iElem, iAttrs, controller) {
	                        var fbAppId = iAttrs.appId || '';
	 
	                        var fb_params = {
	                            appId: iAttrs.appId || "",
	                            cookie: iAttrs.cookie || true,
	                            status: iAttrs.status || true,
	                            xfbml: iAttrs.xfbml || true
	                        };
	 
	                        // Setup the post-load callback
	                        window.fbAsyncInit = function() {
	                            Facebook._init(fb_params);
	 
	                            if('fbInit' in iAttrs) {
	                                iAttrs.fbInit();
	                            }
	                        };
	 
	                        (function(d, s, id, fbAppId) {
	                            var js, fjs = d.getElementsByTagName(s)[0];
	                            if (d.getElementById(id)) return;
	                            js = d.createElement(s); js.id = id; js.async = true;
	                            js.src = "//connect.facebook.net/en_US/all.js";
	                            fjs.parentNode.insertBefore(js, fjs);
	                        }(document, 'script', 'facebook-jssdk', fbAppId));
	                    }
	                }
	            }
	        };
	    }])
		.directive('ngAutoExpand', function() {
			return {
				link: function(scope, elem, attrs) {
						var textArea = elem.get(0);
						var minimum = textArea.rows;
					elem.bind('keyup', function(e){
						while (textArea.rows > minimum && textArea.scrollHeight < textArea.offsetHeight)
						{
								textArea.rows--;
						}
						var h=0;
						while (textArea.scrollHeight > textArea.offsetHeight && h!==textArea.offsetHeight)
						{
							h=textArea.offsetHeight;
							textArea.rows++;
						}
					});
				}
			};
		})
		.directive('ngBlur', ['$parse', function($parse) {
			return function(scope, element, attr) {
				var fn = $parse(attr['ngBlur']);
				element.bind('blur', function(event) {
					scope.$apply(function() {
						fn(scope, {$event:event});
					});
				});
			}
		}])
		.directive('ngOnlyDigits', function () {

			return {
				restrict: 'A',
				require: '?ngModel',
				link: function (scope, element, attrs, ngModel) {
					if (!ngModel) return;
					ngModel.$parsers.unshift(function (inputValue) {
						var digits = inputValue.split('').filter(function (s) { return (!isNaN(s) && s != ' '); }).join('');
						ngModel.$viewValue = digits;
						ngModel.$render();
						return digits;
					});
				}
			};
		})
		.directive('ngOnlyAlphabets', function () {
			return {
				restrict: 'A',
				require: '?ngModel',
				link: function (scope, element, attrs, ngModel) {
					if (!ngModel) return;
					ngModel.$parsers.unshift(function (inputValue) {
						var digits = inputValue.split('').filter(function (s) { return (!s.match(/[^a-zA-Z\s]/gi)); }).join('');
						ngModel.$viewValue = digits;
						ngModel.$render();
						return digits;
					});
				}
			};
		})
		.directive('adDetailView',function() {
			return {
				restrict: 'E',
				templateUrl: 'app/partials/view/ad.html',
				replace:true,
				transclude:true,
				scope: {
					id:'@'
				},
				controller: function($scope, $location, Ad, AdDialog, Amazon) {
					
					$scope.amazonUrl = Amazon;
					
					$scope.callback = function() {
						$scope.viewGift();
						$scope.$parent.callback();
					};
					
					if(typeof($scope.$parent.filterAds)!=='undefined') {
						var ads = $scope.$parent.filterAds;
						var current = ads.indexOf(parseInt($scope.id));
						
						if(current>0)
							$scope.prev=ads[current-1];
						else
							$scope.prev=ads.length-1;
							
						if(current<ads.length-1)
							$scope.next=ads[current+1];
						else
							$scope.next=0;
					}
					
					$scope.move=function(id) {
						AdDialog.close();
						$location.search('view',id);
					}
					
					$scope.loaded = false;
					
					$scope.close = function() {
						$location.search('view',null);
						AdDialog.close();
					}
					$scope.viewGift = function() {
						var ad = Ad.get({id:$scope.id}, function() {
							$scope.ad = ad.ad;
							$scope.comments = ad.comments;
							$scope.loaded=true;
						});
					}
					
					$scope.viewGift();
					
				}
			}
		})
});
