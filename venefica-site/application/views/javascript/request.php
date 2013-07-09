<script language="javascript">
    function request_hide(requestId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/hide_request?requestId=' + requestId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#request_' + requestId).addClass('hide');
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function request_cancel(callerElement, requestId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/cancel_request?requestId=' + requestId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( callerElement !== null ) {
                    var $element = $(callerElement);
                    $element.addClass('disabled');
                    $element.trigger('request_cancelled', [requestId]);
                }
                
                if ( $('#requestContainer').length > 0 ) {
                    $('#requestContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function request_select(requestId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/select_request?requestId=' + requestId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( $('#requestContainer').length > 0 ) {
                    $('#requestContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
