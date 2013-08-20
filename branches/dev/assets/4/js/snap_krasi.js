$.fn.snap = function() {
	var open=false;
	$(this).bind('click',function() {
		open=!open;
		if(open) {
			$('.snap-slide').addClass('active');
		}
		else {
			$('.snap-slide').removeClass('active');
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
});