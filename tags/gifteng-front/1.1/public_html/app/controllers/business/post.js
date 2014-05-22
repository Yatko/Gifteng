define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('PostController', function($scope, $modal, Ad, User, Profile) {
		$scope.step="step1";
		$scope.toPost={};
		$scope.toParse={};
		$scope.toPost.image='';
		$scope.next=function(id) {
			if(id==2) {
				$('.datepicker').datepicker();
			}
			if(id==3) {
				if(!$scope.toPost.price) {
					$scope.error = "Gift value is a mandatory field!";
				} else {
					$scope.error = "";
					$scope.step="step"+id;
				}
			}
			else {
				$scope.step="step"+id;
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
			posting.pickUp=1;
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
	});
	

});