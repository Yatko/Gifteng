define(['angular'], function(angular) {'use strict';

	angular.module('gifteng.services', []).factory('Ad', function($resource) {
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
	}).factory('AdMore', function($resource) {
		return $resource('api/ad/more/:last', {
			last : '@last'
		}, {
			query : {
				method : 'GET',
				isArray : false
			}
		});
	}).factory('AdUser', function($resource) {
		return $resource('api/ad/user/:id', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}).factory('AdRequested', function($resource) {
		return $resource('api/ad/requested/:id', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : false
			}
		});
	}).factory('AdBookmarked', function($resource) {
		return $resource('api/ad/bookmarked/:id', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : false
			}
		});
	}).factory('Shipping', function($resource) {
		return $resource('api/admin/shippingBox', {},{
			query : {
				method : 'GET',
				isArray : false
			}
		});
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
	}).factory('Top', function($resource) {
		return $resource('api/top', {}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}).factory('Geo', function($resource) {
		return $resource('api/geo/:zip', {
			zip : '@zip'
		}, {
			query : {
				method : 'GET',
				isArray : false
			}
		});
	}).factory('Auth', function($resource) {
		return $resource('api/auth/:id', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : false
			}
		});
	}).factory('User', function(Auth) {
		var user = Auth.get();

		return {
			setUser : function(newUser) {
				user = newUser;
			},
			getUser : function() {
				return user;
			}
		}
	}).factory('Facebook', ['$rootScope', function($rootScope) {
 
        var fbLoaded = false;
        
        var _fb =  {
            loaded: fbLoaded,
            _init: function(params) {
                if(window.FB) {
                    angular.extend(window.FB, _fb);
                    angular.extend(_fb, window.FB);
                    _fb.loaded = true;
                    window.FB.init(params);
 
                    if(!$rootScope.$$phase) {
                        $rootScope.$apply();
                    }
                }
            }
        }
        return _fb;
    }])
	.factory('AdImage', function() {
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
	})
	.service('WindowService',  function () { 
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
	})
			
	.service('SharedService',  function (WindowService) { 
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
	    })
		.factory('InviteService', function($resource) {
			return {
				requestInvitation : $resource('api/invite/requestInvitation/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				verifyInvitation : $resource('api/invite/verifyInvitation/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				registerUser    : $resource('api/invite/register/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				})
			}
		})
		.factory('AdminService', function($resource) {
			return {
				getUnApproved : $resource('api/admin/unApproved/', {}, {
					query : {
						method : 'GET',
						isArray : false
					}
				}),
				approved : $resource('api/admin/approve',{},
				{					
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				unapproved : $resource('api/admin/unapprove/',{},
				{
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				online : $resource('api/admin/online/', {},
				{
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				getShippings : $resource('api/admin/getShippings/', {}, {
					query : {
						method : 'GET',
						isArray : false
					}
				}),
				sendEmail : $resource('api/admin/sendEmail/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				deleteShipping : $resource('api/admin/deleteShipping/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				}),
				updateShipping : $resource('api/admin/updateShipping/', {}, {
					query : {
						method : 'POST',
						isArray : false
					}
				})
				
			}
		})
		.factory('IpService', function($http,$q) {
		  var ip = ''
			return{
				get: function () {
					var deferred = $q.defer();
					$http({ method: "jsonp", url: "http://smart-ip.net/geoip-json?callback=JSON_CALLBACK" })
						.success(function (data, status, headers, config) {
							deferred.resolve(data);
						}).error(function (data, status, headers, config) {
							deferred.reject(data);
						});
					return deferred.promise;
				}			
		  };
		})	
		.factory('TestEmailService', function($resource) {
			var testEmail = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;
			
			return {
				sendMail : $resource('api/mail/send', {}, {
					send : {
						method : 'POST',
						isArray : false
					}
				}),
				
				isValid : function(email) {
					if (!testEmail.test(email)){
						return false;
					}else{
						return true;
					}
				}
			};
		})
		.factory('Report', function($resource) {
			return $resource('api/report', {}, {
					query : {
						method : 'POST',
						isArray : false
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
						$('.snap-content').append(dom);
						
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
		.factory('ProfileData', function() {
			var data;
			return {
				get: function() {
					return data;
				},
				set: function(obj) {
					data=obj;
				} 
			}
		})
		.factory('Filters', function() {
			var columns=3;
			var simple=false;
			var mygifts="all";
			var search={order:'newest',keywords:"",category:"",delivery:""};
			return {
				getColumns: function() {
					return columns;
				},
				setColumns: function(num) {
					columns=num;
				},
				getSimple: function() {
					return simple;
				},
				toggleSimple: function() {
					simple=!simple;
				},
				getMyGifts: function() {
					return mygifts;
				},
				setMyGifts: function(str) {
					mygifts=str;
				},
				getSearch: function() {
					return search;
				},
				setSearch: function(obj) {
					search=obj;
				}
			}
		})
		.factory('Amazon', function($rootScope) {
			return $rootScope.amazonUrl;
		});
});
