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
        		}
        	}
		);
	})
	.factory('Auth', function($resource) {
		return $resource('api/auth/:id', { id: '@id' });
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
