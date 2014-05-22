define(['angular','services'], function(angular,services) {
	'use strict';
	
	return angular.module('giftengbiz.controllers', ['giftengbiz.services'])
		/**
		 * Amazon URL
		 */
		.run(function($rootScope) {
			$rootScope.amazonUrl="https://s3.amazonaws.com/ge-dev/";
			$rootScope.networkUrl="http://dev.veneficalabs.com/gifteng-1.0/gifteng-ui-front";
		});
});
