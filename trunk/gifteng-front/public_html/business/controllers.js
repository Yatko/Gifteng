define(['angular','services'], function(angular,services) {
	'use strict';
	
	return angular.module('giftengbiz.controllers', ['giftengbiz.services'])
		
		/**
		 * Index Controller
		 */
		.controller('IndexController', function() {
			
		})
		
		/**
		 * Nav Controller
		 */
		.controller('NavController',function($scope, $location, $modal, User, Auth) {
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user) !== 'undefined') {
						$scope.user = user;
						if($scope.user.logged==false
							&& $location.$$path!='/') {
							$location.path('/');
						} else if($scope.user.logged==true && $location.$$path=='/') {
							$location.path('/profile');
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
			$scope.login = function() {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/login.html',
					controller: function($scope, $modalInstance, Auth, User) {
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.doLogin = function() {
							var auth = new Auth;
							auth.email = $('#email').val();
							auth.password = $('#password').val();
							auth.$save(function(response) {
								var user = new Auth.get();
								User.setUser(user);
								$location.path('/profile');
								$modalInstance.close();
							});
						}
					}
				});
			};
			
			$scope.logout = function() {
				var user = new Auth.remove({id:0});
				User.setUser({});
				$location.path('/');
			}
		})
		
		/**
		 * Register Controller
		 */
		.controller('RegisterController', function($scope, $location, Biz, Auth, User) {
			
			$scope.userData = {};
			$scope.register = function() {
				if($scope.userData.password==$scope.userData.password2) {
					Biz.register.query($scope.userData, function() {
						var auth = new Auth;
						auth.email = $scope.userData.email;
						auth.password = $scope.userData.password;
						auth.$save(function(response) {
							var user = new Auth.get();
							User.setUser(user);
							$location.path('/profile');
						});
					});
				}
			}
			
		})
		
		/**
		 * Profile Controller
		 */
		.controller('ProfileController', function($scope, $routeParams, $location, $modal, User, AdUser) {
			
			$scope.current="gifts";
			
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user) !== 'undefined') {
						$scope.user = user;
						if($scope.user.logged==false
							&& $location.$$path!='/') {
							$location.path('/');
						} else if($scope.user.logged==true && $location.$$path=='/') {
							$location.path('/profile');
						}
						if(typeof(user.data)!=='undefined') {
							if(typeof(user.data.avatar) !== 'undefined')
								$scope.user.data.avatar_url = "https://s3.amazonaws.com/ge-dev/user/"+user.data.avatar.id+"_112";
							else
								$scope.user.data.avatar_url = "http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-no-profile-picture.png";
						
							
							$scope.ads = AdUser.query({id:user.data.id});
						}
					}
				},
				true
			);
			
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
									var user = Profile.reinit.query({}, function() {
										User.setUser(user);
										$modalInstance.close();
									});
								},
							});
					
						}
					}
				});
			}
			
			$scope.editProfile = function(user) {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/edit_profile.html',
					controller: function($scope, $modalInstance, Profile, User) {
						$scope.userData={};
						$scope.userData.businessName=user.businessName;
						$scope.userData.contactName=user.contactName;
						if(typeof(user.address.zipCode)!=='undefined') {
							$scope.userData.zipCode=user.address.zipCode;
						}
						else {
							$scope.userData.zipcode='';
						}
						$scope.userData.contactNumber=user.contactNumber;
						$scope.userData.about=user.about;
						$scope.userData.website=user.website;
						$scope.userData.email=user.email;
						
						$scope.submit = function() {
							Profile.update.query($scope.userData, function (){
								var user = Profile.reinit.query({}, function() {
									User.setUser(user);
									$modalInstance.close();
								});
							});
						}
						
						$scope.close = function () {
							$modalInstance.close();
						};
					}
				});
			};
			
			$scope.addStore = function() {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/add_store.html',
					controller: function($scope, $modalInstance, Store, Profile, User) {
						
						$scope.address = {}
						
						$scope.submit = function() {
							var store = new Store();
							store.name = $scope.address.name;
							store.address1 = $scope.address.address1;
							store.address2 = $scope.address.address2;
							store.city = $scope.address.city;
							store.state = $scope.address.state;
							store.zipCode = $scope.address.zipCode;
							
							store.$save(function(response) {
								var user = Profile.reinit.query({}, function() {
									User.setUser(user);
									$modalInstance.close();
								});
							});
						}
						$scope.close = function () {
							$modalInstance.close();
						};
					}
				});
			};
			
			/* Edit Store */
			$scope.editStore = function(adr) {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/edit_store.html',
					controller: function($scope, $modalInstance, Store, Profile, User) {
						
						$scope.address = angular.copy(adr);
						
						$scope.submit = function() {
							var store = new Store({id:adr.id});
							store.name = $scope.address.name;
							store.address1 = $scope.address.address1;
							store.address2 = $scope.address.address2;
							store.city = $scope.address.city;
							store.state = $scope.address.state;
							store.zipCode = $scope.address.zipCode;
							
							store.$update(function(response) {
								var user = Profile.reinit.query({}, function() {
									User.setUser(user);
									$modalInstance.close();
								});
							});
						}
						$scope.close = function () {
							$modalInstance.close();
						};
					}
				});
			};
			
			/* Delete Store */
			$scope.deleteStore = function(adr) {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/delete_store.html',
					controller: function($scope, $modalInstance, Store, Profile, User) {
						
						$scope.submit = function() {
							var store = new Store({id:adr.id});
							
							store.$delete(function(response) {
								var user = Profile.reinit.query({}, function() {
									User.setUser(user);
									$modalInstance.close();
								});
							});
						}
						$scope.close = function () {
							$modalInstance.close();
						};
					}
				});
			};
			
			$scope.postGift = function() {
				var modalInstance = $modal.open({
					templateUrl: 'business/partials/modal/post.html',
					controller: function($scope, $modalInstance, Ad, User, Profile) {
						$scope.$watch(
							function() {
								return User.getUser();
							},
							function(user) {
								if(typeof(user) !== 'undefined') {
									$scope.user = user;
									if($scope.user.logged==false
										&& $location.$$path!='/') {
										$location.path('/');
									} else if($scope.user.logged==true && $location.$$path=='/') {
										$location.path('/profile');
									}
									if(typeof(user.data)!=='undefined') {
										if(typeof(user.data.avatar) !== 'undefined')
											$scope.user.data.avatar_url = "https://s3.amazonaws.com/ge-dev/user/"+user.data.avatar.id+"_112";
										else
											$scope.user.data.avatar_url = "http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-no-profile-picture.png";
									}
						
									$scope.stores = user.data.addresses;
									
								}
							},
							true
						);
						$scope.step="step1";
						$scope.toPost={};
						$scope.toParse={};
						$scope.toPost.image='';
						$scope.close = function () {
							$modalInstance.close();
						};
						$scope.next=function(id) {
							$scope.step="step"+id;
							if(id==2) {
								$('.datepicker').datepicker();
							}
						}
						
						$scope.post = function() {
							var posting = $scope.toPost;
							
							if($scope.toParse.online && !$scope.toParse.instore) {
								posting.place="ONLINE";
							}
							else if(!$scope.toParse.online && $scope.toParse.instore) {
								posting.place="LOCATION";
							}
							else {
								posting.place="BOTH";
							}
							$scope.toPost.image={url:$scope.toPost.image};
							posting.type="BUSINESS";
							Ad.save({ad:posting}, function() {
								var user = Profile.reinit.query({}, function() {
									User.setUser(user);
								});
							});
							
							$scope.step="step5";
						}
						
						$scope.addStore = function() {
							var modalInstance = $modal.open({
								templateUrl: 'business/partials/modal/add_store.html',
								controller: function($scope, $modalInstance, Store, Profile, User) {
									
									$scope.address = {}
									
									$scope.submit = function() {
										var store = new Store();
										store.name = $scope.address.name;
										store.address1 = $scope.address.address1;
										store.address2 = $scope.address.address2;
										store.city = $scope.address.city;
										store.state = $scope.address.state;
										store.zipCode = $scope.address.zipCode;
										
										store.$save(function(response) {
											var user = Profile.reinit.query({}, function() {
												User.setUser(user);
												$modalInstance.close();
											});
										});
									}
									$scope.close = function () {
										$modalInstance.close();
									};
								}
							});
						};
					}
				});
			};
		})
});
