define(['angular','services','controllers','directives'], function(angular, filters, services, controllers, directives) {
	'use strict';

	return angular.module('giftengbiz', ['giftengbiz.controllers','giftengbiz.services','giftengbiz.directives','ngResource','ui.bootstrap.modal']);
});