define(['directives', "jquery.form"], function(directives) {
	'use strict';

	directives.directive('giftimg', function() {
		return {
			restrict : 'E',
			replace : false,
			templateUrl : 'app/partials/directives/giftimg.html',
			scope : {
				action : '@',
				def : '=',
				ngModel : '='
			},
			controller : function($scope, $rootScope) {

				var sizeToBytes = function(size) {
					var scale = 1;

					if (~ size.indexOf('k')) {
						scale = 1024;
					} else if (~ size.indexOf('m')) {
						scale = 1024 * 1024;
					} else if (~ size.indexOf('g')) {
						scale = 1024 * 1024 * 1024;
					}
					return parseInt(size, 10) * scale;
				};

				$scope.progress = 0;

				$scope.$watch(function() {
					return $scope.def;
				}, function(img) {
					$scope.img = (img == null || $.trim(img).length == 0) ? 'assets/img/ge-upload.png' : img;
				}, true);

				$scope.sendFile = function(el) {
					$('#error-msg').css({
						'display' : 'none'
					});

					if ($(el).val() == '') {
						return false;
					}

					var maxSize = sizeToBytes('5m');
					var fileSize = el.files[0].size;
					var extension = el.value.replace(/^.*\./, '');

					if (extension == el.value) {
						extension = '';
					} else {
						extension = extension.toLowerCase();
					}

					switch(extension) {
						case 'jpg':
						case 'png':
						case 'jpeg':
						case 'JPG':
						case 'PNG':
						case 'JPEG':
							if (fileSize < maxSize) {

								var $form = $(el).parents('form');

								if ($(el).val() == '') {
									return false;
								}

								$form.attr('action', $scope.action);
								$scope.$apply(function() {
									$scope.progress = 0;
								});

								$form.ajaxSubmit({
									type : 'POST',
									uploadProgress : function(event, position, total, percentComplete) {
										$scope.$apply(function() {
											$scope.progress = percentComplete>75 ? 75 : percentComplete;
										});
									},
									error : function(event, statusText, responseText, form) {
										$form.removeAttr('action');
									},
									success : function(responseText, statusText, xhr, form) {
										if ( typeof (responseText.error) === 'undefined') {
											var ar = $(el).val().split('\\'), filename = ar[ar.length - 1];

											$form.removeAttr('action');
											
											$scope.$apply(function() {
												$scope.giftimage = "api/image/" + filename;
												$scope.img = "api/image/" + filename;
												$scope.def = "api/image/" + filename;
												$scope.error = "";
												$scope.ngModel = "api/image/" + filename;
											});
											
											$('.upimg').on('load', function() {
												$scope.$apply(function() {
													$scope.progress = 0;
												});
											});
											
										} else {
											$scope.$apply(function() {
												$scope.progress = 0;
												$('#gift-error-msg').css({
													'display' : 'block'
												});
												$('#error-msg').css({
													'display' : 'none'
												});
												$scope.error = responseText.error;
											});
										}
									}
								});
							} else {
								$(el).value = '';
								$scope.$apply(function() {
									$scope.progress = 0;
									$('#gift-error-msg').css({
										'display' : 'block'
									});
									$('#error-msg').css({
										'display' : 'none'
									});
									$scope.error = 'Please limit photo size to 5 MB ';
								});
								return false;
							}
							break;

						default:
							$(el).value = '';
							$scope.$apply(function() {
								$scope.progress = 0;
								$('#gift-error-msg').css({
									'display' : 'block'
								});
								$('#error-msg').css({
									'display' : 'none'
								});
								$scope.error = 'Invalid file format. Please upload an image';
							});
							return false;
					}

				}
			},
			link : function(scope, elem, attrs, ctrl) {
				scope.$watch("ngModel", function() {
					scope.giftimage = scope.ngModel;
				});
				elem.find('.fake-uploader').click(function() {
					elem.find('input[type="file"]').click();
				});

			}
		};

	})
}); 