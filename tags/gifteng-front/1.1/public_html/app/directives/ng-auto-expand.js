define(['directives'], function(directives) {
	'use strict';
	
	directives.directive('ngAutoExpand', function() {
		return {
			link: function(scope, elem, attrs) {
					var textArea = elem.get(0);
					var minimum = textArea.rows;
				elem.bind('keyup', function(e){
					while (textArea.rows > minimum && textArea.scrollHeight < textArea.offsetHeight)
					{
							textArea.rows--;
					}
					var h=0;
					while (textArea.scrollHeight > textArea.offsetHeight && h!==textArea.offsetHeight)
					{
						h=textArea.offsetHeight;
						textArea.rows++;
					}
				});
			}
		};
	})
});