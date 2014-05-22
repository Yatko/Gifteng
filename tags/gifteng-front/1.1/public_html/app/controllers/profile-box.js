define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('ProfileBoxController', ["$scope", "$routeParams", "$modal", "User", "UserEx", "Filters", "ProfileData","SocialService","JsonObject", function($scope, $routeParams, $modal, User, UserEx, Filters, ProfileData,SocialService,JsonObject) {
			
		$scope.setMyGifts = function(str) {
			Filters.setMyGifts(str);
		}
		
		if(typeof($routeParams.id)!=='undefined' && $routeParams.id) {
			$scope.self = false;
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user.data) !== 'undefined') {
						if(user.data.id == $routeParams.id) {
							$scope.self = true;
						}
					}
				},
				true
			);
		}
		else {
			$scope.self = true;
		}
		
		if($scope.self) {
			$scope.$watch(
				function() {
					return User.getUser();
				},
				function(user) {
					if(typeof(user) !== 'undefined') {
						$scope.profile = user.data;
						SocialService.getNetworks.query({},function(data){
							var networks = data.userConnection;
							if(!JsonObject.isEmpty(networks)){	
								$scope.profile.fbVerified = true;
							}else{
								$scope.profile.fbVerified = false;
							}
						});	
						
						if(user.logged==false
							&& $location.$$path!='/'
							&& $location.$$path!='/media') {
							$scope.logout();
						}
					}
				},
				true
			);
			
			
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
		} else {
			if(typeof(ProfileData.get())!=='undefined' && ProfileData.get().id==$routeParams.id) {
				$scope.profile = ProfileData.get();
				$scope.following = $scope.profile.inFollowings;
				$scope.follow = function() {
					UserEx.follow.query({id:$scope.profile.id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
					$scope.following=true;
				}
				$scope.unfollow = function() {
					UserEx.unfollow.query({id:$scope.profile.id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
					$scope.following=false;
				}
			} else {
				$scope.profile = UserEx.profile.query({id:$routeParams.id}, function() {
					$scope.following = $scope.profile.inFollowings;
					$scope.follow = function() {
						UserEx.follow.query({id:$scope.profile.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$scope.following=true;
					}
					$scope.unfollow = function() {
						UserEx.unfollow.query({id:$scope.profile.id}, function() {
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
							});
						});
						$scope.following=false;
					}
					ProfileData.set($scope.profile);
				});
			}
		}
	}])

});