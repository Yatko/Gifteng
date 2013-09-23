<script language="javascript">
    function request_hide(callerElement, requestId) {
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/hide_request',
            dataType: 'json',
            cache: false,
            data: {
                requestId: requestId
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( callerElement !== null ) {
                    $(callerElement).removeAttr("disabled");
                }
                
                $('#request_' + requestId).removeClass('masonry-brick');
                $('#request_' + requestId).addClass('hide');
                
                if ( $('.user_receiving').length > 0 ) {
                    var num_receivings = response.<?=AJAX_STATUS_RESULT?>.<?=USER_RECEIVINGS_NUM?>;
                    $('.user_receiving').text(num_receivings);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function request_cancel(callerElement, requestType, requestId, adId, userId, callback) {
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/cancel_request',
            dataType: 'json',
            cache: false,
            data: {
                requestType: requestType, //possible values: giving, receiving
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
                    var result = response.<?=AJAX_STATUS_RESULT?>;
                    var resultHtml;
                    var resultNum;
                    
                    if ( result.hasOwnProperty('<?=AD_GIVING_HTML?>') ) {
                        resultHtml = result.<?=AD_GIVING_HTML?>;
                        resultNum = result.<?=USER_GIVINGS_NUM?>;
                    } else if ( result.hasOwnProperty('<?=AD_RECEIVING_HTML?>') ) {
                        resultHtml = result.<?=AD_RECEIVING_HTML?>;
                        resultNum = result.<?=USER_RECEIVINGS_NUM?>;
                    } else {
                        resultHtml = '';
                        resultNum = null;
                    }
                    
                    $element.removeAttr("disabled");
                    $element.trigger('request_canceled', [requestId, adId, resultHtml, resultNum]);
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
    function request_select(callerElement, requestId, adId, userId) {
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/select_request',
            dataType: 'json',
            cache: false,
            data: {
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
                    $(callerElement).removeAttr("disabled");
                }
                
                var result = response.<?=AJAX_STATUS_RESULT?>;
                var resultHtml = result.<?=AD_GIVING_HTML?>;
                $('#ad_' + adId).html(resultHtml);
                
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
    
    function request_send(callerElement, requestId, adId, userId) {
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/send_request',
            dataType: 'json',
            cache: false,
            data: {
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
                    $(callerElement).removeAttr("disabled");
                }
                
                var result = response.<?=AJAX_STATUS_RESULT?>;
                var resultHtml = result.<?=AD_GIVING_HTML?>;
                $('#ad_' + adId).html(resultHtml);
                
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
    function request_receive(callerElement, requestId, adId, userId, callback) {
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/receive_request',
            dataType: 'json',
            cache: false,
            data: {
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
                    $(callerElement).removeAttr("disabled");
                }
                
                var result = response.<?=AJAX_STATUS_RESULT?>;
                var resultHtml = result.<?=AD_RECEIVING_HTML?>;
                $('#request_' + requestId).html(resultHtml);
                
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
</script>
