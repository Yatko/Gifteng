define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('VerifyInvitationController', ["$scope","Auth","User","UserEx","InviteService","$modal","TestEmailService","$location","Amazon", function($scope,Auth,User,UserEx,InviteService,$modal,TestEmailService,$location,Amazon) {
		$scope.error = '';
		$scope.reg={};
		
		if(typeof($scope.defstep)!=='undefined' && $scope.defstep)
			$scope.step=$scope.defstep;
		else
			$scope.step="step1";
		var code = '';
		$scope.$watch(
			function() {
				return User.getUser();
			},
			function(user) {
				if(typeof(user) !== 'undefined') {
					$scope.user = user;
					if(typeof(user.data)!=='undefined') {
						if(typeof(user.data.avatar) !== 'undefined')
							$scope.user.data.avatar_url = Amazon+"user/"+user.data.avatar.id+"_112";
						else
							$scope.user.data.avatar_url = "assets/img/ge-no-profile-picture.png";
					}
				}
			},
			true
		);
		
		$scope.verify = function(){				
			 code = $('#invitationCode').val();
			
			if(typeof code == 'undefined' || code == ''){					
				$scope.error = 'Please enter your invitation code';
				return false;
			}else{
				$scope.error =''; 
					InviteService.verifyInvitation.query({
						'invitation-code' : code
						
					},function(data){				
						if(data.status == false){
							$scope.error = 'Sorry, we don\'t recognize this invitation code. ';
						}else{
							$scope.error = '';
							$scope.step="step2";
							
						}
					});			
			}
		};
		$scope.join = function(){
			$scope.error = '';
			$scope.step="step3";
		};
		
		$scope.registerUser = function(){

			var email = $scope.reg.email;
			var password = $scope.reg.password;
			var confirmPassword = $scope.reg.confirmPassword;
			
	
			if(typeof email == 'undefined' || email == ''){					
				$scope.error = 'Please enter the email address';
				return false;
			}else if (!TestEmailService.isValid(email)){						
					$scope.error = 'Please enter a valid email address ';
					return false;
			}
			if(typeof password == 'undefined' || password == ''){					
				$scope.error = 'You must enter the password twice in order to confirm it.';
				return false;
			}else if(password.length <6){
				$scope.error = 'Password is too short.';
				return false;
			}
			if(typeof confirmPassword == 'undefined' || confirmPassword == ''){					
				$scope.error = 'You must enter the  password twice in order to confirm it.';
				return false;
			}else if(confirmPassword.length <6){
				$scope.error = 'Confirm password is too short.';
				return false;
			}				
			if(confirmPassword != password){
				$scope.error = 'You must enter the password twice in order to confirm it.';
				return false;
			}	
						
			if(typeof($scope.defstep)!=='undefined' && $scope.defstep) {
				code = null;
			}
			
			if(typeof($scope.ref)!=='undefined') {
				var ref = $scope.ref;
			} else {
				var ref = null;
			}
			
			InviteService.registerUser.query({
				'code' : code,
				'email': email,
				'password' : password,
				'ref': ref						
			},function(data){				
				if(!data.success){
					$scope.error = data.faultstring;
				}else{
					var user = new Auth.get();
					User.setUser(user);
					$scope.error = '';
					$scope.step="step4";
					$scope.user = User.getUser();
					$scope.$parent.$parent.unclosable=true;
				}
			});	 
		};
		$scope.saveProfile = function(){
				var firstName = $('#firstName').val();
				var lastName = $('#lastName').val();
				var zipCode =  $('#zipCode').val();
					
				if(typeof firstName == 'undefined' || firstName == ''){					
					$scope.error = 'Please enter your first name';
					return false;
				}
				if(typeof lastName == 'undefined' || lastName == ''){					
					$scope.error = 'Please enter your last name';
					return false;
				} 
				if(typeof zipCode == 'undefined' || zipCode == ''){					
					$scope.error = 'Please enter your zip code to see gifts nearby';
					return false;
				}else if(zipCode.length < 5) {
					$scope.error = 'Please enter your correct zip code.';
					return false;
				}
				
				UserEx.updateProfile.query({
					'first_name':firstName,
					'last_name' : lastName,
					'zipCode': zipCode
				}, function (){
					var user = UserEx.reinit.query({}, function() {
						User.setUser(user);	
						
						$scope.close();
						
						var latestRelease = new Date(1392311475000);
						var lastLoginDate = new Date(parseInt(user.data.previousLoginAt));
						
						var modalInstance = $modal.open({
							templateUrl: 'app/partials/directives/modal/welcome_new.html',
							controller: function($scope, $modalInstance) {
								$scope.close = function () {
									$modalInstance.close();
								};
							
							}
						});
						
						$location.path('/profile');
					});
				});
				
		};
		$scope.changePhoto = function() {
		    var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/change_photo.html',
				controller: function($scope, $modalInstance,UserEx,Amazon) {
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
										$scope.$watch(
												function() {
													return User.getUser();
												},
												function(user) {
													if(typeof(user) !== 'undefined') {
														$scope.user = user;
														if(typeof(user.data)!=='undefined') {
															if(typeof(user.data.avatar) !== 'undefined')
																$scope.user.data.avatar_url = Amazon+"user/"+user.data.avatar.id+"_112";
															else
																$scope.user.data.avatar_url = "assets/img/ge-no-profile-picture.png";
														}
													}
												},
												true
											);									
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
		};
	
	
	}])

});