//code snippet taken from: http://www.cssnewbie.com/cross-browser-support-for-html5-placeholder-text-in-forms#.UUlj-VEu67g
jQuery(function() {
	jQuery.support.placeholder = false;
	var test = document.createElement('input');
	if ('placeholder' in test) jQuery.support.placeholder = true;
});

jQuery.fn.hint = function(blurClass) {
    if (!blurClass) {
        blurClass = 'blur';
    }

    return this.each(function() {
        // get jQuery version of 'this'
        var $input = jQuery(this);
        // capture the rest of the variable to allow for reuse
        var title = $input.attr('title');
        //var placeholder = $input.attr('placeholder'), //this is the HTML5 way - if present no need to use this script
        var $form = jQuery(this.form);
        var $win = jQuery(window);
        
        // only apply logic if the element has the attribute and placeholder text is not supported. 
        if (title && !$.support.placeholder) {
            function remove() {
                if ($input.val() === title && $input.hasClass(blurClass)) {
                    $input.val('').removeClass(blurClass);
                }
            }
            
            // on blur, set value to title attr if text is blank
            $input.blur(function() {
                if (this.value === '') {
                    $input.val(title).addClass(blurClass);
                }
            }).focus(remove).blur(); // now change all inputs to title

            // clear the pre-defined text when form is submitted
            $form.submit(remove);
            $win.unload(remove); // handles Firefox's autocomplete
        }
    });
};