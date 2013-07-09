<script langauge="javascript">
    function show_message_rest(callerElement, messageId) {
        $('.message_' + messageId + '_separator').addClass('hide');
        $('.message_' + messageId + '_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    function add_message() {
        if ( $("#message_post_form").length === 0 ) {
            return;
        }

        var $messageAdId = $("#message_post_form input[name=messageAdId]");
        var $messageToId = $("#message_post_form input[name=messageToId]");
        var $messageText = $("#message_post_form textarea[name=messageText]");

        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/message',
            dataType: 'json',
            cache: false,
            data: {
                messageAdId: $messageAdId.val(),
                messageToId: $messageToId.val(),
                messageText: $messageText.val()
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $messageAdId.val('');
                $messageToId.val('');
                $messageText.val('');

                if ( $('#messageContainer').length > 0 ) {
                    $('#messageContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
