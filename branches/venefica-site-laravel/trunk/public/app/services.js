define(['angular'], function (angular) {
	'use strict';
	
	angular.module('gifteng.services', [])
	.factory('Ad', function($resource) {
		return $resource('api/ad/:id',
			{ 
				id:'@id'
        	},
        	{
        		update: {
        			method:'PUT'
        		},
  				query: {
  					method:'GET',
  					isArray:false
  				}
        	}
		);
	})
	.factory('AdMore', function($resource) {
		return $resource('api/ad/more/:last',
			{ 
				last:'@last'
        	},
        	{
  				query: {
  					method:'GET',
  					isArray:false
  				}
        	}
		);
	})
	.factory('AdUser', function($resource) {
		return $resource('api/ad/user/:id',
			{ 
				id:'@id'
        	},
        	{
  				query: {
  					method:'GET',
  					isArray:true
  				}
        	}
		);
	})
	.factory('AdRequested', function($resource) {
		return $resource('api/ad/requested/:id',
			{ 
				id:'@id'
        	},
        	{
  				query: {
  					method:'GET',
  					isArray:false
  				}
        	}
		);
	})
	.factory('AdBookmarked', function($resource) {
		return $resource('api/ad/bookmarked/:id',
			{ 
				id:'@id'
        	},
        	{
  				query: {
  					method:'GET',
  					isArray:false
  				}
        	}
		);
	})
	.factory('UserEx', function($resource) {
		return {
			profile: $resource('api/user/:id',{id:'@id'},{query:{method:'GET',isArray:false}}),
			following: $resource('api/user/following/:id',{id:'@id'},{query:{method:'GET',isArray:false}}),
			followers: $resource('api/user/followers/:id',{id:'@id'},{query:{method:'GET',isArray:false}}),
			notifications: $resource('api/user/notifications',{},{query:{method:'GET',isArray:false}}),
			message: $resource('api/user/message/:id',{id:'@id'},{query:{method:'GET',isArray:true}}),
			messageHide: $resource('api/user/hidemessage/:id',{id:'@id'},{query:{method:'GET',isArray:false}}),
			follow: $resource('api/user/follow/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			unfollow: $resource('api/user/unfollow/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			bookmark: $resource('api/ad/bookmark/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			unbookmark: $resource('api/ad/unbookmark/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			comment: $resource('api/ad/comment/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			reinit: $resource('api/user/reinit',{},{query:{method:'GET',isArray:false}}),
			relist: $resource('api/ad/relist/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestAd: $resource('api/ad/request/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestCancel: $resource('api/ad/request/cancel/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestSelect: $resource('api/ad/request/select/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestSend: $resource('api/ad/request/send/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestReceive: $resource('api/ad/request/receive/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			requestHide: $resource('api/ad/request/hide/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			updateProfile: $resource('api/user',{},{query:{method:'POST',isArray:false}}),
		}
	})
	.factory('Top', function($resource) {
		return $resource('api/top', {},{query:{method:'GET',isArray:false}});
	})
	.factory('Geo', function($resource) {
		return $resource('api/geo/:zip', { zip: '@zip' },{query:{method:'GET',isArray:false}});
	})
	.factory('Auth', function($resource) {
		return $resource('api/auth/:id', { id: '@id' },{query:{method:'GET',isArray:false}});
	})
	.factory('User', function(Auth) {
		var user=Auth.get();
		
		return {
			setUser: function(newUser) {
				user = newUser;
			},
			getUser: function() {
				return user;
			}
		}
	});

});
