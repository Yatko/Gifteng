$.fn.snap = function() {
	var open=false;
	$(this).bind('click',function() {
		open=!open;
		if(open) {
			$(this).addClass('active');
			$('.snap-content').animate({zIndex:252}, {
				step: function(now,fx) {
					$(this).css('-webkit-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-moz-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-ms-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-o-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('transform','translate3d('+(now-2)+'px, 0, 0)');
				},
				duration:'slow'
			},'linear');
		}
		else {
			$(this).removeClass('active');
			$('.snap-content').animate({zIndex:2}, {
				step: function(now,fx) {
					$(this).css('-webkit-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-moz-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-ms-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('-o-transform','translate3d('+(now-2)+'px, 0, 0)');
					$(this).css('transform','translate3d('+(now-2)+'px, 0, 0)');
				},
				duration:'slow'
			},'linear');
		}
	});
};
$(function() {
	$('#content').scroll(function(){
        $('.ge-navbar').css('top',$('#content').scrollTop());   
   });
});

$(function() {
	$('#open-left').snap();
})