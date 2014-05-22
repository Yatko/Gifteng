define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('userProfile', function() {
		return {
			restrict:'E',
			scope: {
				user:'=',
				callback:'&',
				img:'@',
				imgSize:'@',
				self:'=',
				nested:'@',
				following:'=',
				fullscore:'@',
				stats:'='
			},
			templateUrl: 'app/partials/directives/user-profile.html',
			replace:true,
			controller: function( $scope, $element, $attrs, UserEx, User, Amazon ) {
				$scope.follow = function() {
					$scope.data = UserEx.follow.query({id:$scope.user.id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
					$scope.following=true;
				}
				$scope.unfollow = function() {
					$scope.data = UserEx.unfollow.query({id:$scope.user.id}, function() {
						var user = UserEx.reinit.query({}, function() {
							User.setUser(user);
						});
					});
					$scope.following=false;
				}
				$scope.amazonUrl = Amazon;
			},
			compile: function() {
				return {
					pre: function(scope, iElement, iAttrs) {
			        	iAttrs.$observe('img', function() {
			        		if(!scope.imgSize) {
			        			scope.imgSize=60;
			        		}
							if(scope.img) {
								scope.img = scope.amazonUrl+'user/'+scope.img+'_'+scope.imgSize;
							}
							else {
								scope.img = 'assets/img/ge-no-profile-picture.png';
							}
						});
					},
					post: function(scope, iElement, iAttrs) {
						if(iAttrs.nested) {
							$('.panel',iElement).removeClass('panel').removeClass('panel-default');
							$('.panel-body',iElement).removeClass('panel-body');
						}
					}
				}
			}
		}
	})
});