define(
	[
	
		// General
		'angular','services','controllers','directives','filters',
		
		// Controllers
		"../app/controllers/business/nav",
		"../app/controllers/business/profile",
		"../app/controllers/business/register",
		"../app/controllers/business/post",
		
		// Directives
		"../app/directives/ad",
		"../app/directives/ad-detail-view",
		"../app/directives/ad-item-box",
		"../app/directives/loader",
		"../app/directives/gift-img",
		"../app/directives/date-picker",
		"../app/directives/img-jcrop"
	],
	function(angular, filters, services, controllers, directives) {
	'use strict';

	return angular.module('giftengbiz', ['giftengbiz.controllers','giftengbiz.filters','giftengbiz.services','giftengbiz.directives','ngRoute','ngTouch','ngResource', "ngSanitize",'ui.bootstrap.modal']);
});