jQuery.fn.hint = function(blurClass) {
    if (!blurClass) {
        blurClass = 'blur';
    }

    return this.each(function() {
        // get jQuery version of 'this'
        var $input = jQuery(this),
                // capture the rest of the variable to allow for reuse
                title = $input.attr('title'),
                placeholder = $input.attr('placeholder'), //this is the HTML5 way - if present no need to use this script
                $form = jQuery(this.form),
                $win = jQuery(window);

        // only apply logic if the element has the attribute
        if (title && !placeholder) {
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