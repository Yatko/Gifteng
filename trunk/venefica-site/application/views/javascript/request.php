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
    function request_cancel(callerElement, requestType, requestId, adId, userId, callback) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/cancel_request',
            dataType: 'json',
            cache: false,
            data: {
                requestType: requestType,
                requestId: requestId,
                adId: adId,
                userId: userId
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( callerElement !== null ) {
                    var $element = $(callerElement);
                    //$element.addClass('disabled');
                    $element.trigger('request_canceled', [requestId, adId, response.<?=AJAX_STATUS_RESULT?>]);
                }
                
                if ( $('#requestContainer').length > 0 ) {
                    $('#requestContainer').modal('hide');
                }
                
                if ( callback !== null ) {
                    callback();
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function request_select(requestId, adId, userId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/select_request?requestId=' + requestId + '&adId=' + adId + '&userId=' + userId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var result = response.<?=AJAX_STATUS_RESULT?>;
                $('#ad_' + adId).html(result);
                
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
    
    function request_send(requestId, adId, userId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/send_request?requestId=' + requestId + '&adId=' + adId + '&userId=' + userId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var result = response.<?=AJAX_STATUS_RESULT?>;
                $('#ad_' + adId).html(result);
                
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
    function request_receive(requestId, adId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/receive_request?requestId=' + requestId + '&adId=' + adId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var result = response.<?=AJAX_STATUS_RESULT?>;
                $('#request_' + requestId).html(result);
                
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
