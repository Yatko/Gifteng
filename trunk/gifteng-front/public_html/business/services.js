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
		})
		.factory('AdDialog', ['$compile', '$location', function($compile, $location) {
			var open=false;
			return {
				open: function(id, scope) {
					if(!open) {
						$('.addetailview').remove();
						$('.ng-view').fadeOut();
						var html = angular.element('<ad-detail-view id="'+id+'"></ad-detail-view>');
						var dom = $compile(html)(scope);
						$('body').append(dom);
						
						open=true;
					}
				},
				close: function() {
					$location.search('view',null);
					$('.addetailview').remove();
					$('.ng-view').fadeIn();
					open=false;
				}
			}
		}])
		.factory('Amazon', function($rootScope) {
			return $rootScope.amazonUrl;
		}).factory('UserEx', function($resource) {
		return {
			profile : $resource('api/user/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			following : $resource('api/user/following/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			followers : $resource('api/user/followers/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			ratings : $resource('api/user/ratings/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			notifications : $resource('api/user/notifications', {}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			message : $resource('api/user/message/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : true
				}
			}),
			messageHide : $resource('api/user/hidemessage/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			follow : $resource('api/user/follow/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			unfollow : $resource('api/user/unfollow/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			bookmark : $resource('api/ad/bookmark/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			unbookmark : $resource('api/ad/unbookmark/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			comment : $resource('api/ad/comment/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			reinit : $resource('api/user/reinit', {}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			relist : $resource('api/ad/relist/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestAd : $resource('api/ad/request/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestCancel : $resource('api/ad/request/cancel/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestSelect : $resource('api/ad/request/select/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestSend : $resource('api/ad/request/send/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestReceive : $resource('api/ad/request/receive/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			requestHide : $resource('api/ad/request/hide/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}), 
			requestView : $resource('api/ad/request/:id', {
				id : '@id'
			}, {
				query : {
					method : 'GET',
					isArray : false
				}
			}),
			rateAd : $resource('api/ad/rate/:id', {
				id : '@id'
			}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			updateProfile : $resource('api/user', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			changePassword : $resource('api/user/changePassword/', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			forgotPasswordEmail : $resource('api/user/forgotPasswordEmail/', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			resendVerification : $resource('api/user/resendVerification/', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
            cropnsave : $resource('api/image/profile/cropnsave/:id', {
                id : '@id'
            }, {
                update : {
                    method : 'PUT',
                    isArray : false,
                    params: { id: '@id' }
                }
            }),
            requestIssue: $resource('api/ad/request/issue/:id', {
            	id: '@id'
            }, {
				query : {
					method : 'POST',
					isArray : false
				}
            }),
			resetPassword   : $resource('api/user/resetPassword/', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			}),
			verifyUser   : $resource('api/user/verifyUser/', {}, {
				query : {
					method : 'POST',
					isArray : false
				}
			})
		}
	}).factory('AdImage', function() {
		var adImage = {};
		var ad;
		var request;
		var creator;
		
		return {
			setAdImage : function(image) {
				adImage = image;
			},
			getAdImage : function() {
				return adImage;
			},
			setAd : function(adItem) {
				ad = adItem;
			},
			getAd : function() {
				return ad;
			},
			setRequest : function(req) {
				request = req;
			},
			getRequest : function() {
				return request;
			},
			setCreator : function(user) {
				creator = user;
			},
			getCreator : function() {
				return creator;
			}
		}
	}).service('SharedService',  function (WindowService) { 
	        return {
					shareOnFacebook:function (title, itemUrl, imgUrl) {
										FB.ui({
											link: itemUrl,
											picture: imgUrl,
											method: 'feed',
											display: 'popup',
											name: title + ' - FREE on Gifteng',
											caption: 'www.gifteng.com',
											description: 'Gifteng is a social community where you can give and receive things you love for free.'
										}, function(response){});
									},
					shareOnTwitter:function(title, itemUrl, imgUrl) {
										var width = 575;
										var height = 450;
										var safeTitle = encodeURIComponent(('#Free ' + title.substring(0, 30)).replace(/&amp;/g, "&"));
										var safeItemUrl = encodeURIComponent(itemUrl.replace(/&amp;/g, "&"));
										var safeImgUrl = encodeURIComponent(imgUrl.replace(/&amp;/g, "&"));
										//window.open('https://twitter.com/share?text=' + safeTitle + '&url=' + safeItemUrl + '&via=gifteng&button_hashtag=free', '', width, height);
										WindowService.open_window_clear('https://twitter.com/share?text=' + safeTitle + '&url=' + safeItemUrl + '&via=gifteng&button_hashtag=free', '', width, height);
								   } ,
					shareOnPinterest:function (title, itemUrl, imgUrl) {
										var width = 575;
										var height = 450;
										var safeItemUrl = encodeURIComponent(itemUrl.replace(/&amp;/g, "&"));
										var safeImgUrl = encodeURIComponent(imgUrl.replace(/&amp;/g, "&"));
										var description = 'Check out this and other free gifts on http://www.gifteng.com ♥ Gifteng #free #gifts';
										var safeDescription = encodeURIComponent(description.replace(/&amp;/g, "&"));
										WindowService.open_window_clear('//pinterest.com/pin/create/button/?url=' + safeItemUrl + '&media=' + safeImgUrl + '&description=' + safeDescription, '', width, height);
									}
								
				 
	       };
	    }).service('WindowService',  function () { 
	        return {
			  open_window_clear:function (url, name, width, height) {
									if ( width === null || isNaN(width) || width <= 0 ) {
										width = 300;
									}
									if ( height === null || isNaN(height) || height <= 0 ) {
										height = 200;
									}
									var top = ($(window).height() / 2) - (height / 2);
									var left = ($(window).width() / 2) - (width / 2);
									window.open(url, name, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, channelmode=no, width=' + width + ', height=' + height + ', top=' + top + ', left=' + left);
								}
			
			};
	});

});
