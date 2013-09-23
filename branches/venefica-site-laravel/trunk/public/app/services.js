define(['angular'], function (angular) {
	'use strict';
	
	angular.module('gifteng.services', []).factory('Contact', function($resource) {
		return $resource('api/contact/:Id',
			{ 
				Id:'@id'
        	},
        	{
        		update: {
        			method:'PUT'
        		}
        	}
		);
	});

});
