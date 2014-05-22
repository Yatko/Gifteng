define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('NavController',
		["$scope", "$location", "$routeParams", "$modal", "$window", "Auth", "User", "UserEx", "AdDialog", "Filters", "Amazon", "IpService","SocialService", "SocialNetwork", "$modalStack", "Categories",
		function($scope, $location, $routeParams, $modal, $window, Auth, User, UserEx, AdDialog, Filters, Amazon, IpService, SocialService, SocialNetwork, $modalStack, Categories) {
		
		$scope.$watch(
			function() {
				return Filters.getMyGifts();
			},
			function(filter) {
				$scope.filter=filter;
			},
			true
		);

		if(Categories.get()===null) {
			Categories.set();
		}

		$scope.close = function () {
			$location.search('login',null);
			$location.search('forgot',null);
			$location.search('request',null);
			$location.search('verify',null);
			$location.search('facebookLogin',null);
			if($modalStack.getTop())
				$modalStack.dismiss($modalStack.getTop().key,'close');
		};
		
		$scope.openFilter = function() {
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/filter.html',
				controller: function($scope, $modalInstance, $location, Filters) {
					$scope.filter=Filters.getMyGifts();
					$scope.parsedurl = $location.$$path.split('/');
					$scope.setMyGifts = function(str) {
						Filters.setMyGifts(str);
						$scope.filter=str;
						$scope.close();
					}
					$scope.close = function(){
						$modalInstance.close();
					};
				}
			});
			modalInstance.set()
		};

		$scope.openSearch = function() {
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/search.html',
				controller: function($scope, $modalInstance) {
					$scope.close = function(){
						$modalInstance.close();
					};
				}
			});
		};
		
		$scope.openLeft = function() {
			$('.snap-slide').toggleClass('active');
		}

		$scope.closeLeft = function() {
			$('.snap-slide').removeClass('active');
		}
		
		$scope.setColumns = function(num) {
			Filters.setColumns(num);
		}
		$scope.toggleWells = function() {
			Filters.toggleSimple();
		}
		$scope.setMyGifts = function(str) {
			Filters.setMyGifts(str);
		}
		var country;
		$scope.country = IpService.get();
		
		if($scope.country==null) {
			IpService.call().then(function (data) {
				IpService.set(data);
				$scope.country = data;
			});
		}


		$scope.$watch(function() {
			return $scope.country;
		}, function(data) {

			if(data!==null && typeof(data.country)!=='undefined') {
				$scope.country=data.country.iso_code;
					
				var oLogin = function() {
					$scope.close();
					country = $scope.country;
					if($location.$$path!="/give")
						$location.search('login');
						modalInstance = $modal.open({
							templateUrl: 'app/partials/login.html',
							controller: function($scope, $modalInstance, $modal) {
								$scope.country = country;
								$scope.oSign = oS;
								$scope.oForgot = oForgot;
								$scope.oRequest = oRequest;
								
								$scope.close = function() {
									$modalInstance.close();
									$location.search('login',null);
								};
							}
						});
				}
				var oForgot = function() {
					$scope.close();
					country = $scope.country;
					$location.search('forgot');
					modalInstance = $modal.open({
						templateUrl: 'app/partials/forgot.html',
						controller: function($scope, $modalInstance) {
							$scope.country = country;
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.oSign = oS;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('forgot',null);
							};
						}
					});
				}
				
				var oReset = function() {
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/reset-password.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin=oLogin;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('reset',null);
							};
						}
					});
				}
				var oConfirm = function() {
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/confirm.html',
						controller: function($scope, $modalInstance) {
							$scope.close = function() {
								$modalInstance.close();
								$location.search('confirm',null);
							};
						}
					});
				}
				
				var oRequest = function() {
					$scope.close();
					$location.search('request');
					modalInstance = $modal.open({
						templateUrl: 'app/partials/request.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oVerify = oVerify;
							$scope.close = function() {
								$modalInstance.close();
								$location.search('request',null);
							};
						}
					});
				}
				
				var oS = function() {
					var ref = $routeParams.ref;
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/verify.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.defstep="step3";
							$scope.ref = ref;
							$scope.unclosable=false;
							$scope.close = function() {
								$modalInstance.close();
							};
						}
					});
				}
				
				var oV = function(code) {
					var ref = $routeParams.ref;
					$scope.close();
					modalInstance = $modal.open({
						templateUrl: 'app/partials/verify.html',
						controller: function($scope, $modalInstance) {
							$scope.oLogin = oLogin;
							$scope.oRequest = oRequest;
							$scope.ref = ref;
							$scope.unclosable=false;
							if(typeof(code)!=='undefined') {
								$scope.invitationCode = code;
							} else {
								$scope.invitationCode = '';
							}
							$scope.close = function() {
								$modalInstance.close();
								$location.search('verify',null);
							};
						}
					});
				}
				
				if($scope.country!='US')
					var oVerify = oV;
				else
					var oVerify = oS;
				
				$scope.oVerify = oVerify;
				$scope.oLogin = oLogin;
				$scope.oReport = oReport;
				$scope.oRequest = oRequest;
			
				if(typeof($routeParams.login)!=='undefined') {
					oLogin();
				}
				if(typeof($routeParams.forgot)!=='undefined') {
					oForgot();
				}
				if(typeof($routeParams.request)!=='undefined') {
					oRequest();
				}
				if(typeof($routeParams.reset)!=='undefined') {
					oReset();
				}
				if(typeof($routeParams.confirm)!=='undefined') {
					oConfirm();
				}
				if(typeof($routeParams.oFacebookLogin)!=='undefined') {
					oFacebookLogin();
				}
				if(typeof($routeParams.verify)!=='undefined') {
					if($routeParams.verify!==true) {
						oVerify($routeParams.verify);
					} else {
						oVerify();
					}
				}
				if(typeof($routeParams.ref)!=='undefined') {
					oVerify();
				}



			}
		},true);


		
		var oReport = function(code) {
			$scope.close();
			var firstname = $scope.user.data.firstName;
			modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/report.html',
				controller: function($scope, Report, $modalInstance) {
					$scope.firstname = firstname;
					$scope.issue = {
						description:"",
						url:$location.$$absUrl,
						type:''
					};
					$scope.close = function() {
						$modalInstance.close();
					};
					$scope.send = function() {
						$scope.error=false;
						if($scope.issue.description.length<10) {
							$scope.error=true;
						} else {
							Report.query($scope.issue, function() {
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/report_response.html',
									controller: function($scope, $modalInstance) {
										$scope.close = function () {
											$modalInstance.close();
										};
									}
								});
								$scope.close();
							});
						}
					};
				}
			});
		}
		var oFacebookLogin = function(){
			$scope.close();
			$location.search('facebookLogin');
			$window.location.href = SocialNetwork+'/social-signin/?scope=email,user_status,user_about_me,user_activities,user_birthday,user_education_history,user_likes,user_location,friends_about_me,friends_birthday,friends_likes,friends_location';
		};
		$scope.oFacebookLogin = oFacebookLogin;

		$scope.$on('$routeChangeSuccess', function(next, current) {
			$scope.parsedurl = $location.$$path.split('/');					
			
			var authToken = $location.search().token;			 
			if(typeof authToken != 'undefined'
				 && authToken != null 
				 && $location.$$path == '/social/signin/ok'){
					SocialService.fbLogin.query({
						  authToken : authToken,
						networkName : 'FACEBOOK'
					},function(data){
					    if(data.success == 'true'){
							var user = UserEx.reinit.query({}, function() {
									User.setUser(user);	
									$scope.$watch(function() {
											return user;
										}, function(user) {
											if(typeof(user.data)!=='undefined') {
												var latestRelease = new Date(1392311475000);
												var lastLoginDate = null;
												if(typeof user.data.previousLoginAt != 'undefined' && user.data.previousLoginAt != null){
													 lastLoginDate = new Date(parseInt(user.data.previousLoginAt));
												}
												if(lastLoginDate == null || typeof(lastLoginDate) == 'undefined'){
													var modalInstance = $modal.open({
														templateUrl: 'app/partials/directives/modal/welcome_new.html',
														controller: function($scope, $modalInstance) {
															$scope.close = function () {
																$modalInstance.close();
															};
														
														}
													});
												}
												else if (lastLoginDate < latestRelease) {
													var modalInstance = $modal.open({
														templateUrl: 'app/partials/directives/modal/welcome_old.html',
														controller: function($scope, $modalInstance) {
															$scope.close = function () {
																$modalInstance.close();
															};
														
														}
													});
												}
											}
										}, true);
										$location.path('/browse');										
										
									});
						   }
					});
			}
			
			if($location.$$path == '/social/connect/ok' 
			|| $location.$$path == '/social/connect/error' ){
				$location.path('/profile/account/settings');	
			}
			var auth = Auth.query({}, function() {
				if(!auth.logged && $location.$$path!='/'
				&& $location.$$path!='/media' 
				&& $location.$$path!='/index1' 
				&& $location.$$path!='/index2' 
				&& $location.$$path!='/give'
				&& $location.$$path!='/receive'  
				&& $location.$$path!='/share' 
				&& $location.$$path!='/give-receive-inspire' 
				&& $location.$$path != '/social/signin/ok') {					
					$scope.logout();
				}
			});
			
			if($location.$$path=="/browse") {
				$scope.ptitle="Browse";
			} else if($location.$$path=="/invite") {
				$scope.ptitle = "Invite";
			} else if($location.$$path=="/top-giftengers") {
				$scope.ptitle = " ";
			} else {
				$scope.$watch(
					function() {
						return $routeParams;
					},
					function(routeParams) {
						if(typeof(routeParams.section)!=='undefined') {
							if(routeParams.section!='connections') {
								$scope.ptitle = routeParams.profilePage.charAt(0).toUpperCase() + routeParams.profilePage.slice(1);
							} else {
								$scope.ptitle = "Connections";
							}
						} else {
							$scope.ptitle = "";
						}
					},
					true
				);
			}
		
			if(typeof(current.params)!=='undefined' && typeof(current.params.view)!=='undefined') {
				AdDialog.open(current.params.view, $scope);
			}
			else {
				AdDialog.close();
			}
		});
		
		$scope.$watch(
			function() {
				return User.getUser();
			},
			function(user) {
				if(typeof(user) !== 'undefined') {
					$scope.user = user;
					if($scope.user.logged==false
						&& $location.$$path!='/'
						&& $location.$$path!='/media'
						&& $location.$$path!='/index1' 
						&& $location.$$path!='/index2' 
						&& $location.$$path!='/give'
						&& $location.$$path!='/receive' 
						&& $location.$$path!='/share' 
						&& $location.$$path!='/give-receive-inspire' 
						&& $location.$$path != '/social/signin/ok') {
						$scope.logout();
					}
					if(typeof(user.data)!=='undefined') {
						if(typeof(user.data.avatar) !== 'undefined' && user.data.avatar !== null){												
							$scope.user.data.avatar_url = Amazon+"user/"+user.data.avatar.id+"_60";
						}else{
							$scope.user.data.avatar_url = "assets/img/ge-no-profile-picture.png";
						}	
					}
				}
			},
			true
		);
		$scope.search = function(){
			var searchTxt = $('#sliedbar_search_text').val();
			$('#sliedbar_search_text').val('');
			$location.path('/browse/'+searchTxt);
		
		}
		
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
		
		$scope.inviteFriends = function(){
				FB.ui({
						method: 'feed',
						display: 'popup',
						
					}, function(response){});
		}
		$scope.updateProfile = function(user) {	
			$location.path('/profile/settings');
		   
		}
		
		$scope.logout = function() {
			$('.snap-slide').removeClass('active');
			var user = new Auth.remove({id:0});
			User.setUser({});
			$location.search('token',null);	
			$location.path('/');
			AdDialog.close();
		}
	}])

});