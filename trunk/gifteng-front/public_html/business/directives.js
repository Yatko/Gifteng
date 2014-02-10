define(['angular','services','jquery'], function(angular,services,jQuery) {
	'use strict';
	
	return angular.module('giftengbiz.directives', ['giftengbiz.services'])
		.directive('giftimg', function() {
			return {
				restrict: 'E',
				replace: false,
				templateUrl: 'app/partials/directives/giftimg.html',
				scope: {
					action: '@',
					def: '@',
					ngModel: '='
				},
				controller: function ($scope) {
					$scope.progress = 0;
					
                    $scope.giftimage = ($scope.ngModel == null || $.trim($scope.ngModel).length == 0) ?
                                            'http://veneficalabs.com/gifteng/assets/4/temp-sample/ge-upload.png'
                                        :
                                            $scope.ngModel;
				
					$scope.sendFile = function(el) {
				
						var $form = $(el).parents('form');
				
						if ($(el).val() == '') {
							return false;
						}
				
						$form.attr('action', $scope.action);
				
						$scope.$apply(function() {
							$scope.progress = 0;
						});				
				
						$form.ajaxSubmit({
							type: 'POST',
							uploadProgress: function(event, position, total, percentComplete) { 
								
								$scope.$apply(function() {
									$scope.progress = percentComplete;
								});
				
							},
							error: function(event, statusText, responseText, form) { 
								
								$form.removeAttr('action');
				
							},
							success: function(responseText, statusText, xhr, form) { 
								
								if(typeof(responseText.error)==='undefined') {
									var ar = $(el).val().split('\\'), 
										filename =  ar[ar.length-1];
	
									$form.removeAttr('action');
					
									$scope.$apply(function() {
										$scope.progress = 0;
										$scope.giftimage = "api/image/"+filename;
										$scope.ngModel = "api/image/"+filename;
										$scope.error = "";
									});
								}
								else {
									$scope.$apply(function() {
										$scope.progress = 0;
										$scope.error = responseText.error;
									});
								}
				
							},
						});
				
					}
				
				},
				link: function(scope, elem, attrs, ctrl) {
					elem.find('.fake-uploader').bind('click',function() {
						elem.find('input[type="file"]').click();
					});
				}
			};
		
		})
		.directive('datePicker', function() {
		    return {
		        restrict: 'E',
		        require: ['ngModel'],
		        scope: {
		            ngModel: '='
		        },
		        replace: true,
		        template:
		            '<div class="input-group">'     +
		                    '<input type="text"  class="form-control input-lg" ngModel required>' +
		                    '<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>' +
		            '</div>' ,
		        link: function(scope, element, attrs) {
		            var input = element.find('input');
		            var nowTemp = new Date();
		            var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(),0,0,0,0);
		
		            input.datepicker({
		               format: "yyyy-mm-dd",
		                onRender: function(date) {
		                    return date.valueOf() < now.valueOf() ? 'disabled' : '';
		                }
		            });
		
		            element.bind('blur keyup change', function() {
		                scope.ngModel = input.val();
		                console.info('date-picker event', input.val(), scope.ngModel);
		            });
		        }
		    }
		});
});
