define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ProfileController', function($scope, $routeParams, $location, $modal, User, AdUser, AdDialog, UserEx) {
			
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
		
		$scope.viewGift = function(id) {
			AdDialog.open(id, $scope);
		}
		
		$scope.changePhoto = function() {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/change_photo.html',
				controller: function($scope, $modalInstance) {
                    $scope.mode = "upload";
					$scope.progress = 0;
					$scope.profileimage = "";
                    $scope.cordsv = null;
					$scope.error ='';
					
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
					
					
					$scope.close = function () {
                        // TODO cleanup server uploaded/cropped images
						$modalInstance.close();
					};
					$scope.sendFile = function(el) {
						$scope.action="api/image/storeInWIP";
						var $form = $(el).parents('form');
				
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
								  
						$form.attr('action', $scope.action);
					
						$scope.$apply(function() {
							$scope.progress = 0;
						});

                        $scope.selected = function(cords){
                            $scope.cordsv =  cords;
							
                        }

                        $scope.crop = function(cords){
                            UserEx.cropnsave.update({id:$scope.rspimg},$scope.cordsv, function(){

                                 $form.removeAttr('action');

                                 var user = UserEx.reinit.query({}, function() {
                                     User.setUser(user);
                                     $modalInstance.close();
                                 });

                            });
                        }

                        $scope.cancel = function(){
                            // send a request to delete the last uploaded file
                            $modalInstance.close();
                        }
				
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
											$scope.progress = 0;
							
											var ar = $(el).val().split('\\'),
												filename =  ar[ar.length-1];

											$scope.$apply(function() {
												$scope.error = '';
												$scope.mode = "crop";
												$scope.profileimage = "api/image/wipscaled/"+responseText.filename;
												$scope.rspimg = responseText.filename;
											});
									}else{
										$scope.$apply(function() {
													$scope.progress = 0;														
													$scope.error = responseText.error;
												});
									
									}
                                 /*
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
                                */

							}
						});
						// Ending ...
								
								}else{
										$(el).value='';
										$scope.$apply(function() {
											$scope.progress = 0;												
											$scope.error = 'Please limit photo size to 5 MB ';
										});
										return false;
									}
								break;
								
							default:
								$(el).value='';
								$scope.$apply(function() {
									$scope.progress = 0;									
									$scope.error = 'Invalid file format. Please upload an image';
								});
								return false;
						}
				
				
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
				controller: function($scope, $modalInstance) {
					$scope.close = function () {
						$modalInstance.close();
					};
				}
			});
		};
	})

});