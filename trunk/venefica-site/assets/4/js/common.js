function init_files() {
    if ( $('[type="file"]').length > 0 ) {
        $('[type="file"]').each(function() {
            var $this = $(this);
            hide_file($this);
        });
    }
    
    if ( $('.file').length > 0 ) {
        $('.file').each(function() {
            var $this = $(this);
            attach_file($this);
        });
    }
}

function hide_file($file) {
    if ( !$file.attr('already_processed') ) {
        $file.wrap($('<div/>').css({
            height: 0,
            width: 0,
            'overflow': 'hidden'
        }));
        $file.attr('already_processed', 1);
    }
}
function attach_file($button) {
    var fileInputId = $button.attr('for');
    var fileInput = $('#' + fileInputId);
    
    if ( !$button.attr('already_processed') ) {
        $button.click(function() {
            fileInput.click();
        }).show();
        fileInput.change(function() {
            if ( $button.is("button") ) {
                $button.addClass('btn-ge');
                if ( !$button.attr('original_text') ) {
                    //saving original text to be used later
                    $button.attr('original_text', $button.html());
                }
                $button.text('Done');
            }
            $button.trigger('file_selected');
        });
        
        $button.attr('already_processed', 1);
    }
}

function get_file_size(file) {
    if ( !window.FileReader ) {
        //The file API isn't supported on this browser yet.
        return -1;
    } else if ( !file.files ) {
        //This browser doesn't seem to support the `files` property of file inputs.
        return -1;
    } else if ( !file.files[0] ) {
        //No file is selected.
        return -1;
    }
    return file.files[0].size;
}

$(function() {
    
    init_files();
    
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
    
    if ( $('form#browse_form').length > 0 ) {
        $('form#browse_form input[name=q]').keypress(function(e) {
            if (e.which === 13) {
                $('form#browse_form').submit();
                return false;
            }
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
