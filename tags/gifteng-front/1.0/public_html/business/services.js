define(['angular'], function(angular) {
	
	'use strict';

	angular.module('giftengbiz.services', [])
		.factory('User', function(Auth) {
			var user = Auth.get();
	
			return {
				setUser : function(newUser) {
					user = newUser;
				},
				getUser : function() {
					return user;
				}
			}
		})
		.factory('Profile', function($resource) {
			return {
				get: $resource('api/user/:id', {id : '@id'}, {
					query : {
						method : 'GET',
						isArray : false
					}
				}),
				update: $resource('api/biz/update', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				reinit: $resource('api/user/reinit', {}, {
					query : {
						method : 'GET',
						isArray : false
					}
				})
			};
		})
		.factory('Ad', function($resource) {
			return $resource('api/ad/:id', {
				id : '@id'
			}, {
				update : {
					method : 'PUT'
				},
				query : {
					method : 'GET',
					isArray : false
				}
			});
		})
		.factory('Auth', function($resource) {
			return $resource('api/auth/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			});
		})
		.factory('Store', function($resource) {
			return $resource('api/biz/store/:id', {id:'@id'}, {
				update: {
		            method: 'PUT',
		        },
			});
		})
		.factory('Biz', function($resource) {
			return {
				register : $resource('api/biz/register',
					{},
					{
						query : {
							method : 'POST',
							isArray : false
						}
					}),
			};
		})
		.factory('AdUser', function($resource) {
		return $resource('api/ad/user/:id', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	});

});
