define(['jquery'], function() {
	$.fn.snap = function() {
		var open = false;
		$(this).bind('click', function() {
			open = !open;
			if (open) {
				$('.snap-slide').addClass('active');
			} else {
				$('.snap-slide').removeClass('active');
			}
		});
	};
	window.onscroll = function() {
		window.scrollTo(0, 0);
	}
}); 