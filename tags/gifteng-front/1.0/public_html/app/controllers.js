define(['angular','services','lang','wistia'], function(angular,services,lang) {
	'use strict';
	
	var modalInstance;
	
	return angular.module('gifteng.controllers', ['gifteng.services'])
		
		
		/**
		 * Amazon URL
		 */
		.run(function($rootScope) {
			$rootScope.amazonUrl="https://s3.amazonaws.com/gifteng/";
		})
		
		/**
		 * Index Controller
		 */
		.controller('IndexController', function($scope) {
			var wistiaEmbed = Wistia.embed("4b9homs4ka", {
                videoWidth: 580,
                videoHeight: 326,
                volumeControl: true,
                videoFoam: true
            });
            
            $scope.scrollDown = function() {
				$(".snap-content").animate({ scrollTop: $('.ng-view').height() }, 2000);
            }
            $scope.scrollUp = function() {
				$(".snap-content").animate({ scrollTop: 0 }, 2000);
            }
		})
		
		/**
		 * Reviews Controller
		 */
		.controller('ReviewsController', function($scope, $routeParams, User, UserEx) {
			
			$scope.type="all";
			$scope.percent = {};
			
			if(typeof($routeParams.id)!=='undefined' && $routeParams.id) {
				$scope.selfview=false;
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
							$scope.selfview = true;
						}
					},
					true
				);
			}
			else {
				$scope.selfview=true;
			}
			var id;
			if($scope.selfview) {
				$scope.$watch(
					function() {
						return User.getUser();
					},
					function(user) {
						$scope.user = user;
						$scope.percent.positive = Math.round(($scope.user.data.statistics.numPositiveReceivedRatings / $scope.user.data.statistics.numReceivedRatings)*100);
						$scope.percent.negative = Math.round(($scope.user.data.statistics.numNegativeReceivedRatings / $scope.user.data.statistics.numReceivedRatings)*100);
						id=user.data.id;
			
						var data = UserEx.ratings.query({id:id}, function() {
							$scope.ratings = data.ratings;
							$scope.stats = data.stats;
							$scope.percent.neutral = 100 - ($scope.percent.positive + $scope.percent.negative);
						});
					},
					true
				);
			} else {
				var profile = UserEx.profile.query({id:$routeParams.id}, function() {
					$scope.percent.positive = Math.round((profile.statistics.numPositiveReceivedRatings / profile.statistics.numReceivedRatings)*100);
					$scope.percent.negative = Math.round((profile.statistics.numNegativeReceivedRatings / profile.statistics.numReceivedRatings)*100);
					id=$routeParams.id;
			
					var data = UserEx.ratings.query({id:id}, function() {
						$scope.ratings = data.ratings;
						$scope.stats = data.stats;
						$scope.percent.neutral = 100 - ($scope.percent.positive + $scope.percent.negative);
					});
				});
			}
		})
		
		/***
		 * Invite Controller
		 */
		
		.controller('InviteController', function($scope, Facebook, WindowService, TestEmailService) {
			
			$scope.searchInvites = '';
			$scope.mailTo ='';
			var invitationCode='2550'; //Let's define it here
			$scope.mailTxt ='Have you heard about Gifteng?\nIt\'s a unique social community where you can discover amazing, free gifts and give away the things you no longer need. http://gifteng.com';
			$scope.sendMsg = function(){
				FB.ui({
					method: 'send',
					link: 'http://gifteng.com',
					
				});
			}
			$scope.sendTwitterMessage = function(){
				var width = 575;
				var height = 450;
				WindowService.open_window_clear('https://twitter.com/share?text=' + 'Check out @gifteng, a unique social community for free stuff and giveaways.', '', width, height);
			}
			$scope.sendMail = function(){
				$scope.error = '';
				$scope.success = '';
				var recepiantsAddress = $scope.mailTo.split(",");
				var body = $scope.mailTxt;
				var recepiants = [];
				// TODO validation email addresses
							
				angular.forEach(recepiantsAddress, function(recepiant){
					if(!TestEmailService.isValid(recepiant)){
						$scope.error += recepiant+' ';
						return true;
					}
					this.push(recepiant);
				}, recepiants);
				if($scope.error){
					$scope.success = '';
					$scope.error = "Please enter a valid email address and if you enter multiple emails, separate them with comma (no spaces).";
					return true;
				}
				TestEmailService.sendMail.send({recepiants:recepiants,body:body},function(data){
					if(data.status == 'valid'){					
						$scope.error = '';
						$scope.success = "Invitation sent successfully!";
					}else{
						$scope.success = '';
						$scope.error = "Please enter a valid email address and if you enter multiple emails, separate them with comma (no spaces).";
					}
					
				});
				
			}
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
		.controller('PostController', function($scope, Geo, Ad, UserEx, Shipping, User, AdDialog, $routeParams, $location,Amazon) {
			AdDialog.close();
			$scope.newPost = function(adid) {
				$scope.image = null;
				$scope.step="step1";
				$scope.toPost={};
				$scope.toPost.address={};
				$scope.toPost.image='';
				$scope.error = '';
                var relistFrom = 0;
                
                if(!$scope.isModal || typeof($scope.isModal)==='undefined') {
                	$scope.close = function() {
                		$location.path('/browse');
                	}
                	var callback;
                } else {
                	var callback = function() {
                		$location.path($location.$$path+'/');
                	};
                }
                if(typeof adid !== "undefined"){
                	relistFrom=adid;
                    $scope.ad = Ad.get({id:adid}, function() {
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

                        $scope.toPost = $scope.ad;
                        $scope.toPost.category = $scope.ad.categoryId;
						$scope.toPost.address.zipCode = $scope.ad.address.zipCode;
                        $scope.image = Amazon+"ad/"+$scope.ad.image.id+"_640";
						
                    });
                }
                
				$scope.step1 = function() {
					$scope.step="step1";
				}
				$scope.step2 = function() {
					Shipping.query({},function(data){
						$scope.shippingBox = data;
					})
					var img = $scope.toPost.image;
					if(typeof img != 'undefined' && img != ''){
						$scope.error = '';
						$scope.step="step2";
					}else{	
						$('#gift-error-msg').css({'display':'none'});
						$('#error-msg').css({'display':'block'});
						$scope.error = "Please upload an image.";
						return false;
					}
					
					
				};
				var selectShippingBox = function(id){
					var boxes = $scope.shippingBox.shippingBoxes.shippingBox;
					for(var i=0; i < boxes.length ;i++){
						if(boxes[i].id == id){
							$scope.toPost.shippingBox = boxes[i];
						}
					}
				}
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
					else if(typeof($scope.toPost.address.zipCode)==='undefined' || $scope.toPost.address.zipCode=='') {
						$scope.error = "Please set your Zip code."
					}
					else if(!$scope.toPost.freeShipping && !$scope.toPost.pickUp) {
						$scope.error = "Please select the delivery method.";
					}
					else if($scope.toPost.freeShipping && !$scope.toPost.selectedBox1 && !$scope.toPost.selectedBox2 && !$scope.toPost.selectedBox3){
						$scope.error = "Please select shipping size.";
					}
					else {
						if($scope.toPost.freeShipping){
							if($scope.toPost.selectedBox1){
								selectShippingBox(1);
							}
							else if($scope.toPost.selectedBox2){
								selectShippingBox(2);
							}
							else if($scope.toPost.selectedBox3){
								selectShippingBox(3);
							}
						}
						if($scope.toPost.pickUp){
							$scope.error = "";
							$scope.step="step3";
							showMap(true);
						
						}
						else{
							$scope.step="step4";
							$scope.step4();
						}
					}
				};
				$scope.back = function(){
					if($scope.toPost.pickUp){
							$scope.step="step3";
							showMap(true);						
					}
					else{
						$scope.step="step2";
					}
				}
				$scope.step4 = function() {
					$scope.step="step4";
					if($scope.toPost.image !== null && typeof $scope.toPost.image === 'object'){
							$scope.imgPreview = $scope.image;
					}
					else{
						$scope.imgPreview = $scope.toPost.image;
					}
					
					var categories = [];
					categories[11]="Accessories & Bags";
					categories[12]="Art & Photography";
					categories[13]="Books";
					categories[10]="Collectibles & Craft";
					categories[14]="Electronics";
					categories[15]="Food & Wine";
					categories[20]="Health & Beauty";
					categories[21]="Home";
					categories[9]="Jewelry & Watches";
					categories[16]="Kids";
					categories[17]="Men's Clothing";
					categories[22]="Movie, Music, Games";
					categories[18]="Pets";
					categories[24]="Tickets & Gift Cards";
					categories[19]="Women's Clothing";
					categories[8]="Women's Shoes";
					
					$scope.category = categories[$scope.toPost.category];
				}
				var posting = {};
				$scope.log = function() {				
					posting = {
						title: $scope.toPost.title,
						description: $scope.toPost.description,
						categoryId: $scope.toPost.category,
						address: $scope.toPost.address,
						price: $scope.toPost.price,
						pickUp: $scope.toPost.pickUp,
						freeShipping: $scope.toPost.freeShipping,
						image: {url:$scope.toPost.image}
					}
					if($scope.toPost.freeShipping){
						posting.shippingBox = $scope.toPost.shippingBox;
					}
					
					
					if(typeof $routeParams.update !== 'undefined'){
						if($routeParams.update){
							Ad.update({id:adid, ad:posting,relist:relistFrom}, function(){
								$scope.step = 'step5';
								var user = UserEx.reinit.query({}, function() {
									User.setUser(user);
								});
							})
						}
					}
					else{
						Ad.save({ad:posting,relist:relistFrom}, function() {
							$scope.step='step5';
							var user = UserEx.reinit.query({}, function() {
								User.setUser(user);
								callback();
							});
						});
					}
					
					
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
		                iconUrl: 'assets/img/ge-location-pin-teal.png',
		                iconSize: [64, 64]
		            });
		            
					var geo = Geo.query({zip: $scope.toPost.address.zipCode}, function() {
						
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
			
			var url = $location.$$path.split('/');
			if(url[1]=="edit")
				$scope.newPost($routeParams.id);
			else
				$scope.newPost();
		})
		
		/**
		 * Browse Controller
		 */
		.controller('BrowseController', function($scope, $route, $modal, $window, AdDialog, AdMore, Ad, Filters, $routeParams) {
			
			Filters.setSearch({order:'newest',keywords:"",category:"",delivery:""});
			
			var toCols = function() {
				$scope.cols=[];
				for(var i=0;i<$scope.numberColumns;i++) {
					$scope.cols[i]=[];
				}
				
				if(typeof(ads)!=='undefined') {
					var c=0;
					for(var i=0;i<ads.length;i++) {
						$scope.cols[c].push(ads[i]);
						c++;
						if(c==$scope.numberColumns) {
							c=0;
						}
					}
				}
			}
			
			$scope.$watch(
				function() {
					return Filters.getColumns();
				},
				function(columns) {
					$scope.distributeColumns(columns);
				},
				true
			);
			
			$scope.$watch(
				function() {
					return $scope.colsClass;
				},
				function(colsclass) {
					toCols();
				},
				true
			);
			
			$scope.columnCheck = function() {
				if(window.innerWidth<480) {
					return 1;
				} else if(window.innerWidth<768) {
					return 2;
				} else if(window.innerWidth<992) {
					return 3;
				} else {
					return 4;
				}
			}
			
			$scope.distributeColumns = function(columns) {
				var size = $scope.columnCheck();
				if(size==1) {
					$scope.numberColumns=1;
				} else if(size==2 && columns>2) {
					$scope.numberColumns=2;
				} else if(size==3 && (columns==4 || columns==1)) {
					$scope.numberColumns=3;
				} else if(size==4 && columns<3) {
					$scope.numberColumns=4;
				} else {
					$scope.numberColumns=columns;
				}
				if($scope.colsClass!="col-xs-"+(12/$scope.numberColumns)) {
					$scope.colsClass = "col-xs-"+(12/$scope.numberColumns);
				}
			}

			angular.element($window).bind('resize',function(){
            	$scope.$apply(function() {
					$scope.distributeColumns(Filters.getColumns());
	            });
			});
			
			
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
			
			$scope.$watch(
				function() {
					return Filters.getSimple();
				},
				function(simple) {
					$scope.wellSimple = simple;
				},
				true
			);
			
			$scope.$watch(
				function() {
					return Filters.getSearch();
				},
				function(search) {
					$scope.order=search.order;
					$scope.keywords=search.keywords;
					$scope.category=search.category;
					$scope.delivery=search.delivery;
					$scope.doSearch();
				},
				true
			);
			
			if(typeof($routeParams.id)!=='undefined'){
				$scope.adsearch = $routeParams.id;
			}
			if(typeof($routeParams.view)!=='undefined') {
				AdDialog.open($routeParams.view, $scope);
			}
			$scope.$on('$locationChangeSuccess', function(event) {
				if(typeof($routeParams.view)!=='undefined') {
					AdDialog.open($routeParams.view, $scope);
				}
			});
			$scope.loaded=0;
			var more=0;
			var filterAds=[];
			var ads=[];
			
			$scope.doSearch = function() {
				more=0;
				$scope.loaded=0;
				filterAds=[];
				$scope.loadAds();
				$scope.creators=[];
				ads=[];
			}

			$scope.loadAds = function () {
				var loaded_ads = Ad.query({order:$scope.order,keywords:$scope.keywords,category:$scope.category,delivery:$scope.delivery}, function() {
					$scope.searchTerm = $scope.keywords;
					if(typeof(loaded_ads['ads'])!=='undefined') {
						$scope.hasAds = loaded_ads['ads'].length;
					}
					else {
						$scope.hasAds = -1;
					}
					$scope.loaded=1;
					$scope.creators = $.extend({},$scope.creators,loaded_ads['creators']);
					
					if(typeof(loaded_ads['ads'])!=='undefined') {
						for(var i=0;i<loaded_ads['ads'].length;i++) {
							if(filterAds.indexOf(loaded_ads['ads'][i]['id']) == -1) {
								filterAds.push(loaded_ads['ads'][i]['id']);
								ads.push(loaded_ads['ads'][i]);
							}
							if(i==loaded_ads['ads'].length-1) {
								$scope.last=loaded_ads['ads'][i].lastIndex;
							}
						}
					}
					toCols();
					$scope.filterAds = filterAds;
				});
			}
			
			var loadMore = function() {
				for(var i=0;i<more;i++) {
					var loaded_ads = AdMore.query({'last':$scope.last,order:$scope.order,keywords:$scope.keywords,category:$scope.category,delivery:$scope.delivery}, function() {
						$scope.creators = $.extend({},$scope.creators,loaded_ads['creators']);
						if(typeof(loaded_ads['ads'])!=='undefined') {
							for(var i=0;i<loaded_ads['ads'].length;i++) {
								if(filterAds.indexOf(loaded_ads['ads'][i]['id']) == -1) {
									filterAds.push(loaded_ads['ads'][i]['id']);
									ads.push(loaded_ads['ads'][i]);
								}
								if(i==loaded_ads['ads'].length-1) {
									$scope.last=loaded_ads['ads'][i].lastIndex;
								}
							}
						}
						toCols();
						$scope.filterAds = filterAds;
					});
				}
			}
			
			$scope.loadMore = function() {
				more++;
				loadMore();
			}
			
			$scope.callback = function() {
				$scope.loaded=0;
				filterAds=[];
				$scope.loadAds();
				ads=[];
			};
		})
		
		.controller('SettingsController',function($scope, $location, $modal, Auth, User, UserEx) {
			
			$scope.changed=false;
			
			$scope.user = User.getUser();
			$scope.showLink = false;
			
			$scope.$watch(
				function() {
					return $scope.user;
				},
				function(user) {
					if(typeof(user.data)!=='undefined') {
						$scope.editData = {
							'firstName': user.data.firstName,
							'lastName': user.data.lastName,
							'about': user.data.about,
							'zipCode': user.data.address.zipCode
						}
					}
				},
				true
			);
			
			$scope.resend_verification = function(){						
				UserEx.resendVerification.query({});
				$scope.showLink = true;
			};
			$scope.cancel = function(){
				window.history.back();
			};
			
			$scope.changePassword = function(){
				 var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/change-password.html',
					controller: function($scope, $modalInstance, User, UserEx,$location) {						
						
						$scope.error = '';
						$scope.close = function(){
							$modalInstance.close();
						};
						$scope.forgotPassword = function(){
							$scope.close();
							modalInstance = $modal.open({
								templateUrl: 'app/partials/forgot.html',
								controller: function($scope, $modalInstance) {
									$scope.inner=true;
									$scope.close = function() {
										$modalInstance.close();
									};
								}
							});
						};
						$scope.submit = function() {						
							
							var oldPassword = $('#password').val();
							var new_password_1 = $('#new_password_1').val();
							var new_password_2 = $('#new_password_2').val();
							
								
								if(typeof oldPassword == 'undefined' || oldPassword == ''){
									$scope.error = 'Please enter your current password.';
									return false;
								}
								if(typeof new_password_1 == 'undefined' || new_password_1 == ''){
									$scope.error = 'You must enter the new password twice in order to confirm it.';
									return false;
								}
								if(typeof new_password_2 == 'undefined' || new_password_2 == ''){
									$scope.error = 'You must enter the new password twice in order to confirm it.';
									return false;
								}
								if(new_password_1.length <6){
									$scope.error = 'Password is too short.';
									return false;
								}
								if(new_password_2.length <6){
									$scope.error = 'Password is too short.';
									return false;
								}
								if(new_password_1 == new_password_2){
									$scope.error = "";
									UserEx.changePassword.query({
											'old_password' : oldPassword,
												'password' : new_password_1
									},function(data){									
											if(data.status == 'valid'){
												$scope.error = '';
												$modalInstance.close();
											}else{
												$scope.error = 'Your old password is incorrect. ';
											}
									});
									
								}else{
									$scope.error = 'You must enter the same password twice in order to confirm it.';
									return false;
								}
							
						
						}
					}
				});
			
			};
			
			// TODO: Remove jQuery here !
			$scope.submit  = function(){
				UserEx.updateProfile.query({
					'first_name':$('#firstName').val(),
					'last_name':$('#lastName').val(),
					'about':$('#about').val(),
					'zipCode':$('#zipCode').val()
				}, function (){
					var user = UserEx.reinit.query({}, function() {
						User.setUser(user);
					});
					$scope.changed=false;
				});
			};  
		})
		
		.controller('SearchController', function($scope, Filters) {
			var search = Filters.getSearch();
			$scope.search={};
			angular.copy(search,$scope.search);
			$scope.submit = function() {
				Filters.setSearch($scope.search);
				$scope.close();
			}
		})
		
		/**
		 * Nav Controller
		 */
		.controller('NavController',function($scope, $location, $routeParams, $modal, Auth, User, UserEx, AdDialog, Filters, Amazon) {
			
			$scope.loaded=false;
			
			$scope.$watch(
				function() {
					return Filters.getMyGifts();
				},
				function(filter) {
					$scope.filter=filter;
				},
				true
			);
			
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
			
			var oLogin = function() {
				$scope.close();
				$location.search('login');
				modalInstance = $modal.open({
					templateUrl: 'app/partials/login.html',
					controller: function($scope, $modalInstance, $modal) {
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
				$location.search('forgot');
				modalInstance = $modal.open({
					templateUrl: 'app/partials/forgot.html',
					controller: function($scope, $modalInstance) {
						$scope.oLogin = oLogin;
						$scope.oRequest = oRequest;
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
			var oVerify = function(code) {
				$scope.close();
				$location.search('verify');
				modalInstance = $modal.open({
					templateUrl: 'app/partials/verify.html',
					controller: function($scope, $modalInstance) {
						$scope.oLogin = oLogin;
						$scope.oRequest = oRequest;
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
			$scope.close = function () {
				$location.search('login',null);
				$location.search('forgot',null);
				$location.search('request',null);
				$location.search('verify',null);
				if(modalInstance)
					modalInstance.close();
			};
			
			$scope.oLogin = oLogin;
			$scope.oReport = oReport;
			$scope.oRequest = oRequest;
			$scope.oVerify = oVerify;
			
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
			if(typeof($routeParams.verify)!=='undefined') {
				if($routeParams.verify!==true) {
					oVerify($routeParams.verify);
				} else {
					oVerify();
				}
			}
			
			$scope.$on('$routeChangeSuccess', function(next, current) {
				$scope.parsedurl = $location.$$path.split('/');
				var auth = Auth.query({}, function() {
					if(!auth.logged && $location.$$path!='/'
					&& $location.$$path!='/media') {
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
							&& $location.$$path!='/media') {
							$scope.logout();
						}
						if(typeof(user.data)!=='undefined') {
							if(typeof(user.data.avatar) !== 'undefined')
								$scope.user.data.avatar_url = Amazon+"user/"+user.data.avatar.id+"_60";
							else
								$scope.user.data.avatar_url = "assets/img/ge-no-profile-picture.png";
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
				$location.path('/');
			}
		})
		
		/**
		 * User Ads Controller
		 */
		.controller('UserAdsController',function($scope, $modal, $location, $routeParams, AdDialog, User, UserEx, AdUser, Filters) {
			
			Filters.setMyGifts("all");
			
			$scope.$watch(
				function() {
					return Filters.getMyGifts();
				},
				function(mygifts) {
					$scope.mygifts = "filter-"+mygifts;
				},
				true
			);
			
			$scope.$on('$locationChangeSuccess', function(event) {
				if(typeof($routeParams.view)!=='undefined') {
					AdDialog.open($routeParams.view, $scope);
				}
			});
			
			$scope.loadAds = function() {
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				
				$scope.numberColumns = 4;
				$scope.colsClass = "col-sm-"+(12/$scope.numberColumns);
				$scope.cols=[];
				var c=0;
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					$scope.$watch(
						function() {
							return User.getUser();
						},
						function(user) {
							if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
								$scope.selfview = true;
							}
						},
						true
					);
					var ads = AdUser.query({id:$routeParams.id}, function() {
						$scope.loaded=true;
						$scope.hasAds = ads.length>0;
						for(var i=0;i<$scope.numberColumns;i++) {
							$scope.cols[i]=[];
						}
						for(var i=0;i<ads.length;i++) {
							$scope.cols[c].push(ads[i]);
							c++;
							if(c==$scope.numberColumns) {
								c=0;
							}
						}
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
								var ads = AdUser.query({id:user.data.id}, function() {
									$scope.loaded=true;
									$scope.hasAds = ads.length>0;
									for(var i=0;i<$scope.numberColumns;i++) {
										$scope.cols[i]=[];
									}
									for(var i=0;i<ads.length;i++) {
										$scope.cols[c].push(ads[i]);
										c++;
										if(c==$scope.numberColumns) {
											c=0;
										}
									}
								});
							}
						},
						true
					);
				}
			};
			$scope.loadAds();
			$scope.callback = $scope.loadAds;
			
			
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
			
			if(typeof($routeParams.req)!=='undefined') {
				var id=$routeParams.req;
				var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/request_view.html',
					controller: function($scope, UserEx,AdImage, $modalInstance) {
						var data = UserEx.requestView.query({id:id}, function() {
							$scope.data = data;
							var ad = data.ads;
							AdImage.setAd(ad);
							AdImage.setRequest(data.request);
							if(ad.status ==  'SELECTED'){
								$scope.accepted = true;
							} else {
								$scope.accepted = false;
							}
						});
						$scope.send = function() {
							UserEx.requestSend.query({id:id}, function() {
								$scope.close();
								callback();
							})
						}
						$scope.accept = function() {
							
							UserEx.requestSelect.query({id:id}, function() {
														
								var modalInstance = $modal.open({
										templateUrl: 'app/partials/directives/modal/show-shipping-and-pickup.html',
										controller: function($scope, UserEx,AdImage, $modalInstance) {
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
												$scope.description = "As soon as "+ $scope.requestFirstname +" pays for shipping, through our system, we will email you a prepaid label. Simple as that! :)";
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
												var creatorId = ad.creator.id;
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
											}
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
		})
		
		/**
		 * Requested Ads Controller
		 */
		.controller('RequestedAdsController',function($scope, $routeParams, User, UserEx, AdRequested, AdDialog, GeModel, Filters) {
			Filters.setMyGifts("all");
			
			$scope.$watch(
				function() {
					return Filters.getMyGifts();
				},
				function(mygifts) {
					$scope.mygifts = "filter-"+mygifts;
				},
				true
			);
			
			$scope.$on('$locationChangeSuccess', function(event) {
				if(typeof($routeParams.view)!=='undefined') {
					AdDialog.open($routeParams.view, $scope);
				}
			});
			
			$scope.loadAds = function() {
				$scope.loaded=false;
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				
				$scope.numberColumns = 4;
				$scope.colsClass = "col-sm-"+(12/$scope.numberColumns);
				$scope.cols=[];
				
				var c=0;
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					$scope.$watch(
						function() {
							return User.getUser();
						},
						function(user) {
							if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
								$scope.selfview = true;
							}
						},
						true
					);
					var ads = AdRequested.query({id:$routeParams.id}, function() {
						$scope.ads = ads.ads;
						$scope.creators = ads.creators;
						GeModel.creators = ads.creators;
						$scope.loaded=true;
						$scope.hasAds = $scope.ads.length>0;
						for(var i=0;i<$scope.numberColumns;i++) {
							$scope.cols[i]=[];
						}
						for(var i=0;i<ads.ads.length;i++) {
							$scope.cols[c].push(ads.ads[i]);
							c++;
							if(c==$scope.numberColumns) {
								c=0;
							}
						}
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
									GeModel.creators = ads.creators;
									$scope.loaded=true;
									$scope.hasAds = $scope.ads.length>0;
									for(var i=0;i<$scope.numberColumns;i++) {
										$scope.cols[i]=[];
									}
									for(var i=0;i<ads.ads.length;i++) {
										$scope.cols[c].push(ads.ads[i]);
										c++;
										if(c==$scope.numberColumns) {
											c=0;
										}
									}
								});
							}
						},
						true
					);
				}
			};
			$scope.loadAds();
			$scope.callback = $scope.loadAds;
		})
		
		/**
		 * Bookmarked Ads Controller
		 */
		.controller('BookmarkedAdsController',function($scope, $routeParams, User, UserEx, AdDialog, AdBookmarked, GeModel, Filters) {
			$scope.loaded=false;
			Filters.setMyGifts("all");
			
			$scope.$watch(
				function() {
					return Filters.getMyGifts();
				},
				function(mygifts) {
					$scope.mygifts = "filter-"+mygifts;
				},
				true
			);
			
			$scope.$on('$locationChangeSuccess', function(event) {
				if(typeof($routeParams.view)!=='undefined') {
					AdDialog.open($routeParams.view, $scope);
				}
			});
			
			$scope.loadAds = function() {
				var user = UserEx.reinit.query({}, function() {
					User.setUser(user);
				});
				
				$scope.numberColumns = 4;
				$scope.colsClass = "col-sm-"+(12/$scope.numberColumns);
				$scope.cols=[];
				var c=0;
				
				if(typeof($routeParams.id)!=='undefined') {
					$scope.selfview=false;
					$scope.$watch(
						function() {
							return User.getUser();
						},
						function(user) {
							if(typeof(user.data)!=='undefined' && user.data.id == $routeParams.id) {
								$scope.selfview = true;
							}
						},
						true
					);
					var ads = AdBookmarked.query({id:$routeParams.id}, function() {
						$scope.ads = ads.ads;
						$scope.creators = ads.creators;
						GeModel.creators = ads.creators;
						$scope.loaded=true;
						$scope.hasAds = $scope.ads.length>0;
						for(var i=0;i<$scope.numberColumns;i++) {
							$scope.cols[i]=[];
						}
						for(var i=0;i<ads.ads.length;i++) {
							$scope.cols[c].push(ads.ads[i]);
							c++;
							if(c==$scope.numberColumns) {
								c=0;
							}
						}
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
									GeModel.creators = ads.creators;
									$scope.loaded=true;
									$scope.hasAds = $scope.ads.length>0;
									for(var i=0;i<$scope.numberColumns;i++) {
										$scope.cols[i]=[];
									}
									for(var i=0;i<ads.ads.length;i++) {
										$scope.cols[c].push(ads.ads[i]);
										c++;
										if(c==$scope.numberColumns) {
											c=0;
										}
									}
								});
							}
						},
						true
					);
				}
			};
			$scope.loadAds();
			$scope.callback = $scope.loadAds;
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
			$scope.changed=false;
			$scope.allSelected=true;
			
			var values = UserEx.notifications.query({}, function() {
				var texts = lang.notifications;
				$scope.notifications = {};
				angular.forEach(texts, function(value, key) {
					if(typeof(values.notifiableTypes)!=='undefined' && values.notifiableTypes.indexOf(key)>-1) {
						$scope.notifications[key] = {
							'text':value,
							'checked':true
						}
					}
					else {
						$scope.allSelected=false;
						$scope.notifications[key] = {
							'text':value,
							'checked':false
						}
					}
				});
			});
			
			$scope.toggleAll = function() {
				angular.forEach($scope.notifications, function(value, key) {
					value.checked=!$scope.allSelected;
				});
				$scope.allSelected=!$scope.allSelected;
			}
			
			$scope.submit = function() {
				var notifications = [];
				angular.forEach($scope.notifications, function(value, key) {
					if(value.checked) {
						notifications.push(key);
					}
				});
				values.notifiableTypes = notifications;
				$scope.changed=false;
				values.$save();
			}
		})
		
		/**
		 * Messages Controller
		 */
		.controller('MessagesController',function($scope, $routeParams, $location, UserEx, User) {
			$scope.user = User.getUser();
			$scope.loaded = false;
			$scope.messages = UserEx.message.query({}, function() {
				if($scope.messages.length) {
					$scope.hasMsg = true;
				} else {
					$scope.hasMsg = false;
				}
				$scope.loaded=true;
			});
			$scope.showMessage = false;
			
			
			var openmsg = function(id) {
				$scope.msg = id;
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
						if($scope.messages.length) {
							$scope.hasMsg = true;
						} else {
							$scope.hasMsg = false;
						}
						$scope.loaded=true;
					});
				});
			}
			$scope.openmsg = function(id) {
				$location.search('msg',id);
				openmsg(id);
			}
			$scope.closemsg = function() {
				$location.search('msg',null);
				$scope.msg=null;
				$scope.showMessage=false;
			}
			if(typeof($routeParams.msg)!=='undefined') {
				$scope.openmsg($routeParams.msg);
			}
			$scope.hidemsg = function(id) {
				UserEx.messageHide.query({id:id}, function() {
					var msgs = UserEx.message.query({}, function() {
						$scope.messages = msgs;
						if($scope.messages.length) {
							$scope.hasMsg = true;
						} else {
							$scope.hasMsg = false;
						}
						$scope.loaded=true;
					});
					var user = UserEx.reinit.query({}, function() {
						User.setUser(user);
					});
				});
			}
		})
		/**
		 * Admin Controller
		 */
		.controller('AdminController', function($scope, $location, Auth, User,AdminService, Amazon) {
			$scope.user = User.getUser();
			$scope.showGiftDashboard = false;
			$scope.showShippingDashboard = false;
			var shippingLabels = new Array();
			
			$scope.giftDashboard = function(){
				$scope.showShippingDashboard = false;
				AdminService.getUnApproved.query(function(data){
					if(data.role != 'user'){
						if(typeof data.ads.ad != 'undefined' && data.ads.ad != null){					
								$scope.ads = data.ads.ad;	
								$scope.showGiftDashboard = true;					
								
						}else{
							$scope.showGiftDashboard = false;
						}
					}else{
						$location.path('/signin');						
					}							
				});
			};			
			
			$scope.approve_ad = function(adId){
				AdminService.approved.query(
				{
					'ad_Id':adId,
				}
				,function(data){			
							
				});
			};
			$scope.unapprove_ad = function(adId){			
				AdminService.unapproved.query(
				{
					'ad_Id':adId,
				  'reason' :$('#reason_'+adId).val()
				},
				function(data){
					if(data.status == 'valid'){
						var ads = $scope.ads;
						 angular.forEach(ads, function(v, index){
							if(v.id == adId){
								ads.splice(ads.indexOf(v), 1);
								$scope.ads = ads;	 
							}
						});
					}
								
				});
			};
			$scope.online_ad = function(adId){
				AdminService.online.query(
				{				
					'ad_Id':adId
				},function(data){
				/*	if(data.status == 'valid'){
						var ads = $scope.ads;
						 angular.forEach(ads, function(v, index){
							if(v.id == adId){
								ads.splice(ads.indexOf(v), 1);
								$scope.ads = ads;	 
							}
						});
					}*/
								
				});
			};
			var updateShippingBoxLabelUrl = function(shipping){				
				if(shipping.length > 1){
					for(var i = 0; i < shipping.length; i++){
						if(typeof shipping[i].barcodeImage != 'undefined'){							
							shipping[i].fileUrl = Amazon+"shipping/"+shipping[i].barcodeImage.id;							
						}
						if(!shipping[i].trackingNumber && !shipping[i].receivedAmount && !shipping[i].barcodeImage){
							shipping[i].showUpdate = true;
						}else{
							shipping[i].showUpdate = false;
						}
					}
				}
				else{					
					shipping.shipping.fileUrl = Amazon+"shipping/"+shipping.shipping.barcodeImage.id;					
					if(!shipping.shipping.trackingNumber && !shipping.shipping.receivedAmount && !shipping.shipping.barcodeImage){
							shipping.shipping.showUpdate = true;
						}else{
							shipping.shipping.showUpdate = false;
						}
				}
			};
			$scope.shippingDashboard = function(){				
				$scope.showGiftDashboard = false;				
				AdminService.getShippings.query(function(data){
					if(data.role != 'user'){
						if(typeof data.shippings != 'undefined' && data.shippings != null){					
								if(data.shippings.shipping.length >1){
									$scope.ads = data.shippings.shipping;	
								}else{
									$scope.ads = data.shippings;
								}
								updateShippingBoxLabelUrl($scope.ads);
								$scope.showShippingDashboard = true;					
							
						}else{
							$scope.showShippingDashboard = false;
						}
					}else{
						$location.path('/signin');						
					}							
				});
			};
			
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
				angular.forEach(shippingLabels, function(value , key) {
					if(value.shippingId == shipping.id){
						shippingBox.barcodeImage = {};
						shippingBox.barcodeImage.url = value.fileUrl;
					}
				});							
				
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
				var idar = $(el).attr('id').split('-');
				var id = idar[1];
				var extension = el.value.replace(/^.*\./, '');

				if (extension == el.value) {
					extension = '';
				} else {						
					extension = extension.toLowerCase();
				}
				if(extension!= 'pdf'){
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
								$scope.image = "api/image/"+filename;									
								angular.forEach($scope.ads, function(value , key) {
									if(value.id == id){
										value.fileName = filename;
										value.fileUrl = "api/image/"+filename;
									}
								});										
								 shippingLabels.push({'shippingId':id,'fileUrl':"api/image/"+filename});
							});
						}									
					}
				});
			};			
			
		})
		
		/**
		 * Login Controller
		 */
		.controller('LoginController', function($scope, $location, $cookies, $modal, Auth,UserEx, User,TestEmailService) {
			$scope.remember = false;
			var email_cookie = $cookies.giftengEmail;
			if(email_cookie) {
				$scope.email = email_cookie;
				$scope.remember = true;
			}
			
			$scope.error = '';
			$scope.login = function() {
				$location.search('login',null);
				var email = $scope.email;
				var password = $scope.password;
				var lEmail = $('#email').val();
				if(typeof lEmail == 'undefined' || lEmail == ''){					
					//$scope.error = 'Please enter your email ';
					return false;
				}else if(typeof password != 'undefined' || password != ''){					
					if(!TestEmailService.isValid(lEmail)){						
						$scope.error = 'Please enter a vaild email address';
						return false;
					}
				} 
				
				if(typeof password == 'undefined' || password == ''){					
				//	$scope.error = 'Please enter your password ';
					return false;
				}
			
				var auth = new Auth;
				auth.email = $scope.email;
				auth.password = $scope.password;
				auth.$save(function(response) {
					if(response.success == 'true'){
						var user = new Auth.get();
						User.setUser(user);
						if($scope.remember) {
							$cookies.giftengEmail = $scope.email;
						}
						else {
							$cookies.giftengEmail = '';
						}
						$scope.close();
						var user = UserEx.reinit.query({}, function() {
							
							var lastLoginDate = user.data.lastLoginAt;
							if(lastLoginDate == null || lastLoginDate == 'undefined'){
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/welcome_new.html',
									controller: function($scope, $modalInstance) {
										$scope.close = function () {
											$modalInstance.close();
										
										};
									
									}
								});
							}
							else{
								var modalInstance = $modal.open({
									templateUrl: 'app/partials/directives/modal/welcome_old.html',
									controller: function($scope, $modalInstance) {
										$scope.close = function () {
											$modalInstance.close();
										
										};
									
									}
								});
							}
						
						});
						
							
						
						
						$location.path('/profile/gifts/giving');
					}else{
						$scope.error = 'Oops, the email address or password is not correct. ';
					}
				});
			}
		})
		/**
		 * Request Controller
		 */
		//TODO: Remove jQuery
		.controller('RequestController', function($scope, $http, $routeParams, UserEx,InviteService,TestEmailService,IpService) {
			$scope.error = "";
			$scope.step = "step1";
			var email = '';
			$scope.showOther = false;
			$scope.selectedItem="";
			$scope.country="";
			var source = ['Google','Facebook','Twitter','From a Friend','Other'];
			$scope.source = source;
			var ip = '';
			var country;
			var otherSource = '';
			
			$http.get('app/countries.json').success(function(data) {
				$scope.countries = data;
			});
			
			IpService.get().then(function (data) {						
					ip = data.host;
					country = data.countryName;					
			});		
			
			$scope.changeValue = function(item){
				if(item == 'Other'){
					$scope.showOther = true;
				}else{
					$scope.showOther = false;
				}
			}
			
			$scope.step1 = function() {	
				//var email = $scope.email;	
					 email = $('#email').val();
				
				if(typeof email == 'undefined' || email == ''){					
					$scope.error = 'Please enter your email address';
					return false;
				}else {
					if (!TestEmailService.isValid(email)){						
						$scope.error = 'Please enter a valid email address ';
						return false;
					}else{
							$scope.error = "";
							$scope.step = "step2";
						  }
				}			
				
			}
			
			$scope.confirmRequest = function(){
				$scope.error = '';
				var source = $('#source').val();
				var country=$('#country').val();
				
				if(source == 'Other'){
					otherSource = $('#otherSource').val();
					if(otherSource == ''){
						$scope.error = 'Please enter the other source';
						return false;
					}
				}
				
				if(country == "" || country==null){
					$scope.error = 'Please select your country';
					return false;
				}
				if(source == "" || source==null){
					$scope.error = 'How did you hear about us?';
					return false;
				}
			
				
				if(country != '' && country!=null && source != '' && source!=null){
					var zip=0;
					if(country=="US") {
						zip=10001;
					}
					
					$scope.error =''; 
					InviteService.requestInvitation.query({
						'invite_email' : email,
						'ipAddress' : ip,
						'country' : country,
						'zipCode' : zip,
						'source' : source,
						'otherSource' : otherSource
					},function(data){				
							if(data.status == 'invalid'){
								$scope.error = 'Can not send Invitation now! ';
							}else{
								$scope.error = '';
								$scope.step="step3";
								
							}
					});
				}
			
			}
					
			
		})
		
		
		
		
		/**
		 * Verify Invitation Controller
		 */
		 
		.controller('VerifyInvitationController', function($scope,Auth,User,IpService,UserEx,InviteService,$modal,TestEmailService,$location,Amazon) {
			$scope.error = '';
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
				
				var email = $('#email').val();			
				var password = $('#password').val();
				var confirmPassword = $('#confirmPassword').val();
				
		
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
					
				InviteService.registerUser.query({
							'code' : code,
							'email': email,
							'password' : password							
						},function(data){				
							if(!data.success){
								$scope.error = data.faultstring;
							}else{
								var user = new Auth.get();
								User.setUser(user);
								$scope.error = '';																
								$scope.step="step4";
								$scope.user = User.getUser();
						
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
							$location.path('/browse');
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
		
		
		})
		
		/**
		 * Verify Email Controller
		 */
		 
		.controller('VerifyController', function($scope, $routeParams) {
			
		
		})
		
		/**
		 * Confirm Controller
		 */
		
		.controller('ConfirmController', function($scope, $routeParams, UserEx) {
			var code = $routeParams.confirm;
			UserEx.verifyUser.query({});
		})
		
		/**
		 * Reset Password Controller
		 */
		
		//TODO: Remove jQuery 
		.controller('ResetController', function($scope, $routeParams, $location, $timeout, UserEx) {
			$scope.error = '';
			$scope.completed = false;
			$scope.resetPassword = function(){
				var code = $routeParams.reset;
				var password = $('#password').val();
				var newPassword = $('#new_password').val();
						if(typeof code == 'undefined' || code == ''){
							$scope.error = 'Your session is expired.'
							return false;
						}
						if(typeof password == 'undefined' || password == ''){
							$scope.error = 'You must enter the new password twice in order to confirm it.';
							return false;
						}else if(password.length <6){
							$scope.error = 'Password is too short.';
							return false;
						}
						if(typeof newPassword == 'undefined' || newPassword == ''){
							$scope.error = 'You must enter the new password twice in order to confirm it.';
							return false;
						}else if(newPassword.length <6){
							$scope.error = 'Password is too short.';
							return false;
						}
						
						
						
						if(password == newPassword){
							$scope.error = "";
							UserEx.resetPassword.query({
								'code' : code,
								'password' : password
							},function(data){									
									if(data.status == 'true'){
										$scope.completed = true;
										$timeout(function() {
											$location.search('reset',null);
											$scope.oLogin();
										}, 5000);
									}else{
										$scope.error = 'Password reset does not succeeded!';
									}
							});
							
						}else{
							$scope.error = 'You must enter the same password twice in order to confirm it.';
							return false;
						}


			}
			
		
		})
		
		/**
		 * Forgot Password Controller
		 */
		 
		.controller('ForgotController', function($scope, $routeParams, UserEx,IpService,TestEmailService) {
			var ip = '';
			IpService.get().then(function (data) {					
					ip = data.ip;
			});

			$scope.step="step1";
			$scope.error = "";
				
			$scope.forgot = function() {	
				//var email = $scope.email;
				var email = $('#email').val();
				
				if(typeof email == 'undefined' || email == ''){					
					$scope.error = 'Please enter your email address';
					return false;
				}else {
					if (!TestEmailService.isValid(email)){						
						$scope.error = 'Please enter a valid email address ';
						return false;
					}else{
						  $scope.error = "";
							UserEx.forgotPasswordEmail.query({
								'email_address' : email,
								   'ip_address' : ip
							},function(data){									
									if(data.status == 'valid'){
										$scope.error = '';
										$scope.step="step2";
									}else{
										$scope.error = ' Sorry we don\'t recognize your email address! ';
									}
							});

					}
				}			
				
				
			}
		})
		
		
		/**
		 * Profile Controller
		 */
		.controller('ProfileController', function($scope, $routeParams, User) {
			
			if(!$routeParams.profilePage && !$routeParams.section) {
				$scope.section="";
				$scope.tab_page="";
			}
			else if(!$routeParams.profilePage) {
				$scope.isin=true;
				$scope.section=$routeParams.section;
				if($routeParams.section=="gifts") {
					$scope.tab_page="giving";
				} else if($routeParams.section=="connections") {
					$scope.tab_page="following";
				} else if($routeParams.section=="account") {
					$scope.tab_page="notifications";
				}else if($routeParams.section=="settings") {
					$scope.tab_page="settings";
				}
				
			}
			else {
				$scope.isin=true;
				$scope.section=$routeParams.section;
				$scope.tab_page=$routeParams.profilePage;
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
		})
		
		.controller('ProfileBoxController', function($scope, $routeParams, $modal, User, UserEx, Filters, ProfileData) {
			
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
		})
		
		/**
		 * Request Gift Controller
		 */
		.controller('RequestGiftController', function($scope, $location, $modal, User, UserEx, AdDialog) {
			$scope.user=User.getUser();
			$scope.doPost = function() {
				$scope.close();
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
		})
		
		
		/**
		 * View Gift Controller
		 */
		.controller('ViewGiftController', function($scope, $modal, $routeParams, $location, $timeout, Ad) {
			
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
			
			$scope.close = function() {
				$location.path('/browse');
			}
			$scope.viewGift = function() {
				if($scope.inModal) {
					var adid = $scope.ad_id;
				}
				else {
					var adid = $routeParams.id;
				}
				$scope.ad = Ad.get({id:adid}, function() {
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
					$timeout(showMap, 100);
				});
			};
			
			$scope.requestGift = function(id, callback) {
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
								$location.path('/profile/gifts/receiving');
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
			$scope.editGift = function(id, callback) {
			    var modalInstance = $modal.open({
					templateUrl: 'app/partials/directives/modal/edit_gift.html',
					controller: function($scope, $modalInstance, $location) {
						$scope.close = function () {
							$modalInstance.close();
							
						};
						$scope.edit = function(){
							$modalInstance.close();
							callback();
							$location.path('/edit/gift/'+id+'/true');
						}
					}
				});
			}
			$scope.deleteGift = function(id, callback) {
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
								$location.path('/profile/gifts/giving');
							});
						}
					}
				});
			}
			$scope.relistGift = function(id, callback) {
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
								$location.path('/profile/gifts/giving');
							})
						}
					}
				});
			}
			
			$scope.viewGift();
		});
});
