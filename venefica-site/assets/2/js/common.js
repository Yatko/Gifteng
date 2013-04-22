$(document).ready(function() {
    //pretty check and radio boxes
    $('input[type=checkbox]').prettyCheckboxes();
    $('input[type=radio][class=radionInline]').prettyCheckboxes({
        'display': 'inline'
    });
    $('input[type=radio][class=radioList]').prettyCheckboxes();
    
    //chosen selectbox
    $(".chzn-select").chosen({
        no_results_text: "No results matched",
        disable_search: true,
        allow_single_deselect: false
    });
    
    //textbox hint
    $('input[title != ""]').hint();
    
    $('#about').click(function() {
        $("#navigator2").slideToggle(350, "backinout");
    });
});

//form fader
$(function() {
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
});
