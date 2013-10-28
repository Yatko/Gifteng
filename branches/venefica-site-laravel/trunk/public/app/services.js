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
			follow: $resource('api/user/follow/:id',{id:'@id'},{query:{method:'POST',isArray:false}}),
			unfollow: $resource('api/user/unfollow/:id',{id:'@id'},{query:{method:'POST',isArray:false}})
		}
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
