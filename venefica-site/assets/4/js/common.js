function hide_file($file) {
    $file.wrap($('<div/>').css({
        height: 0,
        width: 0,
        'overflow': 'hidden'
    }));
}
function open_file($button) {
    var fileInputId = $button.attr('for');
    var fileInput = $('#' + fileInputId);
    
    $button.click(function() {
        fileInput.click();
    }).show();
}
function attach_file($button) {
    var fileInputId = $button.attr('for');
    var fileInput = $('#' + fileInputId);
    
    fileInput.change(function() {
        //var fileName = fileInput.val().replace(/C:\\fakepath\\/i, '');
        //$file.text(fileName);
        
        $button.addClass('btn-ge');
        if ( !$button.attr('original_text') ) {
            //saving original text to be used later
            $button.attr('original_text', $button.text());
        }
        $button.text('Done');
        //$file.prop('disabled', true);
        $button.trigger('file_selected');
    });
}


$(function() {
    
    if ( $(':file').length > 0 ) {
        $(':file').each(function() {
            var $this = $(this);
            hide_file($this);
        });
    }
    
    if ( $('.file').length > 0 ) {
        $('.file').each(function() {
            var $this = $(this);
            open_file($this);
            attach_file($this);
        });
    }
    
    if ( $('.modal').length > 0 ) {
        //making auto height of the body for every modal
        $('.modal').each(function() {
            $this = $(this);
            $this.on('show', function () {
                $('.modal-body', this).css({
                    width: 'auto',
                    height: 'auto',
                    'max-height':'100%'
                });
            });
        });
    }
    
    if ( $('#form_wrapper').length > 0 ) {
        //form fader
        //the form wrapper (includes all forms)
        var $form_wrapper = $('#form_wrapper');
        //the current form is the one with class active
        var $currentForm = $form_wrapper.children('form.active');
        //the change form links
        var $linkform = $form_wrapper.find('.linkform');
        var maxWidth = 0;
        var maxHeight = 0;

        //get width and height of each form and store them for later						
        $form_wrapper.children('form').each(function(i) {
            var $theForm = $(this);
            //solve the inline display none problem when using fadeIn fadeOut
            if ( !$theForm.hasClass('active') ) {
                $theForm.hide();
            }
            var w = $theForm.width();
            var h = $theForm.height();
            if ( w > maxWidth ) {
                maxWidth = w;
            }
            if ( h > maxHeight ) {
                maxHeight = h;
            }
            $theForm.data({
                width: w,
                height: h
            });
        });

        //set width and height of wrapper (same of current form)
        $form_wrapper.css({
            width: maxWidth + 'px',
            height: maxHeight + 'px'
        });

        //clicking a link (change form event) in the form
        //makes the current form hide.
        //The wrapper animates its width and height to the 
        //width and height of the new current form.
        //After the animation, the new form is shown
        $linkform.bind('click', function(e){
            var $link = $(this);
            var target = $link.attr('rel');
            $currentForm.fadeOut(400, function() {
                //remove class active from current form
                $currentForm.removeClass('active');
                //new current form
                $currentForm= $form_wrapper.children('form.' + target);
                //animate the wrapper
                $form_wrapper.stop().animate({
                    //width: $currentForm.data('width') + 'px',
                    //height: $currentForm.data('height') + 'px'
                }, 150, function() {
                    //new form gets class active
                    $currentForm.addClass('active');
                    //show the new form
                    $currentForm.fadeIn(250);
                });
            });
            e.preventDefault();
        });
    }
});
