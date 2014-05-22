define(
	[
	
		// General
		"angular", "filters", "services", "model",
		
		// Controllers
		"controllers/admin",
		"controllers/index",
		"controllers/profile-box",
		"controllers/search",
		"controllers/invite",
		"controllers/profile",
		"controllers/settings",
		"controllers/browse",
		"controllers/login",
		"controllers/request-gift",
		"controllers/top",
		"controllers/confirm",
		"controllers/messages",
		"controllers/request",
		"controllers/user-ads",
		"controllers/requested-ads",
		"controllers/bookmarked-ads",
		"controllers/followers",
		"controllers/following",
		"controllers/nav",
		"controllers/verify-invitation",
		"controllers/notifications",
		"controllers/reset",
		"controllers/view-gift",
		"controllers/forgot",
		"controllers/post",
		"controllers/reviews",
		"controllers/give",
		"controllers/login-modals",
		
		
		// Directives
		"directives/ad",
		"directives/ad-detail-view",
		"directives/ad-item-box",
		"directives/back-button",
		"directives/checkbox",
		"directives/facebook",
		"directives/gift-img",
		"directives/img-jcrop",
		"directives/loader",
		"directives/message",
		"directives/ng-auto-expand",
		"directives/ng-blur",
		"directives/ng-only-alphabets",
		"directives/ng-only-digits",
		"directives/user-profile"
		
	],
	function (angular, filters, services, model) {
    	return angular.module("gifteng", ["gifteng.controllers", "gifteng.filters", "gifteng.services", "gifteng.directives", "gifteng.model", "ngResource", "ngCookies", "ngRoute", "ngTouch", "ngSanitize", "ui.bootstrap.modal", "ui.bootstrap.tooltip"])
	}
);