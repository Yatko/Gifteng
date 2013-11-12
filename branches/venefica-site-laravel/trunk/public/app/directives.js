define(['angular','services','jquery'], function(angular,services,jQuery) {
	'use strict';
	
	return angular.module('gifteng.directives', ['gifteng.services'])
		.directive('adItemBox', function($compile) {
			return {
				restrict:'E',
				scope: {
					img:'@',
					status:'@',
					type:'@',
					creatorId:'@',
					creatorName:'@',
					creatorSince:'@',
					creatorFollowing:'=',
					creatorCity:'@',
					creatorAvatar:'@',
					creatorPoints:'@',
					id:'@',
					ad:'=',
					callback:'&',
					selfview:'@'
				},
				templateUrl: 'app/partials/directives/ad-item-box.html',
				replace:true,
				transclude:true,
				controller: function($scope, UserEx, $modal, User) {
					
					/* giving modals */
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
									})
								}
							}
						});
					}
					$scope.relistGift = function(id) {
						var callback=$scope.callback;
					    var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/relist_gift.html',
							controller: function($scope, $modalInstance) {
								$scope.close = function () {
									$modalInstance.close();
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
					$scope.requestReceive = function(id) {
						var callback=$scope.callback;
					    var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/request_receive.html',
							controller: function($scope, UserEx, $location, $modalInstance) {
								$scope.close = function () {
									$modalInstance.close();
								};
								$scope.submit = function() {
									UserEx.requestReceive.query({id:id}, function() {
										$modalInstance.close();
										callback();
									})
								}
							}
						});
					}
					
					/* favorites modals */
					$scope.requestGift = function(id) {
						var callback=$scope.callback;
					    var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/request_create.html',
							controller: function($scope, UserEx, $location, $modalInstance) {
								$scope.close = function () {
									$modalInstance.close();
								};
								$scope.submit = function() {
									UserEx.requestAd.query({id:id}, function() {
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
					
				},
				compile: function compile() {
					return {
				        post: function (scope, iElement, iAttrs) { 
				        	
				        	iAttrs.$observe('status', function() {
								var type = iAttrs.type;
								var action = '';
								var text = '';
								
								if(iAttrs.details=='1' || type=="details") {
									var userprofile = '<div class="well-main"><user-profile name="{{creatorName}}" id="{{creatorId}}" location="{{creatorCity}}" img="https://s3.amazonaws.com/ge-dev/user/{{creatorAvatar}}_60" avatar="{{creatorAvatar}}" points="{{creatorPoints}}" since="{{creatorSince}}" following="creatorFollowing" nested="true"></user-profile></div>';
									$('.well',iElement).prepend($compile(userprofile)(scope));
								}
								
							});
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
					ads:'@',
					following:'=',
					self:'=',
					avatar:'@'
				},
				templateUrl: 'app/partials/directives/user-profile.html',
				replace:true,
				controller: function( $scope, $element, $attrs, UserEx, User ) {
					$scope.follow = function() {
						$scope.data = UserEx.follow.query({id:$scope.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$attrs.$set('following',true);
						$scope.following=true;
					}
					$scope.unfollow = function() {
						$scope.data = UserEx.unfollow.query({id:$scope.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$attrs.$set('following',false);
						$scope.following=false;
					}
					$attrs.$observe('following', function(value) {
						if(typeof value=="Boolean")
							$scope.following=value;
					});
				},
				link: function($scope, $element, $attrs) {
					$attrs.$observe('following', function(value) {
						$scope.following=value;
					});
				},
				compile: function() {
					return {
						post: function(scope, iElement, iAttrs) {
							if(iAttrs.showgifts) {
								iAttrs.$observe('ads', function() {
									if(scope.ads!='undefined' && scope.ads!="") {
										var ads = angular.fromJson(scope.ads);
										var gifts = '<div class="row">';
										for(var i=0;i<ads.length;i++) {
											gifts += '<div class="col-xs-4">'+
														'<a href="#/view/gift/'+ads[i].id+'"><img src="https://s3.amazonaws.com/ge-dev/ad/'+ads[i].image.id+'_320" class="img-rounded img-responsive" alt=""></a>'+
													'</div>';
										}
										gifts += '</div>';
									
										$('.well-main',iElement).append(gifts);
										
									}
								});
							}
							if(iAttrs.nested) {
								$('.well',iElement).removeClass('well');
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
					img:'@',
					title:'@',
					simple:'@',
					numShares: '@',
					numComments: '@',
					numBookmarks: '@',
					comments:'@',
					creatorId:'@',
					creatorName:'@',
					creatorSince:'@',
					creatorFollowing:'=',
					creatorCity:'@',
					creatorAvatar:'@',
					creatorPoints:'@',
					inBookmarks:'@',
					id:'@',
					status:'@',
					canRequest:'@',
					category:'@',
					callback:'&',
					self:'='
				},
				templateUrl: 'app/partials/directives/ad.html',
				replace:true,
				transclude:true,
				controller: function($scope, $modal, UserEx, User) {
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
      						}
      					});
					  };
				},
				compile: function() {
					return {
						post: function(scope, iElement, iAttrs) {
							if(iAttrs.simple) {
								$('.ge-action',iElement).remove();
								$('.title',iElement).remove();
								$('user-profile',iElement).remove();
							}
				        	iAttrs.$observe('comments', function() {
				        		if(scope.numComments>0) {
					        		scope.comments = angular.fromJson(scope.comments);
					        		if(typeof(scope.comments.item.type) !== 'undefined') {
					        			scope.comments.item = [scope.comments.item];
					        		}
				        		}
				        	});
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
			    controller: function($scope, UserEx) {
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
					def: '@'
				},
				controller: function ($scope) {
					$scope.progress = 0;
					$scope.giftimage = 'http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-upload.png';
				
					$scope.sendFile = function(el) {
				
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
				
								var ar = $(el).val().split('\\'), 
									filename =  ar[ar.length-1];

								$form.removeAttr('action');
				
								$scope.$apply(function() {
									$scope.progress = 0;
									$scope.giftimage = "api/image/"+filename;
								});
				
							},
						});
				
					}
				
				},
				link: function(scope, elem, attrs, ctrl) {
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
	    }]);
});
