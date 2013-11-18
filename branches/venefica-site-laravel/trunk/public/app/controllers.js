define(['angular','services','lang'], function(angular,services,lang) {
	'use strict';
	
	return angular.module('gifteng.controllers', ['gifteng.services'])
		
		/**
		 * Index Controller
		 */
		.controller('IndexController', function() {
			
		})
		
		/***
		 * Invite Controller
		 */
		
		.controller('InviteController', function($scope, Facebook) {
			$scope.searchInvites = '';
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
		})
		
		/**
		 * Top Giftengers Controller
		 */
		.controller('TopController', function($scope, Top) {
			var top = Top.query({},function() {
				$scope.users = top;
			});
		})
		
		/**
		 * Edit Post Controller
		 */
		.controller('EditPostController', function($scope, Geo, Ad) {
			$scope.step="step1";
			$scope.toPost={} //place here the ad info
			
			
		})
		
		
		/**
		 * Post Controller
		 */
		.controller('PostController', function($scope, Geo, Ad,UserEx,User) {
			$scope.newPost = function() {
				$scope.step="step1";
				$scope.toPost={};
				$scope.toPost.image='';
				$scope.setImg = function() {
					$scope.toPost.image=$('#giftimage').val();
				}
				$scope.step1 = function() {
					$scope.step="step1";
				}
				$scope.step2 = function() {
					$scope.step="step2";
				};
				$scope.step3 = function() {
					if(!$scope.toPost.title) {
						$scope.error = "Please name your gift."
					}
					else if(!$scope.toPost.category) {
						$scope.error = "Please select a category."
					}
					else if(!$scope.toPost.price) {
						$scope.error = "Please enter your gift's Current Value."
					}
					else if(!$scope.toPost.zip) {
						$scope.error = "Please set your Zip code."
					}
					else if(!$scope.toPost.freeShipping && !$scope.toPost.pickUp) {
						$scope.error = "Please select the delivery method.";
					}
					else {
						$scope.error = "";
						$scope.step="step3";
						showMap(true);
					}
				};
				$scope.step4 = function() {
					$scope.step="step4";
					showMap();
				}
				$scope.log = function() {
					var posting={
						title: $scope.toPost.title,
						description: $scope.toPost.description,
						categoryId: $scope.toPost.category,
						address: $scope.toPost.address,
						price: $scope.toPost.price,
						pickUp: $scope.toPost.pickUp,
						freeShipping: $scope.toPost.freeShipping,
						image: {url:$scope.toPost.image}
					};
					Ad.save({ad:posting}, function() {
						$scope.step='step5';
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
					
				}
				var showMap = function(canDrag) {
					if(typeof(canDrag)!=='undefined') {
						var canDrag = true;
						var dragtitle = "Drag me to the exact location";
					}
					else {
						var canDrag = false;
						var dragTitle = "";
					}
	        		var locationIcon = new L.icon({
		                iconUrl: 'http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-location-pin-teal.png',
		                iconSize: [64, 64]
		            });
		            
					var geo = Geo.query({zip: $scope.toPost.zip}, function() {
						
						if(!$scope.toPost.address || typeof($scope.toPost.address)==="undefined")
							$scope.toPost.address = geo.address;
			            
			            var locationMarker = new L.marker([$scope.toPost.address.latitude, $scope.toPost.address.longitude], {
			                icon: locationIcon,
			                draggable: canDrag,
			                title: dragTitle
			            });

			            if ( canDrag ) {
			                locationMarker.on('dragend', function() {
			                    var latlng = locationMarker.getLatLng();
			                    $scope.toPost.address.longitude = latlng.lng;
			                    $scope.toPost.address.latitude = latlng.lat;
			                });
			            }
			            
			            var tileLayer = new L.tileLayer.provider('Esri.WorldStreetMap');
			
			            var map = new L.map('view_map').setView([$scope.toPost.address.latitude, $scope.toPost.address.longitude], 16);
			            tileLayer.addTo(map);
			            locationMarker.addTo(map);
					});
				}
			}
			
			$scope.newPost();
		})
		
		/**
		 * Browse Controller
		 */
		.controller('BrowseController', function($scope, AdMore, Ad) {
			$scope.order='newest';
			var more=0;
			$scope.loadAds = function () {
				var ads = Ad.query({order:$scope.order}, function() {
					$scope.col1 = [];
					$scope.col2 = [];
					$scope.col3 = [];
					$scope.creators = ads['creators'];
					
					if(typeof(ads['ads'])!=='undefined') {
						for(var i=0;i<ads['ads'].length;i++) {
							if((i%3)==0) {
								$scope.col3.push(ads['ads'][i]);
							}
							else if((i%2)==0) {
								$scope.col2.push(ads['ads'][i]);
							}
							else {
								$scope.col1.push(ads['ads'][i]);
							}
							if(i==ads['ads'].length-1) {
								$scope.last=ads['ads'][i].lastIndex;
							}
						}
					}
					
					loadMore();
				});
			}
			
			var loadMore = function() {
				for(var i=0;i<more;i++) {
					
					var ads = AdMore.query({'last':$scope.last}, function() {
					
						if(typeof(ads['ads'])!=='undefined') {
							for(var i=0;i<ads['ads'].length;i++) {
								if((i%3)==0) {
									$scope.col3.push(ads['ads'][i]);
								}
								else if((i%2)==0) {
									$scope.col2.push(ads['ads'][i]);
								}
								else {
									$scope.col1.push(ads['ads'][i]);
								}
								if(i==ads['ads'].length-1) {
									$scope.last=ads['ads'][i].lastIndex;
								}
							}
						}
					});
				}
			}
			
			$scope.loadMore = function() {
				more++;
				$scope.loadAds();
			}
			
			$scope.loadAds();
		})
		
		/**
		 * Nav Controller
		 */
		.controller('NavController',function($scope, $location, $modal, Auth, User, UserEx) {
		
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user) !== 'undefined') {
						$scope.user = user;
						if($scope.user.logged==false
							&& $location.$$path!='/' 
							&& $location.$$path!='/login'
							&& $location.$$path!='/request'
							&& $location.$$path!='/forgot'
							&& $location.$$path!='/verify') {
							$location.path('/login');
						}
						if(typeof(user.data)!=='undefined') {
							if(typeof(user.data.avatar) !== 'undefined')
								$scope.user.data.avatar_url = "https://s3.amazonaws.com/ge-dev/user/"+user.data.avatar.id+"_112";
							else
								$scope.user.data.avatar_url = "http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-no-profile-picture.png";
						}
					}
				},
				true
			);
			
			$scope.updateProfile = function(user) {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/edit_profile.html',
					controller: function($scope, $modalInstance, User, UserEx) {
						$scope.user=user;
						
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.submit = function() {
							
							UserEx.updateProfile.query({
								'first_name':$('#firstName').val(),
								'last_name':$('#lastName').val(),
								'about':$('#about').val(),
								'zipCode':$('#zipCode').val()
							}, function (){
								var user = UserEx.reinit.query({}, function() {
									User.setUser(user);
									$modalInstance.close();
								});
							});
						}
					}
				});
			}
			
			$scope.changePhoto = function() {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/change_photo.html',
					controller: function($scope, $modalInstance) {
						$scope.progress = 0;
						$scope.profileimage = "";
						
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.sendFile = function(el) {
							$scope.action="api/image/profile";
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
										$scope.profileimage = "api/image/"+filename;
									});
									var user = UserEx.reinit.query({}, function() {
										User.setUser(user);
										$modalInstance.close();
									});
								},
							});
					
						}
					}
				});
			}
			
			$scope.logout = function() {
				$('#open-left').click();
				var user = new Auth.remove({id:0});
				User.setUser({});
				$location.path('/');
			}
		})
		
		/**
		 * User Ads Controller
		 */
		.controller('UserAdsController',function($scope, $routeParams, User, UserEx, AdUser) {
			$scope.loadAds = function() {
				
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					$scope.ads = AdUser.query({id:$routeParams.id});
				}
				else {
					$scope.selfview=true;
					$scope.$watch(
						function() {
							return User.getUser();
						},
						function(user) {
							if(typeof(user.data) !== 'undefined')
								$scope.ads = AdUser.query({id:user.data.id});
						},
						true
					);
				}
			};
			$scope.loadAds();
		})
		
		/**
		 * Requested Ads Controller
		 */
		.controller('RequestedAdsController',function($scope, $routeParams, User, UserEx, AdRequested) {
			$scope.loadAds = function() {
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					var ads = AdRequested.query({id:$routeParams.id}, function() {
						$scope.ads = ads.ads;
						$scope.creators = ads.creators;
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
								var ads = AdRequested.query({id:user.data.id}, function() {
									$scope.ads = ads.ads;
									$scope.creators = ads.creators;
								});
							}
						},
						true
					);
				}
			};
			$scope.loadAds();
		})
		
		/**
		 * Bookmarked Ads Controller
		 */
		.controller('BookmarkedAdsController',function($scope, $routeParams, User, UserEx, AdBookmarked) {
			$scope.loadAds = function() {
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					var ads = AdBookmarked.query({id:$routeParams.id}, function() {
						$scope.ads = ads.ads;
						$scope.creators = ads.creators;
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
								var ads = AdBookmarked.query({id:user.data.id}, function() {
									$scope.ads = ads.ads;
									$scope.creators = ads.creators;
								});
							}
						},
						true
					);
				}
			};
			$scope.loadAds();
		})
		
		/**
		 * Following Controller
		 */
		.controller('FollowingController',function($scope, $routeParams, User, UserEx) {
			if(typeof($routeParams.id)!=='undefined') {
				$scope.data = UserEx.following.query({id:$routeParams.id});
			}
			else {
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data) !== 'undefined')
							$scope.data = UserEx.following.query({id:user.data.id});
					},
					true
				);
			}
		})
		
		/**
		 * Followers Controller
		 */
		.controller('FollowersController',function($scope, $routeParams, User, UserEx) {
			if(typeof($routeParams.id)!=='undefined') {
				$scope.data = UserEx.followers.query({id:$routeParams.id});
			}
			else {
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data) !== 'undefined')
							$scope.data = UserEx.followers.query({id:user.data.id});
					},
					true
				);
			}
		})
		
		/**
		 * Notifications Controller
		 */
		.controller('NotificationsController',function($scope, UserEx) {
			var values = UserEx.notifications.query({}, function() {
				var texts = lang.notifications;
				$scope.notifications = {};
				angular.forEach(texts, function(value, key) {
					if(typeof(values.notifiableTypes)!=='undefined' && values.notifiableTypes.indexOf(key)>-1) {
						$scope.notifications[key] = {
							'text':value,
							'checked':"checked"
						}
					}
					else {
						$scope.notifications[key] = {
							'text':value,
							'checked':""
						}
					}
				});
			});
			$scope.submit = function() {
				var notifications = [];
				$('#notificationsForm :checked').each(function() {
					notifications.push($(this).val());
				});
				values.notifiableTypes = notifications;
				values.$save();
			}
		})
		
		/**
		 * Messages Controller
		 */
		.controller('MessagesController',function($scope, UserEx, User) {
			$scope.user = User.getUser();
			$scope.messages = UserEx.message.query({});
			$scope.showMessage = false;
			$scope.openmsg = function(id) {
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
					});
				});
			}
			$scope.hidemsg = function(id) {
				UserEx.messageHide.query({id:id}, function() {
					var msgs = UserEx.message.query({}, function() {
						$scope.messages = msgs;
					});
					var user = UserEx.reinit.query({}, function() {
						User.setUser(user);
					});
				});
			}
		})
		
		/**
		 * Login Controller
		 */
		.controller('LoginController', function($scope, $location, Auth, User) {
			$scope.login = function() {
				var auth = new Auth;
				auth.email = $('#email').val();
				auth.password = $('#password').val();
				auth.$save(function(response) {
					var user = new Auth.get();
					User.setUser(user);
					$location.path('/browse');
				});
			}
		})
		
		/**
		 * Profile Controller
		 */
		.controller('ProfileController', function($scope, $routeParams) {
			
			if(!$routeParams.profilePage && !$routeParams.section) {
				$scope.section="gifts";
				$scope.tab_page="giving";
			}
			else if(!$routeParams.profilePage) {
				$scope.section=$routeParams.section;
				if($routeParams.section=="gifts") {
					$scope.tab_page="giving";
				} else if($routeParams.section=="connections") {
					$scope.tab_page="following";
				} else if($routeParams.section=="account") {
					$scope.tab_page="notifications";
				}
			}
			else {
				$scope.section=$routeParams.section;
				$scope.tab_page=$routeParams.profilePage;
			}
			
		})
		
		.controller('ProfileBoxController', function($scope, $routeParams, UserEx) {
			$scope.profile = UserEx.profile.query({id:$routeParams.id});
		})
		
		/**
		 * View Gift Controller
		 */
		.controller('ViewGiftController', function($scope, $modal, $routeParams, Ad) {
			$scope.viewGift = function() {
				$scope.ad = Ad.get({id:$routeParams.id}, function() {
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
					try {
		        		var locationIcon = new L.icon({
			                iconUrl: 'http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-location-pin-teal.png',
			                iconSize: [64, 64]
			            });
			            
			            var locationMarker = new L.marker([$scope.ad.address.latitude, $scope.ad.address.longitude], {
			                icon: locationIcon
			            });
			            
			            var tileLayer = new L.tileLayer.provider('Esri.WorldStreetMap');
			
			            var map = new L.map('view_map').setView([$scope.ad.address.latitude, $scope.ad.address.longitude], 16);
			            tileLayer.addTo(map);
			            locationMarker.addTo(map);
					}
					catch ( ex ) {
			            console.log(ex);
			        }
				});
			};
			
			$scope.viewGift();
			
			$scope.requestGift = function(id) {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/request_create.html',
					controller: function($scope, UserEx, $location, $modalInstance) {
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.submit = function() {
							UserEx.requestAd.query({id:id, text:$('#request_message').val()}, function() {
								$modalInstance.close();
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
					}
				});
			}
			$scope.editGift = function(id) {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/edit_gift.html',
					controller: function($scope, $modalInstance) {
						$scope.close = function () {
							$modalInstance.close();
						};
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
								$location.path('/profile/gifts/giving');
							})
						}
					}
				});
			}
			$scope.relistGift = function(id) {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/relist_gift.html',
					controller: function($scope, $modalInstance) {
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.submit = function() {
							UserEx.relist.query({id:id}, function() {
								$modalInstance.close();
								$location.path('/profile/gifts/giving');
							})
						}
					}
				});
			}
		});
});
