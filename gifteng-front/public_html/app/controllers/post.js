define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('PostController', ["$scope", "Geo", "Ad", "UserEx", "Shipping", "User", "AdDialog", "$routeParams", "$location", "Amazon", "Categories", "SharedService", function($scope, Geo, Ad, UserEx, Shipping, User, AdDialog, $routeParams, $location, Amazon, Categories,SharedService) {
		AdDialog.close();
		$scope.user = User.getUser();
		
		$scope.$watch(function() {
			return Categories.get();
		}, function(cat) {
			$scope.categories=cat;
		}, true);
		
		
		$scope.newPost = function(adid) {
			$scope.image = null;
			$scope.step="step1";
			var zip;
			if(typeof($scope.user)!=='undefined' && typeof($scope.user.data)!=='undefined' && typeof($scope.user.data.address.zipCode)!=='undefined') {
				zip=$scope.user.data.address.zipCode;
			}
			$scope.toPost={
				address:{'zipCode':zip},
				image:''
			}
			$scope.error = '';
            var relistFrom = 0;
            
            if(!$scope.isModal || typeof($scope.isModal)==='undefined') {
            	$scope.close = function() {
            		$location.path('/browse');
            	}
            	var callback;
            } else {
				$scope.$parent.$parent.unclosable=true;
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
				else if((typeof($scope.toPost.address.zipCode)==='undefined' || $scope.toPost.address.zipCode=='') && $scope.toPost.pickUp) {
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
				
				angular.forEach($scope.categories, function(v, k) {
					if(v.id==$scope.toPost.category) {
						$scope.category=v.name;
					}
				});
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
					var ad_id = Ad.save({ad:posting,relist:relistFrom}, function() {
						$scope.step='step5';
						var path = $location.$$protocol+"://"+$location.$$host;
						
						var ad = Ad.get({id:ad_id.id}, function() {
							if(typeof(ad.ad)!=='undefined') {
								ad = ad.ad;
								$scope.share_on_facebook_modal = function () {
									 SharedService.shareOnFacebook(ad.title, path+'/#/view/gift/'+ad.id, Amazon+"ad/"+ad.image.id+"_640");		
								}
								$scope.share_on_twitter_modal = function () {
									SharedService.shareOnTwitter(ad.title, path+'/#/view/gift/'+ad.id, Amazon+"ad/"+ad.image.id+"_640");
								}
								$scope.share_on_pinterest_modal = function () {
									SharedService.shareOnPinterest(ad.title, path+'/#/view/gift/'+ad.id, Amazon+"ad/"+ad.image.id+"_640");	
								}
								$scope.share_on_googleplus_modal = function () {
									SharedService.shareOnGooglePlus(ad.title, path+'/#/view/gift/'+ad.id, Amazon+"ad/"+ad.image.id+"_640");	
								}
							}
						});
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
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
	}])

});