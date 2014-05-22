define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('GiveController',
		["$scope", "User", "Auth", "Geo", "TestEmailService", "InviteService", "Ad", "UserEx", "$location", "Shipping", "Categories",
		function($scope, User, Auth, Geo, TestEmailService, InviteService, Ad, UserEx, $location, Shipping, Categories) {
			
			$scope.$watch(function() {
				return Categories.get();
			}, function(cat) {
				console.log(cat);
				$scope.categories=cat;
			}, true);
			
			$scope.shippingBox = {"shippingBoxes":{"shippingBox":[{"type":"ShippingBoxDto","id":1,"name":"Envelop","price":"4.00","paypalUrl":"https:\/\/www.paypal.com\/cgi-bin\/webscr?cmd=_s-xclick&hosted_button_id=XLCXUTP6MGW4L"},{"type":"ShippingBoxDto","id":2,"name":"Medium Box","price":"6.00","paypalUrl":"https:\/\/www.paypal.com\/cgi-bin\/webscr?cmd=_s-xclick&hosted_button_id=RAA2G2T926MQG"},{"type":"ShippingBoxDto","id":3,"name":"Large Box","price":"9.00","paypalUrl":"https:\/\/www.paypal.com\/cgi-bin\/webscr?cmd=_s-xclick&hosted_button_id=AYH8SBEH85ECL"}]}};
			$scope.hasAcc=0;

			var give = function() {
				$scope.image = null;
				$scope.step="details";
				
				$scope.toPost={
					image:'',
					address:{zipCode:null}
				}
				$scope.error='';

				// back
				$scope.details = function(){
					$scope.step="details";
				}
				
				// preview
				$scope.preview = function() {
					if(!$scope.toPost.image) {
						$scope.error = "Please upload a photo of your gift.";
					}
					else if(!$scope.toPost.title) {
						$scope.error = "Please name your gift.";
					}
					else if(!$scope.toPost.description) {
						$scope.error = "Please describe your gift.";
					}
					else if(!$scope.toPost.category) {
						$scope.error = "Please select a category.";
					}
					else if(!$scope.toPost.price) {
						$scope.error = "Please enter your gift's Current Value.";
					}
					else if((typeof($scope.toPost.address.zipCode)==='undefined' || !$scope.toPost.address.zipCode) && $scope.toPost.pickUp) {
						$scope.error = "Please set your Zip code."
					}
					else if(!$scope.toPost.freeShipping && !$scope.toPost.pickUp) {
						$scope.error = "Please select the delivery method.";
					}
					else if($scope.toPost.freeShipping && !$scope.toPost.selectedBox1 && !$scope.toPost.selectedBox2 && !$scope.toPost.selectedBox3){
						$scope.error = "Please select shipping size.";
					}
					else {
						$scope.error="";
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
						$scope.step="preview";
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
				};
				
				$scope.toggleAcc = function() {
					$scope.hasAcc=!$scope.hasAcc;
				}
				// register
				$scope.register = function() {
					$scope.step="register";
					$scope.regstep=1;
					$scope.reg={};
					$scope.lgn={};

					var watchpost = $scope.$watch(function() {
						return User.getUser();
					}, function(user) {
						$scope.user = user;
						if(user.logged) {
							watchpost();
						}
					}, true);
				};
				
				// login
				$scope.login = function() {
					var auth = new Auth;
					auth.email = $scope.lgn.email;
					auth.password = $scope.lgn.password;
					auth.$save(function(response) {
						if(response.success == 'true'){
							var user = new Auth.get();
							User.setUser(user);
						} else{
							$scope.error = 'Oops, the email address or password is not correct. ';
						}
					});
				}

				// registerUser
				$scope.registerUser = function(){

					var email = $scope.reg.email;
					var password = $scope.reg.password;
					var confirmEmail = $scope.reg.confirmEmail;
					
					if(typeof email == 'undefined' || email == ''){					
						$scope.error = 'Please enter your email address';
						return false;
					}else if (!TestEmailService.isValid(email)){						
						$scope.error = 'Please enter a valid email address ';
						return false;
					}
					if(typeof password == 'undefined' || password == ''){					
						$scope.error = 'Please enter your password.';
						return false;
					}else if(password.length <6){
						$scope.error = 'Password is too short.';
						return false;
					}
					if(typeof confirmEmail == 'undefined' || confirmEmail == ''){					
						$scope.error = 'You must enter your email twice in order to confirm it.';
						return false;
					}				
					if(confirmEmail != email){
						$scope.error = "The entered emails don't match.";
						return false;
					}
					
					InviteService.registerUser.query({
						'code' : null,
						'email': email,
						'password' : password,	
					},function(data){				
						if(!data.success){
							$scope.error = data.faultstring;
						} else{
							var user = new Auth.get();
							User.setUser(user);
							$scope.error = '';
							$scope.regstep = 2;
							$scope.user = User.getUser();
						}
					});	 
				};
				
				// save details
				$scope.saveProfile = function(){
					var firstName = $scope.reg.firstName;
					var lastName = $scope.reg.lastName;
					var zipCode = $scope.reg.zipCode;
						
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
						var user = UserEx.reinit.query({});
						$scope.post();
					});
				};
				
				// post
				var posting = {};
				$scope.post = function() {		
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
					
					Ad.save({ad:posting}, function() {
						$scope.step='return';
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
				}
				
				// select shipping box
				var selectShippingBox = function(id){
					var boxes = $scope.shippingBox.shippingBoxes.shippingBox;
					for(var i=0; i < boxes.length ;i++){
						if(boxes[i].id == id){
							$scope.toPost.shippingBox = boxes[i];
						}
					}
				}
				
				// do post
				$scope.doPost = function() {
					$location.path('/profile/gifts/giving');
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
				
			}
			give();
		}]);

});