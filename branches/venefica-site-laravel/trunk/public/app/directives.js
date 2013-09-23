define(['angular','services','jquery'], function(angular) {
	'use strict';
	
	return angular.module('gifteng.directives', ['gifteng.services'])
		.directive('adItemBox', function($compile) {
			return {
				restrict:'E',
				scope: {
					img:'@',
					status:'@',
					type:'@'
				},
				templateUrl: 'app/partials/directives/ad-item-box.html',
				replace:true,
				transclude:true,
				compile: function compile() {
					return {
				        post: function (scope, iElement, iAttrs) { 
							var status = iAttrs.status;
							var type = iAttrs.type;
							
							if(type=="receiving") {
								
								var userprofile = '<user-profile name="Krasimir Stavrev" location="Sofia, Bulgaria" img="http://placehold.it/200x200/FF9999/FFF" points="100" since="August 2013" nested="true"></user-profile>';
								$('.well',iElement).prepend($compile(userprofile)(scope));
								if(status=='accepted') {
									var action='<p class="text-center"><span class="fui-arrow-right"></span> Request accepted <span class="fui-arrow-left"></span></p>'+
									'<div class="row">'+
										'<div class="col-xs-4"><a href="" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-envelope"></span></a></div>'+
										'<div class="col-xs-8"><a href="" class="btn btn-primary btn-block">Gift Received</a></div>'+
									'</div>';
								}
								if(status=='declined') {
									var action='<p class="text-center"><span class="fui-arrow-right"></span> Request declined <span class="fui-arrow-left"></span></p>'+
									'<a href="" class="btn btn-default btn-block">Delete gift</a>';
								}
								if(status=='received') {
									var action='<p class="text-center"><span class="fui-arrow-right"></span> Gift received <span class="fui-arrow-left"></span></p>';
								}
								if(status=="not_requested") {
									var action='<div class="row">'+
										'<div class="col-xs-4"><a href="" class="btn btn-default btn-block"><span class="glyphicon glyphicon-remove"></span></a></div>'+
										'<div class="col-xs-8"><a href="" class="btn btn-primary btn-block">Request Gift</a></div>'+
									'</div>';
								}
								if(status=="ended") {
									var action='<div class="row">'+
										'<div class="col-xs-4"><a href="" class="btn btn-default btn-block"><span class="glyphicon glyphicon-remove"></span></a></div>'+
										'<div class="col-xs-8">ENDED</div>'+
									'</div>';
								}
							}
							else {
								if(status=='active') {
									var action='<div class="row ge-text ge-description">'+
													'<div class="col-xs-12">'+
														'<p class="text-center">'+
															'<span class="fui-arrow-right"></span>'+
															'Share to receive requests'+
															'<span class="fui-arrow-left"></span>'+
														'</p>'+
													'</div>'+
												'</div>'+
												'<div class="row ge-text ge-description ge-action">'+
													'<div class="col-xs-4">'+
														'<button class="btn btn-mini btn-block btn-social-facebook link fui-facebook"></button>'+
													'</div>'+
													'<div class="col-xs-4">'+
														'<button class="btn btn-mini btn-block btn-social-twitter link fui-twitter"></button>'+
													'</div>'+
													'<div class="col-xs-4">'+
														'<button class="btn btn-mini btn-block btn-social-pinterest link fui-pinterest"></button>'+
													'</div>'+
												'</div>';
								}
							}
								
							$('.action',iElement).html(action);
						}
				      }
				}
			}
		})
		.directive('userProfile', function() {
			return {
				restrict:'E',
				scope: {
					img:'@',
					name:'@',
					location:'@',
					since:'@',
					points:'@',
					nested:'@'
				},
				templateUrl: 'app/partials/directives/user-profile.html',
				replace:true,
				transclude:true,
				compile: function() {
					return {
						post: function(scope, iElement, iAttrs) {
							if(iAttrs.showgifts) {
								var gifts = '<div class="row">'+
												'<div class="col-xs-4">'+
													'<a href="#/view/gift"><img src="http://placehold.it/200x200/339999/FFF" class="img-rounded img-responsive" alt=""></a>'+
												'</div>'+
												'<div class="col-xs-4">'+
													'<a href="#/view/gift"><img src="http://placehold.it/200x200/993399/FFF" class="img-rounded img-responsive" alt=""></a>'+
												'</div>'+
												'<div class="col-xs-4">'+
													'<a href="#/view/gift"><img src="http://placehold.it/200x200/339933/FFF" class="img-rounded img-responsive" alt=""></a>'+
												'</div>'+
											'</div>';
								$('.well',iElement).append(gifts);
							}
							if(iAttrs.nested) {
								$('.well',iElement).removeClass('well');
							}
						}
					}
				}
			}
		})
		.directive('ad', function($compile) {
			return {
				restrict:'E',
				scope: {
					img:'@',
					title:'@',
					simple:'@'
				},
				templateUrl: 'app/partials/directives/ad.html',
				replace:true,
				transclude:true,
				compile: function() {
					return {
						post: function(scope, iElement, iAttrs) {
							if(iAttrs.simple) {
								$('.ge-action',iElement).remove();
								$('.title',iElement).remove();
								$('user-profile',iElement).remove();
							}
						}
					}
				}
			}
		}).directive('messageList', function($compile) {
			return {
				restrict:'E',
				templateUrl: 'app/partials/directives/message-list.html',
				replace:true
			}
		}).directive('message', function($compile) {
			return {
				restrict:'E',
				templateUrl: 'app/partials/directives/message.html',
				replace:true
			}
		});
});
