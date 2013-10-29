define(['angular','filters','services','controllers','directives'], function(angular, filters, services, controllers, directives) {
	'use strict';

	return angular.module('gifteng', ['gifteng.controllers','gifteng.filters','gifteng.services','gifteng.directives','ngResource','ui.bootstrap.modal']);
});