define(['directives','jquery','jcrop'], function(directives, jQuery) {
	'use strict';
	
	directives.directive('imgJcrop', function() {
        return {
            restrict : 'E',
            replace : true,
            scope : {
                src : '@',
                selected : '&',
                ngModel : "="
            },
            link : function(scope, element, attr) {
                var myImg;
                var clear = function() {
                    if (myImg) {
                        myImg.next().remove();
                        myImg.remove();
                        myImg = undefined;
                    }
                };
                scope.$watch('src', function(nv) {
                    clear();
                    if (nv) {
                       // element.after("<img  class='img-responsive'/>");
                        myImg = element.next();
                        if(myImg.attr("id") === "imgplaceholder") {
                            myImg.remove();
                        }

                        element.after("<img  class='img-responsive jcimg upimg center-block'  id='imgplaceholder' style='width: none !important;'/>");
                        myImg = element.next();

                        myImg.attr('src', nv);
                        $(myImg).Jcrop({
                            addClass: 'jcrop-dark',
                            minSize:[112,112],
                            setSelect:   [ 250, 250, 112, 112 ],
                            bgColor: 'black', bgOpacity: 0.4, bgFade: true,
                            trackDocument : true,
							
                            onSelect : function(x) {
							$(".jcrop-keymgr").remove();
								scope.ngModel = x;
                                scope.$apply(function() {
                                    scope.selected({
                                        cords : x
                                    });
                                });
                            },
							
                            aspectRatio : 1
                        }, function() {
                            // Use the API to get the real image size
                            var bounds = this.getBounds();
                        });
						
						//$('input[type="radio"]').hide();
                    }else{
                        element.after("<img id='imgplaceholder' src='assets/img/ge-upload.png' class='img-responsive upimg center-block'/>");
                        //$scope.placeholderimg = element.next();
                    }
					$('#imgplaceholder').click(function() {
						$('input[type="file"]').click();
					});
                });
                scope.$on('$destroy', clear);
            }
        };
    })
});